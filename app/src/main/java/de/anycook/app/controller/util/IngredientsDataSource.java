package de.anycook.app.controller.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import de.anycook.app.adapter.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cipo7741 on 02.07.14.
 */
public class IngredientsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private IngredientSQLiteHelper dbHelper;
    private String[] allColumns = {IngredientSQLiteHelper.COLUMN_ID,
            IngredientSQLiteHelper.COLUMN_INGREDIENT, IngredientSQLiteHelper.COLUMN_AMOUNT, String.valueOf(IngredientSQLiteHelper.COLUMN_STROKE)};

    public IngredientsDataSource(Context context) {
        dbHelper = new IngredientSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Ingredient createIngredient(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put(IngredientSQLiteHelper.COLUMN_INGREDIENT, ingredient.getName());
        values.put(IngredientSQLiteHelper.COLUMN_AMOUNT, ingredient.getAmount());
        values.put(String.valueOf(IngredientSQLiteHelper.COLUMN_STROKE), ingredient.getStruckOut());
        long insertId = database.insert(IngredientSQLiteHelper.TABLE_INGREDIENTS, null,
                values);
        Cursor cursor = database.query(IngredientSQLiteHelper.TABLE_INGREDIENTS,
                allColumns, IngredientSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Ingredient newIngredient = cursorToIngredient(cursor);
        cursor.close();
        return newIngredient;
    }

    public void deleteIngredient(Ingredient ingredient) {
        long id = ingredient.getId();
        System.out.println("Ingredient deleted with id: " + id);
        database.delete(IngredientSQLiteHelper.TABLE_INGREDIENTS, IngredientSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> Ingredients = new ArrayList<>();

        Cursor cursor = database.query(IngredientSQLiteHelper.TABLE_INGREDIENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursorToIngredient(cursor);
            Ingredients.add(ingredient);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Ingredients;
    }

    private Ingredient cursorToIngredient(Cursor cursor) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(cursor.getLong(0));
        ingredient.setName(cursor.getString(1));
        ingredient.setAmount(cursor.getString(2));
        ingredient.setStruckOut(Boolean.getBoolean(cursor.getString(3)));
        return ingredient;
    }
} 
