package com.avans.easypay.DomainModel;

import java.io.Serializable;

/**
 * Created by Sander on 5/2/2017.
 */

public class Product implements Serializable {
    private String productName;
    private double productPrice;
    private int productId;
    private int amount;

    public Product(String productName, double productPrice, int productId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
    }

    public Product(String productName, double productPrice, int amount, int productId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productId=" + productId +
                ", amount=" + amount +
                '}';
    }

    public String getFullImageUrl() {
        String url = "https://raw.githubusercontent.com/bartaveld/EasyPayImages/master/" + productId + ".png";
        return url;
    }
}
