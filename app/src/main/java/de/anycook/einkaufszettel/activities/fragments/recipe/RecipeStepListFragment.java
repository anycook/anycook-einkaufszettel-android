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
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.RecipeActivity;
import de.anycook.einkaufszettel.adapter.StepRowAdapter;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.tasks.LoadRecipeStepsTask;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeStepListFragment extends ListFragment {
    private RecipeActivity recipeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeActivity = (RecipeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_list, container, false);

        RecipeResponse recipe = recipeActivity.getRecipe();

        StepRowAdapter stepRowAdapter = new StepRowAdapter(recipeActivity);
        setListAdapter(stepRowAdapter);

        LoadRecipeStepsTask recipeStepsTask = new LoadRecipeStepsTask(stepRowAdapter, view, recipeActivity);
        recipeStepsTask.execute(recipe.getName());

        return view;
    }
}
