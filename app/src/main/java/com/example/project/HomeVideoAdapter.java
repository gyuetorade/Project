package com.example.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.project.MenuBottomSheet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.databinding.ItemHomeVidBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

import java.util.ArrayList;
import java.util.List;

public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.VideoViewHolder> {
    private static final String TAG = "HomeVideoAdapter";

    private final Fragment fragment;
    private final List<ChichaVideo> videoItems = new ArrayList<>();
    private int currentItem = RecyclerView.NO_POSITION;

    public HomeVideoAdapter(@NonNull Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHomeVidBinding binding = ItemHomeVidBinding.inflate(inflater, parent, false);
        return new VideoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(fragment, videoItems.get(position), position == currentItem);
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public void submitList(@NonNull List<ChichaVideo> videos) {
        videoItems.clear();
        videoItems.addAll(videos);
        if (videoItems.isEmpty()) {
            currentItem = RecyclerView.NO_POSITION;
        } else if (currentItem == RecyclerView.NO_POSITION || currentItem >= videoItems.size()) {
            currentItem = 0;
        }
        notifyDataSetChanged();
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position >= videoItems.size()) {
            return;
        }

        if (position == currentItem) {
            return;
        }

        int previous = currentItem;
        currentItem = position;

        if (previous != RecyclerView.NO_POSITION && previous < videoItems.size()) {
            notifyItemChanged(previous);
        }
        notifyItemChanged(currentItem);
    }

    @Override
    public void onViewRecycled(@NonNull VideoViewHolder holder) {
        super.onViewRecycled(holder);
        holder.releasePlayer();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.pause();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeVidBinding binding;
        private YouTubePlayerListener playbackListener;
        private YouTubePlayer youTubePlayer;
        private String boundVideoId;
        private boolean lifecycleRegistered = false;
        private boolean playWhenReady;

        VideoViewHolder(@NonNull ItemHomeVidBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Fragment fragment, @NonNull ChichaVideo item, boolean playWhenReady) {
            binding.ivLogo.setImageResource(R.drawable.logoforshop);
            binding.tvRestaurantName.setText(item.getTitle());
            binding.tvDescription.setText(item.getDescription());
            binding.tvLikeCount.setText(String.valueOf(item.getLikeCount()));
            binding.btnLike.setImageResource(item.isLiked() ? R.drawable.favorite : R.drawable.heart);
            binding.btnFollow.setText(item.isFollowing() ? R.string.following : R.string.follow);

            this.playWhenReady = playWhenReady;

            if (!lifecycleRegistered) {
                fragment.getLifecycle().addObserver(binding.youtubePlayerView);
                lifecycleRegistered = true;
            }

            if (playbackListener != null) {
                binding.youtubePlayerView.removeYouTubePlayerListener(playbackListener);
                playbackListener = null;
            }

            final String videoId = item.getVideoId();

            playbackListener = new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer player) {
                    youTubePlayer = player;
                    boundVideoId = videoId;
                    if (HomeVideoAdapter.VideoViewHolder.this.playWhenReady) {
                        youTubePlayer.loadVideo(videoId, 0f);
                    } else {
                        youTubePlayer.cueVideo(videoId, 0f);
                    }
                }
            };

            binding.youtubePlayerView.addYouTubePlayerListener(playbackListener);

            if (youTubePlayer != null) {
                if (!videoId.equals(boundVideoId)) {
                    boundVideoId = videoId;
                    if (this.playWhenReady) {
                        youTubePlayer.loadVideo(videoId, 0f);
                    } else {
                        youTubePlayer.cueVideo(videoId, 0f);
                    }
                } else if (this.playWhenReady) {
                    youTubePlayer.play();
                } else {
                    youTubePlayer.pause();
                }
            }

            binding.btnLike.setOnClickListener(v -> {
                boolean liked = item.toggleLiked();
                binding.btnLike.setImageResource(liked ? R.drawable.favorite : R.drawable.heart);
                binding.tvLikeCount.setText(String.valueOf(item.getLikeCount()));
                Toast.makeText(fragment.requireContext(), liked ? R.string.liked : R.string.unliked, Toast.LENGTH_SHORT).show();
            });

            binding.btnComment.setOnClickListener(v ->
                    fragment.startActivity(new Intent(fragment.requireContext(), MessagingActivity.class))
            );

            binding.btnMenu.setOnClickListener(v -> {
                try {
                    com.example.project.MenuBottomSheet sheet = new MenuBottomSheet();
                    sheet.show(fragment.getParentFragmentManager(), "MenuBottomSheet");
                } catch (Exception e) {
                    Log.w(TAG, "Unable to show menu", e);
                }
            });

            binding.btnFollow.setOnClickListener(v -> {
                if (!item.isFollowing()) {
                    item.setFollowing(true);
                    binding.btnFollow.setText(R.string.following);
                } else {
                    new AlertDialog.Builder(fragment.requireContext())
                            .setTitle(R.string.unfollow_title)
                            .setMessage(R.string.unfollow_message)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                item.setFollowing(false);
                                binding.btnFollow.setText(R.string.follow);
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            });
        }

        void pause() {
            if (youTubePlayer != null) {
                youTubePlayer.pause();
            }
            playWhenReady = false;
        }

        void releasePlayer() {
            if (playbackListener != null) {
                binding.youtubePlayerView.removeYouTubePlayerListener(playbackListener);
                playbackListener = null;
            }
            if (youTubePlayer != null) {
                youTubePlayer.pause();
                youTubePlayer = null;
            }
            boundVideoId = null;
            playWhenReady = false;
        }
    }
}
