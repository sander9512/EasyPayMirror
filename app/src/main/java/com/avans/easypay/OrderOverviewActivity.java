package com.avans.easypay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class OrderOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);

        String[] orders = {"Bier 3x, Cola 1x, Fanta 1x", "Baco 2x, Wijn 3x", "Friet 2x, Cola 2x"};

        GridView overviewGrid = (GridView) findViewById(R.id.orderListview);
        OverviewAdapter overviewAdapter = new OverviewAdapter(getApplicationContext(), orders);
        overviewGrid.setAdapter(overviewAdapter);
    }
}
