package com.rikkei.meetup.screen.mypage;

import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.UsersRemoteDateSource;
import com.rikkei.meetup.data.source.repository.UsersRepository;

public class MyPagePresenter implements MyPageContract.Presenter {

    private MyPageContract.View mView;
    private UsersRepository mUsersRepository;

    public MyPagePresenter(MyPageContract.View view) {
        mView = view;
        mUsersRepository = UsersRepository
                .getInstance(UsersRemoteDateSource.getInstance(ApiUtils.getApi()));
    }

    @Override
    public void getToken() {

    }
}
