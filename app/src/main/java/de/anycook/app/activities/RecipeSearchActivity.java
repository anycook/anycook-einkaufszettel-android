package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.controller.RecipeResponse;
import de.anycook.app.tasks.LoadRecipesTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * this searchable activity is responsible for returning recipe search results
 *
 * Created by cipo7741 on 13.06.14.
 */
public class RecipeSearchActivity extends Activity {

    private final static String urlPattern;

    static {
        urlPattern = "https://api.anycook.de/recipe?startsWith=%s";
    }

    private ListView recipeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        this.recipeListView = (ListView) this.findViewById(R.id.recipe_list_listview);
        List<RecipeResponse> recipeRowData = new ArrayList<>();
        this.recipeListView.setAdapter(new RecipeRowAdapter(this, R.layout.recipe_row, recipeRowData));
        this.recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(RecipeSearchActivity.this, AddIngredientsActivity.class);
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
            actionBar.setTitle("Rezepte mit \"" + intent.getStringExtra(SearchManager.QUERY) + "\"");
        }
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query == null) return;
            String url = String.format(urlPattern, query);
            LoadRecipesTask loadRecipesTask = new LoadRecipesTask(recipeListView);
            try {
                List<RecipeResponse> recipeResponses = loadRecipesTask.execute(url).get(10, TimeUnit.SECONDS);
                if(recipeResponses.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Entschldigung");
                    builder.setMessage("Leider wurden keine Rezepte mit \"" + "\" gefunden!");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.show();
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}