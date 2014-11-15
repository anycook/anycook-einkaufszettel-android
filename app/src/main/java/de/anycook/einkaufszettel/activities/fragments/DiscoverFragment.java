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

package de.anycook.einkaufszettel.activities.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.AddIngredientsActivity;
import de.anycook.einkaufszettel.adapter.RecipeRowArrayAdapter;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.tasks.LoadDiscoverRecipesTask;

/**
 * @author Claudia Sichting
 * @author Jan Graßegger
 */
public class DiscoverFragment extends ListFragment {
    private static final String URL_PATTERN = "https://api.anycook.de/discover/%s?recipeNumber=20";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list, container, false);

        String url = String.format(URL_PATTERN, getArguments().getString("type"));

        RecipeRowArrayAdapter adapter = new RecipeRowArrayAdapter(getActivity());
        setListAdapter(adapter);
        LoadDiscoverRecipesTask loadDiscoverRecipesTask = new LoadDiscoverRecipesTask(adapter, this.getActivity());
        loadDiscoverRecipesTask.execute(url);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), AddIngredientsActivity.class);
        Bundle b = new Bundle();
        RecipeResponse recipeResponse = (RecipeResponse) getListAdapter().getItem(position);
        b.putString("item", recipeResponse.getName());
        intent.putExtras(b);
        startActivity(intent);
    }
}
