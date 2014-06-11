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
 *
 * Created by cipo7741 on 07.06.14.
 */
public class ListActivityAdapter extends ArrayAdapter<RowItem> {

    private final Context context;
    private final RowItem[] values;

    public ListActivityAdapter(Context context, RowItem[] values) {
        super(context, R.layout.rowlayout, R.id.textview_rowlayout_ingredient, values);
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
            v = inflater.inflate(R.layout.rowlayout, null);
            holder = new ViewHolder();
            holder.ingredientText = (TextView) v.findViewById(R.id.textview_rowlayout_ingredient);
            holder.ingredientText.setText(values[position].getIngredient());
            holder.amountText = (TextView) v.findViewById(R.id.textview_rowlayout_amount);
            holder.amountText.setText(values[position].getAmount());
            holder.strokeView = v.findViewById(R.id.view_rowlayout_stroke);
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
