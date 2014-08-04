package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.app.model.RecipeResponse;

import java.io.Closeable;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeStore implements Closeable {
    private static final Logger LOGGER = LoggerManager.getLogger();

    private final Context context;
    private SQLiteDatabase database;

    public RecipeStore(Context context) {
        this.context = context;
    }

    public void open() {
        LOGGER.d("Open Database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        LOGGER.d("Open Database");
        database.close();
    }

    public Cursor getRecipesForQuery(String like) {
        String query = String.format("SELECT name AS _id, description, image, persons FROM %s WHERE _id LIKE ?",
                SQLiteDB.RECIPE_TABLE);
        return database.rawQuery(query , new String[]{"%" + like + "%"});
    }

    public RecipeResponse getRecipe(String name) throws ItemNotFoundException {
        RecipeResponse recipe = new RecipeResponse();
        String query = String.format("SELECT name AS _id, description, image, persons FROM %s WHERE _id = ?",
                SQLiteDB.RECIPE_TABLE);
        Cursor cursor = database.rawQuery(query, new String[]{name});
        if (!cursor.moveToNext()) { throw new ItemNotFoundException(name); }

        recipe.setName(cursor.getString(SQLiteDB.TableFields.RECIPE_NAME));
        recipe.setDescription(cursor.getString(SQLiteDB.TableFields.RECIPE_DESCRIPTION));
        recipe.setPersons(cursor.getInt(SQLiteDB.TableFields.RECIPE_PERSONS));

        return recipe;
    }

    public void replaceRecipes(List<RecipeResponse> recipeResponses) {
        LOGGER.d("Replacing recipes in DB");
        database.delete(SQLiteDB.RECIPE_TABLE, null, null);
        for (RecipeResponse recipeResponse : recipeResponses) {
            ContentValues values = new ContentValues();
            values.put("name", recipeResponse.getName());
            values.put("description", recipeResponse.getDescription());
            values.put("image", recipeResponse.getImage());
            values.put("persons", recipeResponse.getPersons());
            database.insert(SQLiteDB.RECIPE_TABLE, null, values);
        }
    }
}
