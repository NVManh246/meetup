package com.rikkei.meetup.screen.signup;

import com.rikkei.meetup.data.model.user.TokenResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.UsersRemoteDateSource;
import com.rikkei.meetup.data.source.repository.UsersRepository;
import com.rikkei.meetup.ultis.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter implements RegisterContract.Presenter {

    public static final int MIN_LENGTH_PASSWORD = 6;
    public static final int MAX_LENGTH_PASSWORD = 16;

    private RegisterContract.View mView;
    private UsersRepository mUsersRepository;
    private CompositeDisposable mCompositeDisposable;

    public RegisterPresenter(RegisterContract.View view) {
        mView = view;
        mUsersRepository = UsersRepository
                .getInstance(UsersRemoteDateSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void register(String fullname, final String email, String password) {
        if(!StringUtils.checkEmail(email)) {
            mView.showErrorEmail();
            return;
        }
        if(password.length() < MIN_LENGTH_PASSWORD || password.length() > MAX_LENGTH_PASSWORD) {
            mView.showErrorPassword();
            return;
        }
        mView.showProgress();
        Disposable disposable = mUsersRepository.register(fullname, email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<TokenResponse>() {
                    @Override
                    public void accept(TokenResponse tokenResponse) throws Exception {
                        mView.hideProgress();
                        if(tokenResponse.getStatus() == 0) {
                            mView.showError();
                        } else {
                            mView.navigationToProfile();
                            StringUtils.saveToken(mView.getViewContext(),
                                    tokenResponse.getToken().getToken(), email);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.hideProgress();
                        mView.showError();
                        System.out.println(throwable.toString());
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
