package de.anycook.app.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.anycook.app.adapter.IngredientListRowAdapter;
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

    private final Context context;
    private final IngredientListRowAdapter adapter;

    public LoadRecipeIngredientsTask(Context context, IngredientListRowAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected List<Ingredient> doInBackground(String... recipeNames) {
        try {
            URL url = new URL(String.format(urlPattern, recipeNames[0]));
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
        adapter.addAll(ingredients);
    }
}
