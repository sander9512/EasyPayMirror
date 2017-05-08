package com.avans.easypay.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bart on 3-5-2017.
 */

public class DBConnectLite extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getSimpleName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "easypay.db";

    private Context context;

    //Tables and columns
    //Balance
    private final String DB_TABLE_BALANCE_NAME = "Balance";
    private final String COLUMN_BALANCE_AMOUNT = "Amount";
    private final String COLUMN_BALANCE_TIMELOG = "TimeLog";

    public DBConnectLite(Context context, String name, SQLiteDatabase.CursorFactory factory){
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create balance table
        Log.i(TAG, "Creating balance table");
        String CREATE_BALANCE_TABLE = "CREATE TABLE "+ DB_TABLE_BALANCE_NAME +
                "(" +
                COLUMN_BALANCE_AMOUNT + " REAL," +
                COLUMN_BALANCE_TIMELOG + " TEXT PRIMARY KEY" +
                ")";
        db.execSQL(CREATE_BALANCE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_BALANCE_NAME);
    }

    //Feedback table getters
    public String getDB_TABLE_BALANCE__NAME() {
        return DB_TABLE_BALANCE_NAME;
    }

    public String getCOLUMN_BALANCE_AMOUNT() {
        return COLUMN_BALANCE_AMOUNT;
    }

    public String getCOLUMN_BALANCE_TIMELOG() {
        return COLUMN_BALANCE_TIMELOG;
    }
}
