package com.example.project;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Sample order data
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("ORD001", "Spicy Chicken", "N1,900", "Processing"));
        orders.add(new Order("ORD002", "Veggie Tomato", "N1,900", "Delivered"));
        orders.add(new Order("ORD003", "Egg & Cucumber", "N1,900", "Cancelled"));

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.ordersRecyclerView);
        com.example.project.OrderAdapter adapter = new com.example.project.OrderAdapter(orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // Order model class
    public static class Order {
        public String orderId;
        public String itemName;
        public String price;
        public String status;

        public Order(String orderId, String itemName, String price, String status) {
            this.orderId = orderId;
            this.itemName = itemName;
            this.price = price;
            this.status = status;
        }
    }
}