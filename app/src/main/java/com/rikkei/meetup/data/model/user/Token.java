package com.rikkei.meetup.data.model.user;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("token")
    private String mToken;

    public Token() {
    }

    public String getToken() {
        return mToken;
    }
}
