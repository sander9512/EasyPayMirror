package com.avans.easypay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avans.easypay.ASyncTasks.LoginTask;
import com.avans.easypay.DomainModel.Customer;

public class SignupActivity extends AppCompatActivity implements LoginTask.OnCustomerAvailable {

    private EditText firstnameInput, lastnameInput, usernameInput,
            passwordInput, emailInput, banknumberInput;
    private String firstname, lastname, username, password, email, banknumber;
    private String actualEmail, actualBanknumber;

    private Customer customer;
    private int minUsernameLength = 6, maxUsernameLength = 18,
            minPasswordLength = 8, maxPasswordLength = 32;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialise xml elements
        firstnameInput = (EditText) findViewById(R.id.firstname_edittext);
        lastnameInput = (EditText) findViewById(R.id.lastname_edittext);
        usernameInput = (EditText) findViewById(R.id.username_edittext);
        passwordInput = (EditText) findViewById(R.id.password_edittext);
        emailInput = (EditText) findViewById(R.id.email_edittext);
        banknumberInput = (EditText) findViewById(R.id.banknumber_edittext);
    }

    public void signupBtn(View v) {
        if (!firstnameInput.getText().toString().trim().equals("")) {
            String fn = firstnameInput.getText().toString().trim();
            firstname = fn.substring(0, 1).toUpperCase() + fn.substring(1).toLowerCase(); //capitalize first letter
        } else {
            firstname = "";
        }
        if (!lastnameInput.getText().toString().trim().equals("")) {
            String ln = lastnameInput.getText().toString().trim();
            lastname = ln.substring(0, 1).toUpperCase() + ln.substring(1).toLowerCase(); //capitalize first letter
        } else {
            lastname = "";
        }
        username = usernameInput.getText().toString().trim();
        password = passwordInput.getText().toString(); //no trim
        email = !emailInput.getText().toString().trim().equals("") ?
                "/" + emailInput.getText().toString().trim() : "";
        banknumber = !banknumberInput.getText().toString().trim().equals("") ?
                "/" + banknumberInput.getText().toString().trim() : "";

        //to save in shared preferences
        actualEmail = emailInput.getText().toString().trim();
        actualBanknumber = banknumberInput.getText().toString().trim();

        Log.i(this.getClass().getSimpleName(), username + "|" + password);
        Log.i(this.getClass().getSimpleName(), username.length() + "|" + password.length());


        if (!firstname.equals("") && !lastname.equals("")
                && username.length() > minUsernameLength && username.length() < maxUsernameLength
                && password.length() > minPasswordLength && password.length() < maxPasswordLength) {

            //if all required fields are filled, show progress dialog
            pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.account_being_created_progressdialog));
            pd.show();

            //check if username is already taken
            startLoginTask();

            //username and/or password still empty
        } else if (username.equals("") || password.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            //username too short
        } else if (username.length() < minUsernameLength && !username.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.username_too_short), Toast.LENGTH_SHORT).show();
            //username too long
        } else if (username.length() > maxUsernameLength && !username.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.username_too_long), Toast.LENGTH_SHORT).show();
            //password too short
        } else if (password.length() < minPasswordLength && !password.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.password_too_short), Toast.LENGTH_SHORT).show();
            //password too long
        } else if (password.length() > maxPasswordLength && !password.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.password_too_long), Toast.LENGTH_SHORT).show();
            //if other fields are empty
        } else {
            Toast.makeText(this, getResources().getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
        }
    }

    public void startLoginTask() {
        new LoginTask(this).execute("https://easypayserver.herokuapp.com/api/klant/login/" + username);
    }

    @Override
    public void onCustomerAvailable(Customer customer) {
        this.customer = customer;

        if (customer == null) {
            startSignupTask();
        } else {
            Toast.makeText(this, getResources().getString(R.string.username_already_exists), Toast.LENGTH_SHORT).show();
            pd.cancel();
        }
    }

    //start EasyPayAPIPUTConnector (AsyncTask)
    public void startSignupTask() {
        String url = "https://easypayserver.herokuapp.com/api/klant/" +
                "signup/" + firstname + "/" + lastname + "/" + username + "/" + password + email + banknumber;

        new EasyPayAPIPUTConnector().execute(url);
        Toast.makeText(this, getResources().getString(R.string.account_created1of2) + username
                + getResources().getString(R.string.account_created2of2), Toast.LENGTH_SHORT).show();
        pd.cancel();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
