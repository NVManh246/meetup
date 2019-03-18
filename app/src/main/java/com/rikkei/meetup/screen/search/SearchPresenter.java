package com.rikkei.meetup.screen.search;

import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;
import com.rikkei.meetup.ultis.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                            mView.hideProgress();
                            mView.setCount();
                        } else {
                            filterEvents(events);
                            mView.hideProgress();
                        }
                        mView.hideAlertConnectionError();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showError();
                        mView.hideProgress();
                        mView.showAlertConnectionError();
                        mView.setCount();
                        throwable.printStackTrace();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void filterEvents(List<Event> events) throws ParseException {
        List<Event> eventsUpComing = new ArrayList<>();
        List<Event> eventsPass = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.DATE_FORMAT);
        Date currentDate = Calendar.getInstance().getTime();

        for (Event event : events) {
            Date endDate = dateFormat.parse(event.getScheduleEndDate());
            if (endDate.before(currentDate)) {
                eventsPass.add(event);
            } else {
                eventsUpComing.add(event);
            }
        }
        mView.showEventsPass(eventsPass);
        mView.showEventsUpComing(eventsUpComing);
        mView.setCount();
    }
}
