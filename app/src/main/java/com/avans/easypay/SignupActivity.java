package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialise xml elements
        Button signupBtn = (Button) findViewById(R.id.signup_btn);
    }

    public void signupBtn(View v ) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
