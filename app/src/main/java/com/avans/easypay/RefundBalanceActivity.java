package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.util.Date;

public class RefundBalanceActivity extends AppCompatActivity {

    private TextView currentBalance;
    private ImageView home;

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    private Balance b;

    EasyPayAPIPUTConnector put;

    SharedPreferences customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_refund);

        customer = getSharedPreferences("CUSTOMER", Context.MODE_PRIVATE);

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        if(balanceDAO.selectData().size() == 0){
            Log.i("BALANCE", "No balance yet");
        }
        else{
            b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
        }

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(RefundBalanceActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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
                put = new EasyPayAPIPUTConnector();
                put.execute("https://easypayserver.herokuapp.com/api/klant/id=" + customer.getInt("ID", 0)
                        + "/saldo=" + b.getAmount() * 100 + "&datum=" + b.getTimeLog());
                balanceDAO.insertData(b);
                finish();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Setting balance in toolbar
        if (balanceDAO.selectData().size() == 0){
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€0.00");
        }else{
            Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
        }
    }
}
