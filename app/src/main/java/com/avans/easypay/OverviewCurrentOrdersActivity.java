package com.avans.easypay;

import android.content.Intent;
import android.icu.text.PluralRules;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OverviewCurrentOrdersActivity extends AppCompatActivity {
    private CurrentOrderAdapter adapter;
    private ArrayList<Product> orderedProducts;
    private ProductsTotal total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        ListView currentOrder = (ListView) findViewById(R.id.oco_OrdersList);
        TextView order_location = (TextView) findViewById(R.id.orderLocation);
        TextView order_total = (TextView) findViewById(R.id.order_subtotal);

        orderedProducts = new ArrayList<>();
        Product p1 = new Product("Product 1", 2.50, 5, 1);
        Product p2 = new Product("Product 2", 4.50, 2, 2);
        Product p3 = new Product("Product 3", 7.50, 3, 3);
        Product p4 = new Product("Product 4", 1.50, 1, 4);

        orderedProducts.add(p1);
        orderedProducts.add(p2);
        orderedProducts.add(p3);
        orderedProducts.add(p4);

        total = new ProductsTotal(getApplicationContext(), orderedProducts);

        Order order = new Order(1, 5, "Friettent", orderedProducts);

        adapter = new CurrentOrderAdapter(getApplicationContext(), getLayoutInflater(), orderedProducts);
        currentOrder.setAdapter(adapter);

        order_location.setText("Locatie: " + order.getLocation());
        order_total.setText(total.getPriceTotal());
    }

}

