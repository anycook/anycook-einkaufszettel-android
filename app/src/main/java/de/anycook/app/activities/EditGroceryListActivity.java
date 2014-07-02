package de.anycook.app.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import de.anycook.app.R;
import de.anycook.app.activities.util.SwipeDetector;
import de.anycook.app.adapter.Ingredient;
import de.anycook.app.adapter.IngredientRowAdapter;
import de.anycook.app.controller.util.IngredientsDataSource;

import java.util.List;

/**
 * Implement the ActionMode.Callback interface for enabling the contextual action mode for the ingredient view
 * <p/>
 * Created by cipo7741 on 13.06.14.
 */
public class EditGroceryListActivity extends ActionBarActivity {

    private IngredientsDataSource dataSource;
    //private static final String TAG = EditGroceryListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list);

        dataSource = new IngredientsDataSource(this);
        dataSource.open();

        List<Ingredient> values = dataSource.getAllIngredients();

        final ListView ingredientListView = (ListView) this.findViewById(R.id.ingredient_list_listview);
        ingredientListView.setAdapter(new IngredientRowAdapter(this, R.layout.ingredient_row, values));
        View footerView = getLayoutInflater().inflate(R.layout.footer_layout, ingredientListView, false);
        ingredientListView.addFooterView(footerView);

        final EditText editTextAmount = (EditText) findViewById(R.id.ingredient_list_textview_amount);
        final EditText editTextIngredient = (EditText) findViewById(R.id.ingredient_list_textview_ingredient);
        editTextAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addItem(ingredientListView, editTextIngredient, editTextAmount);
                    return true;
                } else return false;
            }
        });
        final SwipeDetector swipeDetector = new SwipeDetector();
        ingredientListView.setOnTouchListener(swipeDetector);
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient ingredient = ((IngredientRowAdapter) ingredientListView.getAdapter()).getItem(i);
                if (swipeDetector.swipeDetected() && view.findViewById(R.id.ingredient_row_view_stroke).getVisibility() == View.VISIBLE) {
                    dataSource.deleteIngredient(ingredient);
                    ((IngredientRowAdapter) ingredientListView.getAdapter()).remove(ingredient);
                    showTopMessage(String.format("%s wurde gelöscht", ingredient.getName()));
                } else {
                    changeItemStrokeVisibility(ingredient);
                }
                ((IngredientRowAdapter) ingredientListView.getAdapter()).notifyDataSetChanged();
            }
        });
        ingredientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }

    private void addItem(ListView ingredientListView, EditText editTextIngredient, EditText editTextAmount) {
        if (editTextIngredient.getText().toString().isEmpty()) {
            showTopMessage("Wunschlos glücklich! ;-)");
        } else {
            Ingredient ingredient = new Ingredient(editTextIngredient.getText().toString(), editTextAmount.getText().toString());
            ingredient = dataSource.createIngredient(ingredient);
            ((IngredientRowAdapter) ingredientListView.getAdapter()).add(ingredient);
            editTextIngredient.setText("");
            editTextAmount.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_main_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_item_main_locate:
                openLoctionView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openLoctionView() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    private void showTopMessage(String message) {
        Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }


    private void changeItemStrokeVisibility(Ingredient ingredient) {
        if (ingredient.getStruckOut()) {
            ingredient.setStruckOut(false);
        } else {
            ingredient.setStruckOut(true);
        }
    }
}
