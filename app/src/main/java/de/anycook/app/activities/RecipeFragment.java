package de.anycook.app.activities;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.adapter.RecipeRowCursorAdapter;
import de.anycook.app.store.RecipeStore;

/**
 * this searchable activity is responsible for returning recipe search results
 *
 * @author Jan Grassegger <jan@anycook.de>
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipeFragment extends ListFragment implements SearchView.OnQueryTextListener {

    private RecipeStore recipeDatabase;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDatabase = new RecipeStore(getActivity());
        recipeDatabase.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list, container, false);

        setHasOptionsMenu(true);
        RecipeRowCursorAdapter recipeRowCursorAdapter = new RecipeRowCursorAdapter(getActivity());
        setListAdapter(recipeRowCursorAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            onQueryTextChange(savedInstanceState.getString("query"));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menu.clear();
        menuInflater.inflate(R.menu.recipe_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.recipe_menu_search);
        this.searchView = (android.support.v7.widget.SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recipe_menu_camera:
                Intent intent = new Intent(getActivity(), RecipePhotoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AddIngredientsActivity.class);
        Bundle b = new Bundle();
        String item = ((TextView) view.findViewById(R.id.recipe_row_textview_recipe_name)).getText().toString();
        b.putString("item", item); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.setVisibility(View.INVISIBLE);
        searchView.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.v(RecipeFragment.class.getSimpleName(), "Searching for " + query);
        RecipeRowCursorAdapter adapter = (RecipeRowCursorAdapter) getListAdapter();
        adapter.changeCursor(recipeDatabase.getRecipesForQuery(query));
        return false;
    }

    @Override
    public void onResume() {
        Log.d(getClass().getSimpleName(),"OnResume");
        super.onResume();
        recipeDatabase.open();
    }

    @Override
    public void onPause() {
        Log.d(getClass().getSimpleName(),"OnPause");
        super.onPause();
        recipeDatabase.close();
    }
}