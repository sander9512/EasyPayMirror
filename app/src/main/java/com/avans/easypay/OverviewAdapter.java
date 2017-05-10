package com.avans.easypay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Omidleet on 08/05/2017.
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

    public OverviewAdapter(Context applicationContext, String[] orders) {

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
            convertView = layoutInflater.inflate(R.layout.listview_overview_row, null);

            viewHolder = new OverviewAdapter.ViewHolder();
;
            viewHolder.productName = (TextView) convertView.findViewById(R.id.product_name);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
            viewHolder.productAmount = (TextView) convertView.findViewById(R.id.product_amount);
            viewHolder.priceTotal = (TextView) convertView.findViewById(R.id.price_total);
            viewHolder.orderId = (TextView) convertView.findViewById(R.id.order_id);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OverviewAdapter.ViewHolder) convertView.getTag();
        }
        Product p = (Product) productsList.get(position);

        viewHolder.productName.setText("Product Name");
        viewHolder.productPrice.setText("€1,00");
        viewHolder.productAmount.setText("2x");
        viewHolder.priceTotal.setText("€2,00");
        viewHolder.orderId.setText("7");

        return convertView;

    }

    public class ViewHolder {
        private TextView productName, productPrice, productAmount, priceTotal, orderId;
    }

}
