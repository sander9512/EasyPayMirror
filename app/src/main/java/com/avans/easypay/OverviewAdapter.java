package com.avans.easypay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Omidleet on 10/05/2017.
 */

public class OverviewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> productsList;

    public OverviewAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> productsList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.productsList = productsList;
    }

    @Override
    public int getCount() {
        int size = productsList.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final OverviewAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_overview_row, null);

            viewHolder = new OverviewAdapter.ViewHolder();
            viewHolder.orderLocation = (TextView) convertView.findViewById(R.id.location);
            viewHolder.orderDate = (TextView) convertView.findViewById(R.id.date);
            viewHolder.orderId = (TextView) convertView.findViewById(R.id.order_id);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OverviewAdapter.ViewHolder) convertView.getTag();
        }
        Product p = (Product) productsList.get(position);

        viewHolder.orderLocation.setText("Liqueurpaleis");
        viewHolder.orderDate.setText("11/11/2020");
        viewHolder.orderId.setText("7");

        return convertView;

    }


    public class ViewHolder {
        private TextView orderLocation, orderDate, orderId;
    }

}
