package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.controller.RecipeResponse;
import de.anycook.app.tasks.DownloadImageTask;

import java.util.List;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class RecipeRowAdapter extends ArrayAdapter<RecipeResponse> {

    private Activity context;
    private List<RecipeResponse> recipeValues;

    public RecipeRowAdapter(Context context, int recipeRowResourceId,
                            List<RecipeResponse> values) {
        super(context, recipeRowResourceId, values);
        this.context = (Activity) context;
        recipeValues = values;
    }

    /**
     * Improved getView thanks to ViewHolder (findViewById is and expensive function)
     *
     * @param position    where is the view
     * @param convertView what is visible
     * @param parent      which is the parent view
     * @return the view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.recipe_row, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) rowView.findViewById(R.id.recipe_row_textview_recipe_name);
            viewHolder.textViewDescription = (TextView) rowView.findViewById(R.id.recipe_row_textview_recipe_description);
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.recipe_row_imageview);
            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.textViewName.setText(recipeValues.get(position).getName());
        viewHolder.textViewDescription.setText(recipeValues.get(position).getDescription());

        new DownloadImageTask(viewHolder.imageView).execute(recipeValues.get(position).getImage());
        //viewHolder.imageView.setImageResource(R.drawable.ic_launcher);

        return rowView;

    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        ImageView imageView;
    }
}
