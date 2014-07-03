package de.anycook.app.controller;

import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.anycook.app.adapter.GroceryItemRowAdapter;
import de.anycook.app.data.GroceryItem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * The ingredient request to the anycook api.
 * https://api.anycook.de/recipe/%s/ingredients
 * <p/>
 * Runs in its own thread Thread.
 * Gets json with Gson. (https://code.google.com/p/google-gson/)
 * <p/>
 * Created by cipo7741 on 03.07.14.
 */
public class IngredientSelector implements Runnable {
    private static final String TAG = IngredientSelector.class.getSimpleName();

    private final String selectedRecipe;
    private final ListView ingredientListView;

    public IngredientSelector(String selectedRecipe, ListView ingredientListView) {
        this.selectedRecipe = selectedRecipe;
        this.ingredientListView = ingredientListView;
    }

    @Override
    public void run() {
        if (selectedRecipe == null) return;
        try {
            final ArrayList<IngredientResponse> recipeNames = searchRequest(selectedRecipe);
            Log.v(TAG, " run: " + selectedRecipe + " has " + recipeNames.size() + " ingredients.");
            ingredientListView.post(new Runnable() {
                @Override
                public void run() {
                    GroceryItemRowAdapter ingredientRowAdapter = (GroceryItemRowAdapter) ingredientListView.getAdapter();
                    for (IngredientResponse ingredientResponse : recipeNames) {
                        GroceryItem ingredient = new GroceryItem(ingredientResponse.getName(), ingredientResponse.getMenge(), false);
                        ingredientRowAdapter.add(ingredient);
                    }
                    ingredientListView.setAdapter(ingredientRowAdapter);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, String.format("run: %s %s", e.getMessage()));
        }
    }

    private ArrayList<IngredientResponse> searchRequest(String selectedRecipe) throws IOException {
        String urlPattern = "https://api.anycook.de/recipe/%s/ingredients";
        URL recipeUrl = new URL(String.format(urlPattern, Uri.encode(selectedRecipe)));
        HttpURLConnection httpURLConnection = (HttpURLConnection) recipeUrl.openConnection();
        Log.v(TAG, recipeUrl.toString() + "Connection Code:" + httpURLConnection.getResponseCode());
        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }
        Log.v(TAG, recipeUrl.toString());
        Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
        Log.v(TAG, reader.toString());
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<IngredientResponse>>() {
        }.getType();
        return gson.fromJson(reader, collectionType);
    }

    private static class IngredientResponse {

        private String name;
        private String menge;
        private String recipecounter;

        public IngredientResponse(String name, String menge, String recipecounter) {
            setName(name);
            setMenge(menge);
            setRecipecounter(recipecounter);
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMenge(String menge) {
            this.menge = menge;
        }

        public void setRecipecounter(String recipecounter) {
            this.recipecounter = recipecounter;
        }

        public String getName() {
            return name;
        }

        public String getMenge() {
            return menge;
        }

        public String getRecipecounter() {
            return recipecounter;
        }
    }
}
