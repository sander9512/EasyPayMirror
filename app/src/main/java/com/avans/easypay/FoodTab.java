package com.avans.easypay;

/**
 * Created by Sander on 5/2/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;

public class FoodTab extends Fragment implements EasyPayAPIConnector.OnProductAvailable {
    private ArrayList<Product> foodList;
    private ListView listview_food;
    private ArrayList<ArrayList<Product>> products;
    private ProductsTotal.OnTotalChanged totalListener = null;
    private ProductAdapter adapter;

    public void setTotalListener(ProductsTotal.OnTotalChanged totalListener){
        this.totalListener = totalListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        foodList = new ArrayList<Product>();
        View rootView = inflater.inflate(R.layout.fragment_tab_food, container, false);
        //productList = new ArrayList<>();
        //createTestProducts();
        //ProductAdapter adapter = TabbedActivity.adapter
        //ProductAdapter adapter = new ProductAdapter(this.getActivity(), inflater, productList);
        //amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        //total_price = (TextView) rootView.findViewById(R.id.subtotaal);
        getProductItems();
        TextView amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        TextView total_price = (TextView) rootView.findViewById(R.id.subtotal);
        listview_food = (ListView) rootView.findViewById(R.id.foodListView);

        adapter = new ProductAdapter(totalListener, this.getActivity(), inflater, foodList);
        listview_food.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProductAvailable(Product product) {
        Log.i("", "ProductAvailable: " + product);
        foodList.add(product);
        Log.i("", "onProductAvailable: " + foodList);
        adapter.notifyDataSetChanged();
    }

    public void getProductItems() {
        String[] URL = {
                "https://easypayserver.herokuapp.com/api/product/food"
                //bij andere locaties zal er iets met de endpoint moeten worden aangepast: "link/api/product/" + tabname
        };

        new EasyPayAPIConnector(this).execute(URL);
    }
}
