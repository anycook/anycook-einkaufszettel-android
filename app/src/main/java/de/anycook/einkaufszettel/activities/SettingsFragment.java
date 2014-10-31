package de.anycook.einkaufszettel.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import de.anycook.einkaufszettel.R;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
