package com.example.project;

public class OrderItem {
    public final int imageRes;
    public final String name;
    public final int price;
    public int quantity = 1;

    public OrderItem(int imageRes, String name, int price) {
        this.imageRes = imageRes;
        this.name = name;
        this.price = price;
    }
}