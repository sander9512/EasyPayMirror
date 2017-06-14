package com.avans.easypay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.avans.easypay.LocationActivity.ORDER;

public class TabbedActivity extends AppCompatActivity implements ProductsTotal.OnTotalChangedHash {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private DAOFactory factory;
    private BalanceDAO balanceDAO;
    private ImageView home;
    private ViewPager mViewPager;
    private TextView totalProductsView, totalPriceView, category;
    protected static ArrayList<Product> mergedProducts;
    public static final String PRODUCTS = "products";
    private final ProductsTotal.OnTotalChangedHash totalListener = this;
    private Order order;
    private HashSet<Product> hashSet = new HashSet<>();
    private HashSet<Product> mergedHashSet = new HashSet<>();
    protected static double orderTotalPrice = 0;
    private int locationID = 0;
    private SharedPreferences locationPref;
    public static final String PREFERENCELOCATION = "LOCATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TabbedActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        factory = new SQLiteDAOFactory(getApplicationContext());
        balanceDAO = factory.createBalanceDAO();

        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.get(ORDER);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mergedProducts = new ArrayList<>();

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        //adapter = new ProductAdapter(this, getApplicationContext(), getLayoutInflater(), products);

        //totalPriceView = (TextView) findViewById(R.id.subtotaal);
        totalPriceView = (TextView) findViewById(R.id.subtotal);

        totalProductsView = (TextView) findViewById(R.id.products_amount_textview);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_bar_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_dining_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_local_drink_white_24dp);
        locationPref = getSharedPreferences(PREFERENCELOCATION, Context.MODE_PRIVATE);
        //ProductAdapter product_adapter = new ProductAdapter(this, getApplicationContext(), getLayoutInflater(), productList);
        locationID = getLocationID();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Setting balance in toolbar
        if (balanceDAO.selectData().size() == 0) {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        int totalProducts = 0;
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
            totalProducts += product.getAmount();
            totalPrice += product.getProductPrice() * product.getAmount();
        }
        DecimalFormat df = new DecimalFormat("0.00##");
        totalProductsView.setText("Aantal items: " + totalProducts);
        totalPriceView.setText("€" + df.format(totalPrice));
        orderTotalPrice = totalPrice;



    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    DrinksTab tab1 = new DrinksTab();
                    tab1.setTotalListener(totalListener);
                    tab1.setLocationID(locationID);
                    //tab1.setProductAdapter(product_adapter);
                    return tab1;
                case 1:
                    FoodTab tab2 = new FoodTab();
                    tab2.setTotalListener(totalListener);
                    tab2.setLocationID(locationID);
                    //tab2.setFoodAdapter(food_adapter);
                    return tab2;
                case 2:
                    SodaTab tab3 = new SodaTab();
                    tab3.setTotalListener(totalListener);
                    tab3.setLocationID(locationID);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Drinken";
                case 1:
                    return "Eten";
                case 2:
                    return "Frisdrank";
            }
            return null;
        }
    }

    public void overviewCurrentOrderBtn(View v) {
        Intent i = new Intent(this, OverviewCurrentOrdersActivity.class);
        order.setHashProducts(mergedHashSet);
        i.putExtra(PRODUCTS, order);
        if (mergedHashSet.isEmpty()) {
            Toasty.error(this, "Selecteer product(en).", Toast.LENGTH_SHORT).show();
        } else {
            if (balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).getAmount() >= orderTotalPrice) {
                startActivity(i);
            } else {
                Toasty.error(this, "Niet voldoende saldo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
