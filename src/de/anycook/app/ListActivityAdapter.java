package de.anycook.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.anycook.app.adapter.RowItem;

/**
 * Created by cipo7741 on 07.06.14.
 */
public class ListActivityAdapter extends ArrayAdapter<RowItem> {

    private final Context context;
    private final RowItem[] values;



    public ListActivityAdapter(Context context, RowItem[] values) {
        super(context, R.layout.rowlayout, R.id.ingredient, values);
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

            holder.ingredientText = (TextView) v.findViewById(R.id.ingredient);
            holder.ingredientText.setText(values[position].getIngredient());
            holder.amountText = (TextView) v.findViewById(R.id.amount);
            holder.amountText.setText(values[position].getAmount());
            holder.strokeView = v.findViewById(R.id.stroke);
            holder.strokeView.setVisibility(View.INVISIBLE);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
            v.setTag(holder);
        }
        //v.setOnClickListener(new OnItemClickListener(position));


        return v;
    }

    static class ViewHolder {
        TextView ingredientText;
        TextView amountText;
        View strokeView;
    }
}
