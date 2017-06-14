package com.avans.easypaykassa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;

/**
 * Created by TB on 5/22/2017.
 */

public class exception_handler implements Thread.UncaughtExceptionHandler {
    private Context context;

    exception_handler(Context context){
        this.context = context;
    }
    @Override
    public void uncaughtException(Thread t, final Throwable e) {
        new Thread() {
            @Override
            public void run() {
                Log.d("Crashandler: \n", "" + android.os.Build.VERSION.SDK + "/" + android.os.Build.DEVICE + "/"
                        + android.os.Build.MODEL + "/" + android.os.Build.PRODUCT + "/" + "\n error: \n" + e);
                System.getProperty("os.version"); // OS version

                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
                if(activeNetwork != null) {
                    Log.d("We have a","connection");
                    EasyPayAPIPUTConnector put = new EasyPayAPIPUTConnector();
                    String url = "https://dashboard.heroku.com/api/error/add_error/" + android.os.Build.VERSION.SDK + "/" + android.os.Build.DEVICE + "/"
                            + android.os.Build.MODEL + "/" + android.os.Build.PRODUCT + "/" + e;
                    put.execute(url);
                } else {
                    //CREATING AND WRITING TO FILE
                    String FILENAME = "error_file";
                    String string = android.os.Build.VERSION.SDK + "/" + android.os.Build.DEVICE + "/"
                            + android.os.Build.MODEL + "/" + android.os.Build.PRODUCT + "/" + e;
                    try {
                        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                        fos.write(string.getBytes());
                        fos.close();
                    } catch(Exception e){
                        Log.d("Sh*t hit the fos",""+e);
                    }
                }
                Looper.prepare();
                Toast.makeText(context, "We zijn een fout tegengekomen, excuses voor het ongemak.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(4000); //Let the toast display before the app "crashes"
        } catch (InterruptedException z) {
            //ignored
        }
        System.exit(1);
    }
}
