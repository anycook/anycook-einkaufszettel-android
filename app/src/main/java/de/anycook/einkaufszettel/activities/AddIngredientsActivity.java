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

package de.anycook.einkaufszettel.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.dialogs.ChangeIngredientDialog;
import de.anycook.einkaufszettel.adapter.IngredientRowAdapter;
import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.store.GroceryItemStore;
import de.anycook.einkaufszettel.store.ItemNotFoundException;
import de.anycook.einkaufszettel.store.RecipeStore;
import de.anycook.einkaufszettel.tasks.DownloadImageTask;
import de.anycook.einkaufszettel.tasks.DownloadImageViewTask;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Claudia Sichting
 * @author Jan Graßegger
 */
public class AddIngredientsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener, AdapterView.OnItemLongClickListener {
    private static final Logger LOGGER = LoggerManager.getLogger();
    private static final int MAX_PERSONS = 99;
    private static final int MIN_PERSONS = 1;

    private RecipeResponse recipe;
    private ListView ingredientListView;
    private ImageView recipeImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list);

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

        /*this.ingredientListView = (ListView) findViewById(R.id.ingredient_list_listview);
        IngredientRowAdapter adapter = new IngredientRowAdapter(this, recipe.getPersons());
        ingredientListView.setAdapter(adapter);
        ingredientListView.setOnItemClickListener(this);
        ingredientListView.setOnItemLongClickListener(this);*/

        //fillViews();

        //LoadRecipeIngredientsTask loadRecipeIngredientsTask =
        // new LoadRecipeIngredientsTask(adapter, this);
        //loadRecipeIngredientsTask.execute(item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            findViewById(R.id.anycook_toolbar).setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private void fillViews() {
        this.recipeImageView = (ImageView) findViewById(R.id.recipe_image);
        DownloadImageTask downloadImageTask = new DownloadImageViewTask(recipeImageView,
                findViewById(R.id.add_ingredients_button), recipe.getName());
        downloadImageTask.execute(recipe.getImage().getBig());

        findViewById(R.id.recipe_header).setOnClickListener(this);

        TextView titleView = (TextView) findViewById(R.id.recipe_title_text);
        titleView.setText(recipe.getName());

        EditText personsEditText = (EditText) findViewById(R.id.ingredient_list_persons);
        personsEditText.setText(Integer.toString(recipe.getPersons()));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IngredientRowAdapter adapter = (IngredientRowAdapter) ingredientListView.getAdapter();
        Ingredient ingredient = (Ingredient) ingredientListView.getItemAtPosition(position);
        ingredient.setChecked(!ingredient.isChecked());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Ingredient ingredient = (Ingredient) ingredientListView.getItemAtPosition(position);
        ChangeIngredientDialog dialog = new ChangeIngredientDialog(this, ingredient,
                new ChangeIngredientDialog.Callback() {
            @Override
            public void ingredientChanged(Ingredient newIngredient) {
                ingredient.setName(newIngredient.getName());
                ingredient.setAmount(newIngredient.getAmount());
                IngredientRowAdapter adapter = (IngredientRowAdapter) ingredientListView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });

        dialog.showDialog();

        return true;
    }

    public void onAddIngredientsClick(View view) {
        includeCheckedIngredientsToGroceryList();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

    private void includeCheckedIngredientsToGroceryList() {
        GroceryItemStore groceryItemStore = new GroceryItemStore(this);
        try {
            groceryItemStore.open();
            int ingredientsCount = ingredientListView.getAdapter().getCount();
            List<Ingredient> ingredients = new ArrayList<>(ingredientsCount);
            for (int i = 0; i < ingredientsCount; i++) {
                Ingredient ingredient =
                        ((IngredientRowAdapter) ingredientListView.getAdapter()).getMultipliedItem(i);
                if (!ingredient.isChecked()) { continue; }
                ingredients.add(ingredient);
            }
            groceryItemStore.addIngredientsToGroceryList(ingredients);
        } finally {
            groceryItemStore.close();
        }
    }

    public void onNumberPickerClick(View view) {

        final EditText personsEditText = (EditText) view;
        int numPersons = Integer.parseInt(personsEditText.getText().toString());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.for_x_people);

        View alertDialogContent = LayoutInflater.from(alertDialogBuilder.getContext())
                .inflate(R.layout.number_picker_dialog, this.ingredientListView, false);

        final NumberPicker numberPicker = (NumberPicker) alertDialogContent
                .findViewById(R.id.number_picker_dialog_numberpicker);
        numberPicker.setMaxValue(MAX_PERSONS);
        numberPicker.setValue(numPersons);
        numberPicker.setMinValue(MIN_PERSONS);

        alertDialogBuilder.setView(alertDialogContent);

        alertDialogBuilder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String personsString = Integer.toString(numberPicker.getValue());
                personsEditText.setText(personsString);
                if (personsString.length() == 0) { return; }
                int numPersons = Integer.parseInt(personsEditText.getText().toString());
                ((IngredientRowAdapter) ingredientListView.getAdapter()).setCurrentPersons(numPersons);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void onClick(View view) {

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(this.recipeImageView,
            (int) recipeImageView.getX(),
            (int) recipeImageView.getY(),
            recipeImageView.getWidth(),
            recipeImageView.getHeight());
//        Intent intent = new Intent(this, RecipeDetailActivity.class);
//        intent.putExtra("recipe", recipe);

//        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
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
