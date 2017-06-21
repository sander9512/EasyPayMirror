package com.avans.easypay;

import android.content.Context;
import android.util.Log;

import com.avans.easypay.DomainModel.Product;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by Sander on 21-6-2017.
 */
public class ProductsTotalTest  {
    ArrayList<Product> list = new ArrayList<>();
    HashSet<Product> hashList = new HashSet<>();
    ProductsTotal total, total2;

    public ProductsTotalTest() {
        for (int i = 0; i < 20; i++) {
            list.add(new Product("Test Product " + i, 2.50, 1, i));
            hashList.add(new Product("Test Product " + i, 1.50, 2, i));
        }
        total = new ProductsTotal(list);
        total2 = new ProductsTotal(hashList);
    }

    @Test
    public void getPriceTotalDouble() throws Exception {
        double output = 0;
        double delta = 0.1;
        double expected = 50.00;
        output = total.getPriceTotalDouble();
        assertEquals(expected, output, delta);
    }

    @Test
    public void getPriceTotal() throws Exception {
       String output = total.getPriceTotal();
        String expected = "Subtotaal: €" + "50,00";
        assertEquals(expected, output);
    }

    @Test
    public void getTotal() throws Exception {
        String output = total.getTotal();
        String expected = 20 + " Producten";
        assertEquals(expected, output);

    }

    @Test
    public void getPriceTotalHashSet() throws Exception {
        double output = total2.getPriceTotalHashSet();
        double expected = 60.0;
        double delta = 0.1;
        assertEquals(expected, output, delta);


    }

    @Test
    public void getTotalHashSet() throws Exception {
        int output = total2.getTotalHashSet();
        int expected = 40;
        assertEquals(expected, output);

    }

    @Test
    public void combineLists() throws Exception {
        ArrayList<Product> list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product p = new Product("Nieuw Product " + i, 1.50, 1, i);
            list2.add(p);
        }
        list.addAll(list2);

        int expected = 30;
        int output = list.size();
        assertEquals(expected, output);
    }

}