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

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeIngredientNameStore implements Closeable {

    private static final Logger LOGGER = LoggerManager.getLogger();

    private final Context context;
    private SQLiteDatabase database;

    public RecipeIngredientNameStore(Context context) {
        this.context = context;
    }

    public void open() {
        LOGGER.v("Open database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        LOGGER.v("Close database");
        database.close();
    }

    public void addIngredient(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName());
        database.insertWithOnConflict(SQLiteDB.INGREDIENT_NAME_TABLE, null, values,
                                      SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean ingredientExists(String name) {
        Cursor cursor = database.query(SQLiteDB.INGREDIENT_NAME_TABLE,
                                       new String[]{"name"}, "name=?", new String[]{name},
                                       null, null, null);
        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
        }
    }

    public Cursor autocompleteIngredients(CharSequence constraint) {
        String
                query =
                String.format("SELECT name AS _id FROM %s WHERE name LIKE ?",
                              SQLiteDB.INGREDIENT_NAME_TABLE);
        return database.rawQuery(query, new String[]{constraint + "%"});
    }
}
