package de.anycook.app.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.anycook.app.model.GroceryItem;
import de.anycook.app.model.Ingredient;

import java.io.Closeable;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class GroceryItemStore implements Closeable{
    private final SQLiteDatabase db;

    public GroceryItemStore(Context context) {
        SQLiteDB sqLiteDB = new SQLiteDB(context);
        db = sqLiteDB.getWritableDatabase();
    }

    public void addToGroceryList(String name, String amount) {
        if(!ingredientExists(name)) {
            addIngredient(name);
        }
        else {
            try {
                GroceryItem oldGroceryItem = getGroceryItem(name);

                //TODO do a more intelligent addition
                amount = String.format("%s + %s", amount, oldGroceryItem.getAmount());
            } catch (ItemNotFoundException e) {
                //nothing to do
            }
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("amount", amount);
        values.put("orderId", getMinOrderId()-1);

        db.insertWithOnConflict(SQLiteDB.GROCERY_LIST_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public GroceryItem getGroceryItem(String name) throws ItemNotFoundException {
        String[] columns = new String[]{"name", "amount", "stroke"};
        Cursor cursor = db.query(SQLiteDB.GROCERY_LIST_TABLE, columns, "name=?", new String[]{name}, null, null, null);
        if(cursor.moveToNext()) {
            GroceryItem item = new GroceryItem();
            item.setName(cursor.getString(SQLiteDB.TableFields.GROCERY_LIST_NAME));
            item.setAmount(cursor.getString(SQLiteDB.TableFields.GROCERY_LIST_AMOUNT));
            item.setStroked(cursor.getInt(SQLiteDB.TableFields.GROCERY_LIST_STROKE) > 0);
            return item;
        }

        throw new ItemNotFoundException(name);
    }

    public void strokeGroceryListItem(CharSequence groceryItemName, boolean stroke) {
        ContentValues values = new ContentValues();
        values.put("stroke", stroke);

        db.update(SQLiteDB.GROCERY_LIST_TABLE, values,
                String.format("name=\"%s\"", groceryItemName), null);
    }

    public void removeGroceryListItem(String groceryItemName) {
        db.delete(SQLiteDB.GROCERY_LIST_TABLE, "name=?", new String[]{groceryItemName});
    }

    public Cursor getStrokedListItems() {
        return db.query(SQLiteDB.GROCERY_LIST_TABLE, new String[]{"name"}, "stroke=1", null, null, null, null);
    }

    public void addIngredient(String name){

        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insertWithOnConflict(SQLiteDB.GROCERY_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean ingredientExists(String name) {
        Cursor cursor = db.query(SQLiteDB.GROCERY_TABLE, new String[]{"name"}, "name=?", new String[]{name},
                null, null, null);
        return cursor.getCount() > 0;
    }

    public Cursor getAllIngredientsCursor() {
        return db.rawQuery("SELECT name AS _id FROM "+SQLiteDB.GROCERY_TABLE, null);
    }

    public Cursor getAllGroceryItemsCursor() {
        return db.rawQuery("SELECT name AS _id, amount, stroke FROM "+ SQLiteDB.GROCERY_LIST_TABLE + " ORDER BY orderId"
                , null);
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

    private int getMinOrderId() {
        Cursor cursor = db.query(true, SQLiteDB.GROCERY_LIST_TABLE, new String[]{"MIN(orderId)"}, null ,
                null, null, null, null, "1");
        if (cursor.getCount() == 0) return 100000;
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    public void addIngredientsToGroceryList(List<Ingredient> ingredients) {
        int orderId = getMinOrderId() - ingredients.size();
        for (Ingredient ingredient : ingredients) {
            try {
                GroceryItem oldGroceryItem = getGroceryItem(ingredient.name);
                //TODO smarter addition
                ingredient.menge += " + " + oldGroceryItem.getAmount();
            } catch (ItemNotFoundException e) {
                //Nothing to do
            }


            ContentValues values = new ContentValues();
            values.put("name", ingredient.name);
            values.put("amount", ingredient.menge);
            values.put("orderId", orderId++);
            db.insertWithOnConflict(SQLiteDB.GROCERY_LIST_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }


    }

    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String name) {
            super(String.format("Item %s does not exist", name));
        }
    }
}
