package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
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

public class OrderOverviewActivity extends AppCompatActivity {
    private ArrayList<Product> mProductList = new ArrayList<Product>();

    private OverviewAdapter mOverviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);
        mOverviewAdapter = new OverviewAdapter(getApplicationContext(), getLayoutInflater(), mProductList);
        ListView ListOverview = (ListView) findViewById(R.id.orderListview);
        ListOverview.setAdapter(mOverviewAdapter);

        ListOverview.setClickable(true);
        ListOverview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OrderOverviewDetail.class);
                startActivity(intent);
            }
        });

        //Aanmaken van product objecten en toevoegen aan de lijst
        for (int i = 0; i < 4; i++) {
            Product product = new Product("Bier", 2.5, 1, 5);
            mProductList.add(product);
        }

        // Force update listview
        this.mOverviewAdapter.notifyDataSetChanged();


    }
}