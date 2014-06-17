package de.anycook.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.anycook.app.R;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class IngredientRowAdapter extends ArrayAdapter<IngredientRow> {

    private final Context context;
    private final IngredientRow[] values;

    public IngredientRowAdapter(Context context, IngredientRow[] values) {
        super(context, R.layout.main, R.id.ingredient_list_listview, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.main, null);
            holder = new ViewHolder();
            holder.ingredientText = (TextView) v.findViewById(R.id.ingredient_list_textview_ingredient);
            holder.ingredientText.setText(values[position].getIngredient());
            holder.amountText = (TextView) v.findViewById(R.id.ingredient_list_textview_amount);
            holder.amountText.setText(values[position].getAmount());
            holder.strokeView = v.findViewById(R.id.ingredient_list_view_stroke);
            holder.strokeView.setVisibility(View.INVISIBLE);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
            v.setTag(holder);
        }
        return v;
    }

    static class ViewHolder {
        TextView ingredientText;
        TextView amountText;
        View strokeView;
    }
}
