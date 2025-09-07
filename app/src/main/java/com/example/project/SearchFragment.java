package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
        ImageView searchIcon = view.findViewById(R.id.searchIcon);

        // Set up click listeners for all cards
        setupCardClickListeners(view);

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim().toLowerCase();
            if (query.isEmpty()) {
                Toast.makeText(getContext(), "Please enter something to search.", Toast.LENGTH_SHORT).show();
                showAllCards(foodList);
                noResult.setVisibility(View.GONE);
                searchIcon.setVisibility(View.GONE);
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

            // Show/hide no result message and icon
            if (foundResult) {
                noResult.setVisibility(View.GONE);
                searchIcon.setVisibility(View.GONE);
            } else {
                noResult.setVisibility(View.VISIBLE);
                searchIcon.setVisibility(View.VISIBLE);
            }
        });
    }

    // Helper method to set up click listeners for all cards
    private void setupCardClickListeners(View view) {
        int[] cardIds = {R.id.card1, R.id.card2, R.id.card3, R.id.card4, R.id.card5};

        for (int cardId : cardIds) {
            CardView card = view.findViewById(cardId);
            if (card != null) {
                card.setOnClickListener(v -> {
                    CardView cardView = (CardView) v;
                    String foodName = getFoodNameFromCard(cardView);
                    String foodPrice = getFoodPriceFromCard(cardView);
                    int foodImageResId = getFoodImageFromCard(cardView);

                    openFoodDetailActivity(foodName, foodPrice, foodImageResId);
                });
            }
        }
    }

    // Method to open FoodDetailActivity with food details
    private void openFoodDetailActivity(String foodName, String foodPrice, int foodImageResId) {
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra("FOOD_NAME", foodName);
        intent.putExtra("FOOD_PRICE", foodPrice);
        intent.putExtra("FOOD_IMAGE_RES_ID", foodImageResId);
        startActivity(intent);
    }

    // Helper method to extract food name from card
    private String getFoodNameFromCard(CardView cardView) {
        LinearLayout cardLayout = (LinearLayout) cardView.getChildAt(0);

        for (int i = 0; i < cardLayout.getChildCount(); i++) {
            View child = cardLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (!textView.getText().toString().startsWith("N")) {
                    return textView.getText().toString();
                }
            }

            if (child instanceof LinearLayout) {
                LinearLayout innerLayout = (LinearLayout) child;
                for (int j = 0; j < innerLayout.getChildCount(); j++) {
                    View innerChild = innerLayout.getChildAt(j);
                    if (innerChild instanceof TextView) {
                        TextView textView = (TextView) innerChild;
                        if (!textView.getText().toString().startsWith("N")) {
                            return textView.getText().toString();
                        }
                    }
                }
            }
        }
        return "";
    }

    // Helper method to extract food price from card
    private String getFoodPriceFromCard(CardView cardView) {
        LinearLayout cardLayout = (LinearLayout) cardView.getChildAt(0);

        for (int i = 0; i < cardLayout.getChildCount(); i++) {
            View child = cardLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                int id = textView.getId();
                if (id != View.NO_ID) {
                    String name = getResources().getResourceEntryName(id);
                    if (name.startsWith("foodPrice")) {
                        return textView.getText().toString();
                    }
                }
            }

            if (child instanceof LinearLayout) {
                LinearLayout innerLayout = (LinearLayout) child;
                for (int j = 0; j < innerLayout.getChildCount(); j++) {
                    View innerChild = innerLayout.getChildAt(j);
                    if (innerChild instanceof TextView) {
                        TextView textView = (TextView) innerChild;
                        int id = textView.getId();
                        if (id != View.NO_ID) {
                            String name = getResources().getResourceEntryName(id);
                            if (name.startsWith("foodPrice")) {
                                return textView.getText().toString();
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    // Helper method to get food image resource ID from card
    private int getFoodImageFromCard(CardView cardView) {
        // Since we can't directly get the resource ID from the ImageView in this context,
        // we'll map the food names to image resources
        String foodName = getFoodNameFromCard(cardView);

        // Map food names to drawable resources
        switch (foodName) {
            case "Spicy chickens":
                return R.drawable.fried_chicken;
            case "Veggie tomato":
                return R.drawable.veggie_tomato;
            case "Egg n cucumber":
                return R.drawable.egg_cucumber;
            case "Fried chicken m.":
                return R.drawable.fried_chicken;
            case "Moi-moi and ekpa.":
                return R.drawable.moi_moi;
            default:
                return R.drawable.fried_chicken; // default image
        }
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