/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2015 Jan Graßegger, Claudia Sichting
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see [http://www.gnu.org/licenses/].
 */

package de.anycook.einkaufszettel.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import de.anycook.einkaufszettel.model.Step;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeStepsStore implements Closeable {

    private static final Logger LOGGER = LoggerManager.getLogger();

    private final Context context;
    private SQLiteDatabase database;

    public RecipeStepsStore(Context context) {
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

    public List<Step> getSteps(String recipeName) {
        Cursor cursor = database.query(SQLiteDB.RECIPE_STEPS_TABLE,
                                       new String[]{"recipeName", "id", "text"},
                                       "recipeName = ?", new String[]{recipeName}, null, null,
                                       "id");

        List<Step> steps = new LinkedList<>();
        while (cursor.moveToNext()) {
            Step step = new Step();
            step.setId(cursor.getInt(SQLiteDB.TableFields.RECIPE_STEPS_ID));
            step.setText(cursor.getString(SQLiteDB.TableFields.RECIPE_STEPS_TEXT));
            steps.add(step);
        }

        return steps;
    }

    public void removeSteps(String recipeName) {
        database.delete(SQLiteDB.RECIPE_STEPS_TABLE, "recipeName = ?", new String[]{recipeName});
    }

    public void addSteps(String recipeName, List<Step> steps) {
        LOGGER.d("adding steps of %s to db", recipeName);
        removeSteps(recipeName);

        ContentValues values = new ContentValues();
        values.put("recipeName", recipeName);
        for (Step step : steps) {
            values.put("id", step.getId());
            values.put("text", step.getText());
            database.insert(SQLiteDB.RECIPE_STEPS_TABLE, null, values);
        }
    }


}
