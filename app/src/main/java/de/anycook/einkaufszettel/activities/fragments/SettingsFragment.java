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

package de.anycook.einkaufszettel.activities.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import de.anycook.einkaufszettel.R;
import de.anycook.einkaufszettel.store.SQLiteDB;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        findPreference("delete_cache").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        SQLiteDB db = new SQLiteDB(getActivity());
                        try {
                            db.deleteDatabase();
                            final SharedPreferences sharedPreferences =
                                    getActivity().getSharedPreferences("update_data",
                                                                       MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("last-modified-recipes");
                            editor.remove("last-update");
                            editor.apply();
                            Toast.makeText(getActivity(), "Lokale Daten gelöscht",
                                           Toast.LENGTH_SHORT).show();
                        } finally {
                            db.close();
                        }
                        return true;
                    }
                });
    }
}
