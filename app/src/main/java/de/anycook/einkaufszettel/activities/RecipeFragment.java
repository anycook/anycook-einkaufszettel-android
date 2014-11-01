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

package de.anycook.einkaufszettel.activities;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.noveogroup.android.log.Log;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.adapter.RecipeRowCursorAdapter;
import de.anycook.einkaufszettel.store.RecipeStore;

/**
 * this searchable activity is responsible for returning recipe search results
 *
 * @author Jan Grassegger <jan@anycook.de>
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipeFragment extends ListFragment implements SearchView.OnQueryTextListener {

    private RecipeStore recipeDatabase;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDatabase = new RecipeStore(getActivity());
        recipeDatabase.open();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list, container, false);
        setHasOptionsMenu(true);
        RecipeRowCursorAdapter recipeRowCursorAdapter = new RecipeRowCursorAdapter(getActivity());
        setListAdapter(recipeRowCursorAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            onQueryTextChange(savedInstanceState.getString("query"));
        } else {
            onQueryTextChange("");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menu.clear();
        menuInflater.inflate(R.menu.recipe_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.recipe_menu_search);
        this.searchView = (android.support.v7.widget.SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AddIngredientsActivity.class);
        Bundle b = new Bundle();
        String item = ((TextView) view.findViewById(R.id.recipe_row_textview_recipe_name)).getText().toString();
        b.putString("item", item); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.v(RecipeFragment.class.getSimpleName(), "Searching for " + query);
        RecipeRowCursorAdapter adapter = (RecipeRowCursorAdapter) getListAdapter();
        adapter.changeCursor(recipeDatabase.getRecipesForQuery(query));
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        recipeDatabase.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        recipeDatabase.close();
    }
}