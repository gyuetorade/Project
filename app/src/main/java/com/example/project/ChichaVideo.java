package com.example.project;

import androidx.annotation.NonNull;

public class ChichaVideo {
    private final String id;
    private final String title;
    private final String videoId;
    private int likeCount;
    private boolean liked;
    private boolean following;

    public ChichaVideo(@NonNull String id, @NonNull String title, @NonNull String videoId) {
        this.id = id;
        this.title = title;
        this.videoId = videoId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getVideoId() {
        return videoId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean toggleLiked() {
        liked = !liked;
        if (liked) {
            likeCount++;
        } else {
            likeCount = Math.max(0, likeCount - 1);
        }
        return liked;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
