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

public class FoodTab extends Fragment implements ProductTask.OnProductAvailable {
    private ArrayList<Product> foodList;
    ListView listview_food;
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
        foodList = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_tab_food, container, false);
        startProductConnectionTask(locationID);
        listview_food = (ListView) rootView.findViewById(R.id.foodListView);

        adapter = new ProductAdapter(totalListener, this.getActivity(), inflater, foodList);
        listview_food.setAdapter(adapter);

        return rootView;
    }


    public void startProductConnectionTask(int locationID) {
        new ProductTask(this).execute("http://easypayserver.herokuapp.com/api/product/view/Eten/" + locationID);
    }

    @Override
    public void onProductAvailable(Product product){
        this.foodList.add(product);
        System.out.println("productslist" + foodList.toString());
        adapter.notifyDataSetChanged();
    }

}
