package de.anycook.app.activities;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import de.anycook.app.R;
import de.anycook.app.adapter.GroceryItemRowAdapter;
import de.anycook.app.store.GroceryItemStore;
import de.anycook.app.store.SQLiteDB;

/**
 * @author Jan Graßegger
 * @author Claudia Sichting
 */
public class GroceryListFragment extends ListFragment implements MenuItem.OnMenuItemClickListener{
    private AutoCompleteTextView groceryNameTextView;
    private EditText groceryAmountTextView;

    //data
    private GroceryItemStore groceryItemStore;
    //private GroceryItemRowAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // hide keyboard
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.grocery_item_list, container, false);
        // load views
        //ListView listView = (ListView) view.findViewById(R.id.grocery_item_list_listview);
        this.groceryNameTextView =
                (AutoCompleteTextView) view.findViewById(R.id.grocery_item_list_autocompletetextview_grocery_item);
        this.groceryAmountTextView = (EditText) view.findViewById(R.id.grocery_item_list_textview_amount);

        // load and set grocery list data
        this.groceryItemStore = new GroceryItemStore(view.getContext());
        GroceryItemRowAdapter listAdapter = new GroceryItemRowAdapter(view.getContext(), R.layout.grocery_item_row,
                groceryItemStore.getAllGroceryItemsCursor(), 0);


        //init data source adapter
        setListAdapter(listAdapter);

        groceryAmountTextView.setOnEditorActionListener(new AmountOnEditorActionListener());
        groceryNameTextView.setAdapter(getAutocompleteCursorAdapter());


        //getActivity().setTitle(R.string.app_label);

        return view;
    }

    private void addItem(String name, String amount) {
        if (name.equals("")) {
            Toast.makeText(getActivity().getBaseContext(), "Wunschlos glücklich! ;-)", Toast.LENGTH_SHORT).show();
        } else {
            groceryItemStore.addToGroceryList(name, amount);

            GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
            listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
        }
    }

    private CursorAdapter getAutocompleteCursorAdapter() {
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(),
                R.layout.autocomplete_row, null,
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.grocery_list, menu);
        menu.findItem(R.id.action_clear).setOnMenuItemClickListener(this);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_main_locate:
                openLoctionView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }  */

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        TextView groceryName = (TextView) view.findViewById(R.id.grocery_item_row_textview_grocery_item);
        View strokeView = view.findViewById(R.id.grocery_item_row_view_stroke);
        boolean isStroked = strokeView.getVisibility() == View.VISIBLE;
        groceryItemStore.strokeGroceryListItem(groceryName.getText(), !isStroked);
        GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
        listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
    }

    /*private void openLoctionView() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        groceryItemStore.close();
        groceryItemStore = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        groceryItemStore = new GroceryItemStore(getActivity().getBaseContext());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Cursor strokedItemCursor = groceryItemStore.getStrokedListItems();
        if(getListView().getCount() == 0) return false;
        if(strokedItemCursor.getCount() == 0) {
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
                    groceryItemStore.deleteAllGroceryListItems();
                    GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
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
            GroceryItemRowAdapter listAdapter = (GroceryItemRowAdapter) getListAdapter();
            listAdapter.changeCursor(groceryItemStore.getAllGroceryItemsCursor());
        }

        return true;
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