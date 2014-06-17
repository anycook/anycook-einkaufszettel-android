package de.anycook.app;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by cipo7741 on 13.06.14.
 */
public class EditGroceryListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //todo: edit grocery list
        Toast.makeText(this, "edit grocery list", Toast.LENGTH_SHORT);
    }

}