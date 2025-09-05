// chicha/CartActivity.java
package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class CartActivity extends AppCompatActivity {

    SharedPreferences cart;
    TextView tvCart;

    Button toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Button toolbar = findViewById(R.id.toolbar);

        toolbar.setOnClickListener(v -> {
                    Intent intent = new Intent(CartActivity.this, HomeFragment.class);
                    startActivity(intent);
        });

                cart = getSharedPreferences("Cart", MODE_PRIVATE);
        tvCart = findViewById(R.id.tvCartItems);
        Button btnCheckout = findViewById(R.id.btnCheckout);

        renderCart();

        btnCheckout.setOnClickListener(v -> {
            cart.edit().clear().apply();
            renderCart();
        });
    }

    private void renderCart() {
        int burger = cart.getInt("Burger", 0);
        int fries = cart.getInt("Fries", 0);
        int milktea = cart.getInt("Milk Tea", 0);
        int total = cart.getInt("TOTAL", 0);

        StringBuilder sb = new StringBuilder();
        if (burger > 0) sb.append("Burger x ").append(burger).append("\n");
        if (fries > 0) sb.append("Fries x ").append(fries).append("\n");
        if (milktea > 0) sb.append("Milk Tea x ").append(milktea).append("\n");
        sb.append("\nTotal: ₱").append(total);
        if (burger + fries + milktea == 0) sb = new StringBuilder("Your cart is empty.");

        tvCart.setText(sb.toString());
    }
}
