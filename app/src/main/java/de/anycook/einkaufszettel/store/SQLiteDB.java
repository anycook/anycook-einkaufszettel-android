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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class SQLiteDB extends SQLiteOpenHelper {
    private static final String DB_NAME;
    private static final int DB_VERSION;

    public static final String INGREDIENT_NAME_TABLE;
    public static final String GROCERY_ITEM_TABLE;
    public static final String RECIPE_TABLE;

    static {
        DB_NAME = "einkaufszettel.db";
        DB_VERSION = 4;

        INGREDIENT_NAME_TABLE = "Ingredient";
        GROCERY_ITEM_TABLE = "GroceryList";
        RECIPE_TABLE = "Recipe";
    }

    public SQLiteDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //enable foreign key support
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
                        "local INTEGER(1) NOT NULL DEFAULT 0);",
                INGREDIENT_NAME_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
            "amount INTEGER NOT NULL, " +
            "stroke INTEGER(1) NOT NULL DEFAULT 0," +
            "orderId INTEGER NOT NULL," +
            "FOREIGN KEY(name) REFERENCES %s(name));", GROCERY_ITEM_TABLE, INGREDIENT_NAME_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
                "description TEXT," +
                "image VARCHAR(100)," +
                "persons INTEGER NOT NULL);", RECIPE_TABLE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTablePattern = "DROP TABLE IF EXISTS %s";
        db.execSQL(String.format(dropTablePattern, GROCERY_ITEM_TABLE));
        db.execSQL(String.format(dropTablePattern, INGREDIENT_NAME_TABLE));
        db.execSQL(String.format(dropTablePattern, RECIPE_TABLE));
        onCreate(db);
    }

    public static class TableFields {
        public static final int GROCERY_ITEM_NAME = 0;
        public static final int GROCERY_ITEM_AMOUNT = 1;
        public static final int GROCERY_ITEM_STROKE = 2;
        public static final int RECIPE_NAME = 0;
        public static final int RECIPE_DESCRIPTION = 1;
        public static final int RECIPE_IMAGE = 2;
        public static final int RECIPE_PERSONS = 3;

    }
}
