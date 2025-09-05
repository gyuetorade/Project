package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim().toLowerCase();
            if (query.isEmpty()) {
                Toast.makeText(getContext(), "Please enter something to search.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simple filter: hide cards that don't match
            for (int i = 0; i < foodList.getChildCount(); i++) {
                View card = foodList.getChildAt(i);
                String cardText = ((android.widget.TextView)
                        ((LinearLayout) ((androidx.cardview.widget.CardView) card)
                                .getChildAt(0)).getChildAt(0)).getText().toString().toLowerCase();

                card.setVisibility(cardText.contains(query) ? View.VISIBLE : View.GONE);
            }
        });
    }
}
