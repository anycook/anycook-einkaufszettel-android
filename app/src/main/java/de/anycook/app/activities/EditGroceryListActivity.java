package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import de.anycook.app.adapter.IngredientRow;
import de.anycook.app.adapter.IngredientRowAdapter;

import java.util.ArrayList;

/**
 * Created by cipo7741 on 13.06.14.
 */
public class EditGroceryListActivity extends ListActivity {

    private static final String TAG = EditGroceryListActivity.class.getSimpleName();

    static final ArrayList<IngredientRow> Ingredients =
            new ArrayList<IngredientRow>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new IngredientRowAdapter(this, Ingredients));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //todo: edit grocery list
        Toast.makeText(this, "edit grocery list", Toast.LENGTH_SHORT);
    }

}