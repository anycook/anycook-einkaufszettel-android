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

package de.anycook.einkaufszettel.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.tasks.LoadIngredientsTask;
import de.anycook.einkaufszettel.tasks.LoadRecipesTask;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class LoadingActivity extends Activity {
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_screen);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMax(2);

        LoadIngredientsTask loadIngredientsTask = new LoadIngredientsTask(this, new IncrementCallback());
        loadIngredientsTask.execute();
        LoadRecipesTask loadRecipesTask = new LoadRecipesTask(this, new IncrementCallback());
        loadRecipesTask.execute();
    }

    @Override
    public void onBackPressed() {

    }

    private class IncrementCallback implements Callback {
        @Override
        public void call(AsyncTask.Status status) {
            progressBar.setProgress(progressBar.getProgress() + 1);
            if (progressBar.getProgress() == progressBar.getMax()) finish();
        }
    }

    public static interface Callback {
        public void call(AsyncTask.Status status);
    }
}
