package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.activities.util.GPSTracker;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.controller.RecipeResponse;
import de.anycook.app.tasks.LoadRecipesTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by cipo7741 on 01.07.14.
 */
public class LocationActivity extends Activity {

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
            actionBar.setTitle("Rezepte in deiner Nähe");
        }

        ListView recipeListView = (ListView) this.findViewById(R.id.recipe_list_listview);
        List<RecipeResponse> recipeResponseArrayList = new ArrayList<>();
        recipeListView.setAdapter(new RecipeRowAdapter(this, R.layout.recipe_row, recipeResponseArrayList));

        GPSTracker gps = new GPSTracker(this);
        Location location;
        if (gps.canGetLocation()) {
            location = gps.getLocation();
        } else {
            gps.showSettingsAlert();
            location = null;
        }
        gps.stopUsingGPS();

        Log.d("Location", location == null ? "location was null" : location.toString());


        // TODO don't initiate RecipeLocator if location is null! Load standard location or throw an error
        //RecipeLocator recipeLocator = new RecipeLocator(location, recipeListView);
        //recipeLocator.build();
        if (location == null) return;
        String url = String.format(urlPattern, location.getLatitude(), location.getLongitude());
        LoadRecipesTask loadRecipesTask = new LoadRecipesTask(recipeListView);
        try {
            List<RecipeResponse> recipeResponses = loadRecipesTask.execute(url).get(10, TimeUnit.SECONDS);
            if(recipeResponses.isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Entschldigung");
                builder.setMessage("Leider wurden keine Rezepte in deiner Nähe gefunden!");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.show();
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }


        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

}
