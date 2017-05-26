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
            viewHolder.purchasedProduct = (TextView) convertView.findViewById(R.id.product_order_name);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.order_amount);
            viewHolder.price = (TextView) convertView.findViewById(R.id.product_order_price);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Product product = currentList.get(position);
        viewHolder.productImage.setImageResource(R.drawable.ic_local_dining_black_24dp);
        //vervang bovenstaande placeholder met Picasso wanneer dit werkt met API
        //Picasso.with(convertView.getContext()).load(product.getFullImageUrl()).into(viewHolder.productImage);
        DecimalFormat df = new DecimalFormat("0.00##");
        String price = "€" + df.format(product.getProductPrice());
        price = price.replace(".", ",");
        viewHolder.purchasedProduct.setText(product.getProductName());
        viewHolder.price.setText(price);
       // viewHolder.amount.setText(product.getAmount());

        return convertView;
    }


    private static class ViewHolder {
        private TextView purchasedProduct, amount, price;
        private ImageView productImage;

}
}