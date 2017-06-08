package com.avans.easypay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonWriter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;
import java.util.Date;

public class OrderOverviewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener /*implements ListView.OnItemClickListener */{
    private ArrayList<Order> mOrderList = new ArrayList<>();

    private OverviewAdapter mOverviewAdapter;
    public static final String ORDER = "order";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview);



//        Aanmaken van product objecten en toevoegen aan de lijst
//        Order order1 = new Order(1, 17052017, "Liqueurpaleis", "Vodka", 3, 15.00);
//        Order order2 = new Order(2, 17052017, "Bierplaza", "Bier", 5, 12.50);
//        Order order3 = new Order(3, 17052017, "Friettent", "Patat", 1, 2.50);
//        Order order4 = new Order(4, 17052017, "Koffiehuis", "Latte Machiatto", 2, 10.00);
//        mOrderList.add(order1);
//        mOrderList.add(order2);
//        mOrderList.add(order3);
//        mOrderList.add(order4);


        //Aanmaken van product objecten en toevoegen aan de lijst

        //nt orderId, int date, String location, ArrayList<Product> orderedProducts

        ArrayList<Product> prod0 = new ArrayList<Product>();
        //prod0.add(new Product("Testproduct", 213, 1000));

        for (int i = 0; i < 10; i++) {
            Product p = new Product("Testproduct" + i, 2.50, i);
            p.setAmount(2);
            prod0.add(p);
        }
        Date date = new Date();
        Order order = new Order(5, 5, date, "testloc", prod0, 3424, "WAITING");
        Order order2 = new Order(10, 22, date, "loc2", prod0, 2213, "WAITING");
        Order order3 = new Order(15, 55, date, "loc3", prod0, 2534, "WAITING");

        //       Order order1 = new Order(1, 17052017, "Liqueurpaleis", "Vodka", 3, 15.00);
        //       Order order2 = new Order(2, 17052017, "Bierplaza", "Bier", 5, 12.50);
        //       Order order3 = new Order(3, 17052017, "Friettent", "Patat", 1, 2.50);
        //       Order order4 = new Order(4, 17052017, "Koffiehuis", "Latte Machiatto", 2, 10.00);
        mOrderList.add(order);
        mOrderList.add(order2);
        mOrderList.add(order3);
        //      mOrderList.add(order2);
        //      mOrderList.add(order3);
        //      mOrderList.add(order4);

        // Force update listview


        mOverviewAdapter = new OverviewAdapter(getApplicationContext(), getLayoutInflater(), mOrderList);
        ListView ListOverview = (ListView) findViewById(R.id.orderListview);
        ListOverview.setAdapter(mOverviewAdapter);
        this.mOverviewAdapter.notifyDataSetChanged();

        ListOverview.setOnItemClickListener(this);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(OrderOverviewActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Order order = mOrderList.get(position);
        Intent intent = new Intent(getApplicationContext(), OrderOverviewDetail.class);
        intent.putExtra(ORDER, order);
        startActivity(intent);
    }
}