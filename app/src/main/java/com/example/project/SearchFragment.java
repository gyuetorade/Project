package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        EditText etSearch = view.findViewById(R.id.etSearch);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        LinearLayout foodList = view.findViewById(R.id.foodList);
        TextView noResult = view.findViewById(R.id.noResult);

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim().toLowerCase();
            if (query.isEmpty()) {
                Toast.makeText(getContext(), "Please enter something to search.", Toast.LENGTH_SHORT).show();
                showAllCards(foodList);
                noResult.setVisibility(View.GONE);
                return;
            }

            boolean foundResult = false;

            // Iterate through all rows
            for (int i = 0; i < foodList.getChildCount(); i++) {
                View row = foodList.getChildAt(i);
                if (row instanceof LinearLayout) {
                    LinearLayout rowLayout = (LinearLayout) row;

                    // Iterate through all cards in the row
                    for (int j = 0; j < rowLayout.getChildCount(); j++) {
                        View card = rowLayout.getChildAt(j);
                        if (card instanceof CardView) {
                            CardView cardView = (CardView) card;
                            // Get the food name from the card
                            String foodName = getFoodNameFromCard(cardView);

                            if (foodName.toLowerCase().contains(query)) {
                                cardView.setVisibility(View.VISIBLE);
                                foundResult = true;
                            } else {
                                cardView.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            noResult.setVisibility(foundResult ? View.GONE : View.VISIBLE);
        });
    }

    // Helper method to extract food name from card
    private String getFoodNameFromCard(CardView cardView) {
        // The card has a LinearLayout as its first child
        LinearLayout cardLayout = (LinearLayout) cardView.getChildAt(0);

        // Look for a TextView that likely contains the food name
        for (int i = 0; i < cardLayout.getChildCount(); i++) {
            View child = cardLayout.getChildAt(i);
            if (child instanceof TextView) {
                return ((TextView) child).getText().toString();
            }

            // If it's another layout, search inside it
            if (child instanceof LinearLayout) {
                LinearLayout innerLayout = (LinearLayout) child;
                for (int j = 0; j < innerLayout.getChildCount(); j++) {
                    View innerChild = innerLayout.getChildAt(j);
                    if (innerChild instanceof TextView) {
                        return ((TextView) innerChild).getText().toString();
                    }
                }
            }
        }

        return "";
    }

    // Helper method to show all cards
    private void showAllCards(LinearLayout foodList) {
        for (int i = 0; i < foodList.getChildCount(); i++) {
            View row = foodList.getChildAt(i);
            if (row instanceof LinearLayout) {
                LinearLayout rowLayout = (LinearLayout) row;
                for (int j = 0; j < rowLayout.getChildCount(); j++) {
                    View card = rowLayout.getChildAt(j);
                    if (card instanceof CardView) {
                        card.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}