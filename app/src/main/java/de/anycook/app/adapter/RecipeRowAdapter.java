package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.anycook.app.R;

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
public class RecipeRowAdapter extends ArrayAdapter<RecipeRow> {

    private static final String TAG = RecipeRowAdapter.class.getSimpleName();
    private Activity context;
    private ArrayList<RecipeRow> recipeValues;
    ViewHolder viewHolder;
    Bitmap bm;

    public RecipeRowAdapter(Context context, int recipeRowResourceId,
                            ArrayList<RecipeRow> values) {
        super(context, recipeRowResourceId, values);
        this.context = (Activity) context;
        recipeValues = values;
    }

    /**
     * Improved getView thanks to ViewHolder (findViewById is and expensive function)
     *
     * @param position where is the view
     * @param convertView what is visible
     * @param parent which is the parent view
     * @return the view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.recipe_row, parent);

            this.viewHolder = new ViewHolder();
            this.viewHolder.recipeImage = (ImageView) convertView
                    .findViewById(R.id.recipe_row_imageview);
            this.viewHolder.recipeNameText = (TextView) convertView
                    .findViewById(R.id.recipe_row_textview_recipe_name);
            this.viewHolder.recipeDescriptionText = (TextView) convertView
                    .findViewById(R.id.recipe_row_textview_recipe_description);
            convertView.setTag(this.viewHolder);
        } else {
            this.viewHolder = (ViewHolder) convertView.getTag();
        }

        if (recipeValues.get(position).getRecipeImageUrl() == null) {
            this.viewHolder.recipeImage
                    .setImageResource(R.drawable.ic_launcher);
            Log.w(TAG + " getView: ", "imageUrl: " + recipeValues.get(position).getRecipeImageUrl() + " = null");
        } else {
            final int pos = position;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String imageUrl = Uri.decode(recipeValues.get(pos).getRecipeImageUrl());
                        viewHolder.recipeImage.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.recipeImage.setImageBitmap(getImageBitmap(imageUrl));
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "getView: " + e.toString() + " " + e.getMessage(), e.getCause());
                    }
                }
            }).start();
        }

        this.viewHolder.recipeNameText.setText(recipeValues.get(position).getRecipeName());
        this.viewHolder.recipeDescriptionText.setText(recipeValues.get(position).getRecipeDescription());

        return convertView;
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
        TextView recipeNameText;
        TextView recipeDescriptionText;
        ImageView recipeImage;
    }
}
