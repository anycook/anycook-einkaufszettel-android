package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.adapter.RecipeRow;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.controller.RecipeSearcher;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this searchable activity is responsible for returning recipe search results
 * new layout: recipe list
 * <p/>
 * todo: add images and description
 * <p/>
 * Created by cipo7741 on 13.06.14.
 */
public class RecipeSearchActivity extends Activity {
    private static final String TAG = RecipeSearchActivity.class.getSimpleName();
    private ListView recipeListView;
    private ExecutorService threadPool;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate runs");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.recipeListView = (ListView) this.findViewById(R.id.recipe_list_listview);
        ArrayList<RecipeRow> recipeRowData = new ArrayList<>();
        this.recipeListView.setAdapter(new RecipeRowAdapter(this, R.layout.recipe_row, recipeRowData));
        this.threadPool = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
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
            threadPool.submit(new RecipeSearcher(query, recipeListView));
        }
    }
}