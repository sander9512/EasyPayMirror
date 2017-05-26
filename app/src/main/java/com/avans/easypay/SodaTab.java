package com.avans.easypay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SodaTab extends Fragment implements EasyPayAPIConnector.OnProductAvailable {
    private ArrayList<Product> sodaList;
    private ListView listview_soda;
    private ArrayList<ArrayList<Product>> products;
    private ProductsTotal.OnTotalChanged totalListener = null;
    private ProductAdapter adapter;

    public void setTotalListener(ProductsTotal.OnTotalChanged totalListener){
        this.totalListener = totalListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sodaList = new ArrayList<Product>();
        View rootView = inflater.inflate(R.layout.fragment_tab_soda, container, false);
        getProductItems();
//        TextView amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
//        TextView total_price = (TextView) rootView.findViewById(R.id.subtotal);
        listview_soda = (ListView) rootView.findViewById(R.id.sodaListView);

        adapter = new ProductAdapter(totalListener, this.getActivity(), inflater, sodaList);
        listview_soda.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProductAvailable(Product product) {
        Log.i("", "ProductAvailable: " + product);
        sodaList.add(product);
        Log.i("", "onProductAvailable: " + sodaList);
        adapter.notifyDataSetChanged();
    }

    public void getProductItems() {
        String[] URL = {
                "https://easypayserver.herokuapp.com/api/product/frisdrank"
                //bij andere locaties zal er iets met de endpoint moeten worden aangepast: "link/api/product/" + tabname
        };

        new EasyPayAPIConnector(this).execute(URL);
    }
}
