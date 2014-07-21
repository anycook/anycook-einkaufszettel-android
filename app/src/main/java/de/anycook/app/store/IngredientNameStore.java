package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.anycook.app.model.Ingredient;

import java.io.Closeable;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class IngredientNameStore implements Closeable{
    private final Context context;
    private SQLiteDatabase database;

    public IngredientNameStore(Context context) {
        this.context = context;
    }

    public void open() {
        Log.v(getClass().getSimpleName(), "Open database");
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        Log.v(getClass().getSimpleName(),"Close database");
        database.close();
    }

    public void addIngredient(Ingredient ingredient){
        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName());
        database.insertWithOnConflict(SQLiteDB.INGREDIENT_NAME_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean ingredientExists(String name) {
        Cursor cursor = database.query(SQLiteDB.INGREDIENT_NAME_TABLE, new String[]{"name"}, "name=?", new String[]{name},
                null, null, null);
        return cursor.getCount() > 0;
    }

    public Cursor autocompleteIngredients(CharSequence constraint) {
        return database.rawQuery("SELECT name AS _id FROM "+SQLiteDB.INGREDIENT_NAME_TABLE + " WHERE name LIKE ?",
                new String[]{constraint+"%"});
    }
}
