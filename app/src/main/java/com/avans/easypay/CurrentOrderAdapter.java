package com.avans.easypay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Gabrielle on 23-05-17.
 */

public class CurrentOrderAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Order> currentList;
    private ArrayList<ArrayList<Order>> orders;

    public CurrentOrderAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Order> orderList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.currentList = orderList;

        for (int i = 0; i < orderList.size(); i++) {
            orders.add(new ArrayList<Order>());
        }

    } 

    @Override
    public int getCount() {
        return currentList.size();
    }

    @Override
    public Order getItem(int position) {
        return this.currentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  Integer.parseInt(this.currentList.get(position).getOrderId());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CurrentOrderAdapter.ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_order_row, null);

            viewHolder = new CurrentOrderAdapter.ViewHolder();
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.product_image);
            viewHolder.purchasedProduct = (TextView) convertView.findViewById(R.id.Bestelling);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.aantal);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price_id);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (CurrentOrderAdapter.ViewHolder) convertView.getTag();
        }
        final Order o = currentList.get(position);
        double orderPrice = (o.getPrice());
        viewHolder.purchasedProduct.setText(o.getPurchasedProduct());
        viewHolder.price.setText("€ " + o.getPrice());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.orderSpinner.setAdapter(adapter);
        viewHolder.orderSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {


                int spinnerValue = Integer.parseInt(viewHolder.orderSpinner.getSelectedItem().toString());

                ArrayList<Order> orderChosen = new ArrayList<Order>();

                for (int i = 0; i < spinnerValue; i++) {
                    orderChosen.add(o);
                }
                if (orders.size() > position){
                    orders.set(position, orderChosen);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }


    private static class ViewHolder {
        private TextView date, location, purchasedProduct, amount, price;
        private Spinner orderSpinner;
        private ImageView productImage;

}
}