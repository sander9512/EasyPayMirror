package com.avans.easypay;

import android.media.Image;

/**
 * Created by Sander on 5/2/2017.
 */

public class Product {
    private String productName, imageUrl;
    private double productPrice;
    private int amount;

    public Product(String productName, String imageUrl, double productPrice) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.productPrice = productPrice;
    }

    //Default constructor
    public Product() {

    }
    public Product(String productName, String imageUrl, double productPrice, int amount) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.productPrice = productPrice;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName  +
                ", imageUrl='" + imageUrl  +
                ", productPrice=" + productPrice +
                '}';
    }
}
