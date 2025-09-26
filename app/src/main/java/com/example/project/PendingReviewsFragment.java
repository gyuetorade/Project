package com.example.project;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PendingReviewsFragment extends Fragment {

    public PendingReviewsFragment() {
        super(R.layout.fragment_pending_reviews);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View back = view.findViewById(R.id.btnBack);
        if (back != null) {
            back.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        }
    }
}
