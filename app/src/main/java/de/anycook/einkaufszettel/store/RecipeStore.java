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

import de.anycook.einkaufszettel.model.RecipeResponse;

import java.io.Closeable;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeStore implements Closeable {

    private static final Logger LOGGER = LoggerManager.getLogger();

    private final Context context;
    private SQLiteDatabase database;

    public RecipeStore(Context context) {
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

    public boolean empty() {
        final Cursor cursor = database.query(SQLiteDB.RECIPE_TABLE, new String[]{"name"}, null,
                                             null, null, null, null, "1");
        try {
            return !cursor.moveToNext();
        } finally {
            cursor.close();
        }
    }

    public Cursor getRecipesForQuery(String like) {
        final String query =
                String.format("SELECT name AS _id, description, smallImage, bigImage, persons, "
                              + "timeStd, timeMin, lastChange FROM %s WHERE _id LIKE ?",
                              SQLiteDB.RECIPE_TABLE);
        return database.rawQuery(query, new String[]{"%" + like + "%"});
    }

    private boolean checkRecipe(String name) {
        Cursor cursor = database.query(SQLiteDB.RECIPE_TABLE, new String[]{"name"}, "name = ?",
                                       new String[]{name}, null, null, null, "1");
        try {
            return cursor.getCount() == 1;
        } finally {
            cursor.close();
        }
    }

    public RecipeResponse getRecipe(final String name) throws ItemNotFoundException {
        final RecipeResponse recipe = new RecipeResponse();
        final String query = String.format(
                "SELECT name AS _id, description, smallImage, bigImage, persons, timeStd, "
                + "timeMin, category, skill, calorie, tasteNum, lastChange FROM %s WHERE _id = ?",
                SQLiteDB.RECIPE_TABLE);
        final Cursor cursor = database.rawQuery(query, new String[]{name});

        try {
            if (!cursor.moveToNext()) {
                throw new ItemNotFoundException(name);
            }

            recipe.setName(cursor.getString(SQLiteDB.TableFields.RECIPE_NAME));
            recipe.setDescription(cursor.getString(SQLiteDB.TableFields.RECIPE_DESCRIPTION));
            recipe.setPersons(cursor.getInt(SQLiteDB.TableFields.RECIPE_PERSONS));

            final RecipeResponse.Image image = new RecipeResponse.Image();
            image.setSmall(cursor.getString(SQLiteDB.TableFields.RECIPE_IMAGE_SMALL));
            image.setBig(cursor.getString(SQLiteDB.TableFields.RECIPE_IMAGE_BIG));
            recipe.setImage(image);

            final RecipeResponse.Time time = new RecipeResponse.Time();
            time.setStd(cursor.getInt(SQLiteDB.TableFields.RECIPE_TIME_STD));
            time.setMin(cursor.getInt(SQLiteDB.TableFields.RECIPE_TIME_MIN));
            recipe.setTime(time);

            recipe.setCategory(cursor.getString(SQLiteDB.TableFields.RECIPE_CATEGORY));
            recipe.setSkill(cursor.getInt(SQLiteDB.TableFields.RECIPE_SKILL));
            recipe.setCalorie(cursor.getInt(SQLiteDB.TableFields.RECIPE_CALORIE));
            recipe.setTasteNum(cursor.getInt(SQLiteDB.TableFields.RECIPE_TASTE_NUM));

            recipe.setLastChange(cursor.getLong(SQLiteDB.TableFields.RECIPE_LAST_CHANGE));

            return recipe;
        } finally {
            cursor.close();
        }
    }

    public int getVibrantColor(String name) {
        Cursor cursor = database.query(SQLiteDB.RECIPE_TABLE, new String[]{"vibrantColor"},
                                       "name = ?", new String[]{name}, null, null, null);
        try {
            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            }

            return -1;
        } finally {
            cursor.close();
        }
    }

    public void putVibrantColor(String name, int color) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("vibrantColor", color);

        database.update(SQLiteDB.RECIPE_TABLE, contentValues, "name = ?", new String[]{name});
    }


    public void replaceRecipes(List<RecipeResponse> recipeResponses) {
        LOGGER.d("Replacing recipes in DB");

        RecipeIngredientsStore recipeIngredientsStore = new RecipeIngredientsStore(context);
        recipeIngredientsStore.open();

        try {
            for (RecipeResponse recipeResponse : recipeResponses) {
                recipeIngredientsStore.removeIngredients(recipeResponse.getName());

                ContentValues values = new ContentValues();
                values.put("description", recipeResponse.getDescription());
                values.put("smallImage", recipeResponse.getImage().getSmall());
                values.put("bigImage", recipeResponse.getImage().getBig());
                values.put("persons", recipeResponse.getPersons());
                values.put("timeStd", recipeResponse.getTime().getStd());
                values.put("timeMin", recipeResponse.getTime().getMin());
                values.put("category", recipeResponse.getCategory());
                values.put("skill", recipeResponse.getSkill());
                values.put("calorie", recipeResponse.getCalorie());
                values.put("tasteNum", recipeResponse.getTasteNum());
                values.put("lastChange", recipeResponse.getLastChange());

                if (!checkRecipe(recipeResponse.getName())) {
                    values.put("name", recipeResponse.getName());
                    database.insert(SQLiteDB.RECIPE_TABLE, null, values);
                } else {
                    database.update(SQLiteDB.RECIPE_TABLE, values, "name = ?",
                                    new String[]{recipeResponse.getName()});
                }
            }
        } finally {
            recipeIngredientsStore.close();
        }

    }
}
