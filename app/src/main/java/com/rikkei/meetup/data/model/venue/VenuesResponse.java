package com.rikkei.meetup.data.model.venue;

import com.google.gson.annotations.SerializedName;

public class VenuesResponse {
    @SerializedName("status")
    private int mStatus;
    @SerializedName("response")
    private ListVenue mListVenue;

    public VenuesResponse() {
    }

    public int getStatus() {
        return mStatus;
    }

    public ListVenue getListVenue() {
        return mListVenue;
    }
}
