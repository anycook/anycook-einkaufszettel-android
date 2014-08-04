package de.anycook.app.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import de.anycook.app.R;

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