package de.anycook.app.controller;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cipo7741 on 04.07.14.
 */
public class IngredientAutoCompleter implements Runnable {
    private static final String TAG = IngredientAutoCompleter.class.getSimpleName();
    private AutoCompleteTextView autoCompleteTextView;

    public IngredientAutoCompleter(AutoCompleteTextView autoCompleteTextView) {
        this.autoCompleteTextView = autoCompleteTextView;
    }

    @Override
    public void run() {
        try {
            final ArrayList<SuggestionResponse> suggestions = searchRequest();
            autoCompleteTextView.post(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) autoCompleteTextView.getAdapter();
                    for (SuggestionResponse suggestion : suggestions) {
                        arrayAdapter.add(suggestion.getName());
                    }
                    autoCompleteTextView.setAdapter(arrayAdapter);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, String.format("run: %s", e.toString()));
        }
    }

    private ArrayList<SuggestionResponse> searchRequest() throws IOException {
        String url = "https://api.anycook.de/ingredient";
        URL autocompleteUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) autocompleteUrl.openConnection();
        if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(httpURLConnection.getResponseMessage());
        }
        Log.v(TAG, autocompleteUrl.toString());
        Reader reader = new InputStreamReader(httpURLConnection.getInputStream());
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<SuggestionResponse>>() {
        }.getType();
        return gson.fromJson(reader, collectionType);
    }

    private static class SuggestionResponse {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
