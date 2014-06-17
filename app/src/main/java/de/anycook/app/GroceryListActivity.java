package de.anycook.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import de.anycook.app.adapter.IngredientRow;
import de.anycook.app.adapter.IngredientRowAdapter;

/**
 * gracery list, will be changed to main view
 * Created by cipo7741 on 07.06.14.
 */

public class GroceryListActivity extends ListActivity {

    static final IngredientRow[] Ingredients =
            new IngredientRow[]{new IngredientRow("Bananen", "1"), new IngredientRow("Brot", "3")};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setListAdapter(new IngredientRowAdapter(this, Ingredients));


        //Intent intent = new Intent(this, MyActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this, "strike trough", Toast.LENGTH_SHORT);
        View rowView = getListAdapter().getView(position, v, l);
        View strokeView = rowView.findViewById(R.id.ingredient_list_view_stroke);
        if (strokeView.getVisibility() == View.INVISIBLE) {
            strokeView.setVisibility(View.VISIBLE);
        } else {
            strokeView.setVisibility(View.INVISIBLE);
        }
    }

}