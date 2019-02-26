package com.rikkei.meetup.data.model.user;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("status")
    private int mStatus;
    @SerializedName("response")
    private Token mToken;
    @SerializedName("error_message")
    private String mErrorMessage;
    @SerializedName("error_code")
    private int mErrorCode;

    public TokenResponse() {
    }

    public int getStatus() {
        return mStatus;
    }

    public Token getToken() {
        return mToken;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
