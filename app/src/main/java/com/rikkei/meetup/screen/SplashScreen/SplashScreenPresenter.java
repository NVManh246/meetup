package com.rikkei.meetup.screen.SplashScreen;

import android.os.Handler;

import com.rikkei.meetup.data.model.venue.VenuesResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;
import com.rikkei.meetup.ultis.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenPresenter implements SplashScreenContract.Presenter {

    private SplashScreenContract.View mView;
    private EventsRepository mEventsRepository;
    private CompositeDisposable mCompositeDisposable;

    public SplashScreenPresenter(SplashScreenContract.View view) {
        mView = view;
        mEventsRepository = EventsRepository
                .getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }
    @Override
    public void checkTokenExpired(String token) {
        Disposable disposable = mEventsRepository.getVenuesFollowed(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<VenuesResponse>() {
                    @Override
                    public void accept(VenuesResponse venuesResponse) throws Exception {
                        if(venuesResponse.getStatus() == 0) {
                            mView.showResult();
                            StringUtils.saveToken(mView.getViewContext(), null, null);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.navigationToMain();
                            }
                        }, 1500);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.toString());
                        StringUtils.saveToken(mView.getViewContext(), null, null);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.navigationToMain();
                            }
                        }, 1500);
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
