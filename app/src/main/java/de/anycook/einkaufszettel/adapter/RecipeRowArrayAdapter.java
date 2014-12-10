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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.tasks.DownloadImageViewTask;

import java.util.ArrayList;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeRowArrayAdapter extends ArrayAdapter<RecipeResponse> {

    public RecipeRowArrayAdapter(Context context) {
        super(context, R.layout.recipe_row, new ArrayList<RecipeResponse>());
    }

    /**
     * Improved getView thanks to ViewHolder (findViewById is and expensive function)
     *
     * @param position    where is the view
     * @param convertView what is visible
     * @param parent      which is the parent view
     * @return the view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recipe_row, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.recipe_row_textview_recipe_name);
            viewHolder.textViewDescription =
                    (TextView) convertView.findViewById(R.id.recipe_row_textview_recipe_description);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.recipe_row_imageview);
            convertView.setTag(viewHolder);
        }

        RecipeResponse recipeResponse = getItem(position);

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textViewName.setText(recipeResponse.getName());
        viewHolder.textViewDescription.setText(recipeResponse.getDescription());

        new DownloadImageViewTask(viewHolder.imageView, true).execute(recipeResponse.getImage().getSmall());

        return convertView;

    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        ImageView imageView;
    }
}
