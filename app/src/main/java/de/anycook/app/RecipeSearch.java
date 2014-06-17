package de.anycook.app;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cipo7741 on 13.06.14.
 */
public class RecipeSearch extends Activity {
    static String url = "https://api.anycook.de/autocomplete?query=";
    TextView tv1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.tv1 = (TextView) findViewById(R.id.textview_main_display);
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        final String what = query;
        new Thread(new Runnable() {
            public void run() {
                if (what != null) {
                    try {
                        final String ingredients = ProcessResponse(SearchRequest(what));
                        tv1.post(new Runnable() {
                            @Override
                            public void run() {
                                tv1.setText(ingredients);
                            }
                        });
                    } catch (Exception e) {
                        Log.v("Exception anycook auto-complete", e.toString() + " " + e.getMessage());
                    }
                }
            }
        }).start();

        //tv1.setText(ingredients);
    }

    private String SearchRequest(String searchString) throws IOException {
        String newFeed = url + searchString;
        StringBuilder response = new StringBuilder();
        Log.v("auto-complete", "anycook url: " + newFeed);
        URL url = new URL(newFeed);

        HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(httpconn.getInputStream()), 8192);
            String strLine;
            while ((strLine = input.readLine()) != null) {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }

    private String ProcessResponse(String resp) throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        Log.v("auto-complete", "anycook result: " + resp);

        JSONObject mResponseObject = new JSONObject(resp);
        JSONArray mResponseArray = mResponseObject.getJSONArray("recipes");

        Log.v("auto-complete", "number of results: " + mResponseArray.length());
        for (int i = 0; i < mResponseArray.length(); i++) {
            Log.v("result [", i + "] " + mResponseArray.get(i).toString());
            String recipe = mResponseArray.get(i).toString();

            sb.append(recipe);
            sb.append("\n");

        }
        return sb.toString();
    }

}
