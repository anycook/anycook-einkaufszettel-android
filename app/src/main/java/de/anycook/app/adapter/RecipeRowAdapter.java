package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.controller.RecipeResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 * <p/>
 * Created by cipo7741 on 07.06.14.
 */
public class RecipeRowAdapter extends ArrayAdapter<RecipeResponse> {

    private static final String TAG = RecipeRowAdapter.class.getSimpleName();
    private Bitmap bm;
    private Activity context;
    private ArrayList<RecipeResponse> recipeValues;

    public RecipeRowAdapter(Context context, int recipeRowResourceId,
                            ArrayList<RecipeResponse> values) {
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

        viewHolder.imageView.setImageResource(R.drawable.ic_launcher);

        return rowView;

    }

    private Bitmap getImageBitmap(String url) {
        final String tmpUrl = url;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL aURL = new URL(tmpUrl);
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG + " getImageBitmap", "Error getting bitmap", e);
                }
            }
        }).start();
        return bm;
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        ImageView imageView;
    }
}
