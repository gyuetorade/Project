package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageButton btnLike = view.findViewById(R.id.btnLike);
        ImageButton btnComment = view.findViewById(R.id.btnComment);
        ImageButton btnMenu = view.findViewById(R.id.btnMenu); // ✅ Menu button

        TextView tvLikeCount = view.findViewById(R.id.tvLikeCount);
        final int[] likeCount = {0};

        btnLike.setOnClickListener(v -> {
            likeCount[0]++;
            tvLikeCount.setText(String.valueOf(likeCount[0]));
        });

        btnComment.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MessagingActivity.class))
        );

        btnMenu.setOnClickListener(v -> {
            com.example.project.MenuBottomSheet sheet = new com.example.project.MenuBottomSheet();
            sheet.show(getParentFragmentManager(), "MenuBottomSheet");
        });
    }
}
