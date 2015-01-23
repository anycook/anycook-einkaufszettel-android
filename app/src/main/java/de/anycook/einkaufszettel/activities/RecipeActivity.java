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

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.fragments.SlidingTabsColorsFragment;
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
    private ImageView recipeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        Bundle b = getIntent().getExtras();
        String item = b.getString("item");
        try {
            recipe = getRecipe(item);
        } catch (ItemNotFoundException e) {
            LOGGER.e("Failed load recipe", e);
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.anycook_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

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

    private void fillViews() {
        this.recipeImageView = (ImageView) findViewById(R.id.recipe_image);
        DownloadImageTask downloadImageTask = new DownloadImageViewTask(recipeImageView,
                findViewById(R.id.add_ingredients_button), recipe.getName());
        downloadImageTask.execute(recipe.getImage().getBig());

        //findViewById(R.id.recipe_header).setOnClickListener(this);

        TextView titleView = (TextView) findViewById(R.id.recipe_title_text);
        titleView.setText(recipe.getName());

//        EditText personsEditText = (EditText) findViewById(R.id.ingredient_list_persons);
//        personsEditText.setText(Integer.toString(recipe.getPersons()));
        //personsEditText.setOnClickListener(this);
    }

    private RecipeResponse getRecipe(String recipeName) throws ItemNotFoundException {
        RecipeStore recipeStore = new RecipeStore(this);
        try {
            recipeStore.open();
            return recipeStore.getRecipe(recipeName);
        } finally {
            recipeStore.close();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
