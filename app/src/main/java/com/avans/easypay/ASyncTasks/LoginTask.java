package com.avans.easypay.ASyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Customer;
import com.avans.easypay.LoginActivity;

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
import java.util.Date;

/**
 * Created by Felix on 10-5-2017.
 */

public class LoginTask extends AsyncTask<String, Void, String> {

    private static final String TAG = LoginTask.class.getSimpleName();
    private OnCustomerAvailable listener = null;

    public LoginTask(OnCustomerAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        String response = "";
        int responseCode = -1;
        String klantURL = params[0];
        try {
            URL url = new URL(klantURL);
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
            JSONArray items = json.getJSONArray("items");
            JSONObject customer = items.optJSONObject(0);

            if (customer != null) {
                int customerId = customer.optInt("KlantId");
                String username = customer.optString("Gebruikersnaam");
                String password = customer.optString("Wachtwoord");
                String email = customer.optString("Email");
                String firstname = customer.optString("Voornaam");
                String lastname = customer.optString("Achternaam");
                String bankAccountNumber = customer.optString("Bankrekeningnummer");
                float balanceInt = (float) customer.optInt("saldo");
                float b = balanceInt/100;

                String timeLog = customer.optString("TimeLog");

                Balance balance = new Balance(b, new Date());
                Customer c = new Customer(customerId, username, password,
                        email, firstname, lastname, bankAccountNumber, balance, timeLog);

                //call back with customer that was been searched for
                listener.onCustomerAvailable(c);
            } else {
                //return null if no customer was found
                listener.onCustomerAvailable(null);
            }
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
    public interface OnCustomerAvailable {
        void onCustomerAvailable(Customer customer);
    }
}
