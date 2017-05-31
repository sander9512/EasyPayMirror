package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;
import java.util.Date;

import static com.avans.easypay.TabbedActivity.PRODUCTS;

public class OverviewCurrentOrdersActivity extends AppCompatActivity {
    private CurrentOrderAdapter adapter;
    private ArrayList<Product> orderedProducts;
    private ProductsTotal total;
    private Order order;
    public static final String ORDER = "order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.get(PRODUCTS);
        ListView currentOrder = (ListView) findViewById(R.id.oco_OrdersList);
        TextView order_location = (TextView) findViewById(R.id.orderLocation);
        TextView order_total = (TextView) findViewById(R.id.order_subtotal);

        orderedProducts = order.getProducts();
        //dummydata
//        Product p1 = new Product("Product 1", 2.50, 5, 1);
//        Product p2 = new Product("Product 2", 4.50, 2, 2);
//        Product p3 = new Product("Product 3", 7.50, 3, 3);
//        Product p4 = new Product("Product 4", 1.50, 1, 4);

//        orderedProducts.add(p1);
//        orderedProducts.add(p2);
//        orderedProducts.add(p3);
//        orderedProducts.add(p4);

        total = new ProductsTotal(getApplicationContext(), orderedProducts);
        //dummy order
//        Order order = new Order(1, 5, "Friettent", orderedProducts);

        adapter = new CurrentOrderAdapter(getApplicationContext(), getLayoutInflater(), orderedProducts);
        currentOrder.setAdapter(adapter);

        order_location.setText(order.getLocation());
        order_total.setText(total.getPriceTotal());
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), LocationActivity.class);
        startActivity(i);
    }
    public void scanBtn(View view) {
        Intent i = new Intent(getApplicationContext(), ScanActivity.class);
        order.setStatus("WAITING");
        Date date = new Date(30/05/2017);
        order.setDate(date);
        order.setOrderId(1);
        i.putExtra(ORDER, order);
        startActivity(i);
    }

}

