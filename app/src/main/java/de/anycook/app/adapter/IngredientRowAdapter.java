package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.anycook.app.R;

import java.util.List;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class IngredientRowAdapter extends ArrayAdapter<Ingredient> {

    private final List<Ingredient> ingredientValues;
    //private static final String TAG = IngredientRowAdapter.class.getSimpleName();
    private Activity context;


    public IngredientRowAdapter(Context context, int ingredientRowResourceId, List<Ingredient> values) {
        super(context, ingredientRowResourceId, values);
        this.context = (Activity) context;
        ingredientValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
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

        Ingredient ingredientValue = ingredientValues.get(position);
        viewHolder.ingredientText.setText(ingredientValue.getName(), TextView.BufferType.NORMAL);
        viewHolder.amountText.setText(ingredientValue.getAmount(), TextView.BufferType.NORMAL);
        if (!ingredientValue.getStruckOut()) {
            viewHolder.strokeView.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.strokeView.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView ingredientText;
        TextView amountText;
        View strokeView;
    }
}
