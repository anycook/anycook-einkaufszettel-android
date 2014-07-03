package de.anycook.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * ingredients datasource
 * <p/>
 * Created by cipo7741 on 02.07.14.
 */
public class IngredientsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private IngredientSQLiteHelper dbHelper;
    private String[] allColumns = {IngredientSQLiteHelper.COLUMN_ID,
            IngredientSQLiteHelper.COLUMN_INGREDIENT, IngredientSQLiteHelper.COLUMN_AMOUNT};

    public IngredientsDataSource(Context context) {
        dbHelper = new IngredientSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public GroceryItem createIngredient(GroceryItem ingredient) {
        ContentValues values = new ContentValues();
        values.put(IngredientSQLiteHelper.COLUMN_INGREDIENT, ingredient.getName());
        values.put(IngredientSQLiteHelper.COLUMN_AMOUNT, ingredient.getAmount());
        long insertId = database.insert(IngredientSQLiteHelper.TABLE_INGREDIENTS, null,
                values);
        Cursor cursor = database.query(IngredientSQLiteHelper.TABLE_INGREDIENTS,
                allColumns, IngredientSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        GroceryItem newGroceryItem = cursorToIngredient(cursor);
        cursor.close();
        return newGroceryItem;
    }

    public void deleteIngredient(GroceryItem ingredient) {
        long id = ingredient.getId();
        System.out.println("GroceryItem deleted with id: " + id);
        database.delete(IngredientSQLiteHelper.TABLE_INGREDIENTS, IngredientSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<GroceryItem> getAllIngredients() {
        List<GroceryItem> Ingredients = new ArrayList<>();

        Cursor cursor = database.query(IngredientSQLiteHelper.TABLE_INGREDIENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GroceryItem ingredient = cursorToIngredient(cursor);
            Ingredients.add(ingredient);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Ingredients;
    }

    private GroceryItem cursorToIngredient(Cursor cursor) {
        GroceryItem ingredient = new GroceryItem();
        ingredient.setId(cursor.getLong(0));
        ingredient.setName(cursor.getString(1));
        ingredient.setAmount(cursor.getString(2));
        return ingredient;
    }
} 
