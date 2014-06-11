package de.anycook.app;

import android.app.ListActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * gracery list, will be changed to main view
 * Created by cipo7741 on 07.06.14.
 */

import android.widget.*;
import de.anycook.app.adapter.RowItem;
import de.anycook.app.ListActivityAdapter;

public class GroceryListActivity extends ListActivity {

    static final RowItem[] Ingredients =
            new RowItem[] { new RowItem("Bananen","1"), new RowItem("Brot","3")};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        setListAdapter(new ListActivityAdapter(this, Ingredients));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //get selected items
        //String selectedValue = (String) getListAdapter().getItem(position);
        //Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

        //Log.d("GroceryListActivity",v.toString() + " " + position + " " + v.getId());

        //RelativeLayout rl = (RelativeLayout) findViewById(v.getId());
        //TextView tv = (TextView) rl.getChildAt(position);

        //Toast.makeText(this, "position: " + getListAdapter().getItem(position).toString() + " v.id:" + v.getId(), Toast.LENGTH_SHORT).show();
        //strikeTrough(tv);

        //v.findViewById(R.id.stroke).setVisibility(View.VISIBLE);
        View rowView = getListAdapter().getView(position, v, l);
        View strokeView = rowView.findViewById(R.id.stroke);
        if(strokeView.getVisibility() == View.INVISIBLE){
            strokeView.setVisibility(View.VISIBLE);
        } else {
            strokeView.setVisibility(View.INVISIBLE);
        }


        //getListAdapter().getItemId(position);
        //TextView selection = (TextView) l.getItemAtPosition(position);
        //strikeTrough(selection);
        //Toast.makeText(this, selection, Toast.LENGTH_LONG).show();

        //View stroke = new View(this);
        //do stuff like add text and listeners.

        //rl.addView(stroke);

    }


    public void strikeTrough(View view) {
        TextView ingredient = (TextView) findViewById(view.getId());
        Toast.makeText(this, ingredient.getText(), Toast.LENGTH_SHORT).show();
        if ((ingredient.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
            ingredient.setPaintFlags(ingredient.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            ingredient.setTextColor(getResources().getColor(R.color.any_black));
        } else {
            ingredient.setPaintFlags(ingredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ingredient.setTextColor(getResources().getColor(R.color.any_green));
        }
    }

}