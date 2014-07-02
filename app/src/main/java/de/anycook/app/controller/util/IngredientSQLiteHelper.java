package de.anycook.app.controller.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * database helper for
 * <p/>
 * Created by cipo7741 on 02.07.14.
 */
public class IngredientSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INGREDIENT = "ingredient";
    public static final String COLUMN_AMOUNT = "amount";
    public static final boolean COLUMN_STROKE = false;

    private static final String DATABASE_NAME = "ingredients.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_INGREDIENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_INGREDIENT
            + " text not null," + COLUMN_AMOUNT + " text not null," + COLUMN_STROKE + " boolean );";

    public IngredientSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(IngredientSQLiteHelper.class.getSimpleName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(sqLiteDatabase);
    }
}
