package com.avans.easypay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.avans.easypay.ASyncTasks.AssortmentLocationTask;
import com.avans.easypay.ASyncTasks.ProductTask;
import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;

public class SodaTab extends Fragment implements AssortmentLocationTask.OnProductIdAvailable, ProductTask.OnProductsAvailable {
    private ArrayList<Product> sodaList;
    ListView listview_soda;
    private ArrayList<ArrayList<Product>> products;
    private ProductsTotal.OnTotalChangedHash totalListener = null;
    private ProductAdapter adapter;

    public void setTotalListener(ProductsTotal.OnTotalChangedHash totalListener){
        this.totalListener = totalListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sodaList = new ArrayList<Product>();
        View rootView = inflater.inflate(R.layout.fragment_tab_soda, container, false);
        
        //********ENABLE THIS IF THE PRODUCTS ARE IN THE DATABASE****
//        startAssortmentConnectionTask(44);
        //***********************************************************
        listview_soda = (ListView) rootView.findViewById(R.id.sodaListView);

        adapter = new ProductAdapter(totalListener, this.getActivity(), inflater, sodaList);
        listview_soda.setAdapter(adapter);

        return rootView;
    }

    private void startAssortmentConnectionTask(int lid) {
        new AssortmentLocationTask(this).execute("https://easypayserver.herokuapp.com/api/assortiment/location/"+lid);
    }

    @Override
    public void onProductIdAvailable(ArrayList<Integer> productIds) {
        startProductConnectionTask(productIds,"frisdrank");
    }

    //start ProductConnectionTask (AsyncTask)
    private void startProductConnectionTask(ArrayList<Integer> pids, String category){
        Log.d("Size",""+pids.size());
        for(int i = 0; i < pids.size(); i++){
            new ProductTask(this).execute("http://easypayserver.herokuapp.com/api/product/"+pids.get(i)+"/"+category);
        }
    }

    @Override
    public void onProductsAvailable(Product product){
        this.sodaList.add(product);
        adapter.notifyDataSetChanged();
    }
}
