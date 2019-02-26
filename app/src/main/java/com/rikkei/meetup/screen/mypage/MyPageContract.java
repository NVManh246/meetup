package com.rikkei.meetup.screen.mypage;

import android.content.Context;

public interface MyPageContract {
    interface View {
        void showProfileFragment();
        void showRegisterFragment();
        Context getContext();
    }

    interface Presenter {
        void getToken();
    }
}
