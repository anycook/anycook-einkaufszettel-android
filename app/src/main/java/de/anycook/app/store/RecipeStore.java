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
    private final SQLiteDatabase db;

    public RecipeStore(Context context) {
        SQLiteDB sqLiteDB = new SQLiteDB(context);
        db = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        db.close();
    }

    public Cursor autocompleteIngredients(CharSequence constraint) {
        return db.rawQuery("SELECT name AS _id FROM "+SQLiteDB.RECIPE_TABLE + " WHERE name LIKE ?",
                new String[]{constraint+"%"});
    }


    public Cursor getAllRecipesCursor() {
        return db.rawQuery("SELECT name AS _id, description, image FROM " + SQLiteDB.RECIPE_TABLE, null);
    }

    public Cursor getRecipesForQuery(String query) {
        return db.rawQuery("SELECT name AS _id, description, image FROM " + SQLiteDB.RECIPE_TABLE +
                " WHERE _id LIKE ?", new String[]{"%"+query+"%"});
    }

    public void replaceRecipes(List<RecipeResponse> recipeResponses) {
        Log.d(RecipeStore.class.getSimpleName(), "Replacing recipes in DB");
        db.delete(SQLiteDB.RECIPE_TABLE, null, null);
        for (RecipeResponse recipeResponse : recipeResponses) {
            ContentValues values = new ContentValues();
            values.put("name", recipeResponse.getName());
            values.put("description", recipeResponse.getDescription());
            values.put("image", recipeResponse.getImage());
            db.insert(SQLiteDB.RECIPE_TABLE, null, values);
        }
    }

    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String name) {
            super(String.format("Item %s does not exist", name));
        }
    }
}
