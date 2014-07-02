package de.anycook.app.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import de.anycook.app.R;
import de.anycook.app.adapter.Recipe;
import de.anycook.app.controller.util.RecipesDataSource;

import java.util.List;
import java.util.Random;

/**
 * database example activity
 * <p/>
 * Created by cipo7741 on 02.07.14.
 */
public class TestDatabaseActivity extends ListActivity {
    private RecipesDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        datasource = new RecipesDataSource(this);
        datasource.open();

        List<Recipe> values = datasource.getAllRecipes();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Recipe> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Recipe> adapter = (ArrayAdapter<Recipe>) getListAdapter();
        Recipe recipe;
        switch (view.getId()) {
            case R.id.add:
                String[] recipes = new String[]{"Cool", "Very nice", "Hate it"};
                int nextInt = new Random().nextInt(3);
                // save the new recipe to the database
                recipe = datasource.createRecipe(recipes[nextInt]);
                adapter.add(recipe);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    recipe = (Recipe) getListAdapter().getItem(0);
                    datasource.deleteRecipe(recipe);
                    adapter.remove(recipe);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        Log.v(TestDatabaseActivity.class.getSimpleName(), "app resumed");
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(TestDatabaseActivity.class.getSimpleName(), "app paused");
        datasource.close();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(TestDatabaseActivity.class.getSimpleName(), "app stopped");
        super.onDestroy();
    }

    @Override
    protected void onDestroy() {
        Log.v(TestDatabaseActivity.class.getSimpleName(), "app destroyed");
        super.onDestroy();
    }
}
