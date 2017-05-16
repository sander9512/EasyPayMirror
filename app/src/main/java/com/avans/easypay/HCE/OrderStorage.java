package com.avans.easypay.HCE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Felix on 16-5-2017.
 */

public class OrderStorage {

    private static final String PREF_ACCOUNT_NUMBER = "storage_number";
    private static final String DEFAULT_ACCOUNT_NUMBER = "0";
    private static final String TAG = "OrderStorage";
    private static String sOrder = null;
    private static final Object sOrderLock = new Object();

    public static void SetAccount(Context c, String s) {
        synchronized (sOrderLock) {
            Log.i(TAG, "Setting account number: " + s);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
            prefs.edit().putString(PREF_ACCOUNT_NUMBER, s).commit();
            sOrder = s;
        }
    }

    public static String GetAccount(Context c) {
        synchronized (sOrderLock) {
            if (sOrder == null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                String order = prefs.getString(PREF_ACCOUNT_NUMBER, DEFAULT_ACCOUNT_NUMBER);
                sOrder = order;
            }
            return sOrder;
        }
    }
}
