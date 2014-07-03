package de.anycook.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cipo7741 on 02.07.14.
 */
public class RecipesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private RecipeSQLiteHelper dbHelper;
    private String[] allColumns = {RecipeSQLiteHelper.COLUMN_ID,
            RecipeSQLiteHelper.COLUMN_RECIPE};

    public RecipesDataSource(Context context) {
        dbHelper = new RecipeSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Recipe createRecipe(String Recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeSQLiteHelper.COLUMN_RECIPE, Recipe);
        long insertId = database.insert(RecipeSQLiteHelper.TABLE_RECIPES, null,
                values);
        Cursor cursor = database.query(RecipeSQLiteHelper.TABLE_RECIPES,
                allColumns, RecipeSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        de.anycook.app.data.Recipe newRecipe = cursorToRecipe(cursor);
        cursor.close();
        return newRecipe;
    }

    public void deleteRecipe(Recipe recipe) {
        long id = recipe.getId();
        System.out.println("Recipe deleted with id: " + id);
        database.delete(RecipeSQLiteHelper.TABLE_RECIPES, RecipeSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> Recipes = new ArrayList<Recipe>();

        Cursor cursor = database.query(RecipeSQLiteHelper.TABLE_RECIPES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursorToRecipe(cursor);
            Recipes.add(recipe);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Recipes;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(0));
        recipe.setRecipeName(cursor.getString(1));
        return recipe;
    }
} 
