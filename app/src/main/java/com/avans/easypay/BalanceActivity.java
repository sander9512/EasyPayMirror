package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class BalanceActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button IncrBalButton = (Button) findViewById(R.id.balance_wallet_toincrease);
        Button RefBalButton = (Button) findViewById(R.id.balance_wallet_torefund);

        IncrBalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceActivity.this, IncreaseBalanceActivity.class);
                startActivity(intent);
            }
        });

        RefBalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceActivity.this, RefundBalanceActivity.class);
                startActivity(intent);
            }
        });
    }
}
