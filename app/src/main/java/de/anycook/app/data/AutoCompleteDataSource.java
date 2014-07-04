package de.anycook.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * suggestions datasource
 * <p/>
 * Created by cipo7741 on 02.07.14.
 */
public class AutoCompleteDataSource {

    // Database fields
    private SQLiteDatabase database;
    private AutoCompleteSQLiteHelper dbHelper;
    private String[] allColumns = {AutoCompleteSQLiteHelper.COLUMN_ID,
            AutoCompleteSQLiteHelper.COLUMN_SUGGESTION};

    public AutoCompleteDataSource(Context context) {
        dbHelper = new AutoCompleteSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Suggestion createSuggestion(Suggestion suggestion) {
        ContentValues values = new ContentValues();
        values.put(AutoCompleteSQLiteHelper.COLUMN_SUGGESTION, suggestion.getName());
        long insertId = database.insert(AutoCompleteSQLiteHelper.TABLE_AUTOCOMPLETE, null,
                values);
        Cursor cursor = database.query(AutoCompleteSQLiteHelper.TABLE_AUTOCOMPLETE,
                allColumns, AutoCompleteSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Suggestion newSuggestion = cursorToSuggestion(cursor);
        cursor.close();
        return newSuggestion;
    }

    public void deleteSuggestion(Suggestion suggestion) {
        long id = suggestion.getId();
        System.out.println("String deleted with id: " + id);
        database.delete(AutoCompleteSQLiteHelper.TABLE_AUTOCOMPLETE, AutoCompleteSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<String> getAllSuggestions() {
        List<String> stringArrayList = new ArrayList<>();

        Cursor cursor = database.query(AutoCompleteSQLiteHelper.TABLE_AUTOCOMPLETE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Suggestion suggestion = cursorToSuggestion(cursor);
            stringArrayList.add(suggestion.getName());
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stringArrayList;
    }

    private Suggestion cursorToSuggestion(Cursor cursor) {
        Suggestion suggestion = new Suggestion();
        suggestion.setId(cursor.getLong(0));
        suggestion.setName(cursor.getString(1));
        return suggestion;
    }
} 
