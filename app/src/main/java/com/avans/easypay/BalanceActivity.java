package com.avans.easypay;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

public class BalanceActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;
    private ImageView home;
    private TextView balance;

    private Balance b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

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
                Intent intent = new Intent(BalanceActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        Button IncrBalButton = (Button) findViewById(R.id.balance_wallet_toincrease);
        Button RefBalButton = (Button) findViewById(R.id.balance_wallet_torefund);

        IncrBalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BalanceActivity.this, IncreaseBalanceActivity.class);
                startActivity(intent);
            }
        });

        balance = (TextView) findViewById(R.id.balanceText);

        RefBalButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (balance.getText().toString().equals(getResources().getString(R.string.balance) + ": €0.00")
                        || balance.getText().toString().equals(getResources().getString(R.string.balance) + ": €0,00")) {
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

        //Setting balance in toolbar
        if (balanceDAO.selectData().size() == 0){
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€0.00");

            //Setting up TextView balance
            TextView balance = (TextView) findViewById(R.id.balanceText);
            balance.setText(getResources().getString(R.string.balance) + ": €0.00");
        }else{
            Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));

            //Setting up TextView balance
            balance.setText(getResources().getString(R.string.balance) + ": €" + String.format("%.2f", b.getAmount()));
        }
    }
}
