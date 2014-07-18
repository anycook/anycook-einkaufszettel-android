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
import de.anycook.app.adapter.RecipeRowAdapter;
import de.anycook.app.store.GroceryItemStore;

/**
 * this searchable activity is responsible for returning recipe search results
 *
 * Created by cipo7741 on 13.06.14.
 */
public class RecipeFragment extends ListFragment implements SearchView.OnQueryTextListener{

    private final static String urlPattern;

    static {
        urlPattern = "https://api.anycook.de/recipe?startsWith=%s";
    }

    private GroceryItemStore db;
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new GroceryItemStore(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list, container, false);

        setHasOptionsMenu(true);
        RecipeRowAdapter recipeRowAdapter = new RecipeRowAdapter(getActivity(), db.getAllRecipesCursor());
        setListAdapter(recipeRowAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        menu.clear();
        menuInflater.inflate(R.menu.main_activity_actions, menu);

        //SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_item_main_search);
        this.searchView = (android.support.v7.widget.SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);

        /*if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        searchMenuItem.collapseActionView();
                        searchView.setQuery("", false);
                        searchView.setIconified(true);
                    }
                }
            });

            searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // hides and then unhides search tab to make sure
                    // keyboard disappears when query is submitted (=_=;)
                    searchView.setVisibility(View.INVISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }                                              */

        super.onCreateOptionsMenu(menu, menuInflater);
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
    public boolean onQueryTextChange(String s) {
        Log.v(RecipeFragment.class.getSimpleName(), "Searching for " + s);
        RecipeRowAdapter adapter = (RecipeRowAdapter) getListAdapter();
        adapter.changeCursor(db.getRecipesForQuery(s));
        return false;
    }
}