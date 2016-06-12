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

package de.anycook.einkaufszettel.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import de.anycook.einkaufszettel.model.Ingredient;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeIngredientsStore implements Closeable {

    private static final Logger LOGGER = LoggerManager.getLogger();

    private final Context context;
    private SQLiteDatabase database;

    public RecipeIngredientsStore(Context context) {
        this.context = context;
    }

    public void open() {
        LOGGER.d("Open Database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        LOGGER.d("Open Database");
        database.close();
    }

    public List<Ingredient> getIngredients(String recipeName) {
        Cursor cursor = database.query(SQLiteDB.RECIPE_INGREDIENTS_TABLE,
                                       new String[]{"recipeName", "ingredientName",
                                                    "ingredientAmount"},
                                       "recipeName = ?", new String[]{recipeName}, null, null,
                                       "orderId");

        try {
            List<Ingredient> ingredients = new LinkedList<>();
            while (cursor.moveToNext()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(cursor.getString(SQLiteDB.TableFields.RECIPE_INGREDIENTS_NAME));
                ingredient.setAmount(
                        cursor.getString(SQLiteDB.TableFields.RECIPE_INGREDIENTS_AMOUNT));
                ingredients.add(ingredient);
            }

            return ingredients;
        } finally {
            cursor.close();
        }
    }

    public void removeIngredients(String recipeName) {
        database.delete(SQLiteDB.RECIPE_INGREDIENTS_TABLE, "recipeName = ?",
                        new String[]{recipeName});
    }

    public void addIngredients(String recipeName, List<Ingredient> ingredients) {
        LOGGER.d("adding ingredients of %s to db", recipeName);
        removeIngredients(recipeName);

        ContentValues values = new ContentValues();
        values.put("recipeName", recipeName);
        int orderId = 0;
        for (Ingredient ingredient : ingredients) {
            values.put("ingredientName", ingredient.getName());
            values.put("ingredientAmount", ingredient.getAmount());
            values.put("orderId", orderId);
            database.insert(SQLiteDB.RECIPE_INGREDIENTS_TABLE, null, values);
        }
    }
}
