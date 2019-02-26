package com.rikkei.meetup.data.model.venue;

import com.google.gson.annotations.SerializedName;
import com.rikkei.meetup.data.model.event.Venue;

import java.util.List;

public class ListVenue {
    @SerializedName("venues")
    private List<Venue> mVenues;

    public ListVenue() {
    }

    public List<Venue> getVenues() {
        return mVenues;
    }
}
