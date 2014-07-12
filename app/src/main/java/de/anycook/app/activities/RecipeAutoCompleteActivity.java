package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.controller.RecipeAutoCompleter;
import de.anycook.app.model.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * this searchable activity is responsible for returning recipe search results
 *
 * Created by cipo7741 on 13.06.14.
 */
public class RecipeAutoCompleteActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_list);

        List<RecipeResponse> recipeRowData = new ArrayList<>();
        this.setListAdapter(new RecipeRowAdapter(this, R.layout.recipe_row, recipeRowData));
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View view, int position, long id) {
        Intent intent = new Intent(this, AddIngredientsActivity.class);
        Bundle b = new Bundle();
        String item = ((TextView) view.findViewById(R.id.recipe_row_textview_recipe_name)).getText().toString();
        b.putString("item", item); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("Rezepte mit " + intent.getStringExtra(SearchManager.QUERY));
            }
            String query = intent.getStringExtra(SearchManager.QUERY);
            RecipeAutoCompleter autoCompleter = new RecipeAutoCompleter(query, this.getListView());
            autoCompleter.build();
        }
    }
}