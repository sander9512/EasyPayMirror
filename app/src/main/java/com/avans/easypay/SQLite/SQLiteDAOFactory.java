package com.avans.easypay.SQLite;

import android.content.Context;

/**
 * Created by Bart on 3-5-2017.
 */

public class SQLiteDAOFactory implements DAOFactory {

    private Context context;

    public SQLiteDAOFactory(Context context) {
        this.context = context;
    }


    @Override
    public BalanceDAO createBalanceDAO() {
        return new SQLiteBalanceDAO(context);
    }
}