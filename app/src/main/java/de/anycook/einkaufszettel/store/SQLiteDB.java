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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class SQLiteDB extends SQLiteOpenHelper {
    private static final String DB_NAME;
    private static final int DB_VERSION;

    public static final String INGREDIENT_NAME_TABLE,
        GROCERY_ITEM_TABLE,
        RECIPE_TABLE,
        RECIPE_INGREDIENTS_TABLE;


    static {
        DB_NAME = "einkaufszettel.db";
        DB_VERSION = 5;

        INGREDIENT_NAME_TABLE = "Ingredient";
        GROCERY_ITEM_TABLE = "GroceryList";
        RECIPE_TABLE = "Recipe";
        RECIPE_INGREDIENTS_TABLE = "RecipeIngredients";
    }

    public Context context;

    public SQLiteDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //enable foreign key support
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
                        "local INTEGER(1) NOT NULL DEFAULT 0);",
                INGREDIENT_NAME_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
            "amount VARCHAR(45) NOT NULL, " +
            "stroke INTEGER(1) NOT NULL DEFAULT 0," +
            "orderId INTEGER NOT NULL," +
            "FOREIGN KEY(name) REFERENCES %s(name));", GROCERY_ITEM_TABLE, INGREDIENT_NAME_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
            "description TEXT," +
            "smallImage VARCHAR(100)," +
            "bigImage VARCHAR(100)," +
            "persons INTEGER NOT NULL," +
            "timeStd INTEGER," +
            "timeMin INTEGER," +
            "lastChange INTEGER);", RECIPE_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(recipeName VARCHAR(45)," +
            "ingredientName VARCHAR(45)," +
            "ingredientAmount VARCHAR(45), orderId INTEGER," +
            "FOREIGN KEY(recipeName) REFERENCES %s(name)," +
            "FOREIGN KEY(ingredientName) REFERENCES %s(name)," +
            "PRIMARY KEY(recipeName, ingredientName));",
            RECIPE_INGREDIENTS_TABLE, RECIPE_TABLE, INGREDIENT_NAME_TABLE));
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTablePattern = "DROP TABLE IF EXISTS %s";
        db.execSQL(String.format(dropTablePattern, GROCERY_ITEM_TABLE));
        db.execSQL(String.format(dropTablePattern, INGREDIENT_NAME_TABLE));
        db.execSQL(String.format(dropTablePattern, RECIPE_TABLE));
        db.execSQL(String.format(dropTablePattern, RECIPE_INGREDIENTS_TABLE));
        onCreate(db);

        SharedPreferences sharedPrefs = context.getSharedPreferences("update_data", Context.MODE_PRIVATE);
        sharedPrefs.edit().putLong("last_update", 0).putString("last-modified-recipes", null).commit();

    }

    public static class TableFields {
        public static final int GROCERY_ITEM_NAME = 0,
            GROCERY_ITEM_AMOUNT = 1,
            GROCERY_ITEM_STROKE = 2;
        public static final int RECIPE_NAME = 0,
            RECIPE_DESCRIPTION = 1,
            RECIPE_IMAGE_SMALL = 2,
            RECIPE_IMAGE_BIG = 3,
            RECIPE_PERSONS = 4,
            RECIPE_TIME_STD = 5,
            RECIPE_TIME_MIN = 6,
            RECIPE_LAST_CHANGE = 7;
        public static final int RECIPE_INGREDIENTS_RECIPE_NAME = 0,
            RECIPE_INGREDIENTS_NAME = 1,
            RECIPE_INGREDIENTS_AMOUNT = 2,
            RECIPE_INGREDIENTS_ORDER_ID = 3;

    }
}
