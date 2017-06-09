package com.avans.easypay;

import android.os.AsyncTask;
import android.util.Log;

import com.avans.easypay.DomainModel.*;

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
import java.util.Date;

public class EasyPayAPIGETOrderConnector extends AsyncTask<String, Void, String> {

    //tag to use when information is logged, shows class name
    private static final String TAG = EasyPayAPIGETOrderConnector.class.getSimpleName();
    //call back interface
    private OnOrdersAvailable listener = null;

    public EasyPayAPIGETOrderConnector(OnOrdersAvailable listener) {
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

            ArrayList<Integer> products = new ArrayList<>();

            //get first product
            JSONObject order = items.getJSONObject(0);
            int orderID = order.optInt("BestellingId");
            int customerID = order.optInt("KlantId");
            String date = order.optString("Datum");
            String location = order.optInt("locatieId")+"";
            int orderNumber = order.optInt("bestellingNummer");
            String status = order.optString("Status");

            for (int i = 0; i < items.length(); i++) {
                JSONObject orderForProducts = items.getJSONObject(i);
                products.add(orderForProducts.optInt("ProductId"));
            }

            Order o = new Order(orderID, customerID, new Date(), products,  location, orderNumber, status);

            listener.onOrdersAvailable(o);

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
    public interface OnOrdersAvailable {
        void onOrdersAvailable(Order order);
    }
}