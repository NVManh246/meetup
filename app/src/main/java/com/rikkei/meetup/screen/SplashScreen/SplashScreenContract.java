package com.rikkei.meetup.screen.SplashScreen;

import android.content.Context;

public interface SplashScreenContract {
    interface View {
        void showResult();
        void navigationToMain();
        Context getViewContext();
    }

    interface Presenter {
        void checkTokenExpired(String token);
    }
}
