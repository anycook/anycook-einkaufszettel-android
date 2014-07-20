package de.anycook.app.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.app.R;

/**
 * adapter for drawer icons with menu name
 * @author Jan Grassegger <jan@anycook.de>
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class DrawerRowAdapter extends ArrayAdapter<String> {
    private final TypedArray icons;


    public DrawerRowAdapter(Context context, String[] resource) {
        super(context, R.layout.drawer_list, resource);
        icons = context.getResources().obtainTypedArray(R.array.menu_icons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_row, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.drawer_list_item_textview);
            viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.drawer_list_item_imageview);
            convertView.setTag(viewHolder);
        }
        String item = getItem(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textViewName.setText(item);
        viewHolder.imageViewIcon.setImageDrawable(icons.getDrawable(position));
        return convertView;
    }

    static class ViewHolder {
        TextView textViewName;
        ImageView imageViewIcon;
    }
}
