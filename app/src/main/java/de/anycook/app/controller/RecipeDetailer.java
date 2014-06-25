package de.anycook.app.controller;


import android.net.Uri;
import android.util.Log;
import android.widget.ExpandableListView;
import com.google.gson.Gson;
import de.anycook.app.adapter.RecipeRow;
import de.anycook.app.adapter.RecipeRowAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * Runnable to get expanded view details (+image, +description)
 * <p/>
 * Created by cipo7741 on 19.06.14.
 */
public class RecipeDetailer implements Runnable {
    private static final String TAG = RecipeDetailer.class.getSimpleName();

    private final String selectedRecipe;
    private final int position;
    private final ExpandableListView expandableListView;

    public RecipeDetailer(String selectedRecipe, int position, ExpandableListView expandableListView) {
        this.selectedRecipe = selectedRecipe;
        this.position = position;
        this.expandableListView = expandableListView;
    }

    @Override
    public void run() {
        if (this.selectedRecipe == null) return;
        try {
            final RecipeRow recipe = recipeRequest(selectedRecipe);
            expandableListView.post(new Runnable() {

                @Override
                public void run() {
                    RecipeRowAdapter tmpRowAdapter = (RecipeRowAdapter) expandableListView.getAdapter();
                    tmpRowAdapter.add(recipe);
                    expandableListView.setAdapter(tmpRowAdapter);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, String.format("%s %s", e.toString(), e.getMessage()));
            e.printStackTrace();
        }
    }

    private RecipeRow recipeRequest(String recipeName) throws IOException {
        String url = "https://api.anycook.de/recipe/";
        URL recipeUrl = new URL(url + recipeName);
        HttpURLConnection httpURLConnection = (HttpURLConnection) recipeUrl.openConnection();
        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }
        Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
        Gson gson = new Gson();
        RecipeResponse recipeResponse = gson.fromJson(reader, RecipeResponse.class);
        String imageUri = Uri.encode(recipeResponse.getImage().getSmallImage());
        RecipeRow recipeRow = new RecipeRow(imageUri, selectedRecipe, recipeResponse.getDescription());
        return recipeRow;
    }

    private RecipeRow processRecipeResponse(String resp) throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException {
        Log.v("processRecipeResponse", "anycook result: " + resp);
        JSONObject rawResponseObject = new JSONObject(resp);
        String name = rawResponseObject.getString("name");
        String imageUri = rawResponseObject.getJSONObject("image").getString("small");
        String description = rawResponseObject.getString("description");
        return new RecipeRow(Uri.encode(imageUri), name, description);
    }

    private static class RecipeResponse {
        private String description;
        private RecipeImage image;

        public String getDescription() {
            return description;
        }

        public void setRecipe(String recipeDescription) {
            this.description = recipeDescription;
        }

        public RecipeImage getImage() {
            return this.image;
        }

        public void setImage(RecipeImage recipeImage) {
            this.image = recipeImage;
        }
    }

    private static class RecipeImage {
        private String small;

        public String getSmallImage() {
            return this.small;
        }

        public void setSmallImage(String smallImage) {
            this.small = smallImage;
        }
    }
}
