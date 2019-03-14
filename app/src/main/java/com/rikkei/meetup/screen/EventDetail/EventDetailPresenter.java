package com.rikkei.meetup.screen.EventDetail;

import android.util.Log;

import com.google.gson.Gson;
import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EventDetailPresenter implements EventDetailContract.Presenter {

    private EventDetailContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EventsRepository mEventsRepository;

    public EventDetailPresenter(EventDetailContract.View view) {
        mView = view;
        mEventsRepository = EventsRepository
                .getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void followVenue(String token, long venueId) {
        Disposable disposable = mEventsRepository.followVenue(token, venueId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(Message message) throws Exception {
                        Log.d("kiemtra", new Gson().toJson(message));
                        if(message.getStatus() != 0) {
                            mView.showFollowSuccess();
                        } else {
                            mView.showUpdateError(message.getErrorMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showErrorServer();
                        System.out.println(throwable.toString());
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void updateStatusEvent(String token, long status, long eventId) {
        Disposable disposable = mEventsRepository.updateStatusEvent(token, status, eventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Message>() {
                    @Override
                    public void accept(Message message) throws Exception {
                        if(message.getStatus() != 0) {
                            mView.showUpdateSuccess();
                        } else {
                            mView.showUpdateError(message.getErrorMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showErrorServer();
                        System.out.println(throwable.toString());
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
