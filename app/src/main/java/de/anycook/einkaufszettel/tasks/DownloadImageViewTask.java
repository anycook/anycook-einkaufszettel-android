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
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.store.RecipeStore;
import de.anycook.einkaufszettel.util.ImageHelper;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class DownloadImageViewTask extends DownloadImageTask {

    private final ImageView imageView;
    private final View buttonView;
    private final boolean round;

    public DownloadImageViewTask(ImageView imageView) {
        this(imageView, false);
    }

    public DownloadImageViewTask(ImageView imageView, boolean round) {
        this(imageView, null, null, round);
    }

    public DownloadImageViewTask(ImageView imageView, View buttonView, String recipeName) {
        this(imageView, buttonView, recipeName, false);
    }

    public DownloadImageViewTask(ImageView imageView, View buttonView, String recipeName,
                                 boolean round) {
        super(imageView.getContext(), recipeName);
        this.imageView = imageView;
        this.buttonView = buttonView;
        this.round = round;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(round ? ImageHelper.getRoundedCornerBitmap(bitmap) : bitmap);

            if (buttonView != null) {
                RecipeStore recipeStore = new RecipeStore(context);
                try {
                    recipeStore.open();

                    int buttonBackgroundColor = recipeStore.getVibrantColor(recipeName);

                    if (buttonBackgroundColor == -1) {
                        Palette palette = Palette.generate(bitmap);

                        final int anyGreenColor =
                                ContextCompat.getColor(imageView.getContext(), R.color.any_green);

                        buttonBackgroundColor = palette.getVibrantColor(anyGreenColor);
                        recipeStore.putVibrantColor(recipeName, buttonBackgroundColor);
                    }

                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setCornerRadius(56);
                    drawable.setColor(buttonBackgroundColor);
                    buttonView.setBackground(drawable);
                    buttonView.setVisibility(View.VISIBLE);
                } finally {
                    recipeStore.close();
                }
            }
        }
    }
}
