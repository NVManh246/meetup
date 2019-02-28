package com.rikkei.meetup.data.source;

import com.rikkei.meetup.data.model.event.EventsResponse;

import io.reactivex.Flowable;

public interface EventsDataSource {
    interface EventsRemoteDataSource {
        Flowable<EventsResponse> getListEvents(int pageIndex, int pageSize);
    }
}
