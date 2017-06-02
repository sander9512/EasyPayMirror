package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.avans.easypay.DomainModel.Order;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3, btn4, btn5;
    private Context context;
    private String loc1, loc2, loc3, loc4, loc5;
    private Order order = new Order();
    public static final String ORDER = "order";
    
    private DAOFactory factory;
    private BalanceDAO balanceDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        context = getApplicationContext();
        btn1 = (Button) findViewById(R.id.locatie_btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.locatie_btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.locatie_btn3);
        btn3.setOnClickListener(this);
        btn4 = (Button) findViewById(R.id.locatie_btn4);
        btn4.setOnClickListener(this);
        btn5 = (Button) findViewById(R.id.locatie_btn5);
        btn5.setOnClickListener(this);

        loc1 = "Liqueurpaleis";
        loc2 = "Pizzahut";
        loc3 = "Bierplaza";
        loc4 = "Koffiehuis";
        loc5 = "Friettent";

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        }else{
            Balance b = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1);
            Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            TextView balanceToolbar = (TextView) toolbar.findViewById(R.id.toolbar_balance);
            balanceToolbar.setText("€" + String.format("%.2f", b.getAmount()));
        }
    }

    //      Hier mogelijk tags doorgeven vanuit database om assortiment te filteren op locatie
    // order aanmaken en locatie toevoegen, vervolgens doorsturen naar tabbedactivity
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locatie_btn1:
                Intent i = new Intent(context, TabbedActivity.class);
                order.setLocation(loc1);
                i.putExtra(ORDER, order);
                startActivity(i);
                break;

            case R.id.locatie_btn2:
                Intent i2 = new Intent(context, TabbedActivity.class);
                order.setLocation(loc2);
                i2.putExtra(ORDER, order);
                startActivity(i2);
                break;

            case R.id.locatie_btn3:
                Intent i3 = new Intent(context, TabbedActivity.class);
                order.setLocation(loc3);
                i3.putExtra(ORDER, order);
                startActivity(i3);
                break;

            case R.id.locatie_btn4:
                Intent i4 = new Intent(context, TabbedActivity.class);
                order.setLocation(loc4);
                i4.putExtra(ORDER, order);
                startActivity(i4);
                break;

            case R.id.locatie_btn5:
                Intent i5 = new Intent(context, TabbedActivity.class);
                order.setLocation(loc5);
                i5.putExtra(ORDER, order);
                startActivity(i5);
                break;
        }

    }
}
