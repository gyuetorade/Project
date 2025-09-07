package com.example.project;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListOfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offer);

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Sample offers data
        List<Offer> offers = new ArrayList<>();
        offers.add(new Offer("20% OFF", "Get 20% off on all spicy chicken orders", "Valid until: Dec 31, 2024"));
        offers.add(new Offer("FREE Delivery", "Free delivery on orders above N5,000", "Valid until: Ongoing"));
        offers.add(new Offer("Buy 1 Get 1", "Buy one veggie tomato mix, get one free", "Valid until: Jan 15, 2025"));

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.offersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    // Offer model class
    public static class Offer {
        public String title;
        public String description;
        public String validity;

        public Offer(String title, String description, String validity) {
            this.title = title;
            this.description = description;
            this.validity = validity;
        }
    }
}