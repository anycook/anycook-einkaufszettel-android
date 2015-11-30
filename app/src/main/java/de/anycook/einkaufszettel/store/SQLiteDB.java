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
            GROCERY_TABLE,
            RECIPE_TABLE,
            RECIPE_INGREDIENTS_TABLE,
            RECIPE_STEPS_TABLE;

    static {
        DB_NAME = "einkaufszettel.db";
        DB_VERSION = 7;

        INGREDIENT_NAME_TABLE = "Ingredient";
        GROCERY_TABLE = "GroceryList";
        RECIPE_TABLE = "Recipe";
        RECIPE_INGREDIENTS_TABLE = "RecipeIngredients";
        RECIPE_STEPS_TABLE = "RecipeSteps";
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
        createIngredientNameTable(db);
        createGroceryItemTable(db);
        createRecipeTable(db);
        createRecipeIngredientsTable(db);
        createRecipeStepsTable(db);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTablePattern = "DROP TABLE IF EXISTS %s";

        if (oldVersion < 4) {
            db.execSQL(String.format(dropTablePattern, INGREDIENT_NAME_TABLE));
            db.execSQL(String.format(dropTablePattern, GROCERY_TABLE));
            createIngredientNameTable(db);
            createGroceryItemTable(db);
        }
        db.execSQL(String.format(dropTablePattern, RECIPE_TABLE));
        db.execSQL(String.format(dropTablePattern, RECIPE_INGREDIENTS_TABLE));
        db.execSQL(String.format(dropTablePattern, RECIPE_STEPS_TABLE));
        createRecipeTable(db);
        createRecipeIngredientsTable(db);
        createRecipeStepsTable(db);

        SharedPreferences
                sharedPrefs =
                context.getSharedPreferences("update_data", Context.MODE_PRIVATE);
        sharedPrefs.edit().putLong("last_update", 0).putString("last-modified-recipes", null)
                .commit();

    }

    private void createIngredientNameTable(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, local INTEGER(1) "
                                 + "NOT NULL DEFAULT 0);", INGREDIENT_NAME_TABLE));
    }

    private void createGroceryItemTable(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, "
                                 + "amount VARCHAR(45) NOT NULL, "
                                 + "stroke INTEGER(1) NOT NULL DEFAULT 0,"
                                 + "orderId INTEGER NOT NULL,"
                                 + "FOREIGN KEY(name) REFERENCES %s(name));", GROCERY_TABLE,
                                 INGREDIENT_NAME_TABLE));
    }

    private void createRecipeTable(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, "
                                 + "description TEXT,"
                                 + "smallImage VARCHAR(100),"
                                 + "bigImage VARCHAR(100),"
                                 + "persons INTEGER NOT NULL,"
                                 + "timeStd INTEGER,"
                                 + "timeMin INTEGER,"
                                 + "category VARCHAR(45),"
                                 + "skill INTEGER,"
                                 + "calorie INTEGER,"
                                 + "vibrantColor INTEGER DEFAULT -1,"
                                 + "lastChange INTEGER);", RECIPE_TABLE));
    }

    private void createRecipeIngredientsTable(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s(recipeName VARCHAR(45),"
                                 + "ingredientName VARCHAR(45),"
                                 + "ingredientAmount VARCHAR(45), orderId INTEGER,"
                                 + "FOREIGN KEY(recipeName) REFERENCES %s(name),"
                                 + "FOREIGN KEY(ingredientName) REFERENCES %s(name),"
                                 + "PRIMARY KEY(recipeName, ingredientName));",
                                 RECIPE_INGREDIENTS_TABLE, RECIPE_TABLE, INGREDIENT_NAME_TABLE));
    }

    private void createRecipeStepsTable(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s(recipeName VARCHAR(45),"
                                 + "id INTEGER,"
                                 + "text TEXT,"
                                 + "FOREIGN KEY(recipeName) REFERENCES %s(name),"
                                 + "PRIMARY KEY(recipeName, id));",
                                 RECIPE_STEPS_TABLE, RECIPE_TABLE));
    }

    public static class TableFields {

        public static final int GROCERY_NAME = 0,
                GROCERY_AMOUNT = 1,
                GROCERY_STROKE = 2;
        public static final int RECIPE_NAME = 0,
                RECIPE_DESCRIPTION = 1,
                RECIPE_IMAGE_SMALL = 2,
                RECIPE_IMAGE_BIG = 3,
                RECIPE_PERSONS = 4,
                RECIPE_TIME_STD = 5,
                RECIPE_TIME_MIN = 6,
                RECIPE_CATEGORY = 7,
                RECIPE_SKILL = 8,
                RECIPE_CALORIE = 9,
                RECIPE_LAST_CHANGE = 10,
                RECIPE_VIBRANT_COLOR = 11;
        public static final int RECIPE_INGREDIENTS_RECIPE_NAME = 0,
                RECIPE_INGREDIENTS_NAME = 1,
                RECIPE_INGREDIENTS_AMOUNT = 2;
        public static final int RECIPE_STEPS_RECIPE_NAME = 0,
                RECIPE_STEPS_ID = 1,
                RECIPE_STEPS_TEXT = 2;
    }
}
