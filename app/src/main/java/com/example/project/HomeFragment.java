package com.example.project;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager;
    private ExoPlayer player;
    private PlayerView currentPlayerView;
    private int currentPosition = RecyclerView.NO_POSITION;
    private ViewPager2.OnPageChangeCallback pageChangeCallback;
    private HomeVideoAdapter adapter;

    private final List<VideoItem> videoItems = Arrays.asList(
            new VideoItem(
                    "https://storage.googleapis.com/exoplayer-test-media-1/mp4/BigBuckBunny_320x180.mp4",
                    "Gamberetti's",
                    "ITALIAN super duper water mark slope drone stone /n eclipse",
                    R.drawable.logoforshop,
                    128
            ),
            new VideoItem(
                    "https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-minute.mp4",
                    "La Trattoria",
                    "Handmade pasta, coastal views, and a glass of chianti to match.",
                    R.drawable.logoforshop,
                    86
            ),
            new VideoItem(
                    "https://storage.googleapis.com/exoplayer-test-media-1/mp4/android-screens-10s.mp4",
                    "Sunset Bistro",
                    "Seasonal tasting menu featuring local farmers and daily catches.",
                    R.drawable.logoforshop,
                    203
            )
    );

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.videoPager);
        adapter = new HomeVideoAdapter(this, videoItems);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                attachPlayerToPosition(position);
            }
        };
        viewPager.registerOnPageChangeCallback(pageChangeCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        ensurePlayer();
        viewPager.post(() -> attachPlayerToPosition(getCurrentPagerPosition()));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pageChangeCallback != null) {
            viewPager.unregisterOnPageChangeCallback(pageChangeCallback);
            pageChangeCallback = null;
        }
        releasePlayer();
        viewPager = null;
        adapter = null;
        currentPlayerView = null;
    }

    private void ensurePlayer() {
        if (player == null) {
            player = new ExoPlayer.Builder(requireContext()).build();
            player.setRepeatMode(Player.REPEAT_MODE_ONE);
        }
    }

    private void releasePlayer() {
        if (currentPlayerView != null) {
            currentPlayerView.setPlayer(null);
            currentPlayerView = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
        currentPosition = RecyclerView.NO_POSITION;
    }

    private int getCurrentPagerPosition() {
        if (viewPager == null) {
            return 0;
        }
        int position = viewPager.getCurrentItem();
        return position < 0 ? 0 : position;
    }

    private void attachPlayerToPosition(int position) {
        if (viewPager == null || adapter == null || videoItems.isEmpty()) {
            return;
        }
        ensurePlayer();

        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        if (recyclerView == null) {
            viewPager.post(() -> attachPlayerToPosition(position));
            return;
        }
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        if (!(holder instanceof HomeVideoAdapter.VideoViewHolder)) {
            viewPager.post(() -> attachPlayerToPosition(position));
            return;
        }

        HomeVideoAdapter.VideoViewHolder videoHolder = (HomeVideoAdapter.VideoViewHolder) holder;
        PlayerView nextPlayerView = videoHolder.getPlayerView();

        if (currentPlayerView != nextPlayerView) {
            if (currentPlayerView != null) {
                currentPlayerView.setPlayer(null);
            }
            currentPlayerView = nextPlayerView;
            currentPlayerView.setPlayer(player);
        }

        VideoItem item = videoItems.get(position);
        if (currentPosition != position) {
            player.setMediaItem(MediaItem.fromUri(item.getVideoUrl()));
            player.prepare();
            currentPosition = position;
        }
        player.play();
    }
}