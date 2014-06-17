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

import java.util.ArrayList;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class RecipeRowAdapter extends ArrayAdapter<RecipeRow> {

    private Activity myContext;
    private ArrayList<RecipeRow> recipeValues;

    public RecipeRowAdapter(Context context, int recipeRowResourceId,
                            ArrayList<RecipeRow> objects) {
        super(context, recipeRowResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        recipeValues = objects;
    }

    static class ViewHolder {
        TextView recipeNameText;
        TextView recipeDescriptionText;
        ImageView recipeImage;
    }

    /**
     * Improved getView thanks to ViewHolder (findViewById is and expensive function)
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = myContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.recipe_row, null);

            viewHolder = new ViewHolder();
            viewHolder.recipeImage = (ImageView) convertView
                    .findViewById(R.id.recipe_row_imageview);
            viewHolder.recipeNameText = (TextView) convertView
                    .findViewById(R.id.recipe_row_textview_recipe_name);
            viewHolder.recipeDescriptionText = (TextView) convertView
                    .findViewById(R.id.recipe_row_textview_recipe_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (recipeValues.get(position).getRecipeImage() == null) {
            viewHolder.recipeImage
                    .setImageResource(R.drawable.ic_launcher);
        }

        viewHolder.recipeNameText.setText(recipeValues.get(position).getRecipeName());
        viewHolder.recipeDescriptionText.setText(recipeValues.get(position).getRecipeDescription());

        return convertView;
    }
}