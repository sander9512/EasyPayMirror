package com.avans.easypay;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;

import static com.avans.easypay.OrderOverviewActivity.ORDER;


public class OrderOverviewDetail extends AppCompatActivity {
    private ArrayList<Product> productsList = new ArrayList<>();
    private TextView locatie, ordernummer, datum;
    private ListView listView;
    private Context context;
    private Order order;
    private CurrentOrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);
        context = getApplicationContext();
        listView = (ListView) findViewById(R.id.Order_Overview);
        locatie = (TextView) findViewById(R.id.overview_location_id);
        ordernummer = (TextView) findViewById(R.id.overview_order_nr);
        datum = (TextView) findViewById(R.id.overview_date_id);


        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.get(ORDER);
        adapter = new CurrentOrderAdapter(getApplicationContext(), getLayoutInflater(), order.getProducts());
        listView.setAdapter(adapter);
        //String[] oArr = bundle.getStringArray("Order");
        //Order order = new Order(Integer.parseInt(oArr[0]), Integer.parseInt(oArr[1]), oArr[2], oArr[3], Integer.parseInt(oArr[4]), Double.parseDouble(oArr[5]));

        locatie.setText(order.getLocation());
        ordernummer.setText(" " + order.getOrderId());
        datum.setText(" " + order.getDate());

        //((TextView)findViewById(R.id.txt_ordernummer)).setText(order.getOrderId());
        //((TextView)findViewById(R.id.txt_date)).setText(order.getDate());
        // ((TextView)findViewById(R.id.txt_locatie)).setText(order.getLocation());
        //((TextView)findViewById(R.id.txt_amount)).setText(order.getAmount());
        //voeg hier de andere vlden toe
    }
}
