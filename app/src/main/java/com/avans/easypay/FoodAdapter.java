package com.avans.easypay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> productList;
    private ArrayList<ArrayList<Product>> products = new ArrayList<>();
    private ProductsTotal.OnTotalChanged listener;
    private ProductsTotal total;

    public FoodAdapter(ProductsTotal.OnTotalChanged listener ,Context context, LayoutInflater layoutInflater, ArrayList<Product> productList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        int size = productList.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FoodAdapter.ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_food_row, null);

            viewHolder = new FoodAdapter.ViewHolder();
            viewHolder.foodImage = (ImageView) convertView.findViewById(R.id.food_image);
            viewHolder.foodName = (TextView) convertView.findViewById(R.id.food_name);
            viewHolder.foodPrice = (TextView) convertView.findViewById(R.id.food_price);
            viewHolder.foodSpinner = (Spinner) convertView.findViewById(R.id.food_spinner);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (FoodAdapter.ViewHolder) convertView.getTag();
        }
        //placeholder code
        final Product p = productList.get(position);

        //Picasso.with(getContext()).load(p.getFullImageUrl()).into(viewHolder.productImage);
        Picasso.with(convertView.getContext()).load(p.getFullImageUrl()).into(viewHolder.foodImage);

        double ProductPrice = (p.getProductPrice());
        viewHolder.foodName.setText(p.getProductName());
        viewHolder.foodPrice.setText("€" + ProductPrice);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.foodSpinner.setAdapter(adapter);
        viewHolder.foodSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {


                int spinnerValue = Integer.parseInt(viewHolder.foodSpinner.getSelectedItem().toString());

                ArrayList<Product> chosenProducts = new ArrayList<Product>();

                for (int i = 0; i < spinnerValue; i++) {

                    chosenProducts.add(p);

                }
                if(products.size() > position)
                    products.set(position, chosenProducts);
                //listener.onTotalChanged(total.getPriceTotal(), total.getTotal(), products);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private ImageView foodImage;
        private TextView foodName, foodPrice;
        private Spinner foodSpinner;

    }
}
