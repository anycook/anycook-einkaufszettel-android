package de.anycook.app.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.adapter.IngredientRowAdapter;
import de.anycook.app.model.Ingredient;
import de.anycook.app.store.GroceryItemStore;
import de.anycook.app.tasks.LoadRecipeIngredientsTask;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Claudia Sichting
 * @author Jan Graßegger
 */
public class AddIngredientsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private ListView ingredientListView;

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

        this.ingredientListView = (ListView) findViewById(R.id.ingredient_list_listview);
        IngredientRowAdapter adapter = new IngredientRowAdapter(this);
        ingredientListView.setAdapter(adapter);
        ingredientListView.setOnItemClickListener(this);

        LoadRecipeIngredientsTask loadRecipeIngredientsTask = new LoadRecipeIngredientsTask(adapter);
        loadRecipeIngredientsTask.execute(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredient_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IngredientRowAdapter adapter = (IngredientRowAdapter) ingredientListView.getAdapter();
        Ingredient ingredient = (Ingredient) ingredientListView.getItemAtPosition(position);
        ingredient.setChecked(!ingredient.isChecked());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.ingredient_menu_action_add:

                includeCheckedIngredientsToGroceryList();

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void includeCheckedIngredientsToGroceryList() {
        GroceryItemStore groceryItemStore = new GroceryItemStore(this);
        try {
            groceryItemStore.open();
            int ingredientsCount = ingredientListView.getAdapter().getCount();
            List<Ingredient> ingredients = new ArrayList<>(ingredientsCount);
            for (int i = 0; i < ingredientsCount; i++) {
                Ingredient ingredient = ((IngredientRowAdapter) ingredientListView.getAdapter()).getItem(i);
                if(!ingredient.isChecked()) continue;
                ingredients.add(ingredient);
            }
            groceryItemStore.addIngredientsToGroceryList(ingredients);
        } finally {
            groceryItemStore.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
