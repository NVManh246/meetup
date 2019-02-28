package com.rikkei.meetup.data.source.repository;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.source.EventsDataSource;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;

import io.reactivex.Flowable;

public class EventsRepository implements EventsDataSource.EventsRemoteDataSource {

    private static EventsRepository sIntance;
    private EventsDataSource.EventsRemoteDataSource mRemoteDataSource;

    private EventsRepository(EventsRemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public static EventsRepository getInstance(EventsRemoteDataSource remoteDataSource) {
        if(sIntance == null) {
            sIntance = new EventsRepository(remoteDataSource);
        }
        return sIntance;
    }

    @Override
    public Flowable<EventsResponse> getListEvents(int pageIndex, int pageSize) {
        return mRemoteDataSource.getListEvents(pageIndex, pageSize);
    }

    @Override
    public Flowable<EventsResponse> getEventsByCategory(int categoryId, int pageIndex, int pageSize) {
        return mRemoteDataSource.getEventsByCategory(categoryId, pageIndex, pageSize);
    }

    @Override
    public Flowable<EventsResponse> getEventsByCategory(int categoryId) {
        return mRemoteDataSource.getEventsByCategory(categoryId);
    }

    @Override
    public Flowable<EventsResponse> getEventsByKeyword(String keyword, int pageIndex, int pageSize) {
        return mRemoteDataSource.getEventsByKeyword(keyword, pageIndex, pageSize);
    }

    @Override
    public Flowable<GenresResponse> getListGenres() {
        return mRemoteDataSource.getListGenres();
    }
}
