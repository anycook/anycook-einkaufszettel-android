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

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import de.anycook.einkaufszettel.util.BackgroundBitmapDrawable;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class DownloadToolbarBackgroundTask extends DownloadImageTask {
    private final ActionBar actionBar;

    public DownloadToolbarBackgroundTask(ActionBar actionBar, Context context) {
        super(context);
        this.actionBar = actionBar;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            actionBar.setBackgroundDrawable(new BackgroundBitmapDrawable(context.getResources(), bitmap));
        }
    }
}
