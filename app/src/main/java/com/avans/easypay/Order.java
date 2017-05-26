package com.avans.easypay;

import java.io.Serializable;
import java.util.ArrayList;

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
    private ArrayList<Product> orderedProducts;

    public Order(int orderId, int date, String location, String purchasedProduct, int amount, double price) {
        this.orderId = orderId;
        this.date = date;
        this.location = location;
        this.purchasedProduct = purchasedProduct;
        this.amount = amount;
        this.price = price;
    }
        // deze constructor moet gebruikt worden, een order heeft een lijst met producten
    public Order(int orderId, int date, String location, ArrayList<Product> orderedProducts) {
        this.orderId = orderId;
        this.date = date;
        this.location = location;
        this.orderedProducts = orderedProducts;
    }
    public Order() {}

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
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

    public ArrayList<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(ArrayList<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
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
