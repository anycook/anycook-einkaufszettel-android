package de.anycook.app.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.google.common.io.ByteStreams;
import com.noveogroup.android.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private final ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);

            //Check if image is already cached
            String imagePath = url.getPath();
            File cacheDir = imageView.getContext().getCacheDir();
            File imageFile = new File(cacheDir, imagePath);
            File imageDirectory = imageFile.getParentFile();

            if ((imageDirectory.exists() || imageDirectory.mkdirs()) && !imageFile.exists()) {
                imageFile.createNewFile();
                OutputStream os = new FileOutputStream(imageFile);
                ByteStreams.copy(url.openStream(), os);
            }
            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (IOException e) {
            Log.e("failed to load image", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) { imageView.setImageBitmap(bitmap); }
    }
}
