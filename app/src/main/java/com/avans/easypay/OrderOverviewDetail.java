package com.avans.easypay;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Order;

import java.util.ArrayList;


public class OrderOverviewDetail extends AppCompatActivity {
    private ArrayList<Order> mOrderList = new ArrayList<>();
    private TextView productNr, location, date;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);
        context = getApplicationContext();

        productNr = (TextView) findViewById(R.id.overview_order_nr);
        location = (TextView) findViewById(R.id.overview_location_id);
        date = (TextView) findViewById(R.id.overview_date_id);

        Bundle bundle = getIntent().getExtras();
        Order order = bundle.getParcelable("Order");
    }
}
