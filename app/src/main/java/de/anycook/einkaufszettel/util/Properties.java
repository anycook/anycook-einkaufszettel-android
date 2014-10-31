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
