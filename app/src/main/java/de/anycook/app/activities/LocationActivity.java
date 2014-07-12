package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.activities.util.GPSTracker;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.model.RecipeResponse;
import de.anycook.app.tasks.LoadRecipesTask;

/**
 * @author Cladia Sichting
 * @author Jan Graßegger
 */
public class LocationActivity extends ListActivity {

    private final static String urlPattern;

    static {
        urlPattern = "https://api.anycook.de/discover/near?latitude=%s&" +
                "longitude=%s&maxRadius=50&recipeNumber=20";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nearby_recipes);
        }

        GPSTracker gps = new GPSTracker(this);
        Location location;
        if (gps.canGetLocation()) {
            location = gps.getLocation();
        } else {
            gps.showSettingsAlert();
            location = null;
        }
        gps.stopUsingGPS();

        if (location == null) return;

        RecipeRowAdapter adapter = new RecipeRowAdapter(this);
        setListAdapter(adapter);

        String url = String.format(urlPattern, location.getLatitude(), location.getLongitude());
        LoadRecipesTask loadRecipesTask = new LoadRecipesTask(adapter);
        loadRecipesTask.execute(url);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, AddIngredientsActivity.class);
        Bundle b = new Bundle();
        RecipeResponse recipeResponse = (RecipeResponse) getListAdapter().getItem(position);
        b.putString("item", recipeResponse.getName()); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivityForResult(intent, 1234);
    }
}
