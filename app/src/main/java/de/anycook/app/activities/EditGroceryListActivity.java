package de.anycook.app.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import de.anycook.app.R;
import de.anycook.app.activities.util.SwipeDetector;
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
    //private static final String TAG = EditGroceryListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list);

        final ListView ingredientListView = (ListView) this.findViewById(R.id.ingredient_list_listview);
        ingredientListView.setAdapter(new IngredientRowAdapter(this, R.layout.ingredient_row, Ingredients));
        View footerView = getLayoutInflater().inflate(R.layout.footer_layout, ingredientListView, false);
        ingredientListView.addFooterView(footerView);

        final EditText editTextAmount = (EditText) findViewById(R.id.ingredient_list_textview_amount);
        final EditText editTextIngredient = (EditText) findViewById(R.id.ingredient_list_textview_ingredient);
        editTextAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addItem(editTextIngredient, editTextAmount);
                    handled = true;
                    ((IngredientRowAdapter) ingredientListView.getAdapter()).notifyDataSetChanged();
                }
                return handled;
            }
        });
        final SwipeDetector swipeDetector = new SwipeDetector();
        ingredientListView.setOnTouchListener(swipeDetector);
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (swipeDetector.swipeDetected() && view.findViewById(R.id.ingredient_row_view_stroke).getVisibility() == View.VISIBLE) {
                    String removedIngredient = Ingredients.remove(i).getIngredient();
                    showTopMessage(String.format("%s wurde gelöscht", removedIngredient));
                } else {
                    changeItemStrokeVisibility(i);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_main_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    private void showTopMessage(String message) {
        Toast toast = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void addItem(EditText editTextIngredient, EditText editTextAmount) {
        if (editTextIngredient.getText().toString().isEmpty()) {
            showTopMessage("Wunschlos glücklich! ;-)");
        } else {
            IngredientRow ingredientRow = new IngredientRow(editTextIngredient.getText().toString(), editTextAmount.getText().toString());
            Ingredients.add(0, ingredientRow);
            editTextIngredient.setText("");
            editTextAmount.setText("");
        }
    }

    private void changeItemStrokeVisibility(int position) {
        if (Ingredients.get(position).getStruckOut()) {
            Ingredients.get(position).setStruckOut(false);
        } else {
            Ingredients.get(position).setStruckOut(true);
        }
    }
}
