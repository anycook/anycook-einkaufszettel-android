package de.anycook.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.adapter.IngredientRow;
import de.anycook.app.adapter.IngredientRowAdapter;

import java.util.ArrayList;

/**
 * Implement the ActionMode.Callback interface for enabling the contextual action mode for the ingredient view
 * <p/>
 * Created by cipo7741 on 13.06.14.
 */
public class EditGroceryListActivity extends Activity {

    static final ArrayList<IngredientRow> Ingredients = new ArrayList<>();
    private static final String TAG = EditGroceryListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list);

        ListView ingredientListView = (ListView) this.findViewById(R.id.ingredient_list_listview);
        ingredientListView.setAdapter(new IngredientRowAdapter(this, R.layout.ingredient_row, Ingredients));

        final EditText editText;
        editText = (EditText) findViewById(R.id.ingredient_list_textview_amount);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage(editText);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void sendMessage(View view) {
        Log.v(TAG, "starting sendMessage");
    }

}