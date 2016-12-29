/*
 * This file is part of anycook Einkaufszettel
 *  Copyright (C) 2016 Jan Graßegger, Claudia Sichting
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see [http://www.gnu.org/licenses/].
 */

package de.anycook.einkaufszettel.tasks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import de.anycook.einkaufszettel.activities.RecipeActivity;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.store.ItemNotFoundException;
import de.anycook.einkaufszettel.store.RecipeStore;
import de.anycook.einkaufszettel.util.ConnectionStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadRecipeTask extends AsyncTask<Void, Void, RecipeResponse> {

    private static final Logger LOGGER = LoggerManager.getLogger();
    private static final SimpleDateFormat DATE_FORMAT;
    private static final String URL_PATTERN = "https://api.anycook.de/recipe/%s";

    static {
        DATE_FORMAT = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    private final RecipeActivity recipeActivity;
    private final String recipeName;

    public LoadRecipeTask(RecipeActivity recipeActivity, String recipeName) {
        this.recipeActivity = recipeActivity;
        this.recipeName = recipeName;
    }

    @Override
    protected RecipeResponse doInBackground(Void... b) {
        final RecipeStore recipeStore = new RecipeStore(recipeActivity);
        try {
            recipeStore.open();

            if (!ConnectionStatus.isConnected(recipeActivity)) {
                return recipeStore.get(recipeName);
            }

            final URL url = new URL(String.format(URL_PATTERN, recipeName));
            LOGGER.d("Trying to load recipe from %s", url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (recipeStore.exists(recipeName)) {
                final long lastModified = recipeStore.get(recipeName).getLastChange();
                httpURLConnection.setRequestProperty("If-Modified-Since",
                                                     DATE_FORMAT.format(lastModified));
            }

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED &&
                recipeStore.exists(recipeName)) {
                LOGGER.d("Recipe %s not modified.", recipeName);
                return recipeStore.get(recipeName);
            }

            final Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            final Gson gson = new Gson();
            final TypeToken<RecipeResponse> typeToken = new TypeToken<RecipeResponse>() {
            };
            final RecipeResponse recipeResponse = gson.fromJson(reader, typeToken.getType());
            recipeStore.replace(recipeResponse);
            return recipeResponse;
        } catch (IOException | ItemNotFoundException e) {
            LOGGER.e(e, "Failed to load recipe %s", recipeName);
            throw new RuntimeException("Failed to load recipe.");
        }
    }

    @Override
    protected void onPostExecute(RecipeResponse recipeResponse) {
        recipeActivity.setRecipe(recipeResponse);
    }
}

