package com.avans.easypay.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.avans.easypay.DomainModel.Balance;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bart on 3-5-2017.
 */

public class SQLiteBalanceDAO implements BalanceDAO {

    private final String TAG = this.getClass().getSimpleName();

    private DBConnectLite db;
    private Context context;

    private ArrayList<Balance> balances = new ArrayList<>();

    public SQLiteBalanceDAO(Context context){
        this.context = context;

        db = new DBConnectLite(context, null, null);
    }

    @Override
    public ArrayList<Balance> selectData() {
        try{
            SQLiteDatabase readable = db.getReadableDatabase();

            String query = "SELECT * FROM " + db.getDB_TABLE_BALANCE__NAME();
            Cursor cursor = readable.rawQuery(query,null);

            Balance b = null;
            while (cursor.moveToNext() ){

                b = new Balance(cursor.getFloat(cursor.getColumnIndex(db.getCOLUMN_BALANCE_AMOUNT())),
                        new Date(cursor.getString(cursor.getColumnIndex(db.getCOLUMN_BALANCE_TIMELOG()))));

                balances.add(b);
            }

            db.close();
        } catch (SQLiteException e){

            Log.i(TAG, e.getMessage());
        }

        return balances;
    }

    @Override
    public void insertData(Balance balance) {

        try{
            SQLiteDatabase writable = db.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(db.getCOLUMN_BALANCE_AMOUNT(), balance.getAmount());
            values.put(db.getCOLUMN_BALANCE_TIMELOG(), balance.getUpdateTime().toString());

            writable.insert(db.getDB_TABLE_BALANCE__NAME(), null, values);
        }catch (SQLException e){

            Log.i(TAG, e.getMessage());
        }
    }
}
