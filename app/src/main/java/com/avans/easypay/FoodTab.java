package com.avans.easypay;

/**
 * Created by Sander on 5/2/2017.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodTab extends Fragment implements EasyPayAPIConnector.OnProductAvailable {
    private ArrayList<Product> productList;
    private ListView listview_food;
    private TextView amount_products, total_price;
    private ArrayList<ArrayList<Product>> products;
    private ProductsTotal.OnTotalChanged totalListener = null;
    private FoodAdapter food_adapter;

//    public void setTotalListener(ProductsTotal.OnTotalChanged totalListener){
//        this.totalListener = totalListener;
//    }

    public void onCreate(Bundle savedInstanceState, LayoutInflater inflater) {


    }

    public void setFoodAdapter(FoodAdapter adapter) {
        this.food_adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productList = new ArrayList<Product>();
        View rootView = inflater.inflate(R.layout.fragment_tab_food, container, false);
        getProductItems();
        createTestProducts();
        //ProductAdapter adapter = TabbedActivity.adapter
        //FoodAdapter adapter = new FoodAdapter(this.getActivity(), inflater, productList);
        amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        total_price = (TextView) rootView.findViewById(R.id.subtotal);
        listview_food = (ListView) rootView.findViewById(R.id.foodListView);

        getProductItems();
        FoodAdapter adapter = new FoodAdapter(totalListener, this.getActivity(), inflater, productList);
        listview_food.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProductAvailable(Product product) {
        Log.i("", "ProductAvailable: " + product);
        productList.add(product);
        Log.i("", "onProductAvailable: " + productList);
        food_adapter.notifyDataSetChanged();
    }

    public void getProductItems() {
        String[] URL = {
                "https://easypayserver.herokuapp.com/api/product/food"
        };

        new EasyPayAPIConnector(this).execute(URL);
    }
        private void createTestProducts() {
        for (int i = 0; i < 1; i++) {
            Product product = new Product("prod"+i,10.4 ,i);
            productList.add(product);
        }
    }

}
