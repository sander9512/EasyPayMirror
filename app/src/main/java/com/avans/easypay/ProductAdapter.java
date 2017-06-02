package com.avans.easypay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.avans.easypay.DomainModel.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> productsList;
    private ArrayList<ArrayList<Product>> products = new ArrayList<>();
    private ArrayList<Product> chosenProducts = new ArrayList<>();

    private ProductsTotal.OnTotalChanged listener;

    private ProductsTotal total;

    public ProductAdapter(ProductsTotal.OnTotalChanged listener,Context context, LayoutInflater layoutInflater, ArrayList<Product> productsList) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.productsList = productsList;
        this.listener = listener;
    System.out.println("prodList size: "+productsList.size());
        for (int i = 0; i < productsList.size(); i++) {

            products.add(new ArrayList<Product>());
        }
        this.total = new ProductsTotal(context, chosenProducts);


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
            convertView = layoutInflater.inflate(R.layout.listview_product_row, null);

            viewHolder = new ViewHolder();
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.product_image);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.product_name);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
            viewHolder.productAmount = (TextView) convertView.findViewById(R.id.product_amount_row);
           // viewHolder.productSpinner = (Spinner) convertView.findViewById(R.id.product_spinner);
            viewHolder.removeBtn = (ImageButton) convertView.findViewById(R.id.product_remove_button);
            viewHolder.addBtn = (ImageButton) convertView.findViewById(R.id.product_add_button);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //placeholder code
         final Product p = productsList.get(position);

        //Picasso.with(getContext()).load(p.getFullImageUrl()).into(viewHolder.productImage);
        Picasso.with(convertView.getContext()).load(p.getFullImageUrl()).into(viewHolder.productImage);
        DecimalFormat df = new DecimalFormat("0.00##");
        String price = "€" + df.format(p.getProductPrice());
        price = price.replace(".", ",");
            viewHolder.productName.setText(p.getProductName());
            viewHolder.productPrice.setText(price);
            viewHolder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int amount = p.getAmount();
                    amount++;
                    p.setAmount(amount);
                    Log.i("ADDTEST", p.getProductName() +" " +  p.getAmount() +" " +  p.getProductId());
                    viewHolder.productAmount.setText(String.valueOf(p.getAmount()));
                    chosenProducts.add(p);
                    Log.i("chosenproducts: ", chosenProducts.toString());
                }
            });
            viewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int amountRemove = p.getAmount();
                    Log.i("AMOUNT", "" + p.getAmount());
                    if(p.getAmount() > 0) {
                        amountRemove--;
                        p.setAmount(amountRemove);
                        Log.i("REMOVETEST", p.getProductName() +" " +  p.getAmount() + " " +  p.getProductId());
                        chosenProducts.add(p);
                    }
                    if (p.getAmount() == 0) {
                        chosenProducts.remove(p);
                    }
                    viewHolder.productAmount.setText(String.valueOf(p.getAmount()));
                    Log.i("chosenproducts: ", chosenProducts.toString());
                }
            });


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
//                R.array.spinner_array, R.layout.spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        viewHolder.productSpinner.setAdapter(adapter);
//        viewHolder.productSpinner.setSelection(p.getAmount());
//        viewHolder.productSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position2, long id) {
//
//
//                int spinnerValue = Integer.parseInt(viewHolder.productSpinner.getSelectedItem().toString());
//                System.out.println(spinnerValue);
//                System.out.println("item selected " + id + " " + position2 + " " + spinnerValue + " " + viewHolder.productSpinner.getSelectedItem() + " " + view.getId() + " " + parent.getId() + " " + position);
//                //ArrayList<Product> chosenProducts = new ArrayList<Product>();
//
//                //for (int i = 0; i < spinnerValue; i++) {
//                if (spinnerValue > 0) {
//                    p.setAmount(spinnerValue);
//                    chosenProducts.add(p);
//                }
//                //need to save spinner values when scrolling/switching tabs
//                            else if (spinnerValue == 0 ) {
//                                p.setAmount(0);
//                                chosenProducts.remove(p);
//                            }
//                //}
//                //if(products.size() > position)
//                //products.set(position, chosenProducts);
//                System.out.println(" " + chosenProducts.size());
//                Log.i("TAG", "total products " + chosenProducts.size() + " " + products.size() + " " + total.getPriceTotal() + " " + total.getTotal());
                //listener.onTotalChanged(total.getPriceTotal(), total.getTotal(), chosenProducts);

//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
      //  });
        listener.onTotalChanged(total.getPriceTotal(), total.getTotal(), chosenProducts);
        return convertView;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.product_add_button:
//                amount++;
//                p.setAmount(amount);
//                Log.i("ADDTEST", p.getProductName() + p.getAmount());
//                break;
//
//            case R.id.product_remove_button:
//                amount--;
//                p.setAmount(amount);
//                Log.i("REMOVETEST", p.getProductName() + p.getAmount());
//                break;
//
//        }
//        listener.onTotalChanged(total.getPriceTotal(), total.getTotal(), chosenProducts);
//    }

    private static class ViewHolder {
       private ImageView productImage;
        private TextView productName, productPrice, productAmount;
        private Spinner productSpinner;
        private ImageButton removeBtn, addBtn;

    }
}


