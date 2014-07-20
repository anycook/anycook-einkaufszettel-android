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
    private final SQLiteDatabase db;

    public IngredientStore(Context context) {
        SQLiteDB sqLiteDB = new SQLiteDB(context);
        db = sqLiteDB.getWritableDatabase();
    }

    public void addIngredient(String name){
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insertWithOnConflict(SQLiteDB.INGREDIENT_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean ingredientExists(String name) {
        Cursor cursor = db.query(SQLiteDB.INGREDIENT_TABLE, new String[]{"name"}, "name=?", new String[]{name},
                null, null, null);
        return cursor.getCount() > 0;
    }

    @Override
    public void close() {
        db.close();
    }

    public Cursor autocompleteIngredients(CharSequence constraint) {
        return db.rawQuery("SELECT name AS _id FROM "+SQLiteDB.INGREDIENT_TABLE + " WHERE name LIKE ?",
                new String[]{constraint+"%"});
    }

    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String name) {
            super(String.format("Item %s does not exist", name));
        }
    }
}
