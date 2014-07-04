package de.anycook.app.controller;

import android.widget.ListView;
import de.anycook.app.tasks.LoadRecipesTask;

/**
 * The auto completion request to the anycook api.
 * https://api.anycook.de/autocomplete_row?query=
 * <p/>
 * Runs in its own thread Thread.
 * Gets json with Gson. (https://code.google.com/p/google-gson/)
 * <p/>
 * Created by cipo7741 on 19.06.14.
 */
public class RecipeAutoCompleter{
    private final static String urlPattern;

    static {
        urlPattern = "https://api.anycook.de/recipe?startsWith=%s";
    }

    private final String query;
    private final ListView listView;

    public RecipeAutoCompleter(String query, ListView listView) {
        this.query = query;
        this.listView = listView;
    }

    public void build() {
        if (query == null) return;
        String url = String.format(urlPattern, query);
        LoadRecipesTask loadRecipesTask = new LoadRecipesTask(listView);
        loadRecipesTask.execute(url);
    }
}
