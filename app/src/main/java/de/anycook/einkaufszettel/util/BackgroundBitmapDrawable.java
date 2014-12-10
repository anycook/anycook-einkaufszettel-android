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

package de.anycook.einkaufszettel.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class BackgroundBitmapDrawable extends BitmapDrawable {
    private Matrix mMatrix = new Matrix();
    private int moldHeight;
    boolean simpleMapping = false;

    public BackgroundBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        if (bounds.height() > moldHeight) {
            moldHeight = bounds.height();
            Bitmap b = getBitmap();
            RectF src = new RectF(0, 0, b.getWidth(), b.getHeight());
            RectF dst;

            if (simpleMapping) {
                dst = new RectF(bounds);
                mMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            } else {
                // Full Screen Image -> Always scale and center-crop in order to fill the screen
                float dwidth = src.width();
                float dheight = src.height();

                float vwidth = bounds.width();
                float vheight = bounds.height();

                float scale;
                float dx = 0, dy = 0;

                if (dwidth * vheight > vwidth * dheight) {
                    scale = (float) vheight / (float) dheight;
                    dx = (vwidth - dwidth * scale) * 0.5f;
                } else {
                    scale = (float) vwidth / (float) dwidth;
                    dy = (vheight - dheight * scale) * 0.5f;
                }

                mMatrix.setScale(scale, scale);
                mMatrix.postTranslate(dx, dy);

            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(0xaa00ff00);
        canvas.drawBitmap(getBitmap(), mMatrix, null);
    }
}
