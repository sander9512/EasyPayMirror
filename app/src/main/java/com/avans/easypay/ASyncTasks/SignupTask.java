package com.avans.easypay.ASyncTasks;

import android.icu.util.Output;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Felix on 10-5-2017.
 */

public class SignupTask extends AsyncTask<String, Void, String> {

    private static final String TAG = SignupTask.class.getSimpleName();
    private OnCustomerCreated listener = null;

    public SignupTask(OnCustomerCreated listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String signupURL = params[0];

        try {
            URL url = new URL(signupURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.connect();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground | MalformedURLException " + e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground | IOException" + e.getLocalizedMessage());
        }
        return "Account aangemaakt";
    }

    @Override
    protected void onPostExecute(String response) {

        //call back with customer that was been searched for
        listener.onCustomerCreated(response );
    }

    //call back interface
    public interface OnCustomerCreated {
        void onCustomerCreated(String succesMsg);
    }
}
