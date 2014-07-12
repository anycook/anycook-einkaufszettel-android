package de.anycook.app.activities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import de.anycook.app.R;
import de.anycook.app.adapter.GroceryItemRowAdapter;
import de.anycook.app.model.GroceryItem;
import de.anycook.app.store.GroceryItemStore;
import de.anycook.app.store.SQLiteDB;
import de.anycook.app.tasks.LoadIngredientsTask;

/**
 * @author Jan Graßegger
 * @author Claudia Sichting
 */
public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private AutoCompleteTextView groceryNameTextView;
    private EditText groceryAmountTextView;

    //data
    private GroceryItemStore groceryItemStore;
    private GroceryItemRowAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_item_list);

        // hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        // load views
        ListView listView = (ListView) findViewById(R.id.grocery_item_list_listview);
        ImageButton discardButton = (ImageButton) findViewById(R.id.discard_list_button);
        this.groceryNameTextView =
                (AutoCompleteTextView) findViewById(R.id.grocery_item_list_autocompletetextview_grocery_item);
        this.groceryAmountTextView = (EditText) findViewById(R.id.grocery_item_list_textview_amount);

        // load and set grocery list data
        this.groceryItemStore = new GroceryItemStore(this);
        this.listAdapter = new GroceryItemRowAdapter(this, R.layout.grocery_item_row,
                groceryItemStore.getAllGroceryItemsCursor(), 0);


        //init listView event handlers
        listView.setOnItemClickListener(this);

        //init data source adapter
        listView.setAdapter(this.listAdapter);

        discardButton.setOnClickListener(new DiscardOnClickListener());
        groceryAmountTextView.setOnEditorActionListener(new AmountOnEditorActionListener());
        groceryNameTextView.setAdapter(getAutocompleteCursorAdapter());

        LoadIngredientsTask loadIngredientsTask = new LoadIngredientsTask(groceryItemStore);
        loadIngredientsTask.execute();
    }

    private void addItem(String name, String amount) {
        if (name.equals("")) {
            Toast.makeText(getBaseContext(), "Wunschlos glücklich! ;-)", Toast.LENGTH_SHORT).show();
        } else {
            GroceryItem groceryItem = new GroceryItem(name, amount, false);
            groceryItemStore.addGroceryListItem(groceryItem);
            listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
        }
    }

    private CursorAdapter getAutocompleteCursorAdapter() {
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.autocomplete_row, null,
                new String[]{"_id"}, new int[]{android.R.id.text1}, 0);
        cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return groceryItemStore.autocompleteIngredients(constraint);
            }
        });

        cursorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(SQLiteDB.TableFields.GROCERY_LIST_NAME);
            }
        });
        return cursorAdapter;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView groceryName = (TextView) view.findViewById(R.id.grocery_item_row_textview_grocery_item);
        View strokeView = view.findViewById(R.id.grocery_item_row_view_stroke);
        boolean isStroked = strokeView.getVisibility() == View.VISIBLE;
        groceryItemStore.strokeGroceryListItem(groceryName.getText(), !isStroked);
        listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
    }

    private void openLoctionView() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        groceryItemStore.close();
        groceryItemStore = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        groceryItemStore = new GroceryItemStore(this);
    }

    private class DiscardOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d(getLocalClassName(), "DiscardButton was clicked");

            Cursor strokedItemCursor = groceryItemStore.getStrokedListItems();

            if(strokedItemCursor.getCount() == 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
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
                        groceryItemStore.deleteAllGroceryListItems();
                        listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else {
                while(strokedItemCursor.moveToNext()) {
                    groceryItemStore.removeGroceryListItem(
                            strokedItemCursor.getString(SQLiteDB.TableFields.GROCERY_LIST_NAME));
                }
                listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
            }
        }
    }

    private class AmountOnEditorActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addItem(groceryNameTextView.getText().toString(), groceryAmountTextView.getText().toString());
                groceryNameTextView.setText("");
                groceryAmountTextView.setText("");
                return true;
            } else return false;
        }
    }
}
