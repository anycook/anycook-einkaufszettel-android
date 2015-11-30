/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger, Claudia Sichting
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see [http://www.gnu.org/licenses/].
 */

package de.anycook.einkaufszettel.tasks;

import com.google.common.io.ByteStreams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

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
public abstract class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    protected final Context context;
    protected final String recipeName;

    public DownloadImageTask(Context context, String recipeName) {
        this.context = context;
        this.recipeName = recipeName;
    }


    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);

            //Check if image is already cached
            String imagePath = url.getPath();
            File cacheDir = context.getCacheDir();
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
    protected abstract void onPostExecute(Bitmap bitmap);
}
