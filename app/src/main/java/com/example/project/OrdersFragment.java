package com.example.project;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    public OrdersFragment() { super(R.layout.fragment_orders); }

       @Override public void onViewCreated(View v, @Nullable Bundle b) {
        ImageButton back = v.findViewById(R.id.btnBack);
        back.setOnClickListener(view -> requireActivity().onBackPressed());

        RecyclerView rv = v.findViewById(R.id.rvOrders);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(R.drawable.img, "Veggie tomato mix", 1900));
        items.add(new OrderItem(R.drawable.other, "Fishwith mix orange....", 1900));
        OrdersAdapter adapter = new OrdersAdapter(items);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        helper.attachToRecyclerView(rv);
    }

    private static class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private final OrdersAdapter adapter;
        private final Paint paint = new Paint();

        SwipeToDeleteCallback(OrdersAdapter adapter) {
            super(0, ItemTouchHelper.LEFT);
            this.adapter = adapter;
            paint.setColor(0xFFFF3B30);
        }

        @Override public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder vh, RecyclerView.ViewHolder target) { return false; }

        @Override public void onSwiped(RecyclerView.ViewHolder vh, int direction) {
            adapter.removeAt(vh.getBindingAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView rv, RecyclerView.ViewHolder vh, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = vh.itemView;
                RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                c.drawRect(background, paint);

                android.graphics.drawable.Drawable icon =
                        ContextCompat.getDrawable(rv.getContext(), android.R.drawable.ic_menu_delete);
                if (icon != null) {
                    int margin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    int left = itemView.getRight() - margin - icon.getIntrinsicWidth();
                    int right = itemView.getRight() - margin;
                    int top = itemView.getTop() + margin;
                    int bottom = top + icon.getIntrinsicHeight();
                    icon.setBounds(left, top, right, bottom);
                    icon.draw(c);
                }
            }
            super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive);
        }
    }
}