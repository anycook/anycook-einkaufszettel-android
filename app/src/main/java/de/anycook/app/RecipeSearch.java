package de.anycook.app;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import de.anycook.app.adapter.RecipeRow;
import de.anycook.app.adapter.RecipeRowAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by cipo7741 on 13.06.14.
 */
public class RecipeSearch extends Activity {
    static String url = "https://api.anycook.de/autocomplete?query=";
    private ArrayList<RecipeRow> recipeRowData;
    ListView recipeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        this.recipeListView = (ListView) this.findViewById(R.id.recipe_list_listview);
        recipeRowData = new ArrayList<>();
        this.recipeListView.setAdapter(new RecipeRowAdapter(this, R.layout.recipe_row, recipeRowData));

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doRecipeSearch(query);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doRecipeSearch(query);
        }
    }

    private void doRecipeSearch(String query) {

        final String what = query;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (what != null) {
                    try {
                        final ArrayList<RecipeRow> recipes = ProcessResponse(SearchRequest(what));
                        recipeListView.post(new Runnable() {
                            @Override
                            public void run() {
                                RecipeRowAdapter tmpRowAdapter = (RecipeRowAdapter) recipeListView.getAdapter();
                                for (RecipeRow recipe : recipes) {
                                    tmpRowAdapter.add(recipe);
                                }
                                recipeListView.setAdapter(tmpRowAdapter);
                            }
                        });
                    } catch (Exception e) {
                        Log.v("Exception anycook auto-complete", e.toString() + " " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    private String SearchRequest(String searchString) throws IOException {
        String newFeed = url + searchString;
        StringBuilder response = new StringBuilder();
        Log.v("SearchRequest", "anycook url: " + newFeed);
        URL url = new URL(newFeed);

        HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(httpconn.getInputStream()), 8192);
            String strLine;
            while ((strLine = input.readLine()) != null) {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }

    private ArrayList<RecipeRow> ProcessResponse(String resp) throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException {
        Log.v("ProcessResponse", "anycook result: " + resp);
        JSONObject mResponseObject = new JSONObject(resp);
        JSONArray mResponseArray = mResponseObject.getJSONArray("recipes");
        Log.v("ProcessResponse", "number of results: " + mResponseArray.length());
        ArrayList<RecipeRow> recipeRowData = new ArrayList<>();
        for (int i = 0; i < mResponseArray.length(); i++) {
            Log.v("result [", i + "] " + mResponseArray.get(i).toString());
            String recipe = mResponseArray.get(i).toString();
            RecipeRow dataOfRecipe = new RecipeRow(null, recipe, "keine Rezeptbeschreibung :(");
            recipeRowData.add(dataOfRecipe);
        }
        return recipeRowData;
    }

}
