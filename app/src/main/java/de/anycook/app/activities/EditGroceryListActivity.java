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
import de.anycook.app.data.GroceryDataSource;
import de.anycook.app.data.GroceryItem;

import java.util.List;

/**
 * Implement the ActionMode.Callback interface for enabling the contextual action mode for the groceryItem view
 * <p/>
 * Created by cipo7741 on 13.06.14.
 */
public class EditGroceryListActivity extends ActionBarActivity {

    private GroceryDataSource dataSource;
    private List<GroceryItem> groceryItemList;
    private static final String TAG = EditGroceryListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, " onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_item_list);

        dataSource = new GroceryDataSource(this);
        dataSource.open();

        this.groceryItemList = dataSource.getAllGroceryItems();

        final ListView groceryItemListView = (ListView) this.findViewById(R.id.grocery_item_list_listview);
        groceryItemListView.setAdapter(new GroceryItemRowAdapter(this, R.layout.grocery_item_row, groceryItemList));

        final EditText editTextAmount = (EditText) findViewById(R.id.grocery_item_list_textview_amount);
        final EditText editTextGroceryItem = (EditText) findViewById(R.id.grocery_item_list_textview_grocery_item);

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
        final SwipeDetector swipeDetector = new SwipeDetector();
        groceryItemListView.setOnTouchListener(swipeDetector);
        groceryItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroceryItem groceryItem = groceryItemList.get(i);
                if (swipeDetector.swipeDetected() && view.findViewById(R.id.grocery_item_row_view_stroke).getVisibility() == View.VISIBLE) {
                    groceryItemList.remove(i);
                    dataSource.deleteGroceryItem(groceryItem);
                    showTopMessage(String.format("%s wurde gelöscht", groceryItem.getName()));
                } else {
                    dataSource.strokeGroceryItem(groceryItem);
                    changeItemStrokeVisibility(groceryItem);
                }
                ((GroceryItemRowAdapter) groceryItemListView.getAdapter()).notifyDataSetChanged();
            }
        });
        groceryItemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }

    private void addItem(String name, String amount) {
        if (name.equals("")) {
            showTopMessage("Wunschlos glücklich! ;-)");
        } else {
            GroceryItem groceryItem = new GroceryItem(name, amount, false);
            groceryItem = dataSource.createGroceryItem(groceryItem);
            groceryItemList.add(0, groceryItem);
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
        dataSource.close();
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, " onResume");
        dataSource.open();
        this.groceryItemList = dataSource.getAllGroceryItems();
        final ListView groceryItemListView = (ListView) this.findViewById(R.id.grocery_item_list_listview);
        groceryItemListView.setAdapter(new GroceryItemRowAdapter(this, R.layout.grocery_item_row, this.groceryItemList));
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, " onPause");
        dataSource.close();
        super.onPause();
    }
}
