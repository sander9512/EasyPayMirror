package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.ASyncTasks.CheckAvailableOrderNumberTask;
import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.avans.easypay.TabbedActivity.PRODUCTS;

public class OverviewCurrentOrdersActivity extends AppCompatActivity implements CheckAvailableOrderNumberTask.OnOrderNumberAvailable, ProductsTotal.OnTotalChangedHash {
    private CurrentOrderAdapter adapter;
    private ArrayList<Product> orderedProducts = new ArrayList<>();
    private HashSet<Product> hashSet;
    private ProductsTotal total;
    private Order order;
    public static final String ORDER = "ORDER";
    protected static double orderTotalPrice = 0;
    private HashSet<Product> mergedHashSet = new HashSet<>();

    private DAOFactory factory;
    private BalanceDAO balanceDAO;
    private TextView order_total;

    private SharedPreferences customerPref, locationPref;
    public static final String PREFERENCECUSTOMER = "CUSTOMER";
    public static final String PREFERENCELOCATION = "LOCATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.get(PRODUCTS);
        ListView currentOrder = (ListView) findViewById(R.id.oco_OrdersList);
        TextView order_location = (TextView) findViewById(R.id.orderLocation);
        order_total = (TextView) findViewById(R.id.order_subtotal);
        hashSet = order.getHashProducts();
        orderedProducts.addAll(hashSet);

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();
        total = new ProductsTotal(getApplicationContext(), orderedProducts);

        adapter = new CurrentOrderAdapter(this, getApplicationContext(), getLayoutInflater(), orderedProducts);
        currentOrder.setAdapter(adapter);

        order_location.setText(order.getLocation());
        order_total.setText(total.getPriceTotal());

        orderTotalPrice = total.getPriceTotalDouble();
        //get location shared preferences
        locationPref = getSharedPreferences(PREFERENCELOCATION, Context.MODE_PRIVATE);

        //set toolbar buttons
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OverviewCurrentOrdersActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Setting balance in toolbar
        if (balanceDAO.selectData().size() == 0){
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€0.00");
        } else {
            Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
        }
    }

    public void scanBtn(View view) {

        if(orderTotalPrice ==0) {
            Toasty.error(this, "Bestelling is leeg.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {

            //construct this order further
            String getOrderNumberURL = "https://easypayserver.herokuapp.com/api/bestelling/check/available/ordernumber";
            new CheckAvailableOrderNumberTask(this).execute(getOrderNumberURL);

            customerPref = getSharedPreferences(PREFERENCECUSTOMER, Context.MODE_PRIVATE);
            order.setProducts(orderedProducts);
            order.setCustomerId(customerPref.getInt("ID", 0));
            order.setDate(new Date());
            order.setStatus("WAITING");
        }
    }

    @Override
    public void onOrderNumberAvailable(int orderNumber) {
        Log.i(this.getClass().getSimpleName(), orderNumber + "");
        order.setOrderNumber(orderNumber);

        //with this order number, loop through the products and add them to DB

        for (int i = 0; i < order.getProducts().size(); i++) {
            for (int j = 0; j < order.getProducts().get(i).getAmount(); j++) {

                String createOrderURL = "https://easypayserver.herokuapp.com/api/bestelling/create/" +
                        order.getCustomerId() + "/" +
                        order.getProducts().get(i).getProductId() + "/" +
                        order.getStatus() + "/" +
                        orderNumber + "/" +
                        getLocationID();

                //post all products from this order (with same order number) to DB
                new EasyPayAPIPUTConnector().execute(createOrderURL);
                Log.i(this.getClass().getSimpleName(), "URL = " + createOrderURL);
            }
        }

        Intent i = new Intent(getApplicationContext(), ScanActivity.class);
        i.putExtra(ORDER, order);
        startActivity(i);
    }

    public int getLocationID() {
        Map<String, ?> allEntries = locationPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            if (entry.getValue().equals(order.getLocation())) {
                Log.i("Location = ", entry.getKey() + " | " + order.getLocation());
                return Integer.parseInt(entry.getKey());
            }
        }
        return -1;
    }

    @Override
    public void onTotalChangedHash(double priceTotal, int total, HashSet<Product> products) {
        this.hashSet = products;
        mergedHashSet.addAll(hashSet);
        double totalPrice = 0;
        Log.i("mergedProducts", "" + mergedHashSet.size());

        Iterator<Product> iter = mergedHashSet.iterator();

        while (iter.hasNext()) {
            Product p = iter.next();

            if (p.getAmount() == 0) {
                iter.remove();
            }
        }

        for (Product product : mergedHashSet) {
            totalPrice += product.getProductPrice() * product.getAmount();
        }
        DecimalFormat df = new DecimalFormat("0.00##");
        order_total.setText("Subtotaal: €" + df.format(totalPrice));
        orderTotalPrice = totalPrice;
        orderedProducts.clear();
        orderedProducts.addAll(mergedHashSet);
        adapter.notifyDataSetChanged();
    }
}