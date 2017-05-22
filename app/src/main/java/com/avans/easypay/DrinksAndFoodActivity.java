package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DrinksAndFoodActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3, btn4, btn5;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks_and_food);
        context = getApplicationContext();
        btn1 = (Button) findViewById(R.id.locatie_btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.locatie_btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.locatie_btn3);
        btn3.setOnClickListener(this);
        btn4 = (Button) findViewById(R.id.locatie_btn4);
        btn4.setOnClickListener(this);
        btn5 = (Button) findViewById(R.id.locatie_btn5);
        btn5.setOnClickListener(this);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(DrinksAndFoodActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
//      Hier mogelijk tags doorgeven vanuit database om assortiment te filteren op locatie
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.locatie_btn1:
                Intent i = new Intent(context, TabbedActivity.class);
                startActivity(i);
                break;

            case R.id.locatie_btn2:
                Intent i2 = new Intent(context, TabbedActivity.class);
                startActivity(i2);
                break;

            case R.id.locatie_btn3:
                Intent i3 = new Intent(context, TabbedActivity.class );
                startActivity(i3);
                break;

            case R.id.locatie_btn4:
                Intent i4 = new Intent(context, TabbedActivity.class);
                startActivity(i4);
                break;

            case R.id.locatie_btn5:
                Intent i5 = new Intent(context, TabbedActivity.class);
                startActivity(i5);
                break;
        }

    }
}
