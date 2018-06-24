package ua.yurezcv.bakingapp.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class RecipeStep {

    @SerializedName("id")
    private int id;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("description")
    private String description;

    @SerializedName("videoURL")
    private String videoUrl;

    @SerializedName("thumbnailURL")
    private String thumbnailUrl;

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public boolean hasVideo() {
        return !TextUtils.isEmpty(videoUrl);
    }

    public boolean hasThumbnail() {
        return !TextUtils.isEmpty(thumbnailUrl);
    }
}
