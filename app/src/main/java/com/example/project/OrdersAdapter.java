package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.VH> {
    private final List<OrderItem> items;

    public OrdersAdapter(List<OrderItem> items) {
        this.items = items;
    }

    @NonNull
    @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        OrderItem item = items.get(pos);
        h.img.setImageResource(item.imageRes);
        h.name.setText(item.name);
        h.price.setText("\u20b1" + (item.price * item.quantity));
        h.qty.setText(String.valueOf(item.quantity));
        h.plus.setOnClickListener(v -> {
            item.quantity++;
            h.qty.setText(String.valueOf(item.quantity));
            h.price.setText("\u20b1" + (item.price * item.quantity));
        });
        h.minus.setOnClickListener(v -> {
            if (item.quantity > 1) {
                item.quantity--;
                h.qty.setText(String.valueOf(item.quantity));
                h.price.setText("\u20b1" + (item.price * item.quantity));
            }
        });
    }

    @Override public int getItemCount() { return items.size(); }

    void removeAt(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView name; TextView price; TextView qty; TextView plus; TextView minus;
        VH(View v) {
            super(v);
            img = v.findViewById(R.id.imgFood);
            name = v.findViewById(R.id.txtName);
            price = v.findViewById(R.id.txtPrice);
            qty = v.findViewById(R.id.txtQty);
            plus = v.findViewById(R.id.btnPlus);
            minus = v.findViewById(R.id.btnMinus);
        }
    }
}