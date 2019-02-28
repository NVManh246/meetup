package com.rikkei.meetup.data.model.event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListEvents {
    @SerializedName("events")
    private List<Event> mEvents;

    public ListEvents() {
    }

    public List<Event> getEvents() {
        return mEvents;
    }
}
