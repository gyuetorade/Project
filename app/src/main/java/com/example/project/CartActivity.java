package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CartActivity extends AppCompatActivity {

    SharedPreferences cart, profile;
    LinearLayout cartContainer;
    TextView tvTotalAmount, tvName, tvAddress, tvPhone;
    RadioButton rbDoorDelivery, rbPickUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart = getSharedPreferences("Cart", MODE_PRIVATE);
        profile = getSharedPreferences("Profile", MODE_PRIVATE);

        cartContainer = findViewById(R.id.cartContainer);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        rbDoorDelivery = findViewById(R.id.rbDoorDelivery);
        rbPickUp = findViewById(R.id.rbPickUp);

        // Back button
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        // Load address
        loadAddress();

        // Change address
        findViewById(R.id.btnChangeAddress).setOnClickListener(v -> showChangeAddressDialog());

        // Build cart UI
        renderCart();

        // Proceed to payment
        findViewById(R.id.btnProceedPayment).setOnClickListener(v -> {
            String shipping = rbDoorDelivery.isChecked() ? "Door Delivery" : "Pick Up";
            Toast.makeText(this,
                    "Proceeding to payment\nShipping: " + shipping +
                            "\nTotal: " + tvTotalAmount.getText().toString(),
                    Toast.LENGTH_LONG).show();

            // TODO: Start payment activity or integrate payment gateway
            // startActivity(new Intent(this, PaymentActivity.class));
        });
    }

    private void loadAddress() {
        String name = profile.getString("name", "John Doe");
        String address = profile.getString("address", "123 Main Street, City, Country");
        String phone = profile.getString("phone", "+63 900 000 0000");

        tvName.setText(name);
        tvAddress.setText(address);
        tvPhone.setText(phone);
    }

    private void showChangeAddressDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_address, null);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);

        // Pre-fill with current values
        etName.setText(tvName.getText().toString());
        etAddress.setText(tvAddress.getText().toString());
        etPhone.setText(tvPhone.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Change Address")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    profile.edit()
                            .putString("name", etName.getText().toString())
                            .putString("address", etAddress.getText().toString())
                            .putString("phone", etPhone.getText().toString())
                            .apply();
                    loadAddress();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void renderCart() {
        cartContainer.removeAllViews();

        java.util.Set<String> items = cart.getStringSet("ITEMS", new java.util.HashSet<>());
        int total = 0;
        boolean hasItems = false;

        for (String name : items) {
            int qty = cart.getInt(name + "_qty", 0);
            int price = cart.getInt(name + "_price", 0);
            int image = cart.getInt(name + "_image", R.drawable.ic_food_placeholder);

            if (qty > 0) {
                hasItems = true;
                addCartCard(name, price, qty, image);
                total += price * qty;
            }
        }

        if (!hasItems) {
            TextView empty = new TextView(this);
            empty.setText("Your cart is empty.");
            empty.setTextSize(16f);
            cartContainer.addView(empty);
        }

        tvTotalAmount.setText("Total: ₱" + total);
        cart.edit().putInt("TOTAL", total).apply();
    }

    private void addCartCard(String name, int price, int qty, int imageRes) {
        CardView card = new CardView(this);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        card.setRadius(16f);
        card.setCardElevation(8f);
        card.setUseCompatPadding(true);
        card.setPadding(16, 16, 16, 16);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView img = new ImageView(this);
        img.setImageResource(imageRes);
        img.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(16, 0, 0, 0);
        info.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView tvNameItem = new TextView(this);
        tvNameItem.setText(name);
        tvNameItem.setTextSize(16f);
        tvNameItem.setTypeface(null, Typeface.BOLD);

        TextView tvPrice = new TextView(this);
        tvPrice.setText("₱" + (price * qty));
        tvPrice.setTextSize(14f);

        info.addView(tvNameItem);
        info.addView(tvPrice);

        LinearLayout qtyControl = new LinearLayout(this);
        qtyControl.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton btnMinus = new ImageButton(this);
        btnMinus.setImageResource(R.drawable.minus);
        btnMinus.setBackground(null);

        TextView tvQty = new TextView(this);
        tvQty.setText(String.valueOf(qty));
        tvQty.setPadding(8, 0, 8, 0);
        tvQty.setTextSize(16f);

        ImageButton btnPlus = new ImageButton(this);
        btnPlus.setImageResource(R.drawable.add);
        btnPlus.setBackground(null);

        qtyControl.addView(btnMinus);
        qtyControl.addView(tvQty);
        qtyControl.addView(btnPlus);

        layout.addView(img);
        layout.addView(info);
        layout.addView(qtyControl);
        card.addView(layout);
        cartContainer.addView(card);

        attachSwipeToDelete(card, name);

        btnPlus.setOnClickListener(v -> {
            int newQty = Integer.parseInt(tvQty.getText().toString()) + 1;
            tvQty.setText(String.valueOf(newQty));
            tvPrice.setText("₱" + (price * newQty));
            updateCart(name, price, 1);
        });

        btnMinus.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(tvQty.getText().toString());
            if (currentQty > 1) {
                int newQty = currentQty - 1;
                tvQty.setText(String.valueOf(newQty));
                tvPrice.setText("₱" + (price * newQty));
                updateCart(name, price, -1);
            }
        });
    }

    private void updateCart(String name, int price, int qtyChange) {
        int currentQty = cart.getInt(name + "_qty", 0) + qtyChange;

        SharedPreferences.Editor editor = cart.edit();
        editor.putInt(name + "_qty", currentQty);
        editor.putInt("TOTAL", cart.getInt("TOTAL", 0) + (price * qtyChange));
        editor.apply();

        renderCart();
    }

    private void attachSwipeToDelete(View card, String name) {
        GestureDetector detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                    java.util.Set<String> items = new java.util.HashSet<>(cart.getStringSet("ITEMS", new java.util.HashSet<>()));
                    items.remove(name);
                    cart.edit()
                            .remove(name + "_qty")
                            .remove(name + "_price")
                            .remove(name + "_image")
                            .putStringSet("ITEMS", items)
                            .apply();
                    renderCart();
                    return true;
                }
                return false;
            }
        });
        card.setOnTouchListener((v, event) -> detector.onTouchEvent(event));
    }
}
