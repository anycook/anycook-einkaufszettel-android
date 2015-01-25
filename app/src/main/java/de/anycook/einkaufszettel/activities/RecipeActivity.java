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

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.fragments.SlidingTabsColorsFragment;
import de.anycook.einkaufszettel.adapter.IngredientRowAdapter;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.store.ItemNotFoundException;
import de.anycook.einkaufszettel.store.RecipeStore;
import de.anycook.einkaufszettel.tasks.DownloadImageTask;
import de.anycook.einkaufszettel.tasks.DownloadImageViewTask;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeActivity extends ActionBarActivity {
    private static final Logger LOGGER = LoggerManager.getLogger();

    private RecipeResponse recipe;
    private IngredientRowAdapter ingredientRowAdapter;
    private ImageView recipeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        Bundle b = getIntent().getExtras();
        String item = b.getString("item");
        try {
            recipe = getRecipe(this, item);
        } catch (ItemNotFoundException e) {
            LOGGER.e("Failed load recipe", e);
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.anycook_toolbar);
        setSupportActionBar(toolbar);
        ViewCompat.setElevation(toolbar, 8);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        ingredientRowAdapter = new IngredientRowAdapter(this, recipe.getPersons());

        fillViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            findViewById(R.id.anycook_toolbar).setPadding(0, statusBarHeight, 0, 0);
        }

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
            transaction.replace(R.id.recipe_content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredient_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    finish();
                } else {
                    finishAfterTransition();
                }
                return true;
            case R.id.ingredient_menu_open_recipe:
                Uri uri = Uri.parse("http://anycook.de#/recipe/" + recipe.getName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // Create and start the chooser
                Intent chooser = Intent.createChooser(intent, "Open with");
                startActivity(chooser);
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public RecipeResponse getRecipe() {
        return recipe;
    }

    public IngredientRowAdapter getIngredientRowAdapter() {
        return ingredientRowAdapter;
    }

    private void fillViews() {
        this.recipeImageView = (ImageView) findViewById(R.id.recipe_image);
        DownloadImageTask downloadImageTask = new DownloadImageViewTask(recipeImageView,
                findViewById(R.id.add_ingredients_button), recipe.getName());
        downloadImageTask.execute(recipe.getImage().getBig());

        ViewCompat.setElevation(findViewById(R.id.add_ingredients_button), 12);

        TextView titleView = (TextView) findViewById(R.id.recipe_title_text);
        titleView.setText(recipe.getName());
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static RecipeResponse getRecipe(Context context, String recipeName) throws ItemNotFoundException {
        RecipeStore recipeStore = new RecipeStore(context);
        try {
            recipeStore.open();
            return recipeStore.getRecipe(recipeName);
        } finally {
            recipeStore.close();
        }
    }
}
