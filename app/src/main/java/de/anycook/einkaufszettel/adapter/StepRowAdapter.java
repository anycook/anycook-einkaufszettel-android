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

import com.google.common.base.Joiner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.model.Step;

import java.util.ArrayList;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class StepRowAdapter extends ArrayAdapter<Step> {

    public StepRowAdapter(Context context) {
        super(context, R.layout.step_list_row, new ArrayList<Step>());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        StepHolder holder;
        if (convertView == null) {
            final LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.step_list_row, parent, false);
            holder = new StepHolder();
            holder.idTextView = (TextView) convertView.findViewById(R.id.step_id);
            holder.textTextView = (TextView) convertView.findViewById(R.id.step_text);
            holder.ingredientTextView = (TextView) convertView.findViewById(R.id.step_ingredients);
            convertView.setTag(holder);
        } else {
            holder = (StepHolder) convertView.getTag();
        }

        Step step = getItem(position);
        holder.idTextView.setText(Integer.toString(step.getId()) + ".");
        holder.textTextView.setText(step.getText());

        if (!step.getIngredients().isEmpty()) {
            holder.ingredientTextView.setVisibility(View.VISIBLE);
            holder.ingredientTextView.setText(Joiner.on(", ").join(step.getIngredients()));
        }

        return convertView;
    }


    private static class StepHolder {

        TextView idTextView;
        TextView textTextView;
        TextView ingredientTextView;
    }
}
