package com.avans.easypay;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.DBConnectLite;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    private ImageView logout;

    //Balance balance = new Balance(20.00f, new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Setting balance in toolbar
        Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
        balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
    }

    //OnClick
    public void orderButton(View v) {
        Intent intent = new Intent(this, DrinksAndFoodActivity.class);
        startActivity(intent);
    }


    public void balanceButton(View v) {
        Intent intent = new Intent(this, BalanceActivity.class);
        startActivity(intent);
    }

    public void accountButton(View v) {
        Intent intent = new Intent(this, UserDataActivity.class);
        startActivity(intent);
    }
}
