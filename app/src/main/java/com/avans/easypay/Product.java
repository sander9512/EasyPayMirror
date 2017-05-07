package com.avans.easypay;

import android.media.Image;

/**
 * Created by Sander on 5/2/2017.
 */

public class Product {
    private String productName, imageUrl;
    private double productPrice;

    public Product(String productName, String imageUrl, double productPrice) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.productPrice = productPrice;
    }
    //Default constructor
    public Product() {
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

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName  +
                ", imageUrl='" + imageUrl  +
                ", productPrice=" + productPrice +
                '}';
    }
}
