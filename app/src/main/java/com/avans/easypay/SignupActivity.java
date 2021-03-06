package com.avans.easypay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avans.easypay.ASyncTasks.LoginTask;
import com.avans.easypay.DomainModel.Customer;

import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity implements LoginTask.OnCustomerAvailable {

    private EditText firstnameInput, lastnameInput, usernameInput,
            passwordInput, emailInput, banknumberInput;
    private String firstname, lastname, username, password, email, banknumber;
    private String actualEmail, actualBanknumber;

    private Customer customer;
    private int minUsernameLength = 6, maxUsernameLength = 16,
            minPasswordLength = 8, maxPasswordLength = 16;

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
        if (emailInput.getText().toString().trim().equals("")){
            email = "";
        } else{
            if (validEmail(emailInput.getText().toString().trim())){
                email = "/" + emailInput.getText().toString().trim();
            } else {
                email = null;
            }
        }

        if (banknumberInput.getText().toString().trim().equals("")){
            banknumber = "";
        } else {
            if(validIBAN(banknumberInput.getText().toString().trim())){
                banknumber = "/" + banknumberInput.getText().toString().trim();
            } else {
                banknumber = null;
            }
        }
//        email = !emailInput.getText().toString().trim().equals("") ?
//                "/" + emailInput.getText().toString().trim() : "";
//        banknumber = !banknumberInput.getText().toString().trim().equals("") ?
//                "/" + banknumberInput.getText().toString().trim() : "";

        Log.i(this.getClass().getSimpleName(), username + "|" + password);
        Log.i(this.getClass().getSimpleName(), username.length() + "|" + password.length());


        if (!firstname.equals("") && !lastname.equals("")
                && username.length() > minUsernameLength-1 && username.length() <= maxUsernameLength
                && password.length() > minPasswordLength-1 && password.length() <= maxPasswordLength
                && email != null && banknumber != null) {

            //if all required fields are filled, show progress dialog
            pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.account_being_created_progressdialog));
            pd.show();

            //check if username is already taken
            startLoginTask();

            //username and/or password still empty
        } else if (username.equals("") || password.equals("")) {
            Toasty.error(this, getResources().getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            //username too short
        } else if (username.length() < minUsernameLength && !username.equals("")) {
            Toasty.error(this, getResources().getString(R.string.username_too_short), Toast.LENGTH_SHORT).show();
            //username too long
        } else if (username.length() > maxUsernameLength && !username.equals("")) {
            Toasty.error(this, getResources().getString(R.string.username_too_long), Toast.LENGTH_SHORT).show();
            //password too short
        } else if (password.length() < minPasswordLength && !password.equals("")) {
            Toasty.error(this, getResources().getString(R.string.password_too_short), Toast.LENGTH_SHORT).show();
            //password too long
        } else if (password.length() > maxPasswordLength && !password.equals("")) {
            Toasty.error(this, getResources().getString(R.string.password_too_long), Toast.LENGTH_SHORT).show();
            //if other fields are empty
        } else if (email == null){
            Toasty.error(this, "Geen geldig email address", Toast.LENGTH_SHORT).show();
        } else if (banknumber == null){
            Toasty.error(this, "Geen geldig bankrekeningnummer", Toast.LENGTH_SHORT).show();
        } else {
            Toasty.error(this, getResources().getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean validIBAN(String iban){
        try{
            IbanUtil.validate(iban);
            Log.i("IBAN", "VALID IBAN");
            return true;
        }catch (IbanFormatException |
                InvalidCheckDigitException |
                UnsupportedCountryException e) {
            Log.i("IBAN", "UNVALID IBAN");
            return false;
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
            Toasty.error(this, getResources().getString(R.string.username_already_exists), Toast.LENGTH_SHORT).show();
            pd.cancel();
        }
    }

    //start EasyPayAPIPUTConnector (AsyncTask)
    public void startSignupTask() {
        String url = "https://easypayserver.herokuapp.com/api/klant/" +
                "signup/" + firstname + "/" + lastname + "/" + username + "/" + password + email + banknumber;

        new EasyPayAPIPUTConnector().execute(url);
        Toasty.success(this, getResources().getString(R.string.account_created1of2) + username
                + getResources().getString(R.string.account_created2of2), Toast.LENGTH_SHORT).show();
        pd.cancel();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
