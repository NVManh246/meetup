package com.rikkei.meetup.screen.login;

import com.rikkei.meetup.data.model.user.TokenResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.UsersRemoteDateSource;
import com.rikkei.meetup.data.source.repository.UsersRepository;
import com.rikkei.meetup.ultis.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.rikkei.meetup.screen.signup.RegisterPresenter.MAX_LENGTH_PASSWORD;
import static com.rikkei.meetup.screen.signup.RegisterPresenter.MIN_LENGTH_PASSWORD;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private UsersRepository mUsersRepository;
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mUsersRepository = UsersRepository
                .getInstance(UsersRemoteDateSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void login(final String email, String password) {
        if(!StringUtils.checkEmail(email)) {
            mView.showErrorEmail();
            return;
        }
        if(password.length() < MIN_LENGTH_PASSWORD || password.length() > MAX_LENGTH_PASSWORD) {
            mView.showErrorPassword();
            return;
        }
        mView.showProgress();
        Disposable disposable = mUsersRepository.login(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<TokenResponse>() {
                    @Override
                    public void accept(TokenResponse tokenResponse) throws Exception {
                        if(tokenResponse.getStatus() == 0) {
                            mView.hideProgress();
                            mView.showError();
                        } else {
                            StringUtils.saveToken(mView.getViewContext(),
                                    tokenResponse.getToken().getToken(), email);
                            mView.navigationToProfile();
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
