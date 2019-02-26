package com.rikkei.meetup.screen.signup;

import android.content.Context;

public interface RegisterContract {
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
        void register(String fullname, String email, String password);
    }
}
