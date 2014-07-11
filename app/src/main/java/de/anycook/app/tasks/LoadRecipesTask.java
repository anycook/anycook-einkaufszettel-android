package de.anycook.app.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.controller.RecipeResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadRecipesTask extends AsyncTask<String, Void, List<RecipeResponse>> {
    private final ListView listView;

    public LoadRecipesTask(ListView listView) {
        this.listView = listView;
    }

    @Override
    protected List<RecipeResponse> doInBackground(String... url) {
        try {
            URL nearRecipesUrl = new URL(url[0]);
            Log.d(getClass().getSimpleName(), "Trying to load recipes from "+url[0]);

            HttpURLConnection httpURLConnection = (HttpURLConnection) nearRecipesUrl.openConnection();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }

            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<RecipeResponse>>() {
            }.getType();
            return gson.fromJson(reader, collectionType);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "failed to load recipes from "+url[0], e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<RecipeResponse> recipeResponses) {
        if(recipeResponses.size() == 0) {
            Log.v(getClass().getSimpleName(), "Didn't find any nearby recipes");
        } else {
            Log.d(getClass().getSimpleName(),
                    String.format("Found %d different recipes", recipeResponses.size()));
            RecipeRowAdapter recipeRowAdapter = (RecipeRowAdapter) listView.getAdapter();
            for (RecipeResponse recipeResponse : recipeResponses) {
                recipeRowAdapter.add(recipeResponse);
            }
        }

    }
}
