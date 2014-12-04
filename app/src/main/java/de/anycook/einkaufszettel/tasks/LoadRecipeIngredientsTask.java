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

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.adapter.IngredientRowAdapter;
import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.store.RecipeIngredientsStore;
import de.anycook.einkaufszettel.util.Properties;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
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

    static {
        URL_PATTERN = "https://api.anycook.de/recipe/%s/ingredients";
        LOGGER = LoggerManager.getLogger();
    }

    private final IngredientRowAdapter ingredientRowAdapter;
    private final LinearLayout ingredientListProgress;
    private final Context context;

    public LoadRecipeIngredientsTask(IngredientRowAdapter ingredientRowAdapter, Activity activity) {
        this.ingredientRowAdapter = ingredientRowAdapter;
        this.ingredientListProgress = (LinearLayout) activity.findViewById(R.id.ingredient_list_progress);
        this.context = activity;
    }

    @Override
    protected void onPreExecute() {
        ingredientListProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Ingredient> doInBackground(String... recipeNames) {
        String recipeName = recipeNames[0];
        RecipeIngredientsStore recipeIngredientsStore = new RecipeIngredientsStore(context);
        recipeIngredientsStore.open();
        try {
            //check DB first
            List<Ingredient> ingredients = recipeIngredientsStore.getIngredients(recipeName);

            if (ingredients.size() > 0) {
                return ingredients;
            }


            String urlString = String.format(URL_PATTERN, UrlEscapers.urlPathSegmentEscaper().escape(recipeName));
            URL url = new URL(urlString);
            LOGGER.d("Loading ingredients from %s", url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }
            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Ingredient>>() { } .getType();
            ingredients =  gson.fromJson(reader, collectionType);

            //add ingredients to DB
            recipeIngredientsStore.addIngredients(recipeName, ingredients);
            return ingredients;
        } catch (IOException e) {
            LOGGER.e("Failed to load recipe ingredients", e);
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
}
