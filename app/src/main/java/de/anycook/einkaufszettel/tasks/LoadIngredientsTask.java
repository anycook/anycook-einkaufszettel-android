package de.anycook.einkaufszettel.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.model.Ingredient;
import de.anycook.einkaufszettel.store.IngredientNameStore;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadIngredientsTask extends AsyncTask<Void, Void, List<Ingredient>> {
    private static final Logger LOGGER = LoggerManager.getLogger();

    public static URL url;

    static {
        try {
            url = new URL("https://api.anycook.de/ingredient");
        } catch (MalformedURLException e) {
            LOGGER.e("Failed to init url", e);
        }
    }

    private final Context context;

    public LoadIngredientsTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Ingredient> doInBackground(Void... params) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }
            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Ingredient>>() { } .getType();
            return gson.fromJson(reader, collectionType);
        } catch (IOException e) {
            LOGGER.e(e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<Ingredient> ingredients) {
        IngredientNameStore ingredientDatabase = new IngredientNameStore(context);
        try {
            ingredientDatabase.open();
            for (Ingredient ingredient : ingredients) { ingredientDatabase.addIngredient(ingredient); }
        } finally {
            ingredientDatabase.close();
        }
    }

}
