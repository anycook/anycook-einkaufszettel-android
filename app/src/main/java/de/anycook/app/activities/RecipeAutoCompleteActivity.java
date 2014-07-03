package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.controller.RecipeAutoCompleter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this searchable activity is responsible for returning recipe search results
 *
 * Created by cipo7741 on 13.06.14.
 */
public class RecipeAutoCompleteActivity extends Activity {

    private ListView recipeListView;
    private static ExecutorService threadPool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        this.recipeListView = (ListView) this.findViewById(R.id.recipe_list_listview);
        ArrayList<String> recipeRowData = new ArrayList<>();
        this.recipeListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipeRowData));
        threadPool = Executors.newSingleThreadExecutor();
        this.recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(RecipeAutoCompleteActivity.this, AddIngredientsActivity.class);
                Bundle b = new Bundle();
                String item = ((TextView) view).getText().toString();
                b.putString("item", item); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivityForResult(intent, 1234);
                finish();

            }
        });
        Intent intent = getIntent();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rezepte mit " + intent.getStringExtra(SearchManager.QUERY));
        }
        handleIntent(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            threadPool.submit(new RecipeAutoCompleter(query, recipeListView));
        }
    }
}