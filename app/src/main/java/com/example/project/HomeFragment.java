package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private ViewPager2 viewPager;
    private HomeVideoAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;
    private ListenerRegistration registration;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.videoPager);
        progressBar = view.findViewById(R.id.videoProgress);
        emptyView = view.findViewById(R.id.videoEmptyState);

        adapter = new HomeVideoAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        startListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopListening();
        viewPager = null;
        adapter = null;
        progressBar = null;
        emptyView = null;
    }

    private void startListening() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        registration = firestore.collection("chicha")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(snapshotListener);
    }

    private final EventListener<QuerySnapshot> snapshotListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if (!isAdded()) {
                return;
            }

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            if (error != null) {
                Log.e(TAG, "Failed to load videos", error);
                if (emptyView != null) {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(R.string.video_error_loading);
                }
                return;
            }

            if (value == null) {
                showEmptyState();
                return;
            }

            List<ChichaVideo> videos = new ArrayList<>();
            boolean missingVideoId = false;

            for (DocumentSnapshot doc : value.getDocuments()) {
                String id = doc.getId();
                String title = doc.getString("title");
                String videoId = doc.getString("videoId");

                if (videoId == null || videoId.trim().isEmpty()) {
                    missingVideoId = true;
                    continue;
                }

                if (title == null || title.trim().isEmpty()) {
                    title = getString(R.string.video_default_title);
                }

                videos.add(new ChichaVideo(id, title, videoId.trim()));
            }

            if (adapter != null) {
                adapter.submitList(videos);
            }

            if (videos.isEmpty()) {
                showEmptyState();
            } else if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }

            if (missingVideoId) {
                Toast.makeText(requireContext(), R.string.video_missing_id, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void showEmptyState() {
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.video_empty_state);
        }
    }

    private void stopListening() {
        if (registration != null) {
            registration.remove();
            registration = null;
        }
    }
}