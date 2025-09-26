package com.example.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.databinding.ItemHomeVidBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

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

    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getBindingAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            holder.setPlayWhenReady(position == currentItem);
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeVidBinding binding;
        private ExoPlayer player;
        private Player.Listener playbackListener;
        private String boundVideoUrl;
        private String currentVideoUrl;
        private boolean playWhenReady;
        private boolean notifiedInvalidUrl;

        VideoViewHolder(@NonNull ItemHomeVidBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Fragment fragment, @NonNull ChichaVideo item, boolean playWhenReady) {
            binding.ivLogo.setImageResource(R.drawable.logoforshop);
            binding.tvRestaurantName.setText(fragment.getString(R.string.video_branding_label));
            binding.tvDescription.setText(item.getTitle());
            binding.tvLikeCount.setText(String.valueOf(item.getLikeCount()));
            binding.btnLike.setImageResource(item.isLiked() ? R.drawable.favorite : R.drawable.heart);
            binding.btnFollow.setText(item.isFollowing() ? R.string.following : R.string.follow);

            this.currentVideoUrl = item.getVideoId();
            this.notifiedInvalidUrl = false;

            ensurePlayer(binding.getRoot().getContext());
            setPlayWhenReady(playWhenReady);

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
                    com.example.project.MenuBottomSheet sheet = new com.example.project.MenuBottomSheet();
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
            setPlayWhenReady(false);
        }

        void setPlayWhenReady(boolean playWhenReady) {
            this.playWhenReady = playWhenReady;
            updatePlayerState();
        }

        private void ensurePlayer(@NonNull Context context) {
            if (player != null) {
                if (binding.playerView.getPlayer() != player) {
                    binding.playerView.setPlayer(player);
                }
                return;
            }

            player = new ExoPlayer.Builder(context).build();
            player.setRepeatMode(Player.REPEAT_MODE_ONE);

            binding.playerView.setUseController(false);
            binding.playerView.setKeepContentOnPlayerReset(true);
            binding.playerView.setPlayer(player);

            playbackListener = new Player.Listener() {
                @Override
                public void onPlayerError(@NonNull PlaybackException error) {
                    Log.e(TAG, "Playback error", error);
                    Context viewContext = binding.getRoot().getContext();
                    Toast.makeText(viewContext, R.string.video_playback_error, Toast.LENGTH_SHORT).show();
                }
            };

            player.addListener(playbackListener);
        }

        private void updatePlayerState() {
            if (player == null) {
                return;
            }

            if (TextUtils.isEmpty(currentVideoUrl)) {
                return;
            }

            if (!hasValidScheme(currentVideoUrl)) {
                if (!notifiedInvalidUrl) {
                    notifiedInvalidUrl = true;
                    Toast.makeText(binding.getRoot().getContext(), R.string.video_invalid_url, Toast.LENGTH_SHORT).show();
                }
                boundVideoUrl = null;
                player.pause();
                player.clearMediaItems();
                return;
            }

            notifiedInvalidUrl = false;

            if (!currentVideoUrl.equals(boundVideoUrl)) {
                boundVideoUrl = currentVideoUrl;
                MediaItem mediaItem = MediaItem.fromUri(Uri.parse(currentVideoUrl));
                player.setMediaItem(mediaItem, true);
                player.prepare();
            }

            player.setPlayWhenReady(playWhenReady);
            if (!playWhenReady && player.isPlaying()) {
                player.pause();
            }
        }

        private boolean hasValidScheme(@NonNull String value) {
            Uri uri = Uri.parse(value);
            return !TextUtils.isEmpty(uri.getScheme());
        }

        void releasePlayer() {
            if (player != null) {
                if (playbackListener != null) {
                    player.removeListener(playbackListener);
                }
                player.release();
                player = null;
            }

            binding.playerView.setPlayer(null);
            playbackListener = null;
            boundVideoUrl = null;
            currentVideoUrl = null;
            playWhenReady = false;
            notifiedInvalidUrl = false;
        }
    }
}
