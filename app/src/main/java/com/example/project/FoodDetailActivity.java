package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDetailActivity extends AppCompatActivity {

    private boolean isFavorited = false;
    private ImageView favButton, bckButton;

    Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        bckButton = findViewById(R.id.bckButton);
        favButton = findViewById(R.id.favButton);

        bckButton.setOnClickListener(v -> finish());

        // Set up favorite button click listener
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });
        findViewById(R.id.addToCartButton).setOnClickListener(v ->
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
        );
        // Get the food details from the intent
        String foodName = getIntent().getStringExtra("FOOD_NAME");
        String foodPrice = getIntent().getStringExtra("FOOD_PRICE");
        int foodImageResId = getIntent().getIntExtra("FOOD_IMAGE_RES_ID", R.drawable.fried_chicken);

        // Initialize views
        ImageView foodImage = findViewById(R.id.foodImage);
        TextView foodNameTextView = findViewById(R.id.foodName);
        TextView foodPriceTextView = findViewById(R.id.foodPrice);
        TextView foodDescription = findViewById(R.id.foodDescription);

        // Set the food details
        foodImage.setImageResource(foodImageResId);
        foodNameTextView.setText(foodName);
        foodPriceTextView.setText(foodPrice);

        // Set description based on food name
        String description = getFoodDescription(foodName);
        foodDescription.setText(description);
    }

    // Method to toggle favorite state
    private void toggleFavorite() {
        isFavorited = !isFavorited;

        if (isFavorited) {
            // Change to filled heart icon (favorite state)
            favButton.setImageResource(R.drawable.heart);
            Toast.makeText(this, "Added To Favorite", Toast.LENGTH_SHORT).show();
        } else {
            // Change to outline heart icon (not favorite state)
            favButton.setImageResource(R.drawable.heart2); // Make sure you have this drawable
        }
        Toast.makeText(this, "Removed To Favorite", Toast.LENGTH_SHORT).show();
    }

    // Helper method to get food description
    private String getFoodDescription(String foodName) {
        switch (foodName) {
            case "Spicy chickens":
                return "Crispy fried chicken with a spicy kick. Marinated in special spices and fried to perfection.";
            case "Veggie tomato":
                return "Fresh vegetables with tomato sauce. A healthy and delicious option for vegetarians.";
            case "Egg n cucumber":
                return "Boiled eggs served with fresh cucumber slices. A perfect protein-packed meal.";
            case "Fried chicken m.":
                return "Mild fried chicken with our special seasoning. Crispy on the outside, tender on the inside.";
            case "Moi-moi and ekpa.":
                return "Traditional Nigerian steamed bean pudding served with ekpa (coconut wrapped in leaves).";
            default:
                return "Delicious food prepared with the finest ingredients.";
        }

    }
}