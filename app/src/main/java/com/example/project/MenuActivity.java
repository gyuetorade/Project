// chicha/MenuActivity.java
package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class MenuActivity extends AppCompatActivity {

    SharedPreferences cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        cart = getSharedPreferences("Cart", MODE_PRIVATE);

        findViewById(R.id.btnAddBurger).setOnClickListener(v -> addToCart("Burger", 120));
        findViewById(R.id.btnAddFries).setOnClickListener(v -> addToCart("Fries", 60));
        findViewById(R.id.btnAddMilkTea).setOnClickListener(v -> addToCart("Milk Tea", 90));

        Button btnViewCart = findViewById(R.id.btnViewCart);
        btnViewCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }

    private void addToCart(String item, int price) {
        int qty = cart.getInt(item, 0);
        cart.edit().putInt(item, qty + 1).apply();

        int total = cart.getInt("TOTAL", 0);
        cart.edit().putInt("TOTAL", total + price).apply();
    }
}
