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

package de.anycook.einkaufszettel.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.model.Step;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeStepsStore implements Closeable {

    private static final Logger LOGGER = LoggerManager.getLogger();
    private static final String[] STEP_COLUMNS = new String[]{"recipeName", "id", "text"};
    private static final String[] STEP_INGREDIENT_COLUMNS = new String[]{"name", "amount"};

    private final Context context;
    private SQLiteDatabase database;

    public RecipeStepsStore(final Context context) {
        this.context = context;
    }

    public void open() {
        LOGGER.d("Open Database");
        final SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        LOGGER.d("Closing Database");
        database.close();
    }

    public List<Step> getSteps(final String recipeName) {
        final Cursor cursor =
                database.query(SQLiteDB.RECIPE_STEPS_TABLE, STEP_COLUMNS, "recipeName = ?",
                               new String[]{recipeName}, null, null, "id");

        try {
            final List<Step> steps = new LinkedList<>();
            while (cursor.moveToNext()) {
                final Step step = new Step();
                step.setId(cursor.getInt(SQLiteDB.TableFields.RECIPE_STEPS_ID));
                step.setText(cursor.getString(SQLiteDB.TableFields.RECIPE_STEPS_TEXT));

                final Cursor ingredientCursor =
                        database.query(SQLiteDB.RECIPE_STEPS_INGREDIENTS_TABLE,
                                       STEP_INGREDIENT_COLUMNS, "recipeName = ? AND stepId = ?",
                                       new String[]{recipeName, Integer.toString(step.getId())},
                                       null, null, "name");
                try {
                    while (ingredientCursor.moveToNext()) {
                        final Ingredient ingredient = new Ingredient();
                        ingredient.setName(ingredientCursor.getString(0));
                        ingredient.setAmount(ingredientCursor.getString(1));
                        step.addIngredient(ingredient);
                    }
                } finally {
                    ingredientCursor.close();
                }
                steps.add(step);
            }

            return steps;
        } finally {
            cursor.close();
        }
    }

    private void removeSteps(final String recipeName) {
        final String[] args = new String[]{recipeName};
        database.delete(SQLiteDB.RECIPE_STEPS_TABLE, "recipeName = ?", args);
        database.delete(SQLiteDB.RECIPE_STEPS_INGREDIENTS_TABLE, "recipeName = ?", args);
    }

    public void addSteps(final String recipeName, final List<Step> steps) {
        LOGGER.d("adding steps of %s to db", recipeName);
        removeSteps(recipeName);

        final ContentValues values = new ContentValues();
        values.put("recipeName", recipeName);
        for (final Step step : steps) {
            values.put("id", step.getId());
            values.put("text", step.getText().trim());
            database.insert(SQLiteDB.RECIPE_STEPS_TABLE, null, values);

            final ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipeName", recipeName);
            ingredientValues.put("stepId", step.getId());
            for (final Ingredient ingredient : step.getIngredients()) {
                ingredientValues.put("name", ingredient.getName());
                ingredientValues.put("amount", ingredient.getAmount());
                database.insert(SQLiteDB.RECIPE_STEPS_INGREDIENTS_TABLE, null, ingredientValues);
            }
        }
    }


}
