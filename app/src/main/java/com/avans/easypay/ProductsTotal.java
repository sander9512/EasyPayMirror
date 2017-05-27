package com.avans.easypay;

import android.content.Context;

import com.avans.easypay.DomainModel.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Sander on 4-5-2017.
 */

public class ProductsTotal {
    private ArrayList<Product> products;

    public ProductsTotal(Context context, ArrayList<Product> products) {

        this.products = products;

    }

    public String getPriceTotal() {

        double d = 0;

        //for (ArrayList<Product> specificProducts : products) {

        for (Product product : products) {

            Product p;

            p = product;
            int amount = p.getAmount();
            d += p.getProductPrice() * amount;
        }
        //}

        DecimalFormat df = new DecimalFormat("0.00##");

        return "Subtotaal: €" + df.format(d);
    }

    public String getTotal() {

        int total = 0;

        //for (ArrayList<Product> specificProducts : products) {

        for (Product product : products) {
            Product p;
            p = product;

            int amount = p.getAmount();
            total += amount;
        }
        //}

        return total + " Producten";
    }

//    public double getPriceTotalDouble() {
//
//        double d = 0;
//
//        for (ArrayList<Product> specificProducts : products) {
//
//            for (Product product : specificProducts) {
//
//                Product p;
//
//                p = product;
//                d += p.getProductPrice();
//            }
//        }
//
//        return d;
//    }

    public ArrayList<Product> combineLists() {
        ArrayList<Product> mergedProducts = new ArrayList<>();

        mergedProducts.addAll(products);

        return mergedProducts;

    }

    public interface OnTotalChanged {

        void onTotalChanged(String priceTotal, String total, ArrayList<Product> products);
    }

}
