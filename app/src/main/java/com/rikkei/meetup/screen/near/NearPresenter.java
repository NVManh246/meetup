package com.rikkei.meetup.screen.near;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NearPresenter implements NearContract.Presenter {

    private NearContract.View mView;
    private EventsRepository mEventsRepository;
    private CompositeDisposable mCompositeDisposable;

    public NearPresenter(NearContract.View view) {
        mView = view;
        mEventsRepository = EventsRepository
                .getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getNearEvents(String token, int radius, String geoLong, String geoLat) {
        Disposable disposable = mEventsRepository.getNearEvents(token, radius, geoLong, geoLat)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<EventsResponse>() {
                    @Override
                    public void accept(EventsResponse eventsResponse) throws Exception {
                        mView.showEvents(eventsResponse.getListEvents().getEvents());
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
