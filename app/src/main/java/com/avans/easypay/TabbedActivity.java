package com.avans.easypay;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TabbedActivity extends AppCompatActivity implements ProductsTotal.OnTotalChanged, EasyPayAPIConnector.OnProductAvailable{

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
    private ArrayList<ArrayList<Product>> products;
    private ArrayList<Product> productList;
    protected static ProductAdapter adapter;
    private final ProductsTotal.OnTotalChanged totalListener = this;
    private ProductAdapter product_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        productList = new ArrayList<>();

        getProductItems();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //adapter = new ProductAdapter(this, getApplicationContext(), getLayoutInflater(), products);

        totalPriceView = (TextView) findViewById(R.id.subtotal);
        totalProductsView = (TextView) findViewById(R.id.products_amount_textview);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_bar_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_dining_white_24dp);
        product_adapter = new ProductAdapter(this, getApplicationContext(), getLayoutInflater(), productList);


    }

    @Override
    public void onProductAvailable(Product product) {
        productList.add(product);
        product_adapter.notifyDataSetChanged();
    }

    public void getProductItems() {
        String[] URL = {
                "https://easypayserver.herokuapp.com/api/product"
        };

        new EasyPayAPIConnector(this).execute(URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTotalChanged(String priceTotal, String total, ArrayList<ArrayList<Product>> products) {
        totalProductsView.setText(total);
        totalPriceView.setText(priceTotal);

        this.products = products;

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Laden van de juiste klasse als je op een tab klikt
         switch(position) {
             case 0:
                 DrinksTab tab1 = new DrinksTab();
                 //tab1.setTotalListener(totalListener);
                 tab1.setProductAdapter(product_adapter);
                 return tab1;
             case 1:
                 FoodTab tab2 = new FoodTab();
                 //tab2.setTotalListener(totalListener);
                 tab2.setProductAdapter(product_adapter);
                 return tab2;
             default:
                 return null;

         }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Drinken";
                case 1:
                    return "Eten";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    public void overviewCurrentOrderBtn(View v) {
        Intent i = new Intent(this, OverviewCurrentOrdersActivity.class);
        startActivity(i);
    }
}
