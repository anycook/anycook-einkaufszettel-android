package de.anycook.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        EditText editText = (EditText) findViewById(R.id.edittext_main);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    sendMessage(findViewById(R.id.edittext_main));
                    handled = true;
                }
                return handled;
            }
        });

    }

    private void sendMessage(View view) {
        EditText query = (EditText) findViewById(view.getId());
        TextView result = (TextView) findViewById(R.id.textview_main_output);
        result.setText(query.getText());
    }


    public void strikeTrough(View view) {
        TextView ingredient = (TextView) findViewById(view.getId());
        if ((ingredient.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
            ingredient.setPaintFlags(ingredient.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            ingredient.setTextColor(getResources().getColor(R.color.any_black));
        } else {
            ingredient.setPaintFlags(ingredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ingredient.setTextColor(getResources().getColor(R.color.any_green));
        }
    }

    public void goToList(View view) {
        Intent myIntent = new Intent(view.getContext(), GroceryListActivity.class); /** Class name here */
        startActivityForResult(myIntent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v("MyActivity", "at least it's in the oncreateoptions");
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_item_main_search:
                sendMessage(findViewById(R.id.edittext_main));
                return true;
            case R.id.menu_item_main_edit:
                goToList(findViewById(R.id.imageview_main));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                sendMessage(findViewById(R.id.search));
                return true;
            case R.id.action_compose:
                //composeMessage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


}

