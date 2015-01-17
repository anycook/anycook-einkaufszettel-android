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

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.activities.dialogs.ChangeIngredientDialog;
import de.anycook.einkaufszettel.adapter.GroceryItemRowAdapter;
import de.anycook.einkaufszettel.adapter.IngredientAutocompleteAdapter;
import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.store.GroceryItemStore;
import de.anycook.einkaufszettel.store.SQLiteDB;
import de.anycook.einkaufszettel.util.StringTools;


/**
 * @author Jan Graßegger
 * @author Claudia Sichting
 */
public class GroceryListFragment extends ListFragment implements AdapterView.OnItemLongClickListener {
    private static final Logger LOGGER = LoggerManager.getLogger();

    private AutoCompleteTextView groceryNameTextView;
    private EditText groceryAmountTextView;

    private GroceryItemStore groceryItemStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.grocery_item_list, container, false);
        // load views
        this.groceryNameTextView =
                (AutoCompleteTextView) view.findViewById(R.id.grocery_item_list_autocompletetextview_grocery_item);
        this.groceryAmountTextView = (EditText) view.findViewById(R.id.grocery_item_list_textview_amount);

        // load and set grocery list data
        this.groceryItemStore = new GroceryItemStore(view.getContext());
        groceryItemStore.open();
        GroceryItemRowAdapter listAdapter = new GroceryItemRowAdapter(view.getContext(), R.layout.grocery_item_row,
                groceryItemStore.getAllGroceryItemsCursor(), 0);

        //init data source adapter
        setListAdapter(listAdapter);

        groceryAmountTextView.setOnEditorActionListener(new AmountOnEditorActionListener());
        groceryNameTextView.setAdapter(getAutocompleteCursorAdapter());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(this);
    }

    private void addItem(String name, String amount) {
        if (name.equals("")) {
            Toast.makeText(getActivity().getBaseContext(), "Wunschlos glücklich! ;-)", Toast.LENGTH_SHORT).show();
        } else {
            groceryItemStore.addGroceryItem(name, amount);

            GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
            listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());

            // set focus back to name field after adding ingredient
            groceryNameTextView.requestFocus();
        }
    }

    private CursorAdapter getAutocompleteCursorAdapter() {
        return new IngredientAutocompleteAdapter(getActivity());
    }

    private boolean isGroceryItemStroked() {
        Cursor strokedItemCursor = groceryItemStore.getStrokedGroceryItems();
        try {
            return strokedItemCursor.getCount() > 0;
        } finally {
            strokedItemCursor.close();
        }
    }

    private void refreshMenuIcon() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.grocery_menu, menu);

        if (!isGroceryItemStroked()) {
            menu.getItem(0).setIcon(R.drawable.ic_action_discard);
        }

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grocery_menu_action_clear:
                clickedClearButton();
                refreshMenuIcon();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        TextView groceryName = (TextView) view.findViewById(R.id.grocery_item_row_textview_grocery_item);
        groceryItemStore.changeStrokeVisibilityOfGroceryItem(groceryName.getText());
        GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
        listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
        refreshMenuIcon();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView groceryName = (TextView) view.findViewById(R.id.grocery_item_row_textview_grocery_item);
        TextView groceryAmount = (TextView) view.findViewById(R.id.grocery_item_row_textview_amount);
        final Ingredient ingredient = new Ingredient(groceryName.getText().toString(),
                groceryAmount.getText().toString());

        ChangeIngredientDialog dialog = new ChangeIngredientDialog(this.getActivity(), ingredient,
                new ChangeIngredientDialog.Callback() {
                    @Override
                    public void ingredientChanged(Ingredient newIngredient) {
                        newIngredient.setChecked(false);
                        groceryItemStore.update(ingredient.getName(), newIngredient);
                        GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
                        listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
                    }
                });
        dialog.showDialog();

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        LOGGER.v("Pause: Close database");
        groceryItemStore.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        LOGGER.v("Resume: Open database");
        groceryItemStore.open();
    }

    public void clickedClearButton() {
        Cursor strokedItemCursor = groceryItemStore.getStrokedGroceryItems();
        if (getListView().getCount() == 0) { return; }
        if (strokedItemCursor.getCount() == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage(R.string.clear_ingredients);
            alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    groceryItemStore.deleteAllGroceryItems();
                    GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
                    listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            while (strokedItemCursor.moveToNext()) {
                groceryItemStore.removeGroceryItem(
                        strokedItemCursor.getString(SQLiteDB.TableFields.GROCERY_ITEM_NAME));
            }
            GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
            listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
        }
    }

    private class AmountOnEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String name = groceryNameTextView.getText().toString();
                String amount = StringTools.formatAmount(groceryAmountTextView.getText().toString());
                addItem(name, amount);
                groceryNameTextView.setText("");
                groceryAmountTextView.setText("");
                return true;
            } else { return false; }
        }
    }
}
