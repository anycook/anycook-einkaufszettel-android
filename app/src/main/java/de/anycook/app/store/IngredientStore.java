package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class IngredientStore implements Closeable{
    private final Context context;
    private SQLiteDatabase database;

    public IngredientStore(Context context) {
        this.context = context;
        open();
    }

    public void open() {
        SQLiteDB sqLiteDB = new SQLiteDB(this.context);
        database = sqLiteDB.getWritableDatabase();
    }

    @Override
    public void close() {
        database.close();
    }

    public void addIngredient(String name){
        ContentValues values = new ContentValues();
        values.put("name", name);
        database.insertWithOnConflict(SQLiteDB.INGREDIENT_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean ingredientExists(String name) {
        Cursor cursor = database.query(SQLiteDB.INGREDIENT_TABLE, new String[]{"name"}, "name=?", new String[]{name},
                null, null, null);
        return cursor.getCount() > 0;
    }

    public Cursor autocompleteIngredients(CharSequence constraint) {
        return database.rawQuery("SELECT name AS _id FROM "+SQLiteDB.INGREDIENT_TABLE + " WHERE name LIKE ?",
                new String[]{constraint+"%"});
    }
}
