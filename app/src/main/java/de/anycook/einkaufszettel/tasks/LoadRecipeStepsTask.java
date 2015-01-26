/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2015 Jan Graßegger, Claudia Sichting
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
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.adapter.StepRowAdapter;
import de.anycook.einkaufszettel.model.Step;
import de.anycook.einkaufszettel.store.RecipeStepsStore;
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
public class LoadRecipeStepsTask extends AsyncTask<String, Void, List<Step>> {

    private static final String URL_PATTERN;
    private static final Logger LOGGER;

    static {
        URL_PATTERN = "https://api.anycook.de/recipe/%s/steps";
        LOGGER = LoggerManager.getLogger();
    }

    private final StepRowAdapter stepRowAdapter;
    private final Activity context;

    public LoadRecipeStepsTask(StepRowAdapter stepRowAdapter, View view, Activity context) {
        this.stepRowAdapter = stepRowAdapter;
        this.context = context;
    }

    @Override
    protected List<Step> doInBackground(String... recipeNames) {
        String recipeName = recipeNames[0];
        RecipeStepsStore recipeStepsStore = new RecipeStepsStore(context);
        recipeStepsStore.open();
        try {
            //check DB first
            List<Step> steps = recipeStepsStore.getSteps(recipeName);
            if (steps.size() > 0) {
                return steps;
            }

            // if ingredient not in db check if internet connection is available
            // if not stop activity
            if (!ConnectionStatus.isConnected(context)) {
                shownOfflineMessage();
                return steps;
            }

            String urlString = String.format(URL_PATTERN, UrlEscapers.urlPathSegmentEscaper().escape(recipeName));
            URL url = new URL(urlString);
            LOGGER.d("Loading steps from %s", url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }
            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Step>>() { } .getType();
            steps =  gson.fromJson(reader, collectionType);

            //add ingredients to DB
            recipeStepsStore.addSteps(recipeName, steps);
            return steps;
        } catch (IOException e) {
            LOGGER.e(e, "Failed to load recipe ingredients");
            return Collections.emptyList();
        } finally {
            recipeStepsStore.close();
        }
    }

    @Override
    protected void onPostExecute(List<Step> steps) {
        for (Step step : steps) {
            stepRowAdapter.add(step);
        }
    }

    private void shownOfflineMessage() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ConnectionStatus.showNoConnectionDialog(context, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                });
            }
        });
    }
}
