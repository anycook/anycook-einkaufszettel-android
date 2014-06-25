package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.anycook.app.R;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class IngredientRowAdapter extends ArrayAdapter<IngredientRow> {

    private static final String TAG = IngredientRowAdapter.class.getSimpleName();
    private Activity context;
    private final ArrayList<IngredientRow> ingredientValues;


    public IngredientRowAdapter(Context context, int ingredientRowResourceId, ArrayList<IngredientRow> values) {
        super(context, ingredientRowResourceId, values);
        this.context = (Activity) context;
        ingredientValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            Log.v(TAG, String.format("getView convertView == null, pos: %s, parent: %s", position, parent.toString()));
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.ingredient_row, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.ingredientText = (TextView) convertView
                    .findViewById(R.id.ingredient_row_textview_ingredient);
            viewHolder.amountText = (TextView) convertView
                    .findViewById(R.id.ingredient_row_textview_amount);
            viewHolder.strokeView = convertView
                    .findViewById(R.id.ingredient_row_view_stroke);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IngredientRow ingredientValue = ingredientValues.get(position);
        viewHolder.ingredientText.setText(ingredientValue.getIngredient(), TextView.BufferType.NORMAL);
        viewHolder.amountText.setText(ingredientValue.getAmount(), TextView.BufferType.NORMAL);
        //viewHolder.strokeView.setVisibility(View.VISIBLE);
        viewHolder.strokeView.setClickable(false);

        return convertView;
    }

    static class ViewHolder {
        TextView ingredientText;
        TextView amountText;
        View strokeView;
    }
}
