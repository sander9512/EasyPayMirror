package com.avans.easypay;

import android.content.Context;

import com.avans.easypay.DomainModel.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Sander on 4-5-2017.
 */

public class ProductsTotal {
    private ArrayList<Product> products;
    private HashSet<Product> hashProducts;

    public ProductsTotal(Context context, ArrayList<Product> products) {

        this.products = products;

    }
    public ProductsTotal(Context context, HashSet<Product> hashProducts) {
        this.hashProducts = hashProducts;
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

    public double getPriceTotalHashSet() {

        double d = 0;

        //for (ArrayList<Product> specificProducts : products) {

        for (Product product : hashProducts) {

            Product p;

            p = product;
            int amount = p.getAmount();
            d += p.getProductPrice() * amount;
        }
        //}

        DecimalFormat df = new DecimalFormat("0.00##");

        return d;
    }

    public int getTotalHashSet() {

        int total = 0;

        //for (ArrayList<Product> specificProducts : products) {

        for (Product product : hashProducts) {
            Product p;
            p = product;

            int amount = p.getAmount();
            total += amount;
        }
        //}

        return total;
    }

    public ArrayList<Product> combineLists() {
        ArrayList<Product> mergedProducts = new ArrayList<>();

        mergedProducts.addAll(products);

        return mergedProducts;

    }

    public interface OnTotalChangedHash {

        void onTotalChangedHash(double priceTotal, int total, HashSet<Product> products);
    }
    public interface OnTotalChanged {

        void onTotalChanged(String priceTotal, String total, ArrayList<Product> products);
    }

}
