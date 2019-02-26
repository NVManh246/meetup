package com.rikkei.meetup.data.source.remote;

import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.user.TokenResponse;
import com.rikkei.meetup.data.networking.ApiClient;
import com.rikkei.meetup.data.source.UsersDataSource;

import io.reactivex.Flowable;

public class UsersRemoteDateSource implements UsersDataSource.UsersRemoteDataSource {

    private static UsersRemoteDateSource sIntance;
    private ApiClient mApiClient;

    private UsersRemoteDateSource(ApiClient apiClient) {
        mApiClient = apiClient;
    }

    public static UsersRemoteDateSource getInstance(ApiClient apiClient) {
        if(sIntance == null) {
            sIntance = new UsersRemoteDateSource(apiClient);
        }
        return sIntance;
    }

    @Override
    public Flowable<TokenResponse> login(String email, String password) {
        return mApiClient.login(email, password);
    }

    @Override
    public Flowable<TokenResponse> register(String fullname, String email, String password) {
        return mApiClient.register(fullname, email, password);
    }

    @Override
    public Flowable<Message> resetPassword(String email) {
        return mApiClient.resetPassword(email);
    }
}
