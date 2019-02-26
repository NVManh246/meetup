package com.rikkei.meetup.screen.login;

public interface LoginContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showErrorEmail();
        void showErrorPassword();
        void showError();
        void navigationToProfile();
    }

    interface Presenter {
        void login(String email, String password);
    }
}
