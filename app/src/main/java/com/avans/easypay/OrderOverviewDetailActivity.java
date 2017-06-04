package com.avans.easypay;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static com.avans.easypay.R.id.locatie_id;
import static com.avans.easypay.R.id.order_nummer;

public class OrderOverviewDetailActivity extends AppCompatActivity {
    private ArrayList<Order> mOrderList = new ArrayList<>();
    private TextView locatie, ordernummer, datum;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);
        context = getApplicationContext();

        locatie = (TextView) findViewById(R.id.locatie_id);
        ordernummer = (TextView) findViewById(R.id.order_nummer);
        datum = (TextView) findViewById(R.id.date_id);

        Bundle bundle = getIntent().getExtras();
        String[] oArr = bundle.getStringArray("Order");
        Order order = new Order(Integer.parseInt(oArr[0]), Integer.parseInt(oArr[1]), oArr[2], oArr[3], Integer.parseInt(oArr[4]), Double.parseDouble(oArr[5]));

        ((TextView)findViewById(R.id.txt_ordernummer)).setText(order.getOrderId());
        ((TextView)findViewById(R.id.txt_date)).setText(order.getDate());
        ((TextView)findViewById(R.id.txt_locatie)).setText(order.getLocation());
        ((TextView)findViewById(R.id.txt_amount)).setText(order.getAmount());
        //voeg hier de andere vlden toe
    }
}
