package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.anycook.app.model.RecipeResponse;

import java.io.Closeable;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeStore implements Closeable{
    private final Context context;
    private SQLiteDatabase database;

    public RecipeStore(Context context) {
        this.context = context;
    }

    public void open() {
        Log.d(RecipeStore.class.getSimpleName(), "Open Database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        Log.d(RecipeStore.class.getSimpleName(), "Open Database");
        database.close();
    }

    public Cursor getAllRecipesCursor() {
        return database.rawQuery("SELECT name AS _id, description, image FROM " + SQLiteDB.RECIPE_TABLE, null);
    }

    public Cursor getRecipesForQuery(String query) {
        return database.rawQuery("SELECT name AS _id, description, image FROM " + SQLiteDB.RECIPE_TABLE +
                " WHERE _id LIKE ?", new String[]{"%"+query+"%"});
    }

    public void replaceRecipes(List<RecipeResponse> recipeResponses) {
        Log.d(RecipeStore.class.getSimpleName(), "Replacing recipes in DB");
        database.delete(SQLiteDB.RECIPE_TABLE, null, null);
        for (RecipeResponse recipeResponse : recipeResponses) {
            ContentValues values = new ContentValues();
            values.put("name", recipeResponse.getName());
            values.put("description", recipeResponse.getDescription());
            values.put("image", recipeResponse.getImage());
            database.insert(SQLiteDB.RECIPE_TABLE, null, values);
        }
    }
}
