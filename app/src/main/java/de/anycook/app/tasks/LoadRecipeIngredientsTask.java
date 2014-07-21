package de.anycook.app.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.anycook.app.adapter.IngredientRowAdapter;
import de.anycook.app.model.Ingredient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadRecipeIngredientsTask extends AsyncTask<String, Void, List<Ingredient>>{

    private static final String urlPattern;

    static {
        urlPattern = "https://api.anycook.de/recipe/%s/ingredients";
    }

    private final IngredientRowAdapter ingredientRowAdapter;

    public LoadRecipeIngredientsTask(IngredientRowAdapter ingredientRowAdapter) {
        this.ingredientRowAdapter = ingredientRowAdapter;
    }

    @Override
    protected List<Ingredient> doInBackground(String... recipeNames) {
        try {
            //URLCodec urlCodec = new URLCodec();
            String urlString = String.format(urlPattern, UrlEscapers.urlPathSegmentEscaper().escape(recipeNames[0]));
            URL url = new URL(urlString);
            Log.d(getClass().getSimpleName(), "Loading ingredients from "+url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }
            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Ingredient>>() {
            }.getType();
            return gson.fromJson(reader, collectionType);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Failed to load recipe ingredients", e);
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<Ingredient> ingredients) {
        ingredientRowAdapter.addAll(ingredients);
    }
}
