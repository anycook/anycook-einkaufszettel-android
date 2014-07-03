package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.data.GroceryItem;

import java.util.List;

/**
 * Custom ArrayAdapter to fill EditMode with grocery item and amount
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class GroceryItemRowAdapter extends ArrayAdapter<GroceryItem> {

    private final List<GroceryItem> groceryValues;
    //private static final String TAG = GroceryItemRowAdapter.class.getSimpleName();
    private Activity context;


    public GroceryItemRowAdapter(Context context, int groceryRowResourceId, List<GroceryItem> values) {
        super(context, groceryRowResourceId, values);
        this.context = (Activity) context;
        groceryValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.grocery_item_row, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.itemText = (TextView) convertView
                    .findViewById(R.id.grocery_item_row_textview_grocery_item);
            viewHolder.amountText = (TextView) convertView
                    .findViewById(R.id.grocery_item_row_textview_amount);
            viewHolder.strokeView = convertView
                    .findViewById(R.id.grocery_item_row_view_stroke);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GroceryItem groceryValue = groceryValues.get(position);

        viewHolder.itemText.setText(groceryValue.getName(), TextView.BufferType.NORMAL);
        viewHolder.amountText.setText(groceryValue.getAmount(), TextView.BufferType.NORMAL);
        int defaultTextColor = viewHolder.itemText.getCurrentTextColor();
        if (groceryValue.isStroked()) {
            viewHolder.strokeView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.strokeView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView itemText;
        TextView amountText;
        View strokeView;
    }
}
