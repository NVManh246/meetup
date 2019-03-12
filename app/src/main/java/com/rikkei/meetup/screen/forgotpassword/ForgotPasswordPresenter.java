package com.rikkei.meetup.screen.forgotpassword;

import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.UsersRemoteDateSource;
import com.rikkei.meetup.data.source.repository.UsersRepository;
import com.rikkei.meetup.ultis.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {

    private ForgotPasswordContract.View mView;
    private UsersRepository mUsersRepository;
    private CompositeDisposable mCompositeDisposable;

    public ForgotPasswordPresenter(ForgotPasswordContract.View view) {
        mView = view;
        mUsersRepository = UsersRepository
                .getInstance(UsersRemoteDateSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void forgotPassword(String email) {
        if(!StringUtils.checkEmail(email)) {
            mView.showErrorEmail();
            return;
        }
        mView.showProgress();
        Disposable disposable = mUsersRepository.resetPassword(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(Message message) throws Exception {
                        mView.hideProgress();
                        if(message.getStatus() == 0) {
                            mView.showError();
                        } else {
                            mView.showSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.hideProgress();
                        mView.showError();
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
