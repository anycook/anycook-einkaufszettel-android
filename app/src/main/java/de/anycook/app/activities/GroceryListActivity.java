package de.anycook.app.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import de.anycook.app.R;
import de.anycook.app.adapter.Ingredient;

import java.util.ArrayList;

/**
 * gracery list, will be changed to main view
 * Created by cipo7741 on 07.06.14.
 */

public class GroceryListActivity extends ListActivity {

    static final ArrayList<Ingredient> Ingredients =
            new ArrayList<Ingredient>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //setListAdapter(new IngredientRowAdapter(this, Ingredients));


        //Intent intent = new Intent(this, MyActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this, "strike trough", Toast.LENGTH_SHORT);
        View rowView = getListAdapter().getView(position, v, l);
        View strokeView = rowView.findViewById(R.id.ingredient_row_view_stroke);
        if (strokeView.getVisibility() == View.INVISIBLE) {
            strokeView.setVisibility(View.VISIBLE);
        } else {
            strokeView.setVisibility(View.INVISIBLE);
        }
    }

}