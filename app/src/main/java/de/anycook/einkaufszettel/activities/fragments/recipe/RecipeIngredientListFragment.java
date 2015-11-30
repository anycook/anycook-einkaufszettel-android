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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.RecipeActivity;
import de.anycook.einkaufszettel.activities.dialogs.ChangeIngredientDialog;
import de.anycook.einkaufszettel.adapter.RecipeIngredientRowAdapter;
import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.model.RecipeResponse;
import de.anycook.einkaufszettel.tasks.LoadRecipeIngredientsTask;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeIngredientListFragment extends Fragment
        implements AdapterView.OnItemClickListener,
                   AdapterView.OnItemLongClickListener, View.OnClickListener {

    private static final int MAX_PERSONS = 99;
    private static final int MIN_PERSONS = 1;


    private RecipeActivity recipeActivity;
    private ListView ingredientListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeActivity = (RecipeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_ingredient_list, container, false);

        RecipeResponse recipe = recipeActivity.getRecipe();

        this.ingredientListView = (ListView) view.findViewById(R.id.ingredient_list);

        ingredientListView.setAdapter(recipeActivity.getIngredientRowAdapter());
        ingredientListView.setOnItemClickListener(this);
        ingredientListView.setOnItemLongClickListener(this);

        EditText personsEditText = (EditText) view.findViewById(R.id.edittext_persons);
        personsEditText.setText(Integer.toString(recipe.getPersons()));

        LoadRecipeIngredientsTask loadRecipeIngredientsTask =
                new LoadRecipeIngredientsTask(recipeActivity.getIngredientRowAdapter(), view,
                                              recipeActivity);
        loadRecipeIngredientsTask.execute(recipe.getName());

        view.findViewById(R.id.edittext_persons).setOnClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecipeIngredientRowAdapter
                adapter =
                (RecipeIngredientRowAdapter) ingredientListView.getAdapter();
        Ingredient ingredient = (Ingredient) ingredientListView.getItemAtPosition(position);
        ingredient.setChecked(!ingredient.isChecked());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Ingredient ingredient = (Ingredient) ingredientListView.getItemAtPosition(position);
        final ChangeIngredientDialog.Callback callback = new ChangeIngredientDialog.Callback() {
            @Override
            public void ingredientChanged(Ingredient newIngredient) {
                ingredient.setName(newIngredient.getName());
                ingredient.setAmount(newIngredient.getAmount());
                final RecipeIngredientRowAdapter adapter =
                        (RecipeIngredientRowAdapter) ingredientListView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        };

        final ChangeIngredientDialog dialog = new ChangeIngredientDialog(getActivity(),
                                                                         ingredient, callback);

        dialog.showDialog();

        return true;
    }

    @Override
    public void onClick(View view) {
        final EditText personsEditText = (EditText) view;
        int numPersons = Integer.parseInt(personsEditText.getText().toString());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.for_x_people);

        View alertDialogContent = LayoutInflater.from(alertDialogBuilder.getContext())
                .inflate(R.layout.number_picker_dialog, this.ingredientListView, false);

        final NumberPicker numberPicker = (NumberPicker) alertDialogContent
                .findViewById(R.id.number_picker);
        numberPicker.setMaxValue(MAX_PERSONS);
        numberPicker.setValue(numPersons);
        numberPicker.setMinValue(MIN_PERSONS);

        alertDialogBuilder.setView(alertDialogContent);

        alertDialogBuilder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String personsString = Integer.toString(numberPicker.getValue());
                personsEditText.setText(personsString);
                if (personsString.length() == 0) {
                    return;
                }
                int numPersons = Integer.parseInt(personsEditText.getText().toString());
                recipeActivity.getIngredientRowAdapter().setCurrentPersons(numPersons);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
