package com.example.project;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.viewpager2.widget.ViewPager2;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FoodDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        ImageButton back = findViewById(R.id.btnBack);
        ImageButton favorite = findViewById(R.id.btnFavorite);
        ViewPager2 imagePager = findViewById(R.id.imagePager);
        TextView txtFoodName = findViewById(R.id.txtFoodName);
        TextView txtPrice = findViewById(R.id.txtPrice);

        back.setOnClickListener(v -> onBackPressed());

        String name = getIntent().getStringExtra("name");
        int price = getIntent().getIntExtra("price", 0);
        int[] images = getIntent().getIntArrayExtra("images");
        if (images == null) {
            images = new int[]{R.drawable.img, R.drawable.other, R.drawable.facelanding};
        }

        if (name != null) {
            txtFoodName.setText(name);
        }
        if (price > 0) {
            txtPrice.setText("P" + price);
        }
        imagePager.setAdapter(new ImagePagerAdapter(images));

        favorite.setOnClickListener(v ->
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.btnAddToCart).setOnClickListener(v ->
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
        );
    }
}