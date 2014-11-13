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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.tasks.LoadIngredientsTask;
import de.anycook.einkaufszettel.tasks.LoadRecipesTask;
import de.anycook.einkaufszettel.util.ConnectionStatus;
import de.anycook.einkaufszettel.util.Properties;

/**
 * First activity on application startup. Loads recipe and ingredient database data
 * @author Jan Graßegger<jan@anycook.de>
 */
public class StartupActivity extends Activity {
    private Logger logger = LoggerManager.getLogger();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        long lastUpdate = sharedPrefs.getLong("last_update", 0);

        Properties properties = new Properties(this);
        int updateInterval = properties.getUpdateInterval();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate > updateInterval * 1000) {
            if (ConnectionStatus.isConnected(this)) {
                updateData(sharedPrefs);
                return;
            }
            logger.i("no active internet connection found");
        }
        startMainActivity();
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateData(SharedPreferences sharedPrefs) {
        setContentView(R.layout.load_screen);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMax(2);

        LoadIngredientsTask loadIngredientsTask = new LoadIngredientsTask(this, new IncrementCallback());
        loadIngredientsTask.execute();
        LoadRecipesTask loadRecipesTask = new LoadRecipesTask(this, new IncrementCallback());
        loadRecipesTask.execute();

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong("last_update", System.currentTimeMillis());
        editor.apply();
    }

    public interface Callback {
        void call(AsyncTask.Status status);
    }

    private class IncrementCallback implements Callback {
        @Override
        public void call(AsyncTask.Status status) {
            progressBar.incrementProgressBy(1);
            if (progressBar.getProgress() == progressBar.getMax()) {
                startMainActivity();
            }
        }
    }
}


