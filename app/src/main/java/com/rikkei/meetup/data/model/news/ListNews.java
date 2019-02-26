package com.rikkei.meetup.data.model.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListNews {
    @SerializedName("news")
    @Expose
    private List<News> mNews;

    public List<News> getNews() {
        return mNews;
    }
}
