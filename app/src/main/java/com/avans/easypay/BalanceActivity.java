package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

public class BalanceActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    private Balance b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();
        b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);


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
                if (b.getAmount() == 0.f){
                    Toast.makeText(BalanceActivity.this, getResources().getString(R.string.nomoney), Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(BalanceActivity.this, RefundBalanceActivity.class);
                    startActivity(intent);
                }
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

        //Setting up TextView balance
        TextView balance = (TextView) findViewById(R.id.balanceText);
        balance.setText(getResources().getString(R.string.balance) + ": €" + String.format("%.2f", b.getAmount()));
    }
}
