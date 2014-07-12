package de.anycook.app.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.anycook.app.store.GroceryItemStore;

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
public class LoadIngredientsTask extends AsyncTask<Void, Void, List<LoadIngredientsTask.SuggestionResponse>> {
    public static URL url;

    static {
        try {
            url = new URL("https://api.anycook.de/ingredient");
        } catch (MalformedURLException e) {
            Log.e(LoadIngredientsTask.class.getSimpleName(), "Failed to init url", e);
        }
    }

    private final GroceryItemStore db;

    public LoadIngredientsTask(GroceryItemStore db) {
        this.db = db;
    }

    @Override
    protected List<SuggestionResponse> doInBackground(Void... params) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage());
            }
            Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<SuggestionResponse>>() {
            }.getType();
            return gson.fromJson(reader, collectionType);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    protected void onPostExecute(List<SuggestionResponse> ingredients) {
        for(SuggestionResponse ingredient : ingredients) db.addIngredient(ingredient.getName());
    }

    public static class SuggestionResponse {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}
