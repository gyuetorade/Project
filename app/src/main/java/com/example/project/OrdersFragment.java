package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class OrdersFragment extends Fragment {

    public OrdersFragment() {
        super(R.layout.fragment_orders);
    }

    private LinearLayout orderContainer;
    private TextView tvSubtotal, tvDelivery, tvDiscount, tvTotal;
    private Button btnPlaceOrder;

    private int deliveryCharge = 10;
    private int discount = 10;
    private int subtotal = 0;

    private SharedPreferences cart;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        orderContainer = view.findViewById(R.id.orderContainer);
        tvSubtotal = view.findViewById(R.id.tvSubtotal);
        tvDelivery = view.findViewById(R.id.tvDelivery);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);

        cart = requireContext().getSharedPreferences("Cart", Context.MODE_PRIVATE);

        renderCart();

        // ✅ FIX: Place My Order → CartActivity
        btnPlaceOrder.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CartActivity.class);
            startActivity(intent);
        });
    }

    private void renderCart() {
        orderContainer.removeAllViews();
        subtotal = 0;

        java.util.Set<String> items = cart.getStringSet("ITEMS", new java.util.HashSet<>());
        for (String name : items) {
            int qty = cart.getInt(name + "_qty", 0);
            int price = cart.getInt(name + "_price", 0);
            int imageRes = cart.getInt(name + "_image", R.drawable.ic_food_placeholder);
            if (qty > 0) {
                addOrderCard(name, price, qty, imageRes);
                subtotal += price * qty;
            }
        }

        updateSummary();
    }

    private void addOrderCard(String keyName, int price, int qty, int imageResId) {
        Context context = requireContext();

        CardView card = new CardView(context);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        card.setRadius(16f);
        card.setCardElevation(8f);
        card.setUseCompatPadding(true);
        card.setPadding(16, 16, 16, 16);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView img = new ImageView(context);
        img.setImageResource(imageResId);
        img.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout info = new LinearLayout(context);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(16, 0, 0, 0);
        info.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView tvName = new TextView(context);
        tvName.setText(keyName);
        tvName.setTextSize(16f);
        tvName.setTypeface(null, Typeface.BOLD);

        TextView tvPrice = new TextView(context);
        tvPrice.setText("₱" + (price * qty));
        tvPrice.setTextSize(14f);

        info.addView(tvName);
        info.addView(tvPrice);

        LinearLayout qtyControl = new LinearLayout(context);
        qtyControl.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton btnMinus = new ImageButton(context);
        btnMinus.setImageResource(R.drawable.minus);
        btnMinus.setBackground(null);

        TextView tvQty = new TextView(context);
        tvQty.setText(String.valueOf(qty));
        tvQty.setPadding(8, 0, 8, 0);
        tvQty.setTextSize(16f);

        ImageButton btnPlus = new ImageButton(context);
        btnPlus.setImageResource(R.drawable.add);
        btnPlus.setBackground(null);

        qtyControl.addView(btnMinus);
        qtyControl.addView(tvQty);
        qtyControl.addView(btnPlus);

        layout.addView(img);
        layout.addView(info);
        layout.addView(qtyControl);
        card.addView(layout);
        orderContainer.addView(card);

        attachSwipeToDelete(card, keyName);

        btnPlus.setOnClickListener(v -> {
            int newQty = Integer.parseInt(tvQty.getText().toString()) + 1;
            tvQty.setText(String.valueOf(newQty));
            cart.edit().putInt(keyName + "_qty", newQty).apply();
            subtotal += price;
            tvPrice.setText("₱" + (price * newQty));
            updateSummary();
        });

        btnMinus.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(tvQty.getText().toString());
            if (currentQty > 1) {
                int newQty = currentQty - 1;
                tvQty.setText(String.valueOf(newQty));
                cart.edit().putInt(keyName + "_qty", newQty).apply();
                subtotal -= price;
                tvPrice.setText("₱" + (price * newQty));
                updateSummary();
            }
        });
    }

    private void attachSwipeToDelete(View card, String keyName) {
        GestureDetector detector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
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
                    items.remove(keyName);
                    cart.edit()
                            .remove(keyName + "_qty")
                            .remove(keyName + "_price")
                            .remove(keyName + "_image")
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

    private void updateSummary() {
        tvSubtotal.setText("Sub-total: ₱" + subtotal);
        tvDelivery.setText("Delivery Charge: ₱" + deliveryCharge);
        tvDiscount.setText("Discount: ₱" + discount);
        int total = subtotal + deliveryCharge - discount;
        tvTotal.setText("Total: ₱" + total);

        cart.edit().putInt("TOTAL", total).apply();
    }
}
