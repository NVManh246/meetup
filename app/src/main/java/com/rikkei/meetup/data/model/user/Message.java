package com.rikkei.meetup.data.model.user;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("status")
    private int mStatus;

    public Message() {
    }

    public int getStatus() {
        return mStatus;
    }
}
