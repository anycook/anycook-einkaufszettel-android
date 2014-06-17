package de.anycook.app;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MyActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_main_search).getActionView();
        //TextView tw = (TextView) findViewById(R.id.textview_main_display);
        //tw.setText(searchView.getQuery());
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_main_edit) {
            actionEdit(findViewById(R.id.menu_item_main_edit));
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionEdit(View view) {
        //todo: implement edit activity to add custom ingredients
        //Intent myIntent = new Intent(view.getContext(), GroceryListActivity.class); /** Class name here */
        //startActivityForResult(myIntent, 0);
    }

}

