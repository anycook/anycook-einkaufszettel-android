package de.anycook.app.tasks;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.app.adapter.RecipeRowArrayAdapter;
import de.anycook.app.model.RecipeResponse;

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
public class LoadNearbyRecipesTask extends AsyncTask<String, Void, List<RecipeResponse>> {
    private static final Logger LOGGER = LoggerManager.getLogger();

    private final RecipeRowArrayAdapter adapter;

    public LoadNearbyRecipesTask(RecipeRowArrayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<RecipeResponse> doInBackground(String... url) {
        try {
            URL nearRecipesUrl = new URL(url[0]);
            LOGGER.v("Trying to load recipes from %s", url[0]);

            HttpURLConnection httpURLConnection = (HttpURLConnection) nearRecipesUrl.openConnection();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }

            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<RecipeResponse>>() { } .getType();
            return gson.fromJson(reader, collectionType);
        } catch (IOException e) {
            LOGGER.e("failed to load recipes from " + url[0], e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<RecipeResponse> recipeResponses) {
        if (recipeResponses.size() == 0) {
            LOGGER.i("Didn't find any nearby recipes");
        } else {
            LOGGER.d(String.format("Found %d different recipes", recipeResponses.size()));
            adapter.addAll(recipeResponses);
        }

    }
}
