package com.rikkei.meetup.data.source;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;

import io.reactivex.Flowable;

public interface EventsDataSource {
    interface EventsRemoteDataSource {
        Flowable<EventsResponse> getListEvents(int pageIndex, int pageSize);
        Flowable<EventsResponse> getEventsByCategory(int categoryId, int pageIndex, int pageSize);
        Flowable<EventsResponse> getEventsByKeyword(String keyword, int pageIndex, int pageSize);
        Flowable<GenresResponse> getListGenres();
    }
}
