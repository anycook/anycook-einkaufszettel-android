/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger
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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.anycook.einkaufszettel.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Wraps the application properties stored with SharedPreferences
 * @author Jan Graßegger<jan@anycook.de>
 */
public class Properties {
    private final SharedPreferences preferences;

    public Properties(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Set<String> getBlacklistedIngredients() {
        String ingredientString = preferences.getString("blacklisted_ingredients", null);
        return new HashSet<>(Arrays.asList(ingredientString.split("[, ]+")));
    }
}
