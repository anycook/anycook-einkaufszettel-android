package de.anycook.app.activities;

import android.app.Activity;
import android.os.Bundle;
import de.anycook.app.R;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

}

