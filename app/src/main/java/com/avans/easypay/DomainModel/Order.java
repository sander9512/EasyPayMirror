package com.avans.easypay.DomainModel;

import java.io.Serializable;

/**
 * Created by Omidleet on 17/05/2017.
 */

public class Order implements Serializable {
    private int orderId;
    private int date;
    private String location;
    private String purchasedProduct;
    private int amount;
    private double price;

    public Order(int orderId, int date, String location, String purchasedProduct, int amount, double price) {
        this.orderId = orderId;
        this.date = date;
        this.location = location;
        this.purchasedProduct = purchasedProduct;
        this.amount = amount;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAmount() {
        return ""+amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return ""+orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return ""+date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPurchasedProduct() {
        return purchasedProduct;
    }

    public void setPurchasedProduct(String purchasedProduct) {
        this.purchasedProduct = purchasedProduct;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", date=" + date +
                ", location='" + location +
                ", purchasedProduct=" + purchasedProduct +
                ", amount =" + amount +
                ", price = " + price +
                '}';
    }
}
