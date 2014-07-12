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

    public static final String GROCERY_TABLE;
    public static final String GROCERY_LIST_TABLE;

    static {
        DB_NAME = "einkaufszettel.db";
        DB_VERSION = 1;

        GROCERY_TABLE = "Grocery";
        GROCERY_LIST_TABLE = "GroceryList";
    }

    public static class TableFields {
        public static final int GROCERY_NAME = 0,
                GROCERY_LIST_NAME = 0,
                GROCERY_LIST_AMOUNT = 1,
                GROCERY_LIST_STROKE = 2;

    }

    public SQLiteDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO create tables and fill grocery table with data!
        //enable foreign key support
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY);", GROCERY_TABLE));
        db.execSQL(String.format("CREATE TABLE %s(name VARCHAR(45) PRIMARY KEY, " +
                "amount INTEGER NOT NULL, " +
                "stroke INTEGER(1) NOT NULL DEFAULT 0," +
                "FOREIGN KEY(name) REFERENCES %s(name));", GROCERY_LIST_TABLE, GROCERY_TABLE));
        /*LoadIngredientsTask loadIngredientsTask = new LoadIngredientsTask(this);
        loadIngredientsTask.execute(); */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", GROCERY_LIST_TABLE));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", GROCERY_TABLE));
        onCreate(db);
    }
}
