package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OverviewCurrentOrdersActivity extends AppCompatActivity {

    private ArrayList<Product> mProductList = new ArrayList<Product>();
    private ListView mProductListView;
    private Button button;
    private TextView subtotal;
    private double price;
    private String totalprice;
    private ProductsTotal.OnTotalChanged totalListener = null;

    //private ArrayAdapter mPersonAdapter;
    private ProductAdapter   mProductAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_current_orders);

        // Maak de referentie naar de array list
        mProductListView = (ListView) findViewById(R.id.oco_OrdersList);
        subtotal = (TextView) findViewById(R.id.subtotal_text_oco);

        for (int i = 0; i < 10; i++) {
            Product product = new Product("Friet", 4.14,5);
            mProductList.add(product);
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        mProductAdapter = new ProductAdapter(totalListener, this, inflater, mProductList);

        // Link adapter to ListView
        mProductListView.setAdapter(mProductAdapter);

        // Update the sub-total price
        for(int i = 0; i < mProductList.size(); i++){
            price += mProductList.get(i).getProductPrice() * mProductList.get(i).getAmount();
        }
        totalprice = "" + price;
        subtotal.setText(totalprice);

        //Finding the button and linking the onclick
        button = (Button) findViewById(R.id.button_oco);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OverviewCurrentOrdersActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        // Force update listview
        this.mProductAdapter.notifyDataSetChanged();
    }

}

