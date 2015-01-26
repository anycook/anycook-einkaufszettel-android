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
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.store.SQLiteDB;

/**
 * Custom ArrayAdapter to fill EditMode with grocery item and amount
 *
 * @author Jan Grassegger <jan@anycook.de>
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class GroceryRowAdapter extends ResourceCursorAdapter {

    public GroceryRowAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //TextView groceryName = (TextView) view.findViewById(R.id.grocery_item_row_textview_grocery_item);
        TextView groceryName = (TextView) view.findViewById(R.id.textview_grocery);
        groceryName.setText(cursor.getString(SQLiteDB.TableFields.GROCERY_NAME));

        TextView groceryAmount = (TextView) view.findViewById(R.id.textview_amount);
        groceryAmount.setText(cursor.getString(SQLiteDB.TableFields.GROCERY_AMOUNT));

        View strokeView = view.findViewById(R.id.view_stroke);
        boolean isVisible = cursor.getInt(SQLiteDB.TableFields.GROCERY_STROKE) > 0;
        strokeView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }


}
