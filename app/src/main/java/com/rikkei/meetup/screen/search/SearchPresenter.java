package com.rikkei.meetup.screen.search;

import android.util.Log;

import com.google.gson.Gson;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.networking.ApiClient;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View mView;
    private EventsRepository mEventsRepository;
    private CompositeDisposable mCompositeDisposable;

    public SearchPresenter(SearchContract.View view) {
        mView = view;
        mEventsRepository
                = EventsRepository.getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getEventsByKeyword(String token, String keyword, int pageIndex, int pageSize) {
        Disposable disposable = mEventsRepository.getEventsByKeyword(token, keyword, pageIndex, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<EventsResponse>() {
                    @Override
                    public void accept(EventsResponse eventsResponse) throws Exception {
                        List<Event> events = eventsResponse.getListEvents().getEvents();
                        if (events.size() == 0) {
                            mView.noResultSearching();
                        } else {
                            mView.showEvents(eventsResponse.getListEvents().getEvents());
                        }
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
