package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //OnClick
    public void orderButton(View v) {
        Intent intent = new Intent(this, DrinksAndFoodActivity.class);
        startActivity(intent);
    }

    public void accountButton(View v) {
        Intent intent = new Intent(this, UserDataActivity.class);
        startActivity(intent);
    }
}
