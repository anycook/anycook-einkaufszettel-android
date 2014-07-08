package de.anycook.app.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import de.anycook.app.R;
import de.anycook.app.activities.util.SwipeDetector;
import de.anycook.app.adapter.GroceryItemRowAdapter;
import de.anycook.app.controller.IngredientAutoCompleter;
import de.anycook.app.data.AutoCompleteDataSource;
import de.anycook.app.data.GroceryDataSource;
import de.anycook.app.data.GroceryItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>main activity implementing</p>
 * <ul>
 *     <li></li>
 * </ul>
 *
 *
 * Created by cipo7741 on 13.06.14.
 */
public class EditGroceryListActivity extends ActionBarActivity {

    private GroceryDataSource groceryDataSource;
    private List<GroceryItem> groceryItemList;
    private static final String TAG = EditGroceryListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, " onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_item_list);

        // hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // load and set grocery list data
        groceryDataSource = new GroceryDataSource(this);
        groceryDataSource.open();
        this.groceryItemList = groceryDataSource.getAllGroceryItems();

        groceryDataSource.close();
        final ListView groceryItemListView = (ListView) this.findViewById(R.id.grocery_item_list_listview);
        //View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grocery_item_list_footer_view, null, false);
        //groceryItemListView.addFooterView(footerView);
        groceryItemListView.setAdapter(new GroceryItemRowAdapter(this, R.layout.grocery_item_row, groceryItemList));
        
        // load and set autocomplete ingredients data
        AutoCompleteDataSource autoCompleteDataSource = new AutoCompleteDataSource(this);
        autoCompleteDataSource.open();
        List<String> suggestionList = autoCompleteDataSource.getAllSuggestions();

        final AutoCompleteTextView editTextGroceryItem = (AutoCompleteTextView) findViewById(R.id.grocery_item_list_autocompletetextview_grocery_item);
        if(suggestionList.isEmpty()){
            editTextGroceryItem.setAdapter(new ArrayAdapter<>(this, R.layout.autocomplete_row, suggestionList));
        }
        autoCompleteDataSource.close();

        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        threadPool.submit(new IngredientAutoCompleter(editTextGroceryItem));

        // listener for adding grocery item
        final EditText editTextAmount = (EditText) findViewById(R.id.grocery_item_list_textview_amount);
        editTextAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addItem(editTextGroceryItem.getText().toString(), editTextAmount.getText().toString());
                    editTextGroceryItem.setText("");
                    editTextAmount.setText("");
                    ((GroceryItemRowAdapter) groceryItemListView.getAdapter()).notifyDataSetChanged();
                    return true;
                } else return false;
            }
        });

        // listener for checking and deleting of grocery items
        final SwipeDetector swipeDetector = new SwipeDetector();
        groceryItemListView.setOnTouchListener(swipeDetector);
        final Button button = (Button) findViewById(R.id.grocery_item_list_button);
        groceryItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroceryItem groceryItem = groceryItemList.get(i);
                groceryDataSource.open();
                if (swipeDetector.swipeDetected() && view.findViewById(R.id.grocery_item_row_view_stroke).getVisibility() == View.VISIBLE) {
                    groceryItemList.remove(i);
                    groceryDataSource.deleteGroceryItem(groceryItem);
                    Toast.makeText(getBaseContext(), String.format("%s wurde gelöscht", groceryItem.getName()), Toast.LENGTH_SHORT).show();
                } else {

                    if (button.getVisibility() == View.GONE) button.setVisibility(View.VISIBLE);
                    groceryDataSource.strokeGroceryItem(groceryItem);
                    changeItemStrokeVisibility(groceryItem);
                }
                groceryDataSource.close();
                ((GroceryItemRowAdapter) groceryItemListView.getAdapter()).notifyDataSetChanged();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedGroceryItems = 0;
                while(checkedGroceryItems != groceryItemList.size()){
                    if(groceryItemList.get(checkedGroceryItems).isStroked()) {
                        groceryDataSource.open();
                        groceryDataSource.deleteGroceryItem(groceryItemList.remove(checkedGroceryItems));
                        groceryDataSource.close();
                    } else {
                        checkedGroceryItems = checkedGroceryItems + 1;
                    }
                }
                ((GroceryItemRowAdapter) groceryItemListView.getAdapter()).notifyDataSetChanged();
                button.setVisibility(View.GONE);
            }
        });
    }

    private void addItem(String name, String amount) {
        if (name.equals("")) {
            Toast.makeText(getBaseContext(), "Wunschlos glücklich! ;-)", Toast.LENGTH_SHORT).show();
        } else {
            GroceryItem groceryItem = new GroceryItem(name, amount, false);
            groceryDataSource.open();
            groceryItem = groceryDataSource.createGroceryItem(groceryItem);
            groceryDataSource.close();
            groceryItemList.add(0, groceryItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.menu_item_main_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(true);

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        searchMenuItem.collapseActionView();
                        searchView.setQuery("", false);
                        searchView.setIconified(true);
                    }
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // hides and then unhides search tab to make sure
                    // keyboard disappears when query is submitted (=_=;)
                    searchView.setVisibility(View.INVISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);

        /*searchView.setIconifiedByDefault(true);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private void changeItemStrokeVisibility(GroceryItem groceryItem) {
        if (groceryItem.isStroked()) {
            groceryItem.setStroked(false);
        } else {
            groceryItem.setStroked(true);
        }
    }

    @Override
    protected void onStop() {
        Log.v(TAG, " onStop");
        groceryDataSource.close();
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, " onResume");

        groceryDataSource.open();
        this.groceryItemList = groceryDataSource.getAllGroceryItems();
        final ListView groceryItemListView = (ListView) this.findViewById(R.id.grocery_item_list_listview);
        groceryItemListView.setAdapter(new GroceryItemRowAdapter(this, R.layout.grocery_item_row, this.groceryItemList));

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, " onPause");
        groceryDataSource.close();
        super.onPause();
    }

}
