package com.rikkei.meetup.screen.browser;

import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BrowserPresenter implements BrowserContract.Presenter {

    private BrowserContract.View mView;
    private EventsRepository mEventsRepository;
    private CompositeDisposable mCompositeDisposable;

    public BrowserPresenter(BrowserContract.View view) {
        mView = view;
        mEventsRepository
                = EventsRepository.getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getGenres() {
        Disposable disposable = mEventsRepository.getListGenres()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<GenresResponse>() {
                    @Override
                    public void accept(GenresResponse genresResponse) throws Exception {
                        mView.showEvents(genresResponse.getListGenre().getGenres());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showError();
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
