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

public class DrinksTab extends Fragment implements AssortmentLocationTask.OnProductIdAvailable, ProductTask.OnProductsAvailable {
    private ArrayList<Product> drinksList;

    ListView listview_drinks;
    private ArrayList<ArrayList<Product>> products;
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
        startAssortmentConnectionTask(4);
        //gebruik onderstaande AssortmentTask zodra DB assortiment aangevuld is.
        //startAssortmentConnectionTask(locationID);
        TextView amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        TextView total_price = (TextView) rootView.findViewById(R.id.subtotal);
        listview_drinks = (ListView) rootView.findViewById(R.id.drinksListView);

        //amount_products = (TextView) rootView.findViewById(R.id.products_amount_textview);
        //total_price = (TextView) rootView.findViewById(R.id.subtotaal);



        adapter = new ProductAdapter(totalListener, this.getActivity(), inflater, drinksList);
        listview_drinks.setAdapter(adapter);

        return rootView;
    }
    private void startAssortmentConnectionTask(int lid) {
        new AssortmentLocationTask(this).execute("https://easypayserver.herokuapp.com/api/assortiment/location/"+lid);
    }

    @Override
    public void onProductIdAvailable(ArrayList<Integer> productIds) {
        startProductConnectionTask(productIds,"drank");
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
        this.drinksList.add(product);
        adapter.notifyDataSetChanged();
    }
}

