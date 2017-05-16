package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avans.easypay.ASyncTasks.SignupTask;
import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Customer;

import java.util.Date;

public class SignupActivity extends AppCompatActivity implements SignupTask.OnCustomerCreated {

    private EditText firstnameInput, lastnameInput, usernameInput,
            passwordInput, emailInput, banknumberInput;
    private String firstname, lastname, username, password, email, banknumber;

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
        String fn = firstnameInput.getText().toString().trim();
        firstname = fn.substring(0, 1).toUpperCase() + fn.substring(1).toLowerCase(); //capitalize first letter
        String ln = lastnameInput.getText().toString().trim();
        lastname = ln.substring(0, 1).toUpperCase() + ln.substring(1).toLowerCase(); //capitalize first letter
        username = usernameInput.getText().toString().trim();
        password = passwordInput.getText().toString(); //no trim
        email = emailInput.getText().toString().trim();
        banknumber = banknumberInput.getText().toString().trim();

        Customer c = new Customer(0, username, password, email, firstname,
                lastname, banknumber, new Balance(0, new Date()), new Date().toString());

        Log.i(this.getClass().getSimpleName(), c.toString());

        startLoginTask();
    }

    //start SignupTask (AsyncTask)
    public void startLoginTask() {
        String url = "https://easypayserver.herokuapp.com/api/klant/" +
                "signup/" + firstname + "/" + lastname + "/" + username + "/" + password + "/" + email + "/" + banknumber;
        new SignupTask(this).execute(url);
        Log.i(this.getClass().getSimpleName(), url);

//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(SignupActivity.this, "Unsuccesful volley task.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        queue.add(stringRequest);
    }

    @Override
    public void onCustomerCreated(String succesMsg) {
        Toast.makeText(this, succesMsg, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
