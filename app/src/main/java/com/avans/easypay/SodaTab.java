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

public class SodaTab extends Fragment implements ProductTask.OnProductAvailable {
    private ArrayList<Product> sodaList;
    ListView listview_soda;
    private ProductsTotal.OnTotalChangedHash totalListener = null;
    private ProductAdapter adapter;
    private int locationID;

    public void setTotalListener(ProductsTotal.OnTotalChangedHash totalListener){
        this.totalListener = totalListener;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sodaList = new ArrayList<Product>();
        View rootView = inflater.inflate(R.layout.fragment_tab_soda, container, false);
        startProductConnectionTask(locationID);
        Log.i("Locatie", " " + locationID);
        listview_soda = (ListView) rootView.findViewById(R.id.sodaListView);

        adapter = new ProductAdapter(totalListener, this.getActivity(), inflater, sodaList);
        listview_soda.setAdapter(adapter);

        return rootView;
    }

    public void startProductConnectionTask(int locationID) {
        new ProductTask(this).execute("http://easypayserver.herokuapp.com/api/product/view/Frisdrank/" + locationID);
    }

    @Override
    public void onProductAvailable(Product product){
        this.sodaList.add(product);
        System.out.println("productslist" + sodaList.toString());
        adapter.notifyDataSetChanged();
    }
}
