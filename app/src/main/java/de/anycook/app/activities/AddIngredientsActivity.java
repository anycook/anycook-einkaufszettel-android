package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.adapter.GroceryItemRowAdapter;
import de.anycook.app.controller.IngredientSelector;
import de.anycook.app.data.GroceryDataSource;
import de.anycook.app.data.GroceryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by cipo7741 on 03.07.14.
 */
public class AddIngredientsActivity extends ListActivity {

    private List<GroceryItem> ingredientList;

    public AddIngredientsActivity() {
        this.ingredientList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list);

        Bundle b = getIntent().getExtras();
        String item = b.getString("item");
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(item);
        }

        setListAdapter(new GroceryItemRowAdapter(this, R.layout.grocery_item_row, ingredientList));
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        threadPool.submit(new IngredientSelector(item, getListView()));

        final GroceryDataSource dataSource = new GroceryDataSource(this);
        dataSource.open();

        final Button button = (Button) findViewById(R.id.ingredient_list_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<GroceryItem> groceryItems = dataSource.getAllGroceryItems();
                for (GroceryItem ingredient : ingredientList) {
                    if (!ingredient.isStroked()) {
                        dataSource.createGroceryItem(ingredient);
                    }
                }

                for (GroceryItem groceryItem : groceryItems) {
                    dataSource.deleteGroceryItem(groceryItem);
                    dataSource.createGroceryItem(groceryItem);
                }

                Intent intent = new Intent(button.getContext(), EditGroceryListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        changeItemStrokeVisibility(ingredientList.get(position));
        ((GroceryItemRowAdapter) getListView().getAdapter()).notifyDataSetChanged();
    }

    private void changeItemStrokeVisibility(GroceryItem groceryItem) {
        if (groceryItem.isStroked()) {
            groceryItem.setStroked(false);
        } else {
            groceryItem.setStroked(true);
        }
    }
}
