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
import android.os.AsyncTask;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.adapter.RecipeRowArrayAdapter;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.util.ConnectionStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadDiscoverRecipesTask extends AsyncTask<String, Void, List<RecipeResponse>> {
    private static final Logger LOGGER = LoggerManager.getLogger();

    private final RecipeRowArrayAdapter adapter;
    private final Activity activity;

    public LoadDiscoverRecipesTask(RecipeRowArrayAdapter adapter,
                                   Activity activity) {
        this.adapter = adapter;
        this.activity = activity;
    }

    @Override
    protected List<RecipeResponse> doInBackground(String... url) {
        if (!ConnectionStatus.isConnected(activity)) {
            ConnectionStatus.showOfflineMessage(activity);
            return Collections.emptyList();
        }

        try {
            URL recipesUrl = new URL(url[0]);
            LOGGER.v("Trying to load recipes from %s", url[0]);

            HttpURLConnection httpURLConnection = (HttpURLConnection) recipesUrl.openConnection();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }

            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<RecipeResponse>>() { } .getType();
            return gson.fromJson(reader, collectionType);
        } catch (IOException e) {
            LOGGER.e("failed to load recipes from " + url[0], e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<RecipeResponse> recipeResponses) {
        if (recipeResponses.size() == 0) {
            LOGGER.i("Didn't find any nearby recipes");
            activity.findViewById(R.id.recipe_list_textview_nothing_found).setVisibility(View.VISIBLE);
            activity.findViewById(android.R.id.empty).setVisibility(View.GONE);
        } else {
            LOGGER.d(String.format("Found %d different recipes", recipeResponses.size()));
            adapter.setRecipes(recipeResponses);
        }

    }
}
