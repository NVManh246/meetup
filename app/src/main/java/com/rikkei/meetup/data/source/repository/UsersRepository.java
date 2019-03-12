package com.rikkei.meetup.data.source.repository;

import android.content.Context;

import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.user.TokenResponse;
import com.rikkei.meetup.data.source.UsersDataSource;
import com.rikkei.meetup.data.source.remote.UsersRemoteDateSource;

import io.reactivex.Flowable;

public class UsersRepository implements UsersDataSource.UsersRemoteDataSource {

    private static UsersRepository sIntance;
    private UsersDataSource.UsersRemoteDataSource mRemoteDataSource;

    private UsersRepository(UsersRemoteDateSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public static UsersRepository getInstance(UsersRemoteDateSource remoteDataSource) {
        if(sIntance == null) {
            sIntance = new UsersRepository(remoteDataSource);
        }
        return sIntance;
    }

    @Override
    public Flowable<TokenResponse> login(String email, String password) {
        return mRemoteDataSource.login(email, password);
    }

    @Override
    public Flowable<TokenResponse> register(String fullname, String email, String password) {
        return mRemoteDataSource.register(fullname, email, password);
    }

    @Override
    public Flowable<Message> resetPassword(String email) {
        return mRemoteDataSource.resetPassword(email);
    }
}
