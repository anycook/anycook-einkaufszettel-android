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

import android.graphics.Bitmap;
import android.widget.ImageView;
import de.anycook.einkaufszettel.util.ImageHelper;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class DownloadImageViewTask extends DownloadImageTask {
    private final ImageView imageView;
    private final boolean round;

    public DownloadImageViewTask(ImageView imageView) {
        this(imageView, false);
    }

    public DownloadImageViewTask(ImageView imageView, boolean round) {
        super(imageView.getContext());
        this.imageView = imageView;
        this.round = round;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(round ? ImageHelper.getRoundedCornerBitmap(bitmap) : bitmap);
    }
}
