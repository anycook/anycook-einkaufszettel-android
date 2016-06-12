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

package de.anycook.einkaufszettel.activities.fragments.recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.RecipeActivity;
import de.anycook.einkaufszettel.model.RecipeResponse;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeDetailFragment extends Fragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recipe_details, container, false);

        final RecipeResponse recipeResponse = ((RecipeActivity) getActivity()).getRecipe();

        final TextView recipeDescriptionView =
                (TextView) view.findViewById(R.id.recipe_description_text);
        recipeDescriptionView.setText(recipeResponse.getDescription());

        final TextView recipeTimeView = (TextView) view.findViewById(R.id.recipe_time_text);
        RecipeResponse.Time time = recipeResponse.getTime();
        recipeTimeView.setText(String.format("%d:%02d h", time.getStd(), time.getMin()));

        final TextView categoryView = (TextView) view.findViewById(R.id.recipe_category);
        categoryView.setText(recipeResponse.getCategory());

        final TextView skillView = (TextView) view.findViewById(R.id.recipe_skill);
        skillView.setText(getString(R.string.ofFive, recipeResponse.getSkill()));

        final TextView calorieView = (TextView) view.findViewById(R.id.recipe_calorie);
        calorieView.setText(getString(R.string.ofFive, recipeResponse.getCalorie()));

        return view;
    }
}
