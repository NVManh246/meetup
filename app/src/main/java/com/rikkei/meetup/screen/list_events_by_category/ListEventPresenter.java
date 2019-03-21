package com.rikkei.meetup.screen.list_events_by_category;

import android.text.TextUtils;

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

public class ListEventPresenter implements ListEventContract.Presenter {

    private ListEventContract.View mView;
    private EventsRepository mEventsRepository;
    private CompositeDisposable mCompositeDisposable;

    public ListEventPresenter(ListEventContract.View view) {
        mView = view;
        mEventsRepository
                = EventsRepository.getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getEventsByCategory(String token, int categoryId, int pageIndex, int pageSize) {
        if(pageIndex == -1 && pageSize == -1) {
            Disposable disposable = mEventsRepository.getEventsByCategory(token, categoryId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<EventsResponse>() {
                        @Override
                        public void accept(EventsResponse eventsResponse) throws Exception {
                            classifyEvents(eventsResponse.getListEvents().getEvents());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mView.showError();
                        }
                    });
            mCompositeDisposable.add(disposable);
        } else {
            Disposable disposable = mEventsRepository.getEventsByCategory(token, categoryId, pageIndex, pageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<EventsResponse>() {
                        @Override
                        public void accept(EventsResponse eventsResponse) throws Exception {
                            List<Event> events = eventsResponse.getListEvents().getEvents();
                            if (events.size() == 0) {
                                mView.endData();
                            } else {
                                mView.showEvents(events);
                            }
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

    private void classifyEvents(List<Event> events) throws ParseException {
        List<Event> eventsToday = new ArrayList<>();
        List<Event> eventsTomorrow = new ArrayList<>();
        List<Event> eventsWeekend = new ArrayList<>();
        List<Event> eventsNextWeek = new ArrayList<>();
        List<Event> eventsEndMonth = new ArrayList<>();
        List<Event> eventsNextMonth = new ArrayList<>();
        List<Event> eventsForever = new ArrayList<>();

        //Ends
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        Date currentDate = calendar.getTime();
        int currentWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);

        SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.DATE_FORMAT);
        for(Event event : events) {
            if(TextUtils.isEmpty(event.getScheduleEndDate())){
                eventsForever.add(event);
                continue;
            }
            Date endDate = dateFormat.parse(event.getScheduleEndDate());
            calendar.setTime(endDate);
            int week = calendar.get(Calendar.WEEK_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int dayOffset = getDayOffset(currentDate, endDate);
            System.out.println(dayOffset);
            if(dayOffset == 0) {
                eventsToday.add(event);
            } else if(dayOffset == 1) {
                eventsTomorrow.add(event);
            } else if(dayOffset >= 2 && week == currentWeek && month == currentMonth) {
                eventsWeekend.add(event);
            } else if(dayOffset >= 3 && (week - 1) == currentWeek && month == currentMonth) {
                eventsNextWeek.add(event);
            } else if(dayOffset >= 8 && month == currentMonth) {
                eventsEndMonth.add(event);
            } else if(dayOffset >= 9 && month > currentMonth) {
                eventsNextMonth.add(event);
            }
        }

        if(eventsToday.size() != 0) {
            mView.showEventsToday(eventsToday);
        }
        if(eventsTomorrow.size() != 0) {
            mView.showEventsTomorrow(eventsTomorrow);
        }
        if(eventsWeekend.size() != 0) {
            mView.showEventsWeekend(eventsWeekend);
        }
        if(eventsNextWeek.size() != 0) {
            mView.showEventsNextWeek(eventsNextWeek);
        }
        if(eventsEndMonth.size() != 0) {
            mView.showEventsEndMonth(eventsEndMonth);
        }
        if(eventsNextMonth.size() != 0) {
            mView.showEventsNextMonth(eventsNextMonth);
        }
        if(eventsForever.size() != 0) {
            mView.showEventsForever(eventsForever);
        }
    }

    private int getDayOffset(Date currentDate, Date date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtils.DATE_FORMAT);
        String current = dateFormat.format(currentDate);
        Date date1 = dateFormat.parse(current);
        long currentTime = date1.getTime();
        long time = date.getTime();
        long timeOffset = time - currentTime;
        int dayOffet = (int) (timeOffset/StringUtils.TIME_MILLIS_OF_DAY);
        return dayOffet;
    }
}
