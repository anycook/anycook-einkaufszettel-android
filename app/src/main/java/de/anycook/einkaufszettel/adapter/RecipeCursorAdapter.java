/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger, Claudia Sichting
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
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.store.SQLiteDB;
import de.anycook.einkaufszettel.tasks.DownloadImageViewTask;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 *
 * @author Jan Grassegger <jan@anycook.de>
 */
public class RecipeCursorAdapter extends ResourceCursorAdapter {

    public RecipeCursorAdapter(Context context) {
        this(context, null);
    }

    public RecipeCursorAdapter(Context context, Cursor cursor) {
        super(context, R.layout.recipe_row, cursor, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.textview_recipe_name);
        name.setText(cursor.getString(SQLiteDB.TableFields.RECIPE_NAME));

        TextView descriptionView = (TextView) view.findViewById(R.id.textview_description);
        descriptionView.setText(cursor.getString(SQLiteDB.TableFields.RECIPE_DESCRIPTION));

        ImageView imageView = (ImageView) view.findViewById(R.id.recipe_imageview);
        new DownloadImageViewTask(imageView, true)
                .execute(cursor.getString(SQLiteDB.TableFields.RECIPE_IMAGE_SMALL));
    }
}
