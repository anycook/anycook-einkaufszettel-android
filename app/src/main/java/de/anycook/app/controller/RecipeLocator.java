package de.anycook.app.controller;

import android.location.Location;
import android.util.Log;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.controller.util.RecipeResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * recipe locator
 * <p/>
 * Created by cipo7741 on 01.07.14.
 */
public class RecipeLocator implements Runnable {

    private final String TAG = RecipeLocator.class.getSimpleName();

    private final Location location;
    private final ListView listView;

    public RecipeLocator(Location location, ListView recipeListView) {
        this.location = location;
        this.listView = recipeListView;
    }

    @Override
    public void run() {

        if (location == null) return;
        try {
            final ArrayList<RecipeResponse> recipeData = searchRequest(location);
            listView.post(new Runnable() {
                @Override
                public void run() {
                    RecipeRowAdapter recipeRowAdapter = (RecipeRowAdapter) listView.getAdapter();
                    for (RecipeResponse recipeResponse : recipeData) {
                        Log.v(TAG, recipeResponse.getName());
                        recipeRowAdapter.add(recipeResponse);
                    }
                    listView.setAdapter(recipeRowAdapter);
                    //recipeRowAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, String.format("doRecipeSearch: %s %s", e.toString(), e.getMessage()));
        }
    }

    private ArrayList<RecipeResponse> searchRequest(Location location) throws IOException {
        String urlPattern = "https://api.anycook.de/discover/near?latitude=%d&longitude=%d&maxRadius=50&recipeNumber=20";
        urlPattern = String.format(urlPattern, location.getLatitude(), location.getLongitude());

        URL nearRecipesUrl = new URL(urlPattern);
        Log.v(TAG, urlPattern);

        HttpURLConnection httpURLConnection = (HttpURLConnection) nearRecipesUrl.openConnection();

        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }

        Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<RecipeResponse>>() {
        }.getType();
        return gson.fromJson(reader, collectionType);
    }

}
