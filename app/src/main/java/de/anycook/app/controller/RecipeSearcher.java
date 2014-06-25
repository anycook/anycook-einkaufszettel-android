package de.anycook.app.controller;

import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import com.google.gson.Gson;
import de.anycook.app.adapter.RecipeRow;
import de.anycook.app.adapter.RecipeRowAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cipo7741 on 19.06.14.
 */
public class RecipeSearcher implements Runnable {
    private static final String TAG = RecipeSearcher.class.getSimpleName();

    private final String query;
    private final ListView listView;

    public RecipeSearcher(String query, ListView listView) {
        this.query = query;
        this.listView = listView;
    }

    @Override
    public void run() {
        if (query == null) return;
        try {
            List<String> recipes = searchRequest(query);
            final ArrayList<RecipeRow> recipeViews = processResponse(recipes);
            listView.post(new Runnable() {
                @Override
                public void run() {
                    RecipeRowAdapter tmpRowAdapter = (RecipeRowAdapter) listView.getAdapter();
                    for (RecipeRow recipe : recipeViews) {
                        tmpRowAdapter.add(recipe);
                    }
                    listView.setAdapter(tmpRowAdapter);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, String.format("doRecipeSearch: %s %s", e.toString(), e.getMessage()), e.getCause());
        }
    }

    private List<String> searchRequest(String searchString) throws IOException {
        String url = "https://api.anycook.de/autocomplete?query=";
        URL autocompleteUrl = new URL(url + searchString);

        HttpURLConnection httpURLConnection = (HttpURLConnection) autocompleteUrl.openConnection();

        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }

        Reader reader = new InputStreamReader(httpURLConnection.getInputStream());

        Gson gson = new Gson();
        AutoCompleteResponse autoCompleteResponse = gson.fromJson(reader, AutoCompleteResponse.class);
        return autoCompleteResponse.getRecipes();
    }

    private ArrayList<RecipeRow> processResponse(List<String> recipeNames) throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException {
        ArrayList<RecipeRow> recipeListData = new ArrayList<>();
        for (String recipeName : recipeNames) {
            RecipeRow dataOfRecipe = processRecipeResponse(recipeRequest(recipeName));
            recipeListData.add(dataOfRecipe);
        }
        return recipeListData;
    }

    private String recipeRequest(String recipeName) throws IOException {
        String recipeUrl = "https://api.anycook.de/recipe/";
        String newFeed = recipeUrl + Uri.encode(recipeName);
        StringBuilder response = new StringBuilder();
        Log.v("recipeRequest", "anycook url: " + newFeed);
        URL url = new URL(newFeed);

        HttpURLConnection http_connection = (HttpURLConnection) url.openConnection();

        if (http_connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(http_connection.getInputStream()), 8192);
            String strLine;
            while ((strLine = input.readLine()) != null) {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }

    private RecipeRow processRecipeResponse(String resp) throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException {
        Log.v("processRecipeResponse", "anycook result: " + resp);
        JSONObject rawResponseObject = new JSONObject(resp);
        String name = rawResponseObject.getString("name");
        String imageUri = rawResponseObject.getJSONObject("image").getString("small");
        String description = rawResponseObject.getString("description");

        return new RecipeRow(Uri.encode(imageUri), name, description);
    }


    private static class AutoCompleteResponse {
        private List<String> recipes;

        public List<String> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<String> recipes) {
            this.recipes = recipes;
        }
    }
}
