package com.rikkei.meetup.data.model.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("response")
    @Expose
    private ListNews mListNews;

    public int getStatus() {
        return status;
    }

    public ListNews getListNews() {
        return mListNews;
    }
}
