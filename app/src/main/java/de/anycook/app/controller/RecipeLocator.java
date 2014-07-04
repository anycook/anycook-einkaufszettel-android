package de.anycook.app.controller;

import android.location.Location;
import android.widget.ListView;
import de.anycook.app.tasks.LoadRecipesTask;

/**
 * The near recipe request to the anycook api.
 * https://api.anycook.de/discover/near?latitude=%s&longitude=%s&maxRadius=50&recipeNumber=20
 * <p/>
 * Runs in its own thread Thread.
 * Gets json with Gson. (https://code.google.com/p/google-gson/)
 * <p/>
 * Created by cipo7741 on 01.07.14.
 */
public class RecipeLocator {

    private final static String urlPattern;

    static {
        urlPattern = "https://api.anycook.de/discover/near?latitude=%s&" +
                "longitude=%s&maxRadius=50&recipeNumber=20";
    }

    private final Location location;
    private final ListView listView;

    public RecipeLocator(Location location, ListView recipeListView) {
        this.location = location;
        this.listView = recipeListView;
    }

    public void build() {
        if (location == null) return;
        String url = String.format(urlPattern, location.getLatitude(), location.getLongitude());
        LoadRecipesTask loadRecipesTask = new LoadRecipesTask(listView);
        loadRecipesTask.execute(url);
    }
}
