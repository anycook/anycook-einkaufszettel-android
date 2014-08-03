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
public class RecipeStore implements Closeable{
    private static final Logger logger = LoggerManager.getLogger();

    private final Context context;
    private SQLiteDatabase database;

    public RecipeStore(Context context) {
        this.context = context;
    }

    public void open() {
        logger.d("Open Database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        logger.d("Open Database");
        database.close();
    }

    public Cursor getRecipesForQuery(String query) {
        return database.rawQuery("SELECT name AS _id, description, image FROM " + SQLiteDB.RECIPE_TABLE +
                " WHERE _id LIKE ?", new String[]{"%"+query+"%"});
    }

    public void replaceRecipes(List<RecipeResponse> recipeResponses) {
        logger.d("Replacing recipes in DB");
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
