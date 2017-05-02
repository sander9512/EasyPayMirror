package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Balance;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Balance balance = new Balance(14.00f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
        balanceToolbar.setText("$" + balance.getAmount());
    }

    //OnClick
    public void orderButton(View v) {
        Intent intent = new Intent(this, DrinksAndFoodActivity.class);
        startActivity(intent);
    }

    public void balanceButton(View v){
        Intent intent = new Intent(this, BalanceActivity.class);
        startActivity(intent);
    }
}
