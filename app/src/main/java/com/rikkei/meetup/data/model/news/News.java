package com.rikkei.meetup.data.model.news;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tblNews")
public class News {
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;
    @ColumnInfo(name = "feed")
    @SerializedName("feed")
    private String mFeed;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String mTitle;
    @ColumnInfo(name = "thumb_img")
    @SerializedName("thumb_img")
    private String mThumbImg;
    @ColumnInfo(name = "detail_url")
    @SerializedName("detail_url")
    private String mDetailUrl;
    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String mDescription;
    @ColumnInfo(name = "author")
    @SerializedName("author")
    private String mAuthor;
    @ColumnInfo(name = "publish_date")
    @SerializedName("publish_date")
    private String mPublicDate;
    @ColumnInfo(name = "create_at")
    @SerializedName("created_at")
    private String mCreateAt;
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
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

    public void setId(long id) {
        mId = id;
    }

    public void setFeed(String feed) {
        mFeed = feed;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setThumbImg(String thumbImg) {
        mThumbImg = thumbImg;
    }

    public void setDetailUrl(String detailUrl) {
        mDetailUrl = detailUrl;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public void setPublicDate(String publicDate) {
        mPublicDate = publicDate;
    }

    public void setCreateAt(String createAt) {
        mCreateAt = createAt;
    }

    public void setUpdateDate(String updateDate) {
        mUpdateDate = updateDate;
    }
}
