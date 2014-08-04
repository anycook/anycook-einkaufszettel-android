package de.anycook.app.activities;

import android.app.ListFragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.noveogroup.android.log.Log;
import de.anycook.app.R;
import de.anycook.app.util.GPSTracker;
import de.anycook.app.adapter.RecipeRowArrayAdapter;
import de.anycook.app.model.RecipeResponse;
import de.anycook.app.tasks.LoadNearbyRecipesTask;

/**
 * @author Cladia Sichting
 * @author Jan Graßegger
 */
public class LocationFragment extends ListFragment {

    private final static String urlPattern;

    static {
        urlPattern = "https://api.anycook.de/discover/near?latitude=%s&" +
                "longitude=%s&maxRadius=50&recipeNumber=20";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list, container, false);

        try {
            GPSTracker gps = new GPSTracker(getActivity());
            Location location = gps.getLocation();
            RecipeRowArrayAdapter adapter = new RecipeRowArrayAdapter(getActivity());
            setListAdapter(adapter);
            String url = String.format(urlPattern, location.getLatitude(), location.getLongitude());
            LoadNearbyRecipesTask loadNearbyRecipesTask = new LoadNearbyRecipesTask(adapter);
            loadNearbyRecipesTask.execute(url);

        } catch (GPSTracker.UnableToRetrieveLocationException e) {
            Log.i(e.getMessage());
            GPSTracker.showSettingsAlert(getActivity());
        }
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), AddIngredientsActivity.class);
        Bundle b = new Bundle();
        RecipeResponse recipeResponse = (RecipeResponse) getListAdapter().getItem(position);
        b.putString("item", recipeResponse.getName());
        intent.putExtras(b);
        startActivity(intent);
    }
}
