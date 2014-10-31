/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger
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
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.einkaufszettel.R;

/**
 * adapter for drawer icons with menu name
 * @author Jan Grassegger <jan@anycook.de>
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class DrawerRowAdapter extends ArrayAdapter<String> {
    private final TypedArray icons;


    public DrawerRowAdapter(Context context, String[] resource) {
        super(context, R.layout.drawer_list, resource);
        icons = context.getResources().obtainTypedArray(R.array.menu_icons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_row, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.drawer_list_item_textview);
            viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.drawer_list_item_imageview);
            convertView.setTag(viewHolder);
        }
        String item = getItem(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textViewName.setText(item);
        viewHolder.imageViewIcon.setImageDrawable(icons.getDrawable(position));
        return convertView;
    }

    static class ViewHolder {
        TextView textViewName;
        ImageView imageViewIcon;
    }
}
