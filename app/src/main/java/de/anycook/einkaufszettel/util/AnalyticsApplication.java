/*
 * This file is part of anycook Einkaufszettel
 *  Copyright (C) 2016 Jan Graßegger, Claudia Sichting
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see [http://www.gnu.org/licenses/].
 */

package de.anycook.einkaufszettel.util;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.anycook.einkaufszettel.R;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class AnalyticsApplication extends Application {

    private Tracker tracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    public synchronized Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            final Properties properties = new Properties(getApplicationContext());
            analytics.setAppOptOut(!properties.getAnalyticsEnabled());
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            tracker = analytics.newTracker(R.xml.global_tracker);

            SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            userPrefs.registerOnSharedPreferenceChangeListener(
                    new SharedPreferences.OnSharedPreferenceChangeListener() {

                        @Override
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                              String key) {
                            if (key.equals("enable_analytics")) {
                                GoogleAnalytics.getInstance(getApplicationContext())
                                        .setAppOptOut(!properties.getAnalyticsEnabled());
                            }
                        }
                    });
        }
        return tracker;
    }
}
