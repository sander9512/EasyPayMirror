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

import java.util.ArrayList;

public class DrinksTab extends Fragment {
    private ArrayList<Product> products;
    private ListView listview_drinks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_drinks, container, false);
        products = new ArrayList<>();
        FrameLayout footerLayout = (FrameLayout) inflater.inflate(R.layout.footer_tabs,null);

        ProductAdapter adapter = new ProductAdapter(this.getActivity(),inflater,products);
        listview_drinks = (ListView) rootView.findViewById(R.id.drinksListView);
        listview_drinks.setAdapter(adapter);
        //listview_drinks.addFooterView(footerLayout);
        createTestProducts();
        return rootView;
    }
    private void createTestProducts() {
        for (int i = 0; i < 20; i++) {
            Product product = new Product("","",1);
            products.add(product);
        }
    }
}
