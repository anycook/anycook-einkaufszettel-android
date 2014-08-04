package de.anycook.app.activities;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.app.R;
import de.anycook.app.util.StringTools;
import de.anycook.app.adapter.GroceryItemRowAdapter;
import de.anycook.app.store.GroceryItemStore;
import de.anycook.app.store.IngredientNameStore;
import de.anycook.app.store.SQLiteDB;

/**
 * @author Jan Graßegger
 * @author Claudia Sichting
 */
public class GroceryListFragment extends ListFragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

    private void addItem(String name, String amount) {
        if (name.equals("")) {
            Toast.makeText(getActivity().getBaseContext(), "Wunschlos glücklich! ;-)", Toast.LENGTH_SHORT).show();
        } else {
            groceryItemStore.addGroceryItem(name, amount);

            GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
            listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
        }
    }

    private CursorAdapter getAutocompleteCursorAdapter() {
        final IngredientNameStore ingredientNameStore = new IngredientNameStore(this.groceryNameTextView.getContext());
        ingredientNameStore.open();
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(),
                R.layout.autocomplete_row, null,
                new String[]{"_id"}, new int[]{android.R.id.text1}, 0);
        cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return ingredientNameStore.autocompleteIngredients(constraint);
            }
        });

        cursorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(SQLiteDB.TableFields.GROCERY_ITEM_NAME);
            }
        });
        return cursorAdapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.grocery_menu, menu);

        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grocery_menu_action_clear:
                clickedClearButton();
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
