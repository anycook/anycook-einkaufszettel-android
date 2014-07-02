package de.anycook.app.controller;


import android.util.Log;
import android.widget.TextView;
import com.google.gson.Gson;
import de.anycook.app.controller.util.RecipeResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Runnable to get expanded view details (+image, +description)
 * <p/>
 * Created by cipo7741 on 19.06.14.
 */
public class RecipeDetailer implements Runnable {
    private static final String TAG = RecipeDetailer.class.getSimpleName();

    private final String selectedRecipe;
    private final TextView recipeDescriptionTextView;

    public RecipeDetailer(String selectedRecipe, TextView recipeDescriptionTextView) {
        this.selectedRecipe = selectedRecipe;
        this.recipeDescriptionTextView = recipeDescriptionTextView;
    }

    @Override
    public void run() {
        if (this.selectedRecipe == null) return;
        try {
            final RecipeResponse recipeResponse = recipeDescriptionRequest(selectedRecipe);
            recipeDescriptionTextView.post(new Runnable() {

                @Override
                public void run() {
                    recipeDescriptionTextView.setText(recipeResponse.getDescription());
                }
            });
        } catch (IOException e) {
            Log.e(TAG, String.format("%s %s", e.toString(), e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * String imageUri = Uri.encode(recipeResponse.getImage().getSmallImage());
     * return new RecipeRow(imageUri, selectedRecipe, recipeResponse.getDescription());
     *
     * @param recipeName
     * @return RecipeResponse
     * @throws IOException
     */
    private RecipeResponse recipeDescriptionRequest(String recipeName) throws IOException {
        String url = "https://api.anycook.de/recipe/";
        URL recipeUrl = new URL(url + recipeName);
        HttpURLConnection httpURLConnection = (HttpURLConnection) recipeUrl.openConnection();
        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }
        Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
        Gson gson = new Gson();
        return gson.fromJson(reader, RecipeResponse.class);
    }

}
