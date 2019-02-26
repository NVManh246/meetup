package com.rikkei.meetup.screen.main;

import android.content.Context;

public interface MainContract {
    interface View {
        void showResult();
        Context getViewContext();
    }

    interface Presenter {
        void checkTokenExpired(String token);
    }
}
