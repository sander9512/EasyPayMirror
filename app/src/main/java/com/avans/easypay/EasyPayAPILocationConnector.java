package com.avans.easypay;

import android.os.AsyncTask;
import android.util.Log;

import com.avans.easypay.DomainModel.Location;

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

public class EasyPayAPILocationConnector extends AsyncTask<String, Void, String> {

    private ArrayList<Location> locations = new ArrayList<>();

    //tag to use when information is logged, shows class name
    private static final String TAG = EasyPayAPILocationConnector.class.getSimpleName();
    //call back interface
    private OnLocationAvailable listener = null;

    public EasyPayAPILocationConnector(OnLocationAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        //create string of bytes
        InputStream inputStream = null;
        //result we will return
        String response = "";
        int responseCode = -1;
        //create URL object with given URL as parameter in .execute()
        String productUrl = params[0];
        try {
            URL url = new URL(productUrl);
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

            //check if succeeded
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
        if (response == null || response == "") {
            Log.e(TAG, "onPostExecute received empty response" + response);
            return;
        }

        //JSON parsing
        JSONObject json;

        try {
            //entire JSON file in JSONObject
            json = new JSONObject(response);

            //get all items
            JSONArray items = json.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                //get first product
                JSONObject location = items.getJSONObject(i);
                String locationName = location.optString("Naam");
                int locationID = location.optInt("LocatieID");

                Location l = new Location(locationID, locationName);
                locations.add(l);

            }

            listener.onLocationAvailable(locations);

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
    public interface OnLocationAvailable {
        void onLocationAvailable(ArrayList<Location> locations);
    }
}
