package de.anycook.app.controller;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cipo7741 on 19.06.14.
 */
public class RecipeAutoCompleter implements Runnable {
    private static final String TAG = RecipeAutoCompleter.class.getSimpleName();

    private final String query;
    private final ListView listView;

    public RecipeAutoCompleter(String query, ListView listView) {
        this.query = query;
        this.listView = listView;
    }

    @Override
    public void run() {
        if (query == null) return;
        try {
            final ArrayList<String> recipeNames = searchRequest(query);
            listView.post(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> tmpRowAdapter = (ArrayAdapter<String>) listView.getAdapter();

                    for (String recipe : recipeNames) {

                        tmpRowAdapter.add(recipe);
                    }
                    listView.setAdapter(tmpRowAdapter);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, String.format("doRecipeSearch: %s %s", e.toString(), e.getMessage()));
        }
    }

    private ArrayList<String> searchRequest(String searchString) throws IOException {
        String url = "https://api.anycook.de/autocomplete?query=";
        URL autocompleteUrl = new URL(url + searchString);

        HttpURLConnection httpURLConnection = (HttpURLConnection) autocompleteUrl.openConnection();

        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }

        Reader reader = new InputStreamReader(httpURLConnection.getInputStream());

        Gson gson = new Gson();
        AutoCompleteResponse autoCompleteResponse = gson.fromJson(reader, AutoCompleteResponse.class);
        ArrayList<String> recipeNames = autoCompleteResponse.getRecipes();
        if (recipeNames.isEmpty()) {
            recipeNames.add("Leider keine Rezepte mit " + this.query + " gefunden.");
        }
        return recipeNames;
    }

    private static class AutoCompleteResponse {
        private ArrayList<String> recipes;

        public ArrayList<String> getRecipes() {
            return recipes;
        }

        public void setRecipes(ArrayList<String> recipes) {
            this.recipes = recipes;
        }
    }
}
