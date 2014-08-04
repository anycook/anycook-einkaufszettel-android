package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.app.model.Ingredient;

import java.io.Closeable;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class IngredientNameStore implements Closeable {
    private static final Logger LOGGER = LoggerManager.getLogger();

    private final Context context;
    private SQLiteDatabase database;

    public IngredientNameStore(Context context) {
        this.context = context;
    }

    public void open() {
        LOGGER.v("Open database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        LOGGER.v("Close database");
        database.close();
    }

    public void addIngredient(Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName());
        database.insertWithOnConflict(SQLiteDB.INGREDIENT_NAME_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean ingredientExists(String name) {
        Cursor cursor = database.query(SQLiteDB.INGREDIENT_NAME_TABLE, new String[]{"name"}, "name=?",
                new String[]{name}, null, null, null);
        return cursor.getCount() > 0;
    }

    public Cursor autocompleteIngredients(CharSequence constraint) {
        String query = String.format("SELECT name AS _id FROM %s WHERE name LIKE ?", SQLiteDB.INGREDIENT_NAME_TABLE);
        return database.rawQuery(query, new String[]{constraint + "%"});
    }
}
