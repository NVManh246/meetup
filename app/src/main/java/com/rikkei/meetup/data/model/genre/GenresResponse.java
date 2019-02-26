package com.rikkei.meetup.data.model.genre;

import com.google.gson.annotations.SerializedName;

public class GenresResponse {
    @SerializedName("status")
    private int mStatus;
    @SerializedName("response")
    private ListGenre mListGenre;

    public GenresResponse() {
    }

    public int getStatus() {
        return mStatus;
    }

    public ListGenre getListGenre() {
        return mListGenre;
    }
}
