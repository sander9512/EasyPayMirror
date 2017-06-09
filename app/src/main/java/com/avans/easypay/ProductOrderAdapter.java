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

import java.util.ArrayList;

public class ProductOrderAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> productsList;
    private ArrayList<ArrayList<Product>> products = new ArrayList<>();

    public ProductOrderAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> productsList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.productsList = productsList;

    System.out.println("prodList size: "+productsList.size());
        for (int i = 0; i < productsList.size(); i++) {

            products.add(new ArrayList<Product>());
        }
    }

    @Override
    public int getCount() {
        return productsList.size();
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
        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_product_order_row, null);

            viewHolder = new ViewHolder();
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.product_image);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.product_name);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.product_price);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //placeholder code
        final Product p = productsList.get(position);

        //Picasso.with(getContext()).load(p.getFullImageUrl()).into(viewHolder.productImage);
        Picasso.with(convertView.getContext()).load(p.getFullImageUrl()).into(viewHolder.productImage);
        String price = "€" + String.format("%.2f", p.getProductPrice());
        price = price.replace(".", ",");
            viewHolder.productName.setText(p.getProductName());
            viewHolder.productPrice.setText(price);

        return convertView;
    }

    private static class ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice;
    }
}


