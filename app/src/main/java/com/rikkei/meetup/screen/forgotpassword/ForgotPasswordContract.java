package com.rikkei.meetup.screen.forgotpassword;

public interface ForgotPasswordContract {
    interface View {
        void showProgress();
        void hideProgress();
        void showErrorEmail();
        void showError();
        void showSuccess();
    }

    interface Presenter {
        void forgotPassword(String email);
    }
}
