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
import de.anycook.app.adapter.IngredientListRowAdapter;
import de.anycook.app.model.Ingredient;
import de.anycook.app.tasks.LoadRecipeIngredientsTask;


/**
 * @author Claudia Sichting
 * @author Jan Graßegger
 */
public class AddIngredientsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private ListView listView;


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

        this.listView = (ListView) findViewById(android.R.id.list);


        IngredientListRowAdapter adapter = new IngredientListRowAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

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
        IngredientListRowAdapter adapter = (IngredientListRowAdapter) listView.getAdapter();
        Ingredient ingredient = (Ingredient) listView.getItemAtPosition(position);
        ingredient.checked = !ingredient.checked;
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.ingredient_menu_action_add:
                IngredientListRowAdapter adapter = (IngredientListRowAdapter) listView.getAdapter();
                adapter.saveChecked();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
