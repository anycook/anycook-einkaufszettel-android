package de.anycook.app.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import de.anycook.app.R;
import de.anycook.app.adapter.RecipeRowCursorAdapter;
import de.anycook.app.store.GroceryItemStore;
import de.anycook.app.tasks.UploadTask;

import java.io.File;

/**
 * upload photos of recipes
 *
 * @author Claudia Sichting <claudia.sichting@uni-weimar.de>
 */
public class RecipePhotoActivity extends ListActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private File imageFile;
    private GroceryItemStore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        setTitle("Wähle ein Rezept aus:");
        // create Intent to take a picture and return control to the calling application
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        this.imageFile = getNewImageFile();
        if(imageFile == null) {
            Log.e(getClass().getSimpleName(), "Failed to create image file");
            return;
        }

        this.db = new GroceryItemStore(this);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));


        // start the image capture Intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setListAdapter(new RecipeRowCursorAdapter(this, db.getAllRecipesCursor()));
        }
        if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //String item = ((TextView) v.findViewById(R.id.recipe_row_textview_recipe_name)).getText().toString();
        UploadTask uploadTask = new UploadTask(this);
        uploadTask.execute(imageFile);
    }

    private File getNewImageFile() {
        File mediaDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Einkaufszettel");

        if (! mediaDir.exists()){
            if (! mediaDir.mkdirs()){
                Log.d(getClass().getSimpleName(), "failed to create directory");
                return null;
            }
        }

        return new File(mediaDir, String.format("GerichtetesAllerlei_%d.png", System.currentTimeMillis()));
    }
}
