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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
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
import de.anycook.einkaufszettel.activities.RecipeActivity;
import de.anycook.einkaufszettel.adapter.RecipeCursorAdapter;
import de.anycook.einkaufszettel.store.RecipeStore;
import de.anycook.einkaufszettel.tasks.LoadIngredientsTask;
import de.anycook.einkaufszettel.tasks.LoadRecipesTask;
import de.anycook.einkaufszettel.util.AnalyticsApplication;
import de.anycook.einkaufszettel.util.Callback;
import de.anycook.einkaufszettel.util.ConnectionStatus;
import de.anycook.einkaufszettel.util.Properties;

import static android.content.Context.MODE_PRIVATE;

/**
 * this searchable activity is responsible for returning recipe search results
 *
 * @author Jan Grassegger <jan@anycook.de>
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipesFragment extends ListFragment implements SearchView.OnQueryTextListener {

    private RecipeStore recipeDatabase;
    private SearchView searchView;
    private SwipeRefreshLayout refreshLayout;
    private String query;
    private Tracker tracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDatabase = new RecipeStore(getActivity());
        recipeDatabase.open();
        final AnalyticsApplication application =
                (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list, container, false);
        setHasOptionsMenu(true);
        RecipeCursorAdapter recipeCursorAdapter = new RecipeCursorAdapter(getActivity());
        setListAdapter(recipeCursorAdapter);

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

        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecipes(true);
            }
        });
        refreshLayout.setColorSchemeResources(R.color.any_green, R.color.accent_color);

        loadRecipes(false);
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
        switch (item.getItemId()) {
            case R.id.recipe_menu_search:

                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), RecipeActivity.class);

        Bundle bundle = new Bundle();
        String item =
                ((TextView) view.findViewById(R.id.textview_recipe_name)).getText().toString();

        bundle.putString("item", item); //Your id
        intent.putExtras(bundle); //Put your id to your next Intent

        tracker.send(new HitBuilders.EventBuilder()
                             .setCategory("Action")
                             .setAction("ClickOnRecipe")
                             .setLabel(item)
                             .build());
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
        tracker.send(new HitBuilders.EventBuilder()
                             .setCategory("Action")
                             .setAction("Search")
                             .setLabel(query)
                             .build());

        this.query = query;
        Log.v(RecipesFragment.class.getSimpleName(), "Searching for " + query);
        RecipeCursorAdapter adapter = (RecipeCursorAdapter) getListAdapter();
        adapter.changeCursor(recipeDatabase.getRecipesForQuery(query));
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        recipeDatabase.open();
        tracker.setScreenName("Recipelist");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPause() {
        super.onPause();
        recipeDatabase.close();
    }

    private void loadRecipes(boolean reload) {
        final SharedPreferences sharedPrefs =
                getActivity().getSharedPreferences("update_data", MODE_PRIVATE);
        final long lastUpdate = sharedPrefs.getLong("last-update", 0);

        boolean emptyRecipes = recipeDatabase.empty();

        Properties properties = new Properties(getActivity());
        int updateInterval = properties.getUpdateInterval();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > updateInterval * 1000 || emptyRecipes || reload) {
            if (ConnectionStatus.isConnected(getActivity())) {
                if (!emptyRecipes) {
                    refreshLayout.setRefreshing(true);
                }
                updateData(sharedPrefs, emptyRecipes);
            }
        }
    }

    private void updateData(SharedPreferences sharedPrefs, boolean emptyRecipes) {

        tracker.send(new HitBuilders.EventBuilder()
                             .setCategory("Action")
                             .setAction("RefreshRecipes")
                             .build());

        final Activity activity = getActivity();

        final Callback callback = new TaskCallback();

        final LoadIngredientsTask loadIngredientsTask =
                new LoadIngredientsTask(activity, null);
        loadIngredientsTask.execute();
        final LoadRecipesTask loadRecipesTask =
                new LoadRecipesTask(activity, sharedPrefs, callback, emptyRecipes);
        loadRecipesTask.execute();

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong("last-update", System.currentTimeMillis());
        editor.apply();
    }

    private final class TaskCallback implements Callback {

        @Override
        public void call(Status status) {
            refreshLayout.setRefreshing(false);
            if (status == Status.FINISHED) {
                RecipeCursorAdapter adapter = (RecipeCursorAdapter) getListAdapter();
                adapter.changeCursor(recipeDatabase.getRecipesForQuery(query));
            }
        }
    }
}
