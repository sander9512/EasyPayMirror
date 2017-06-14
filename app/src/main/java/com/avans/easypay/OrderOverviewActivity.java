package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.util.ArrayList;


public class OrderOverviewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        EasyPayAPIOrdersConnector.OnOrdersAvailable{
    private DAOFactory factory;
    private BalanceDAO balanceDAO;
    private ImageView home;
    private OrderOverviewAdapter adapter;
    private ArrayList<Order> orders = new ArrayList<>();
    private EasyPayAPIOrdersConnector get;
    private ArrayList<Integer> orderNumbers = new ArrayList<>();
    private SharedPreferences customerPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);


        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(OrderOverviewActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

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
    protected void onResume(){
        super.onResume();

        //Setting balance in toolbar
        if (balanceDAO.selectData().size() == 0){
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€0.00");
        }else{
            Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
        }
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