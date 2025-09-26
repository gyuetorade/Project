package com.example.project;

import androidx.annotation.DrawableRes;

public class VideoItem {
    private final String videoUrl;
    private final String restaurantName;
    private final String description;
    private final @DrawableRes int logoRes;
    private int likeCount;
    private boolean liked;
    private boolean following;

    public VideoItem(String videoUrl,
                     String restaurantName,
                     String description,
                     @DrawableRes int logoRes,
                     int likeCount) {
        this.videoUrl = videoUrl;
        this.restaurantName = restaurantName;
        this.description = description;
        this.logoRes = logoRes;
        this.likeCount = likeCount;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public int getLogoRes() {
        return logoRes;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return liked;
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

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
