package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.adapter.IngredientListRowAdapter;
import de.anycook.app.model.Ingredient;
import de.anycook.app.tasks.LoadRecipeIngredientsTask;


/**
 * @author Claudia Sichting
 * @author Jan Graßegger
 */
public class AddIngredientsActivity extends ListActivity implements View.OnClickListener{

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

        IngredientListRowAdapter adapter = new IngredientListRowAdapter(this);
        setListAdapter(adapter);
        LoadRecipeIngredientsTask loadRecipeIngredientsTask = new LoadRecipeIngredientsTask(adapter);
        loadRecipeIngredientsTask.execute(item);

        ImageButton addButton = (ImageButton) findViewById(R.id.add_ingredients_button);
        addButton.setOnClickListener(this);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        IngredientListRowAdapter adapter = (IngredientListRowAdapter) getListAdapter();
        Ingredient ingredient = (Ingredient) l.getItemAtPosition(position);
        ingredient.checked = !ingredient.checked;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        IngredientListRowAdapter adapter = (IngredientListRowAdapter) getListAdapter();
        adapter.saveChecked();
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
