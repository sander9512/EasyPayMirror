package com.avans.easypay;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.ASyncTasks.LoginTask;
import com.avans.easypay.DomainModel.Customer;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.util.Date;

public class LoginActivity extends AppCompatActivity implements LoginTask.OnCustomerAvailable {

    private String TAG = this.getClass().getSimpleName();

    private TextView usernameInput, passwordInput;
    private Customer customer;

    private String username, password;
    private int loginDelay = 400;

    //handler to regulate LoginTask
    private Handler handler = new Handler();

    //DB objects
    private DAOFactory factory;
    BalanceDAO balanceDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialise xml elements
        usernameInput = (TextView) findViewById(R.id.username_textview);
        passwordInput = (TextView) findViewById(R.id.password_textview);

        //initialise DB objects
        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();
    }

    public void loginBtn(View v) {
        username = usernameInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        checkLoginData();
    }

    public void signupBtn(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    public void checkLoginData() {
        startLoginTask();

        //wait for x seconds, so the LoginTask finished. Then verify input
        handler.postDelayed(new Runnable() {
            public void run() {

                //EditTexts empty?
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Een of meer velden zijn niet ingevuld.", Toast.LENGTH_LONG).show();
                } else {

                    //LoginTask did not return a customer, so username = invalid
                    if (customer == null) {
                        Toast.makeText(LoginActivity.this, "Gebruikersnaam bestaat niet.", Toast.LENGTH_LONG).show();

                        //username and password input is a valid customer
                    } else if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) {
                        compareOnlineWithLocalTimelog();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("Customer", customer);
                        startActivity(i);
                        finish();

                        //username exists, but password is invalid
                    } else {
                        Toast.makeText(LoginActivity.this, "Gegevens zijn onjuist.", Toast.LENGTH_LONG).show();
                        passwordInput.setText("");
                    }
                }
                //log input and LoginTask result
//                Log.i("Username = ", "" + username);
//                Log.i("Password = ", "" + password);
//                Log.i("Customer = ", "" + customer);
            }
        }, loginDelay);

    }

    @Override
    public void onCustomerAvailable(Customer customer) {
        this.customer = customer;
    }

    //start AsyncTask (LoginTask)
    public void startLoginTask() {
        new LoginTask(this).execute("https://easypayserver.herokuapp.com/api/klant/login/" + username);
    }

    public void compareOnlineWithLocalTimelog() {

        //get local balance
        float localBalance = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).getAmount();
        //get online balance
        float onlineBalance = customer.getBalance().getAmount();

        //log balances
        Log.i(this.getClass().getSimpleName(), "Local balance = " + localBalance + " | | Online balance = " + onlineBalance);

        //check whether local and online balance are the same. if not, update local DB
        if (localBalance != onlineBalance) {
            Log.i(TAG, "Online balance and local balance are different. Updating local DB!");
            Log.i(TAG, "Before: " + balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).toString());
            balanceDAO.insertData(customer.getBalance());
            Log.i(TAG, "After: " + balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).toString());
        } else {
            Log.i(TAG, "Online balance and local balance are the same. No local update required.");
        }
    }
}
