package com.rikkei.meetup.data.model.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("id")
    @Expose
    private long mId;
    @SerializedName("feed")
    @Expose
    private String mFeed;
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("thumb_img")
    @Expose
    private String mThumbImg;
    @SerializedName("detail_url")
    @Expose
    private String mDetailUrl;
    @SerializedName("description")
    @Expose
    private String mDescription;
    @SerializedName("author")
    @Expose
    private String mAuthor;
    @SerializedName("publish_date")
    @Expose
    private String mPublicDate;
    @SerializedName("created_at")
    @Expose
    private String mCreateAt;
    @SerializedName("updated_at")
    @Expose
    private String mUpdateDate;

    public News() {
    }

    public long getId() {
        return mId;
    }

    public String getFeed() {
        return mFeed;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getThumbImg() {
        return mThumbImg;
    }

    public String getDetailUrl() {
        return mDetailUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublicDate() {
        return mPublicDate;
    }

    public String getCreateAt() {
        return mCreateAt;
    }

    public String getUpdateDate() {
        return mUpdateDate;
    }
}
