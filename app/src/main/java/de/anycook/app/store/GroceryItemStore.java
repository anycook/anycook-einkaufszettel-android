package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.anycook.app.model.GroceryItem;

import java.io.Closeable;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class GroceryItemStore implements Closeable{
    private final SQLiteDatabase db;

    public GroceryItemStore(Context context) {
        SQLiteDB sqLiteDB = new SQLiteDB(context);
        db = sqLiteDB.getWritableDatabase();
    }

    public void addGroceryListItem(GroceryItem groceryItem) {
        //TODO needs to check if already exists
        ContentValues values = new ContentValues();
        values.put("name", groceryItem.getName());
        values.put("amount", groceryItem.getAmount());
        values.put("stroke", groceryItem.isStroked() ? 1 : 0);

        db.insert(SQLiteDB.GROCERY_LIST_TABLE, null, values);
    }

    public void strokeGroceryListItem(CharSequence groceryItemName, boolean stroke) {
        ContentValues values = new ContentValues();
        values.put("stroke", stroke);

        db.update(SQLiteDB.GROCERY_LIST_TABLE, values,
                String.format("name=\"%s\"", groceryItemName), null);
    }

    public void addIngredient(String name){

        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insertWithOnConflict(SQLiteDB.GROCERY_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Cursor getAllIngredientsCursor() {
        return db.rawQuery("SELECT name AS _id FROM "+SQLiteDB.GROCERY_TABLE, null);
    }

    public Cursor getAllGroceryItemsCursor() {
        return db.rawQuery("SELECT name AS _id, amount, stroke FROM "+ SQLiteDB.GROCERY_LIST_TABLE, null);
    }

    public void deleteAllGroceryListItems() {
        db.delete(SQLiteDB.GROCERY_LIST_TABLE, null, null);
    }

    @Override
    public void close() {
        db.close();
    }

    public Cursor autocompleteIngredients(CharSequence constraint) {
        return db.rawQuery("SELECT name AS _id FROM "+SQLiteDB.GROCERY_TABLE + " WHERE name LIKE ?",
                new String[]{constraint+"%"});
    }
}
