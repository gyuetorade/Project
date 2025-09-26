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

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        cart = getSharedPreferences("Cart", MODE_PRIVATE);
        }

    private void addToCart(String item, int price, int imageRes) {
        int qty = cart.getInt(item + "_qty", 0) + 1;
        java.util.Set<String> items = new java.util.HashSet<>(cart.getStringSet("ITEMS", new java.util.HashSet<>()));
        items.add(item);

        cart.edit()
                .putStringSet("ITEMS", items)
                .putInt(item + "_qty", qty)
                .putInt(item + "_price", price)
                .putInt(item + "_image", imageRes)
                .putInt("TOTAL", cart.getInt("TOTAL", 0) + price)
                .apply();
    }
}
