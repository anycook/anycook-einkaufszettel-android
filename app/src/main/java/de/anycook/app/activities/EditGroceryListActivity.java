package de.anycook.app.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import de.anycook.app.R;
import de.anycook.app.adapter.IngredientRow;
import de.anycook.app.adapter.IngredientRowAdapter;

import java.util.ArrayList;

/**
 * Implement the ActionMode.Callback interface for enabling the contextual action mode for the ingredient view
 * <p/>
 * Created by cipo7741 on 13.06.14.
 */
public class EditGroceryListActivity extends ActionBarActivity {

    static final ArrayList<IngredientRow> Ingredients = new ArrayList<>();
    private static final String TAG = EditGroceryListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list);

        ListView ingredientListView = (ListView) this.findViewById(R.id.ingredient_list_listview);
        ingredientListView.setAdapter(new IngredientRowAdapter(this, R.layout.ingredient_row, Ingredients));

        final EditText editTextAmount;
        editTextAmount = (EditText) findViewById(R.id.ingredient_list_textview_amount);
        final EditText editTextIngredient;
        editTextIngredient = (EditText) findViewById(R.id.ingredient_list_textview_ingredient);
        editTextAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addGroceryItem(editTextAmount, editTextIngredient);
                    handled = true;
                }
                return handled;
            }
        });
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView ingredientTextView = (TextView) view.findViewById(R.id.ingredient_row_textview_ingredient);
                Toast.makeText(getBaseContext(), ingredientTextView.getText(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_main_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    private void addGroceryItem(EditText editTextAmount, EditText editTextIngredient) {

        ListView ingredientListView = (ListView) this.findViewById(R.id.ingredient_list_listview);
        IngredientRowAdapter ingredientRowAdapter = (IngredientRowAdapter) ingredientListView.getAdapter();
        ingredientRowAdapter.add(new IngredientRow(editTextIngredient.getText().toString(), editTextAmount.getText().toString()));
        Log.v(TAG, "starting addGroceryItem");
    }

}