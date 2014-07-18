package de.anycook.app.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class SQLiteDB extends SQLiteOpenHelper{
    private static final String DB_NAME;
    private static final int DB_VERSION;

    public static final String INGREDIENT_TABLE;
    public static final String GROCERY_LIST_TABLE;
    public static final String RECIPE_TABLE;

    static {
        DB_NAME = "einkaufszettel.db";
        DB_VERSION = 3;

        INGREDIENT_TABLE = "Ingredient";
        GROCERY_LIST_TABLE = "GroceryList";
        RECIPE_TABLE = "Recipe";
    }

    public static class TableFields {
        public static final int INGREDIENT_NAME = 0,
                GROCERY_LIST_NAME = 0,
                GROCERY_LIST_AMOUNT = 1,
                GROCERY_LIST_STROKE = 2,
                RECIPE_NAME = 0,
                RECIPE_DESCRIPTION = 1,
                RECIPE_IMAGE = 2;

    }

    public SQLiteDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //enable foreign key support
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, local INTEGER(1) NOT NULL DEFAULT 0);",
                INGREDIENT_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
            "amount INTEGER NOT NULL, " +
            "stroke INTEGER(1) NOT NULL DEFAULT 0," +
            "orderId INTEGER NOT NULL," +
            "FOREIGN KEY(name) REFERENCES %s(name));", GROCERY_LIST_TABLE, INGREDIENT_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
                "description TEXT," +
                "image VARCHAR(100));", RECIPE_TABLE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTablePattern = "DROP TABLE IF EXISTS %s";
        db.execSQL(String.format(dropTablePattern, GROCERY_LIST_TABLE));
        db.execSQL(String.format(dropTablePattern, INGREDIENT_TABLE));
        db.execSQL(String.format(dropTablePattern, RECIPE_TABLE));
        onCreate(db);
    }
}
