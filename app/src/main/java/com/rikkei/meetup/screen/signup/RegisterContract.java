package com.rikkei.meetup.screen.signup;

public interface RegisterContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showErrorEmail();
        void showErrorPassword();
        void showError();
        void navigationToProfile();
    }

    interface Presenter {
        void register(String fullname, String email, String password);
    }
}
