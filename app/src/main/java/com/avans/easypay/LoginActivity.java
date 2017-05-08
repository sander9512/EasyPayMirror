package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialise xml elements
        Button loginBtn = (Button) findViewById(R.id.login_btn);
        Button signupBtn = (Button) findViewById(R.id.signup_btn);
    }

    public void loginBtn(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void signupBtn(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }
}
