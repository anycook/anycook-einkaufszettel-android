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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadRecipesTask extends AsyncTask<Void, Void, List<RecipeResponse>> {
    private final static Logger logger = LoggerManager.getLogger();

    public static URL url;

    static {
        try {
            url = new URL("https://api.anycook.de/recipe");
        } catch (MalformedURLException e) {
            logger.e("Failed to init url", e);
        }
    }

    private final Context context;

    public LoadRecipesTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<RecipeResponse> doInBackground(Void... b) {
        try {
            logger.d("Trying to load recipes from " + url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }

            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<RecipeResponse>>() {
            }.getType();
            return gson.fromJson(reader, collectionType);
        } catch (IOException e) {
            logger.e("failed to load recipes from "+url, e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final List<RecipeResponse> recipeResponses) {
        if(recipeResponses == null || recipeResponses.size() == 0) {
            logger.v("Didn't find any nearby recipes");
        } else {
            logger.d(String.format("Found %d different recipes", recipeResponses.size()));
            RecipeStore recipeStore = new RecipeStore(context);
            try{
                recipeStore.open();
                recipeStore.replaceRecipes(recipeResponses);
            } finally {
                recipeStore.close();
            }
        }
    }

}
