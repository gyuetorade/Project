package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderActivity.Order> orders;

    public OrderAdapter(List<OrderActivity.Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderActivity.Order order = orders.get(position);
        holder.orderId.setText("Order #: " + order.orderId);
        holder.itemName.setText("Item: " + order.itemName);
        holder.price.setText("Price: " + order.price);
        holder.status.setText("Status: " + order.status);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, itemName, price, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            itemName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.orderPrice);
            status = itemView.findViewById(R.id.orderStatus);
        }
    }
}