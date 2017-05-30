package com.avans.easypay;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Order;

import java.util.ArrayList;

import static com.avans.easypay.R.id.amount_id;
import static com.avans.easypay.R.id.product_id;

public class OrderOverviewDetail extends AppCompatActivity {
    private ArrayList<Order> mOrderList = new ArrayList<>();
    private TextView productName, amount, price;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);
        context = getApplicationContext();

        productName = (TextView) findViewById(R.id.product_id);
        amount = (TextView) findViewById(R.id.amount_id);
        price = (TextView) findViewById(R.id.price_id);

        Bundle bundle = getIntent().getExtras();
        Order order = bundle.getParcelable("Order");
    }
}
