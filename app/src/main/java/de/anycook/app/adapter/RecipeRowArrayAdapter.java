package de.anycook.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.model.RecipeResponse;
import de.anycook.app.tasks.DownloadImageTask;

import java.util.ArrayList;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class RecipeRowArrayAdapter extends ArrayAdapter<RecipeResponse> {

    public RecipeRowArrayAdapter(Context context) {
        super(context, R.layout.recipe_row, new ArrayList<RecipeResponse>());
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.recipe_row, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.recipe_row_textview_recipe_name);
            viewHolder.textViewDescription =
                    (TextView) convertView.findViewById(R.id.recipe_row_textview_recipe_description);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.recipe_row_imageview);
            convertView.setTag(viewHolder);
        }

        RecipeResponse recipeResponse = getItem(position);

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textViewName.setText(recipeResponse.getName());
        viewHolder.textViewDescription.setText(recipeResponse.getDescription());

        new DownloadImageTask(viewHolder.imageView).execute(recipeResponse.getImage());

        return convertView;

    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        ImageView imageView;
    }
}
