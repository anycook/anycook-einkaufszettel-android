package de.anycook.app.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.app.model.RecipeResponse;
import de.anycook.app.store.RecipeStore;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadRecipesTask extends AsyncTask<Void, Void, List<RecipeResponse>> {
    private static final Logger LOGGER = LoggerManager.getLogger();

    public static URL url;

    static {
        try {
            url = new URL("https://api.anycook.de/recipe");
        } catch (MalformedURLException e) {
            LOGGER.e("Failed to init url", e);
        }
    }

    private final Context context;

    public LoadRecipesTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<RecipeResponse> doInBackground(Void... b) {
        try {
            LOGGER.d("Trying to load recipes from %s", url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }

            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            TypeToken<ArrayList<RecipeResponse>> typeToken = new TypeToken<ArrayList<RecipeResponse>>() { };
            return gson.fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            LOGGER.e("failed to load recipes from " + url, e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<RecipeResponse> recipeResponses) {
        if (recipeResponses == null || recipeResponses.size() == 0) {
            LOGGER.v("Didn't find any nearby recipes");
        } else {
            LOGGER.d(String.format("Found %d different recipes", recipeResponses.size()));
            RecipeStore recipeStore = new RecipeStore(context);
            try {
                recipeStore.open();
                recipeStore.replaceRecipes(recipeResponses);
            } finally {
                recipeStore.close();
            }
        }
    }

}
