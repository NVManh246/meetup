package com.rikkei.meetup.screen.profile;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.venue.VenuesResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EventStatusPresenter implements EventStatusContract.Presenter {

    private EventStatusContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EventsRepository mEventsRepository;

    public EventStatusPresenter(EventStatusContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mEventsRepository = EventsRepository
                .getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
    }

    @Override
    public void getMyEvetns(String token, int status) {
        Disposable disposable = mEventsRepository.getMyEvents(token, status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<EventsResponse>() {
                    @Override
                    public void accept(EventsResponse eventsResponse) throws Exception {
                        mView.showEvents(eventsResponse.getListEvents().getEvents());
                        mView.hideProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.hideProgress();
                        mView.showError();
                        System.out.printf(throwable.toString());
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getVenuesFollowed(String token) {
        Disposable disposable = mEventsRepository.getVenuesFollowed(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<VenuesResponse>() {
                    @Override
                    public void accept(VenuesResponse venuesResponse) throws Exception {
                        mView.showVenues(venuesResponse.getListVenue().getVenues());
                        mView.hideProgress();
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
