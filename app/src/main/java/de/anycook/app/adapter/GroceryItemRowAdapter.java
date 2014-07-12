package de.anycook.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.store.SQLiteDB;

/**
 * Custom ArrayAdapter to fill EditMode with grocery item and amount
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class GroceryItemRowAdapter extends ResourceCursorAdapter {

    public GroceryItemRowAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView groceryName = (TextView) view.findViewById(R.id.grocery_item_row_textview_grocery_item);
        groceryName.setText(cursor.getString(SQLiteDB.TableFields.GROCERY_LIST_NAME));

        TextView groceryAmount = (TextView) view.findViewById(R.id.grocery_item_row_textview_amount);
        groceryAmount.setText(cursor.getString(SQLiteDB.TableFields.GROCERY_LIST_AMOUNT));

        View strokeView = view.findViewById(R.id.grocery_item_row_view_stroke);
        boolean isVisible = cursor.getInt(SQLiteDB.TableFields.GROCERY_LIST_STROKE) > 0;
        strokeView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
}
