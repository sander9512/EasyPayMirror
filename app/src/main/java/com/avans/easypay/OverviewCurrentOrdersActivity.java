package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypay.ASyncTasks.CheckAvailableOrderNumberTask;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import static com.avans.easypay.TabbedActivity.PRODUCTS;

public class OverviewCurrentOrdersActivity extends AppCompatActivity implements CheckAvailableOrderNumberTask.OnOrderNumberAvailable {
    private CurrentOrderAdapter adapter;
    private ArrayList<Product> orderedProducts = new ArrayList<>();
    private HashSet<Product> hashSet;
    private ProductsTotal total;
    private Order order;
    public static final String ORDER = "ORDER";

    private SharedPreferences customerPref;
    public static final String PREFERENCECUSTOMER = "CUSTOMER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.get(PRODUCTS);
        ListView currentOrder = (ListView) findViewById(R.id.oco_OrdersList);
        TextView order_location = (TextView) findViewById(R.id.orderLocation);
        TextView order_total = (TextView) findViewById(R.id.order_subtotal);
        hashSet = order.getHashProducts();
        orderedProducts.addAll(hashSet);

        total = new ProductsTotal(getApplicationContext(), orderedProducts);

        adapter = new CurrentOrderAdapter(getApplicationContext(), getLayoutInflater(), orderedProducts);
        currentOrder.setAdapter(adapter);

        order_location.setText(order.getLocation());
        order_total.setText(total.getPriceTotal());
    }

//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(getApplicationContext(), LocationActivity.class);
//        startActivity(i);
//    }

    public void scanBtn(View view) {

        //construct this order further
        String getOrderNumberURL = "https://easypayserver.herokuapp.com/api/bestelling/check/available/ordernumber";
        new CheckAvailableOrderNumberTask(this).execute(getOrderNumberURL);

        customerPref = getSharedPreferences(PREFERENCECUSTOMER, Context.MODE_PRIVATE);
        order.setProducts(orderedProducts);
        order.setCustomerId(customerPref.getInt("ID", 0));
        order.setDate(new Date());
        order.setStatus("WAITING");
    }

    @Override
    public void onOrderNumberAvailable(int orderNumber) {
        Log.i(this.getClass().getSimpleName(), orderNumber + "");
        order.setOrderNumber(orderNumber);

        //with this order number, loop through the products and add them to DB

        for (int i = 0; i < order.getProducts().size(); i++) {
            for (int j = 0; j < order.getProducts().get(i).getAmount(); j++) {

                String unparsedCreateOrderURL = "https://easypayserver.herokuapp.com/api/bestelling/create/" +
                        order.getCustomerId() + "/" +
                        order.getProducts().get(i).getProductId() + "/" +
                        order.getStatus() + "/" +
                        orderNumber + "/" +
//                    order.getDate() + "/" +
//                    order.getLocation();
                        4; //dit is nog hardcoded 4. Een order moet namelijk een location id hebben.

                String createOrderURL = unparsedCreateOrderURL.replace(" ", "%20");
                new EasyPayAPIPUTConnector().execute(unparsedCreateOrderURL);
                Log.i(this.getClass().getSimpleName(), "URL = " + createOrderURL);
            }
        }

        Intent i = new Intent(getApplicationContext(), ScanActivity.class);
        i.putExtra(ORDER, order);
        Log.i(this.getClass().getSimpleName() + "order = ", order.toString());
        startActivity(i);
    }
}

