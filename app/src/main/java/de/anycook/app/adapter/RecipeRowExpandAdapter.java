package de.anycook.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
 * json response class
 * <p/>
 * Created by cipo7741 on 01.07.14.
 */
public class RecipeRowExpandAdapter extends BaseExpandableListAdapter {

    private static String TAG = RecipeRowExpandAdapter.class.getSimpleName();
    Activity context;
    ArrayList<RecipeResponse> recipeList;
    RecipeNameViewHolder recipeNameViewHolder;
    RecipeDetailsViewHolder recipeDetailsViewHolder;
    private Bitmap bm;

    public RecipeRowExpandAdapter(Context context, ArrayList<RecipeResponse> recipeList) {
        this.context = (Activity) context;
        this.recipeList = recipeList;
    }

    @Override
    public int getGroupCount() {
        return this.recipeList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (getGroupCount() < groupPosition) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String getGroup(int groupPosition) {
        return this.recipeList.get(groupPosition).getName();
    }

    @Override
    public RecipeResponse getChild(int groupPosition, int childPosition) {
        return this.recipeList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupID) {
        return groupID;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater;
            inflater = context.getLayoutInflater();
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent);
            this.recipeNameViewHolder.recipeNameText = (TextView) convertView.findViewById(android.R.id.text1);
        } else {
            this.recipeNameViewHolder = (RecipeNameViewHolder) convertView.getTag();
        }
        this.recipeNameViewHolder.recipeNameText.setText(recipeList.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater;
            inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.expandable_recipe_list, parent);
            this.recipeDetailsViewHolder.recipeDescriptionText = (TextView) convertView.findViewById(R.id.recipe_row_textview_recipe_description);
            this.recipeDetailsViewHolder.recipeImage = (ImageView) convertView.findViewById(R.id.recipe_row_imageview);
        } else {
            this.recipeDetailsViewHolder = (RecipeDetailsViewHolder) convertView.getTag();
        }
        this.recipeDetailsViewHolder.recipeDescriptionText.setText(recipeList.get(groupPosition).getDescription());
        String imageUrl = recipeList.get(groupPosition).getImage();
        this.recipeDetailsViewHolder.recipeImage.setImageBitmap(getImageBitmap(imageUrl));
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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class RecipeNameViewHolder {
        TextView recipeNameText;
    }

    static class RecipeDetailsViewHolder {
        TextView recipeDescriptionText;
        ImageView recipeImage;
    }

}
