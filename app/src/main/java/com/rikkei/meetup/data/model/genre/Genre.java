package com.rikkei.meetup.data.model.genre;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("slug")
    private String mSlug;

    public Genre() {
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getSlug() {
        return mSlug;
    }
}
