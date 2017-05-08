package com.avans.easypay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.util.Date;

public class RefundBalanceActivity extends AppCompatActivity {

    private TextView currentBalance;

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    private Balance b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_refund);

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();
        b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button CancelButton = (Button) findViewById(R.id.balance_refund_cancel);
        Button ConfirmButton = (Button) findViewById(R.id.balance_refund_confirm);
        CancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Current balance
        currentBalance = (TextView) findViewById(R.id.balanceRefund);
        currentBalance.setText(getResources().getString(R.string.balance_wallet_increase)
                + " " + "€" + String.format("%.2f", b.getAmount()));

        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Balance b = new Balance(0f, new Date());
                balanceDAO.insertData(b);
                finish();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);


        //Setting balance in toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
        balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
    }
}
