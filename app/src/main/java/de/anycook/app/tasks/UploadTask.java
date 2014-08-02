package de.anycook.app.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import de.anycook.app.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class UploadTask extends AsyncTask<Uri, Void, String> {
    private final static String url;

    static {
        url = "https://api.anycook.de/upload/image/recipe";
    }

    private final Context context;
    private AlertDialog alertDialog;

    public UploadTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Bild wird hochgeladen!");
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected String doInBackground(Uri... uris) {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Einkaufszettel");
        try {
            File imageFile = new File(uris[0].getPath());
            FileBody fileBody = new FileBody(imageFile);
            MultipartEntity entity = new MultipartEntity();
                entity.addPart("file", fileBody);

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            if (response == null) {
                Log.w(getClass().getSimpleName(), "response is null");
                return null;
            }
            return response.getFirstHeader("Location").getValue();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
            return null;
        } finally {
            httpClient.close();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        this.alertDialog.dismiss();
        Log.v(getClass().getSimpleName(), s);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(String.format(context.getString(R.string.upload_message), s));
        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
