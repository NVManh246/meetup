package com.rikkei.meetup.data.model.event;

import com.google.gson.annotations.SerializedName;

public class EventsResponse {
    @SerializedName("status")
    private int mStatus;
    @SerializedName("response")
    private ListEvents mListEvents;

    public EventsResponse() {
    }

    public int getStatus() {
        return mStatus;
    }

    public ListEvents getListEvents() {
        return mListEvents;
    }
}
