package com.avans.easypay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class OrderOverviewDetailActivity extends AppCompatActivity implements EasyPayAPIConnector.OnProductAvailable,
        EasyPayAPIGETOrderConnector.OnOrdersAvailable {

    private DAOFactory factory;
    private BalanceDAO balanceDAO;
    private ImageView home;

    private String TAG = this.getClass().getSimpleName();

    private ArrayList<Product> productList;
    private ListView listview;
    private TextView total_price, id, location, date;
    private CheckBox checkbox;
    private ImageView xCheckbox;

    private SharedPreferences locationPref;

    private double price;
    private ProductOrderAdapter adapter;

    private Order order;

    private boolean statusPaid = false;


    //url to update order status
    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/update/";

    //ProgressDialog
    ProgressDialog pd;

    //variable to get rid of difference in DB order time and actual Dutch time
    private long dateInMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_overview_detail);

        locationPref = getSharedPreferences(LoginActivity.PREFERENCELOCATION, Context.MODE_PRIVATE);

        //get orderNumber that has either been received by OrderOverviewActivity or from NFC scan
        order = (Order) getIntent().getSerializableExtra("order");
        dateInMillis = getIntent().getLongExtra("dateInMillis", 0);

        Log.i("DATE IN LONG", dateInMillis + "");

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(OrderOverviewDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        //initialise views
        listview = (ListView) findViewById(R.id.order_detailed_list);
        total_price = (TextView) findViewById(R.id.order_price_detailed);
        id = (TextView) findViewById(R.id.order_number_detailed);
        location = (TextView) findViewById(R.id.order_location_detailed);
        date = (TextView) findViewById(R.id.order_date_detailed);
        checkbox = (CheckBox) findViewById(R.id.status_checkbox);
        xCheckbox = (ImageView) findViewById(R.id.status_imageview);


        //initialise productlist & fill it with data from DB
        productList = new ArrayList<>();
        getOrder(order.getOrderNumber());

        LayoutInflater inflater = LayoutInflater.from(this);
        adapter = new ProductOrderAdapter(this, inflater, productList);
        listview.setAdapter(adapter);

        //show progress dialog
        pd = new ProgressDialog(this);
        pd.setMessage("Bestelling ophalen...");
        pd.show();

        //set initial textview values
        total_price.setText("");
        id.setText("");
        location.setText("");
        date.setText("");
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
    
    private void getOrder(int orderNumber) {
        //get order data from DB
        String URL = "https://easypayserver.herokuapp.com/api/bestelling/" + orderNumber;
        new EasyPayAPIGETOrderConnector(this).execute(URL);
    }

    @Override
    public void onProductAvailable(Product product) {
        price = 0;

        productList.add(product);

        for (int i = 0; i < productList.size(); i++) {
            price = price + productList.get(i).getProductPrice();
        }

        total_price.setText("Totaalprijs €" + String.format("%.2f", price));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onOrdersAvailable(Order order) {
        //Stop loading screen
        pd.cancel();

        Log.i("DetailDateFORMAT", order.getDate() + "");
        id.setText("Bestelnummer #" + order.getOrderNumber() + "");
        location.setText(locationPref.getString(order.getLocation(), "Geen locatie"));
        date.setText(formatDateFromMillis(dateInMillis));
        //check order status, show adequate view (x/unchecked checkmark/checked checkmark)
        checkStatusForCheckbox(order.getStatus());

        this.order = order;

        //get all products from this order from DB
        for (int i = 0; i < order.getProductsIDs().size(); i++) {
            String[] URL = {
                    "https://easypayserver.herokuapp.com/api/product/" + order.getProductsIDs().get(i)
            };
            new EasyPayAPIConnector(this).execute(URL);
        }
    }


    public String formatDateFromMillis(long dateInMillis) {
        //convert long dateInMillis back to Date
        Date date = new Date(dateInMillis);

        //format the date
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        return sdf.format(date);
    }

    public void checkStatusForCheckbox(String status) {
        switch (status) {
            case "PAID":
                checkbox.setChecked(true);
                checkbox.setVisibility(View.VISIBLE);
                break;
            case "WAITING":
                checkbox.setVisibility(View.VISIBLE);
                checkbox.setChecked(false);
                break;
            default:
                xCheckbox.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void updateCustomerBalance() {
        if (!statusPaid) {
            //update database, so that the customer balance is updated
            String paymentURL = "https://easypayserver.herokuapp.com/api/klant/afrekening/" +
                    order.getCustomerId() + "/" + price * 100;
            new EasyPayAPIPUTConnector().execute(paymentURL);
            Log.i(TAG, paymentURL);
            Log.i(TAG, order.toString());
            statusPaid = true;
        }
    }
}
