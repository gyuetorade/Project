package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
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
        cart.edit().clear().apply(); // start fresh

        // Add sample items
        addOrderCard("Burger", "Burger Factory LTD", 120, R.drawable.moi_moi);
        addOrderCard("Fries", "Pizza Palace", 60, R.drawable.egg_cucumber);
        addOrderCard("Milk Tea", "Hot Cool Spot", 90, R.drawable.veggie_tomato);

        updateSummary();

        // ✅ FIX: Place My Order → CartActivity
        btnPlaceOrder.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CartActivity.class);
            startActivity(intent);
        });
    }

    private void addOrderCard(String keyName, String place, int price, int imageResId) {
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

        TextView tvPlace = new TextView(context);
        tvPlace.setText(place);
        tvPlace.setTextSize(14f);

        TextView tvPrice = new TextView(context);
        tvPrice.setText("₱" + price);
        tvPrice.setTextSize(14f);

        info.addView(tvName);
        info.addView(tvPlace);
        info.addView(tvPrice);

        LinearLayout qtyControl = new LinearLayout(context);
        qtyControl.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton btnMinus = new ImageButton(context);
        btnMinus.setImageResource(R.drawable.minus);
        btnMinus.setBackground(null);

        TextView tvQty = new TextView(context);
        tvQty.setText("1");
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

        // Initial add to cart
        subtotal += price;
        cart.edit()
                .putInt(keyName, 1)
                .putInt("TOTAL", subtotal)
                .apply();
        updateSummary();

        btnPlus.setOnClickListener(v -> {
            int qty = Integer.parseInt(tvQty.getText().toString()) + 1;
            tvQty.setText(String.valueOf(qty));
            subtotal += price;
            cart.edit()
                    .putInt(keyName, qty)
                    .putInt("TOTAL", subtotal)
                    .apply();
            updateSummary();
        });

        btnMinus.setOnClickListener(v -> {
            int qty = Integer.parseInt(tvQty.getText().toString());
            if (qty > 1) {
                qty--;
                tvQty.setText(String.valueOf(qty));
                subtotal -= price;
                cart.edit()
                        .putInt(keyName, qty)
                        .putInt("TOTAL", subtotal)
                        .apply();
                updateSummary();
            }
        });
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
