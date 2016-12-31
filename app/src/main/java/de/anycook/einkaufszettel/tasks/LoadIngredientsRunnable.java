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

import android.content.Context;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.store.RecipeIngredientNameStore;

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
public class LoadIngredientsRunnable implements Runnable {

    private static final Logger LOGGER = LoggerManager.getLogger();

    private static URL url;

    static {
        try {
            url = new URL("https://api.anycook.de/ingredient");
        } catch (MalformedURLException e) {
            LOGGER.e("Failed to init url", e);
        }
    }

    private final Context context;

    public LoadIngredientsRunnable(Context context) {
        this.context = context;
    }

    private List<Ingredient> loadIngredients() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }
            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            final TypeToken<ArrayList<Ingredient>> typeToken =
                    new TypeToken<ArrayList<Ingredient>>() { };
            return gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            LOGGER.e(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public void run() {
        final List<Ingredient> ingredients = loadIngredients();

        RecipeIngredientNameStore ingredientDatabase = new RecipeIngredientNameStore(context);
        try {
            ingredientDatabase.open();
            for (Ingredient ingredient : ingredients) {
                ingredientDatabase.addIngredient(ingredient);
            }
        } finally {
            ingredientDatabase.close();
        }
    }
}
