package com.rikkei.meetup.data.source;

import android.content.Context;

import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.user.TokenResponse;

import io.reactivex.Flowable;

public interface UsersDataSource {
    interface UsersRemoteDataSource {
        Flowable<TokenResponse> login(String email, String password);
        Flowable<TokenResponse> register(String fullname, String email, String password);
        Flowable<Message> resetPassword(String email);
    }
}
