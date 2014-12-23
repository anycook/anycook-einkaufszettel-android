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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.activities.StartupActivity;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.store.RecipeStore;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadRecipesTask extends AsyncTask<Void, Void, List<RecipeResponse>> {
    private static final Logger LOGGER = LoggerManager.getLogger();

    public static URL url;

    static {
        try {
            url = new URL("https://api.anycook.de/recipe");
        } catch (MalformedURLException e) {
            LOGGER.e("Failed to init url", e);
        }
    }

    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final StartupActivity.Callback callback;
    private final boolean emptyRecipes;

    public LoadRecipesTask(Context context, SharedPreferences sharedPreferences,
                           StartupActivity.Callback callback, boolean emptyRecipes) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.callback = callback;
        this.emptyRecipes = emptyRecipes;
    }

    @Override
    protected List<RecipeResponse> doInBackground(Void... b) {
        try {
            LOGGER.d("Trying to load recipes from %s", url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (sharedPreferences.contains("last-modified-recipes") && !emptyRecipes) {
                httpURLConnection.setRequestProperty("If-Modified-Since",
                    sharedPreferences.getString("last-modified-recipes", null));
            }

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }


            String newLastModified = httpURLConnection.getHeaderField("last-modified");
            if (newLastModified != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("last-modified-recipes", newLastModified);
                editor.apply();
            }

            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            TypeToken<ArrayList<RecipeResponse>> typeToken = new TypeToken<ArrayList<RecipeResponse>>() { };
            return gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            LOGGER.e("failed to load recipes from " + url, e);
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(final List<RecipeResponse> recipeResponses) {
        if (isCancelled()) { return; }

        if (recipeResponses == null || recipeResponses.size() == 0) {
            LOGGER.v("Didn't find any nearby recipes");
            callback.call(getStatus());
        } else {
            LOGGER.d(String.format("Found %d different recipes", recipeResponses.size()));
            RecipeStore recipeStore = new RecipeStore(context);
            try {
                recipeStore.open();
                recipeStore.replaceRecipes(recipeResponses);
            } finally {
                recipeStore.close();
                callback.call(getStatus());
            }
        }
    }

}
