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
import android.widget.CheckBox;
import android.widget.TextView;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.model.Ingredient;

import java.util.ArrayList;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class IngredientRowAdapter extends ArrayAdapter<Ingredient> {

    private final int recipePersons;
    private int currentPersons;

    public IngredientRowAdapter(Context context, int recipePersons) {
        super(context, R.layout.ingredient_list_row, new ArrayList<Ingredient>());
        this.recipePersons = recipePersons;
        this.currentPersons = recipePersons;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        IngredientHolder holder;
        if (convertView == null) {
            final LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ingredient_list_row, parent, false);
            holder = new IngredientHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.ingredient_list_row_name);
            holder.amountTextView = (TextView) convertView.findViewById(R.id.ingredient_list_row_amount);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.ingredient_list_row_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (IngredientHolder) convertView.getTag();
        }
        Ingredient ingredient = getMultipliedItem(position);
        holder.nameTextView.setText(ingredient.getName());

        holder.amountTextView.setText(ingredient.getAmount());
        holder.checkBox.setChecked(ingredient.isChecked());

        return convertView;
    }


    public Ingredient getMultipliedItem(int position) {
        Ingredient ingredient = new Ingredient(getItem(position));
        ingredient.multiplyAmount(recipePersons, currentPersons);
        return ingredient;
    }

    public void setCurrentPersons(int currentPersons) {
        if (currentPersons == 0) { return; }
        this.currentPersons = currentPersons;
        notifyDataSetChanged();
    }

    private static class IngredientHolder {
        TextView nameTextView;
        TextView amountTextView;
        CheckBox checkBox;
    }
}
