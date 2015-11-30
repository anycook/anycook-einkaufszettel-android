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

package de.anycook.einkaufszettel.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import de.anycook.einkaufszettel.store.RecipeIngredientNameStore;
import de.anycook.einkaufszettel.store.SQLiteDB;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeIngredientAutocompleteAdapter extends ResourceCursorAdapter
        implements Closeable {

    private final RecipeIngredientNameStore ingredientNameStore;

    public RecipeIngredientAutocompleteAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line, null, true);
        ingredientNameStore = new RecipeIngredientNameStore(context);
        ingredientNameStore.open();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view;
        textView.setText(convertToString(cursor));
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        return ingredientNameStore.autocompleteIngredients(constraint);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(SQLiteDB.TableFields.GROCERY_NAME);
    }

    @Override
    public void close() throws IOException {
        ingredientNameStore.close();
    }
}
