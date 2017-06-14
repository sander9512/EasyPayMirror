package com.avans.easypay.ASyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by TheJollyBest at 6/7/2017.
 */

public class AssortmentLocationTask extends AsyncTask<String, Void, String> {

    private static final String TAG = AssortmentLocationTask.class.getSimpleName();
    private OnProductIdAvailable listener = null;

    public AssortmentLocationTask(OnProductIdAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        String response = "";
        int responseCode = -1;
        String URL = params[0];
        try {
            URL url = new URL(URL);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                return null;
            }

            //initialise HTTP connection
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");

            //request connection using given URL
            httpConn.connect();

            responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
                response = getStringFromInputStream(inputStream);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground | MalformedURLException " + e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground | IOException" + e.getLocalizedMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        //check if response is valid
        if (response == null || response.equals("")) {
            return;
        }

        JSONObject json;

        try {
            json = new JSONObject(response);
            Log.i(TAG, json.toString());

            JSONArray items = json.getJSONArray("items");
            Log.i(TAG, items.toString());

            ArrayList<JSONObject> jsonObjects = new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {
                jsonObjects.add(items.optJSONObject(i));
                Log.i(TAG, items.optJSONObject(i).toString());
            }

            ArrayList<Integer> productIds = new ArrayList<>();


            for (int i = 0; i < jsonObjects.size(); i++) {
                Log.i(TAG, Integer.toString(jsonObjects.get(i).optInt("ProductId")));
                productIds.add(jsonObjects.get(i).optInt("ProductId"));
            }

            //call back with productid's that was searched for
            listener.onProductIdAvailable(productIds);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    //call back interface
    public interface OnProductIdAvailable {
        void onProductIdAvailable(ArrayList<Integer> productIds);
    }
}
