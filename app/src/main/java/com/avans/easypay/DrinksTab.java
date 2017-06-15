package com.avans.easypay;

/**
 * Created by Sander on 5/2/2017.
 * And me! aka TB. on 6/9/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.avans.easypay.ASyncTasks.AssortmentLocationTask;
import com.avans.easypay.ASyncTasks.ProductTask;
import com.avans.easypay.DomainModel.Product;

import java.util.ArrayList;

public class DrinksTab extends Fragment implements ProductTask.OnProductAvailable {
    private ArrayList<Product> drinksList;

    ListView listview_drinks;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        drinksList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_tab_drinks, container, false);
        startProductConnectionTask(locationID);
        listview_drinks = (ListView) rootView.findViewById(R.id.drinksListView);

        adapter = new ProductAdapter(totalListener, this.getActivity(), inflater, drinksList);
        listview_drinks.setAdapter(adapter);

        return rootView;
    }


    public void startProductConnectionTask(int locationID) {
        new ProductTask(this).execute("http://easypayserver.herokuapp.com/api/product/view/Drank/" + locationID);
    }

    @Override
    public void onProductAvailable(Product product){
        this.drinksList.add(product);
        System.out.println("productslist" + drinksList.toString());
        adapter.notifyDataSetChanged();
    }
}

