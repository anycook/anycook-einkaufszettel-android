package de.anycook.app.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import org.apache.commons.io.IOUtils;

import java.io.*;
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

            if(!imageDirectory.exists()) {
                imageDirectory.mkdirs();
            }

            if(!imageFile.exists()) {
                imageFile.createNewFile();
                OutputStream os = new FileOutputStream(imageFile);
                InputStream is = url.openStream();
                IOUtils.copy(is, os);

            }
            return BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "failed to load image", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null) imageView.setImageBitmap(bitmap);
    }
}
