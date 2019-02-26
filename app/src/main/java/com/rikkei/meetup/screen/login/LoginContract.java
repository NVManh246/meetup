package com.rikkei.meetup.screen.login;

import android.content.Context;

public interface LoginContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showErrorEmail();
        void showErrorPassword();
        void showError();
        void navigationToProfile();
        Context getViewContext();
    }

    interface Presenter {
        void login(String email, String password);
    }
}
