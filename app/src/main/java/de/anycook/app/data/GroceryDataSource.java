package de.anycook.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * groceryItems datasource
 * <p/>
 * Created by cipo7741 on 02.07.14.
 */
public class GroceryDataSource {

    // Database fields
    private SQLiteDatabase database;
    private GrocerySQLiteHelper dbHelper;
    private String[] allColumns = {GrocerySQLiteHelper.COLUMN_ID,
            GrocerySQLiteHelper.COLUMN_ITEM, GrocerySQLiteHelper.COLUMN_AMOUNT, String.valueOf(GrocerySQLiteHelper.COLUMN_STROKE)};

    public GroceryDataSource(Context context) {
        dbHelper = new GrocerySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public GroceryItem createGroceryItem(GroceryItem groceryItem) {
        ContentValues values = new ContentValues();
        values.put(GrocerySQLiteHelper.COLUMN_ITEM, groceryItem.getName());
        values.put(GrocerySQLiteHelper.COLUMN_AMOUNT, groceryItem.getAmount());
        values.put(String.valueOf(GrocerySQLiteHelper.COLUMN_STROKE), groceryItem.isStroked());
        long insertId = database.insert(GrocerySQLiteHelper.TABLE_GROCERY, null,
                values);
        Cursor cursor = database.query(GrocerySQLiteHelper.TABLE_GROCERY,
                allColumns, GrocerySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        GroceryItem newGroceryItem = cursorToGroceryItem(cursor);
        cursor.close();
        return newGroceryItem;
    }

    public void strokeGroceryItem(GroceryItem groceryItem) {
        ContentValues values = new ContentValues();
        values.put(GrocerySQLiteHelper.COLUMN_ITEM, groceryItem.getName());
        values.put(GrocerySQLiteHelper.COLUMN_AMOUNT, groceryItem.getAmount());
        values.put(String.valueOf(GrocerySQLiteHelper.COLUMN_STROKE), !groceryItem.isStroked());
        database.update(GrocerySQLiteHelper.TABLE_GROCERY, values, GrocerySQLiteHelper.COLUMN_ID + "=" + groceryItem.getId(), null);
    }

    public void deleteGroceryItem(GroceryItem groceryItem) {
        long id = groceryItem.getId();
        System.out.println("GroceryItem deleted with id: " + id);
        database.delete(GrocerySQLiteHelper.TABLE_GROCERY, GrocerySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<GroceryItem> getAllGroceryItems() {
        List<GroceryItem> GroceryItems = new ArrayList<>();

        Cursor cursor = database.query(GrocerySQLiteHelper.TABLE_GROCERY,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GroceryItem groceryItem = cursorToGroceryItem(cursor);
            GroceryItems.add(0,groceryItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return GroceryItems;
    }

    private GroceryItem cursorToGroceryItem(Cursor cursor) {
        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setId(cursor.getLong(0));
        groceryItem.setName(cursor.getString(1));
        groceryItem.setAmount(cursor.getString(2));
        groceryItem.setStroked(Boolean.getBoolean(cursor.getString(3)));
        return groceryItem;
    }
} 
