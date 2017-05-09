package com.avans.easypay;

/**
 * Created by Sander on 5/2/2017.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrinksTab extends Fragment {
    private ArrayList<ArrayList<Product>> product;
    private ArrayList<Product> productList;
    private ListView listview_drinks;
    private TextView amount_products, total_price;
    private ArrayList<ArrayList<Product>> products;
    //private ProductsTotal.OnTotalChanged totalListener = null;
    private ProductAdapter adapter;

//    public void setTotalListener(ProductsTotal.OnTotalChanged totalListener){
//        this.totalListener = totalListener;
//    }
    public void setProductAdapter(ProductAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_drinks, container, false);
//        productList = new ArrayList<>();
//        createTestProducts();
        //ProductAdapter adapter = TabbedActivity.adapter;
        //ProductAdapter adapter = new ProductAdapter(totalListener, this.getActivity(),inflater,productList);
        listview_drinks = (ListView) rootView.findViewById(R.id.drinksListView);
        amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        total_price = (TextView) rootView.findViewById(R.id.subtotal);

        listview_drinks.setAdapter(adapter);

        return rootView;
    }


//    private void createTestProducts() {
//        for (int i = 0; i < 20; i++) {
//            Product product = new Product("prod" + i,"",i);
//            productList.add(product);
//        }
//    }

}
