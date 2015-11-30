/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger, Claudia Sichting
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see [http://www.gnu.org/licenses/].
 */

package de.anycook.einkaufszettel.tasks;

import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.adapter.RecipeIngredientRowAdapter;
import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.store.RecipeIngredientsStore;
import de.anycook.einkaufszettel.util.ConnectionStatus;
import de.anycook.einkaufszettel.util.Properties;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadRecipeIngredientsTask extends AsyncTask<String, Void, List<Ingredient>> {

    private static final String URL_PATTERN;
    private static final Logger LOGGER;
    private static final Gson GSON;

    static {
        URL_PATTERN = "https://api.anycook.de/recipe/%s/ingredients";
        LOGGER = LoggerManager.getLogger();
        GSON = new Gson();
    }

    private final RecipeIngredientRowAdapter ingredientRowAdapter;
    private final LinearLayout ingredientListProgress;
    private final Activity context;

    public LoadRecipeIngredientsTask(RecipeIngredientRowAdapter ingredientRowAdapter, View view,
                                     Activity activity) {
        this.ingredientRowAdapter = ingredientRowAdapter;
        this.ingredientListProgress = (LinearLayout) view.findViewById(R.id.progress);
        this.context = activity;
    }

    @Override
    protected void onPreExecute() {
        ingredientListProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Ingredient> doInBackground(String... recipeNames) {
        final String recipeName = recipeNames[0];
        final RecipeIngredientsStore recipeIngredientsStore = new RecipeIngredientsStore(context);
        recipeIngredientsStore.open();
        try {
            //check DB first
            List<Ingredient> ingredients = recipeIngredientsStore.getIngredients(recipeName);
            if (ingredients.size() > 0) {
                return ingredients;
            }

            // if ingredient not in db check if internet connection is available
            if (!ConnectionStatus.isConnected(context)) {
                ConnectionStatus.showOfflineMessage(context);
                return ingredients;
            }

            ingredients = loadIngredients(recipeName);

            //add ingredients to DB
            recipeIngredientsStore.addIngredients(recipeName, ingredients);

            return ingredients;
        } catch (IOException e) {
            LOGGER.e(e, "Failed to load recipe ingredients");
            return Collections.emptyList();
        } finally {
            recipeIngredientsStore.close();
        }
    }

    @Override
    protected void onPostExecute(List<Ingredient> ingredients) {
        ingredientListProgress.setVisibility(View.GONE);
        Properties properties = new Properties(ingredientRowAdapter.getContext());
        Set<String> blacklistedIngredients = properties.getBlacklistedIngredients();

        for (Ingredient ingredient : ingredients) {
            if (blacklistedIngredients.contains(ingredient.getName())) {
                ingredient.setChecked(false);
            }

            ingredientRowAdapter.add(ingredient);
        }
    }

    private List<Ingredient> loadIngredients(final String recipeName) throws IOException {
        final String escapedRecipeName = UrlEscapers.urlPathSegmentEscaper().escape(recipeName);
        final String urlString = String.format(URL_PATTERN, escapedRecipeName);
        final URL url = new URL(urlString);
        LOGGER.d("Loading ingredients from %s", url);

        final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }

        final Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
        final TypeToken<ArrayList<Ingredient>> typeToken =
                new TypeToken<ArrayList<Ingredient>>() { };
        return GSON.fromJson(reader, typeToken.getType());
    }
}
