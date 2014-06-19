package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
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

    private Activity context;
    private final ArrayList<IngredientRow> ingredientValues;
    ViewHolder viewHolder;

    public IngredientRowAdapter(Context context, ArrayList<IngredientRow> values) {
        super(context, R.layout.ingredient_list, R.id.ingredient_list_listview, values);
        this.context = (Activity) context;
        this.ingredientValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.recipe_row, null);

            this.viewHolder.ingredientText = (TextView) convertView
                    .findViewById(R.id.ingredient_list_textview_ingredient);
            this.viewHolder.amountText = (TextView) convertView
                    .findViewById(R.id.ingredient_list_textview_amount);

            this.viewHolder = new ViewHolder();
        } else {
            this.viewHolder = (ViewHolder) convertView.getTag();
        }

        this.viewHolder.ingredientText.setText(ingredientValues.get(position).getIngredient());
        this.viewHolder.amountText.setText(ingredientValues.get(position).getAmount());
        this.viewHolder.strokeView.setVisibility(View.VISIBLE);
        this.viewHolder.strokeView.setClickable(false);

        return convertView;
    }

    static class ViewHolder {
        TextView ingredientText;
        TextView amountText;
        View strokeView;
    }
}
