package com.rikkei.meetup.data.model.user;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("status")
    private int mStatus;
    @SerializedName("error_message")
    private String mErrorMessage;
    @SerializedName("error_code")
    private int mErrorCode;

    public Message() {
    }

    public int getStatus() {
        return mStatus;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
