package de.anycook.app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import de.anycook.app.R;
import de.anycook.app.store.SQLiteDB;
import de.anycook.app.tasks.DownloadImageTask;

/**
 * Custom ArrayAdapter to fill EditMode with amount and ingredients
 *
 * @author Jan Grassegger <jan@anycook.de>
 */
public class RecipeRowCursorAdapter extends ResourceCursorAdapter {

    public RecipeRowCursorAdapter(Context context) {
        this(context, null);
    }
    public RecipeRowCursorAdapter(Context context, Cursor cursor) {
        super(context, R.layout.recipe_row, cursor, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.recipe_row_textview_recipe_name);
        name.setText(cursor.getString(SQLiteDB.TableFields.RECIPE_NAME));

        TextView descriptionView =  (TextView) view.findViewById(R.id.recipe_row_textview_recipe_description);
        descriptionView.setText(cursor.getString(SQLiteDB.TableFields.RECIPE_DESCRIPTION));

        ImageView imageView = (ImageView) view.findViewById(R.id.recipe_row_imageview);
        new DownloadImageTask(imageView).execute(cursor.getString(SQLiteDB.TableFields.RECIPE_IMAGE));
    }
}
