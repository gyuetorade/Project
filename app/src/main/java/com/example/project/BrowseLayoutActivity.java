package com.example.project;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BrowseLayoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView resultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_layout);

        recyclerView = findViewById(R.id.foodRecyclerView);
        resultCount = findViewById(R.id.resultCount);

        List<FoodName> foodList = new ArrayList<>();
        foodList.add(new FoodName("Veggie tomato mix", "₦1,900", R.drawable.veggie_tomato));
        foodList.add(new FoodName("Egg and cucumber", "₦1,900", R.drawable.egg_cucumber));
        foodList.add(new FoodName("Fried chicken m.", "₦1,900", R.drawable.fried_chicken));
        foodList.add(new FoodName("Moi-moi and ekpa.", "₦1,900", R.drawable.moi_moi));
        // Add more items as needed

        resultCount.setText("Found " + foodList.size() + " results");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FoodAdapter(foodList));
    }
}