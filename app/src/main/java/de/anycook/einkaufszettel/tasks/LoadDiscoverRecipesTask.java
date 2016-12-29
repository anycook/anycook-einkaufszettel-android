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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.adapter.RecipeArrayAdapter;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.util.ConnectionStatus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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

    private final RecipeArrayAdapter adapter;
    private final Activity activity;

    public LoadDiscoverRecipesTask(RecipeArrayAdapter adapter,
                                   Activity activity) {
        this.adapter = adapter;
        this.activity = activity;
    }

    @Override
    protected List<RecipeResponse> doInBackground(String... url) {
        if (!ConnectionStatus.isConnected(activity)) {
            LOGGER.d("failed to load recipes from %s", url[0]);
            showOfflineMessage();
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
            final TypeToken<ArrayList<RecipeResponse>> typeToken =
                    new TypeToken<ArrayList<RecipeResponse>>() { };
            return gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            LOGGER.e("failed to load recipes from " + url[0], e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<RecipeResponse> recipeResponses) {
        if (recipeResponses.size() == 0) {
            LOGGER.i("Didn't find any nearby recipes");
            activity.findViewById(R.id.textview_nothing_found).setVisibility(View.VISIBLE);
            activity.findViewById(android.R.id.empty).setVisibility(View.GONE);
        } else {
            LOGGER.d(String.format("Found %d different recipes", recipeResponses.size()));
            adapter.addRecipes(recipeResponses);
        }

    }

    private void showOfflineMessage() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ConnectionStatus
                        .showNoConnectionDialog(activity, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });
    }
}
