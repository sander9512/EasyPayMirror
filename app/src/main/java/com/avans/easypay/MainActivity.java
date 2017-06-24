package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Customer;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;
    //private ArrayList<Product> products = new ArrayList<>();
    private ImageView logout;

    //Balance balance = new Balance(20.00f, new Date());
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //exception_handler eh = new exception_handler(this);
        //Thread.setDefaultUncaughtExceptionHandler(eh);

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

        //READING THE PREVIOUSLY CREATED FILE
        try {
            File file = getFileStreamPath("error_file");
            if (file.exists()) {
                Log.d("Hey","IT EXISTS");
                //Check if there is a internet connection
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
                if(activeNetwork != null) {
                    //If there is a internet connection
                    FileInputStream fis = openFileInput("error_file");
                    InputStreamReader inputStreamReader = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    fis.close();
                    String data = stringBuilder.toString();

                    //Send the data to the database
                    Log.d("We have a","connection");
                    EasyPayAPIPUTConnector put = new EasyPayAPIPUTConnector();
                    String url = "https://dashboard.heroku.com/api/error/add_error/" + data;
                    put.execute(url);

                    //DELETING THE CACHE
                    deleteFile("error_file");
                }
            } else{
                Log.d("Aw","IT DOESNT EXIST");
            }


        } catch(Exception e){
            Log.d("Sh*t hit the fis",""+e);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Setting balance in toolbar
        if (balanceDAO.selectData().size() == 0){
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€0.00");
        } else {
            Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
        }
    }

    //OnClick
    public void orderButton(View v) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    public void overviewButton(View v) {
        Intent intent = new Intent(this, OrderOverviewActivity.class);
        startActivity(intent);
    }

    public void balanceButton(View v) {
        Intent intent = new Intent(this, BalanceActivity.class);
        startActivity(intent);
    }

    public void accountButton(View v) {
        Intent intent = new Intent(this, UserDataActivity.class);
        intent.putExtra("Customer", customer);
        startActivity(intent);
    }
}
