package com.avans.easypay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.ASyncTasks.LoginTask;
import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Customer;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements LoginTask.OnCustomerAvailable {

    private String TAG = this.getClass().getSimpleName();

    private TextView usernameInput, passwordInput;
    private Customer customer;

    private CheckBox check;

    private String username, password;
    private int loginDelay = 400;

    //DB objects
    private DAOFactory factory;
    BalanceDAO balanceDAO;

    //Progress Dialog
    ProgressDialog pd;

    private SharedPreferences loginPref;
    private SharedPreferences.Editor loginEdit;

    private SharedPreferences customerPref;
    private SharedPreferences.Editor customerEdit;

    public static final String PREFERENCECUSTOMER = "CUSTOMER";
    public static final String PREFERENCELOGIN = "LOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialise xml elements
        usernameInput = (TextView) findViewById(R.id.username_textview);
        passwordInput = (TextView) findViewById(R.id.password_textview);
        check = (CheckBox) findViewById(R.id.saveUser);

        //initialise DB objects
        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        customerPref = getSharedPreferences(PREFERENCECUSTOMER, Context.MODE_PRIVATE);
        customerEdit = customerPref.edit();

        loginPref = getSharedPreferences(PREFERENCELOGIN, Context.MODE_PRIVATE);
        loginEdit = loginPref.edit();

        if (loginPref.getBoolean("Check", false)){
            usernameInput.setText(loginPref.getString("Username", ""));
            check.setChecked(true);
        } else{
            usernameInput.setText("");
            check.setChecked(false);
        }
    }

    public void loginBtn(View v) {

        if (check.isChecked()){
            loginEdit.putString("Username", usernameInput.getText().toString());
            loginEdit.putBoolean("Check", true);
            loginEdit.commit();
        } else{
            loginEdit.putString("Username", "");
            loginEdit.putBoolean("Check", false);
            loginEdit.commit();
        }

        //show loading animation
        username = usernameInput.getText().toString().trim().toLowerCase();
        password = passwordInput.getText().toString();

        //EditTexts empty?
        if (!username.equals("") && !password.equals("")) {
            pd = new ProgressDialog(this);
            pd.setMessage("Aan het inloggen...");
            pd.show();

            startLoginTask();
        } else {
            Toasty.error(this, "Een of meer velden zijn niet ingevuld.", Toast.LENGTH_LONG).show();
        }
    }

    public void signupBtn(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    @Override
    public void onCustomerAvailable(Customer customer) {
        this.customer = customer;

        //end ProgressDialog
        pd.cancel();

        //LoginTask did not return a customer, so username = invalid
        if (customer == null) {
            Toasty.error(LoginActivity.this, "Gebruikersnaam bestaat niet.", Toast.LENGTH_LONG).show();
            passwordInput.setText("");

            //username and password input is a valid customer
        } else if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) {
            compareOnlineWithLocalBalance();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            i.putExtra("Customer", customer);
            customerEdit.putInt("ID", customer.getCustomerId());
            customerEdit.putString("Username", customer.getUsername());
            customerEdit.putString("Password", customer.getPassword());
            customerEdit.putString("Email", customer.getEmail());
            customerEdit.putString("FirstName", customer.getFirstname());
            customerEdit.putString("LastName", customer.getLastname());
            customerEdit.putString("Bank", customer.getBankAccountNumber());
            customerEdit.commit();
            startActivity(i);
            finish();

            //username exists, but password is invalid
        } else {
            Toasty.error(LoginActivity.this, "Gegevens zijn onjuist.", Toast.LENGTH_LONG).show();
            passwordInput.setText("");
        }
    }

    //start LoginTask (AsyncTask)
    public void startLoginTask() {
        new LoginTask(this).execute("https://easypayserver.herokuapp.com/api/klant/login/" + username);
    }

    public void compareOnlineWithLocalBalance() {
        //if there is no local balance yet, insert a row with €0,00
        //this is the case when a user first logs in
        if (balanceDAO.selectData().size() == 0) {
            Calendar calDate = Calendar.getInstance();
            long t = calDate.getTimeInMillis();
            Date date = new Date(t + 1000);

            balanceDAO.insertData(new Balance(0, date));
        }

        //get local balance
        float localBalance = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).getAmount();
        //get online balance
        float onlineBalance = customer.getBalance().getAmount();

        //log balances
        Log.i(this.getClass().getSimpleName(), "Local balance = " + localBalance + " | | Online balance = " + onlineBalance);

        //check whether local and online balance are the same. if not, update local DB
        if (localBalance != onlineBalance) {
            Log.i(TAG, "Online balance and local balance are different. Updating local DB!");
//            Log.i(TAG, "Before: " + balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).toString());
            balanceDAO.insertData(customer.getBalance());
//            Log.i(TAG, "After: " + balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).toString());
        } else {
            Log.i(TAG, "Online balance and local balance are the same. No local update required.");
        }
    }
}
