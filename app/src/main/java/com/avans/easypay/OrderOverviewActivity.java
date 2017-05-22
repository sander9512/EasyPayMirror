package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderOverviewActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private ArrayList<Order> mOrderList = new ArrayList<>();

    private OverviewAdapter mOverviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);
        mOverviewAdapter = new OverviewAdapter(getApplicationContext(), getLayoutInflater(), mOrderList);
        ListView ListOverview = (ListView) findViewById(R.id.orderListview);
        ListOverview.setAdapter(mOverviewAdapter);

        //Aanmaken van product objecten en toevoegen aan de lijst
        Order order1 = new Order(1, 17052017, "Liqueurpaleis", "Vodka", 3, 15.00);
        Order order2 = new Order(2, 17052017, "Bierplaza", "Bier", 5, 12.50);
        Order order3 = new Order(3, 17052017, "Friettent", "Patat", 1, 2.50);
        Order order4 = new Order(4, 17052017, "Koffiehuis", "Latte Machiatto", 2, 10.00);
        mOrderList.add(order1);
        mOrderList.add(order2);
        mOrderList.add(order3);
        mOrderList.add(order4);

        // Force update listview
        this.mOverviewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order = (Order) mOrderList.get(position);
        Intent intent = new Intent(getApplicationContext(), OrderOverviewDetail.class);
        intent.putExtra("Order", order);
        startActivity(intent);
    }
}