// chicha/HomeFragment.java
package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.project.R;

public class HomeFragment extends Fragment {

    public HomeFragment() { super(R.layout.fragment_home); }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageButton btnLike = view.findViewById(R.id.btnLike);
        ImageButton btnComment = view.findViewById(R.id.btnComment);
        Button btnMenu = view.findViewById(R.id.btnMenu);

        TextView tvLikeCount = view.findViewById(R.id.tvLikeCount);
        final int[] likeCount = {0};

        btnLike.setOnClickListener(v -> {
            likeCount[0]++;
            tvLikeCount.setText(String.valueOf(likeCount[0]));
        });

        btnComment.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MessagingActivity.class))
        );

        btnMenu.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MenuActivity.class))
        );
    }
}
