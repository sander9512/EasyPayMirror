package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;
import com.avans.easypay.SQLite.*;


import java.util.ArrayList;


public class OrderOverviewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        EasyPayAPIOrdersConnector.OnOrdersAvailable{
    private ArrayList<Order> mOrderList = new ArrayList<>();
    private OverviewAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();
    private EasyPayAPIOrdersConnector get;
    private ArrayList<Integer> orderNumbers = new ArrayList<>();


    private OverviewAdapter   mOverviewAdapter;
    public static final String ORDER = "order";

    private OrderOverviewAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();
    private EasyPayAPIOrdersConnector get;
    private ArrayList<Integer> orderNumbers = new ArrayList<>();
    private SharedPreferences customerPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);


        customerPref = getSharedPreferences("CUSTOMER", Context.MODE_PRIVATE);

        //get orders from DB
        get = new EasyPayAPIOrdersConnector(this);
        get.execute("https://easypayserver.herokuapp.com/api/bestelling/klant/" + customerPref.getInt("ID", 0));


        //initialise listview
        ListView orderListview = (ListView) findViewById(R.id.orderOverviewListview);
        //initialise adapter and attach to listview
        adapter = new OrderOverviewAdapter(this, orders);
        orderListview.setAdapter(adapter);

        //set listener(s)
        orderListview.setOnItemClickListener(this);
    }

    @Override
    public void onOrdersAvailable(Order order) {
        if (!orderNumbers.isEmpty()){
            if (!orderNumbers.contains(order.getOrderNumber())){
                orderNumbers.add(order.getOrderNumber());
                orders.add(order);
                adapter.notifyDataSetChanged();
            } else
                return;
        } else {
            orders.add(order);
            orderNumbers.add(order.getOrderNumber());
            adapter.notifyDataSetChanged();
        }
    }

    public void onOrdersAvailable(Order order) {
        if (!orderNumbers.isEmpty()){
            if (!orderNumbers.contains(order.getOrderNumber())){
                orderNumbers.add(order.getOrderNumber());
                orders.add(order);
                Log.i("PRODUCTS", "  " + order.getProductsIDs());
                adapter.notifyDataSetChanged();
            } else
                return;
        } else {
            orders.add(order);
            orderNumbers.add(order.getOrderNumber());
            adapter.notifyDataSetChanged();
        }
        //Log.i("PRODUCTS", "  " + order.getProducts());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order = orders.get(position);

        Intent i = new Intent(this, OrderOverviewDetailActivity.class);
        i.putExtra("order", order);
        //convert Date to milliseconds and add to intent
        long dateInMillis = order.getDate().getTime() + new Double(2.16e+7).longValue();
        i.putExtra("dateInMillis", dateInMillis);
        startActivity(i);
    }
}