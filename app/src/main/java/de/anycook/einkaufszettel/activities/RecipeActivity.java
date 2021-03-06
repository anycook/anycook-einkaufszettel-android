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

package de.anycook.einkaufszettel.activities;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.fragments.SlidingTabsColorsFragment;
import de.anycook.einkaufszettel.adapter.RecipeIngredientRowAdapter;
import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.store.GroceryStore;
import de.anycook.einkaufszettel.tasks.DownloadImageTask;
import de.anycook.einkaufszettel.tasks.DownloadImageViewTask;
import de.anycook.einkaufszettel.tasks.LoadRecipeTask;
import de.anycook.einkaufszettel.util.AnalyticsApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeActivity extends ActionBarActivity {

    private String recipeName;
    private RecipeResponse recipe;
    private RecipeIngredientRowAdapter ingredientRowAdapter;
    private Tracker tracker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        final AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        final Bundle b = getIntent().getExtras();
        recipeName = b.getString("item");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.anycook_toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setElevation(toolbar, 8);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            findViewById(R.id.anycook_toolbar).setPadding(0, statusBarHeight, 0, 0);
        }

        if (savedInstanceState == null) {
            final LoadRecipeTask recipeTask = new LoadRecipeTask(this, recipeName);
            recipeTask.execute();
        } else {
            recipe = savedInstanceState.getParcelable("recipe");
            showRecipe(savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredient_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName("Recipe~" + recipeName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    finish();
                } else {
                    finishAfterTransition();
                }
                return true;
            case R.id.ingredient_menu_open_recipe:
                tracker.send(new HitBuilders.EventBuilder()
                                     .setCategory("Action")
                                     .setAction("OpenRecipeOnWebsite")
                                     .setLabel(recipeName)
                                     .build());
                final Uri uri = Uri.parse("http://anycook.de#/recipe/" + recipe.getName());
                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // Create and start the chooser
                final Intent chooser = Intent.createChooser(intent, "Open with");
                startActivity(chooser);
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRecipe(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
            transaction.replace(R.id.recipe_content_fragment, fragment);
            transaction.commit();
        }

        ingredientRowAdapter = new RecipeIngredientRowAdapter(this, recipe.getPersons());

        final ImageView recipeImageView = (ImageView) findViewById(R.id.recipe_image);
        final DownloadImageTask downloadImageTask = new DownloadImageViewTask(
                recipeImageView, findViewById(R.id.add_ingredients_button),
                recipe.getName());
        downloadImageTask.execute(recipe.getImage().getBig());

        ViewCompat.setElevation(findViewById(R.id.add_ingredients_button), 12);

        final TextView titleView = (TextView) findViewById(R.id.recipe_title_text);
        titleView.setText(recipe.getName());

        findViewById(R.id.empty).setVisibility(View.GONE);
        findViewById(R.id.recipe_wrapper).setVisibility(View.VISIBLE);
    }

    public RecipeResponse getRecipe() {
        return recipe;
    }

    public void setRecipe(final RecipeResponse recipe) {
        this.recipe = recipe;
        showRecipe(null);
    }

    public RecipeIngredientRowAdapter getIngredientRowAdapter() {
        return ingredientRowAdapter;
    }

    private int getStatusBarHeight() {
        int result = 0;
        final int resourceId =
                getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void onAddIngredientsClick(View view) {
        tracker.send(new HitBuilders.EventBuilder()
                             .setCategory("Action")
                             .setAction("AddRecipeToGroceryList")
                             .setLabel(recipeName)
                             .build());

        includeCheckedIngredientsToGroceryList();
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (recipe != null) {
            outState.putParcelable("recipe", recipe);
        }
        super.onSaveInstanceState(outState);
    }

    private void includeCheckedIngredientsToGroceryList() {
        final GroceryStore groceryItemStore = new GroceryStore(this);
        try {
            groceryItemStore.open();
            final int ingredientsCount = ingredientRowAdapter.getCount();
            final List<Ingredient> ingredients = new ArrayList<>(ingredientsCount * 2);
            for (int i = 0; i < ingredientsCount; i++) {
                final Ingredient ingredient = ingredientRowAdapter.getMultipliedItem(i);
                if (!ingredient.isChecked()) {
                    continue;
                }
                ingredients.add(ingredient);
            }
            groceryItemStore.addIngredientsToGroceryList(ingredients);
        } finally {
            groceryItemStore.close();
        }
    }
}
