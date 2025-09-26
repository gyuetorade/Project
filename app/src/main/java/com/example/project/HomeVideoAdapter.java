package com.example.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.media3.ui.PlayerView;

import com.example.project.databinding.ItemHomeVideoBinding;

import java.util.List;

public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.VideoViewHolder> {
    private final Fragment fragment;
    private final List<VideoItem> videoItems;

    public HomeVideoAdapter(@NonNull Fragment fragment, @NonNull List<VideoItem> videoItems) {
        this.fragment = fragment;
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHomeVideoBinding binding = ItemHomeVideoBinding.inflate(inflater, parent, false);
        return new VideoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(fragment, videoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeVideoBinding binding;

        VideoViewHolder(@NonNull ItemHomeVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Fragment fragment, VideoItem item) {
            binding.playerView.setUseController(false);
            binding.ivLogo.setImageResource(item.getLogoRes());
            binding.tvRestaurantName.setText(item.getRestaurantName());
            binding.tvDescription.setText(item.getDescription());
            binding.tvLikeCount.setText(String.valueOf(item.getLikeCount()));
            binding.btnLike.setImageResource(item.isLiked() ? R.drawable.heart : R.drawable.hearty);
            binding.btnFollow.setText(item.isFollowing() ? R.string.following : R.string.follow);

            binding.btnLike.setOnClickListener(v -> {
                boolean liked = item.toggleLiked();
                binding.btnLike.setImageResource(liked ? R.drawable.heart : R.drawable.hearty);
                binding.tvLikeCount.setText(String.valueOf(item.getLikeCount()));
                Toast.makeText(fragment.requireContext(), liked ? R.string.liked : R.string.unliked, Toast.LENGTH_SHORT).show();
            });

            binding.btnComment.setOnClickListener(v ->
                    fragment.startActivity(new Intent(fragment.requireContext(), MessagingActivity.class))
            );

            binding.btnMenu.setOnClickListener(v -> {
                MenuBottomSheet sheet = new MenuBottomSheet();
                sheet.show(fragment.getParentFragmentManager(), "MenuBottomSheet");
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

        PlayerView getPlayerView() {
            return binding.playerView;
        }
    }
}
