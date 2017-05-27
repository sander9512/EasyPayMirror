package com.avans.easypay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Gabrielle on 23-05-17.
 */

public class CurrentOrderAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> currentList;
    public CurrentOrderAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> orderList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.currentList = orderList;


    } 

    @Override
    public int getCount() {
        return currentList.size();
    }

    @Override
    public Product getItem(int position) {
        return this.currentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_order_row, null);

            viewHolder = new ViewHolder();
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.product_order_image);
            viewHolder.purchasedProductName = (TextView) convertView.findViewById(R.id.product_order_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.product_order_price);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.product_amount_order);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Product product = currentList.get(position);

        Picasso.with(convertView.getContext()).load(product.getFullImageUrl()).into(viewHolder.productImage);
        DecimalFormat df = new DecimalFormat("0.00##");
        String price = "€" + df.format(product.getProductPrice());
        price = price.replace(".", ",");
        viewHolder.purchasedProductName.setText(product.getProductName());
        viewHolder.amount.setText(String.valueOf(product.getAmount()));
        viewHolder.price.setText(price);


        return convertView;
    }


    private static class ViewHolder {
        private TextView purchasedProductName, price;
        private TextView amount;
        private ImageView productImage;

}
}