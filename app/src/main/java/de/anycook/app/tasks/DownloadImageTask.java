package de.anycook.app.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
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
            Log.d(getClass().getSimpleName(), "Trying to load image from "+urls[0]);
            URL url = new URL(urls[0]);
            InputStream is = url.openStream();
            return BitmapFactory.decodeStream(is);
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
