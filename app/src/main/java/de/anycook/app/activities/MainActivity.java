package de.anycook.app.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.adapter.DrawerRowAdapter;
import de.anycook.app.tasks.LoadIngredientsTask;
import de.anycook.app.tasks.LoadRecipesTask;


/**
 *
 *
 * @author Jan Graßegger <jan@anycook.de>
 * @author Claudia Sichting
 */
public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private String title;
    private String[] menuTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // menu button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // create Drawer
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerList = (ListView) findViewById(R.id.left_drawer);
        this.menuTitles = getResources().getStringArray(R.array.menu_names);
        drawerList.setAdapter(new DrawerRowAdapter(this, menuTitles));
        drawerList.setOnItemClickListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //hide keyboard
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
                //getActionBar().setTitle();
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            selectMenuItem(0);
            LoadIngredientsTask loadIngredientsTask = new LoadIngredientsTask(this);
            loadIngredientsTask.execute();
            LoadRecipesTask loadRecipesTask = new LoadRecipesTask(this);
            loadRecipesTask.execute();
        } else {
            selectMenuItem(savedInstanceState.getInt("fragment"));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectMenuItem(int position) {
        title = menuTitles[position];
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new GroceryListFragment();
                break;
            case 1:
                fragment = new RecipeFragment();
                break;
            case 2:
                fragment = new LocationFragment();
                break;
            default:
                return;
        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                .addToBackStack(title)
                .commit();
        // update selected item and title, then close the drawer
        drawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        drawerLayout.closeDrawer(drawerList);
        invalidateOptionsMenu();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectMenuItem(position);
    }
}
