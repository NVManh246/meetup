package com.rikkei.meetup.data.model.genre;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListGenre {
    @SerializedName("categories")
    private List<Genre> mGenres;

    public ListGenre() {
    }

    public List<Genre> getGenres() {
        return mGenres;
    }
}
