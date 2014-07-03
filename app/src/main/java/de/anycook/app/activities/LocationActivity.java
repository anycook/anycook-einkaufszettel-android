package de.anycook.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.activities.util.GPSTracker;
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.controller.RecipeLocator;
import de.anycook.app.controller.RecipeResponse;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cipo7741 on 01.07.14.
 */
public class LocationActivity extends Activity {


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
        ArrayList<RecipeResponse> recipeResponseArrayList = new ArrayList<>();
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
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        threadPool.submit(new RecipeLocator(location, recipeListView));

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

}
