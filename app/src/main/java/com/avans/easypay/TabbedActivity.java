package com.avans.easypay;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;

import static com.avans.easypay.LocationActivity.ORDER;

public class TabbedActivity extends AppCompatActivity implements ProductsTotal.OnTotalChanged {

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
    private ViewPager mViewPager;
    private TextView totalProductsView, totalPriceView, category;
    private ArrayList<Product> products;
    protected static ArrayList<Product> mergedProducts;
    protected static ProductAdapter adapter;
    public static final String PRODUCTS = "products";
    private final ProductsTotal.OnTotalChanged totalListener = this;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.get(ORDER);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mergedProducts = new ArrayList<>();

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //adapter = new ProductAdapter(this, getApplicationContext(), getLayoutInflater(), products);

        //totalPriceView = (TextView) findViewById(R.id.subtotaal);
        totalPriceView = (TextView) findViewById(R.id.subtotal);

        totalProductsView = (TextView) findViewById(R.id.products_amount_textview);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_bar_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_dining_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_local_drink_white_24dp);
        //ProductAdapter product_adapter = new ProductAdapter(this, getApplicationContext(), getLayoutInflater(), productList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTotalChanged(String priceTotal, String total, ArrayList<Product> products) {
        totalProductsView.setText(total);
        totalPriceView.setText(priceTotal);
        mergedProducts.addAll(products);
        Log.i("mergedProducts", "" + mergedProducts.size());

        this.mergedProducts = products;


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

         switch(position) {
             case 0:
                 DrinksTab tab1 = new DrinksTab();
                 tab1.setTotalListener(totalListener);
                 //tab1.setProductAdapter(product_adapter);
                 return tab1;
             case 1:
                 FoodTab tab2 = new FoodTab();
                 tab2.setTotalListener(totalListener);
                 //tab2.setFoodAdapter(food_adapter);
                 return tab2;
             case 2:
                 SodaTab tab3 = new SodaTab();
                 tab3.setTotalListener(totalListener);
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
        order.setProducts(mergedProducts);
        i.putExtra(PRODUCTS, order);
        if (mergedProducts.isEmpty()) {
            Toast.makeText(this, "Selecteer product(en)", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(i);
        }
    }
}
