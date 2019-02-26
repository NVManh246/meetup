package com.rikkei.meetup.data.source.remote;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.networking.ApiClient;
import com.rikkei.meetup.data.source.EventsDataSource;

import io.reactivex.Flowable;

public class EventsRemoteDataSource implements EventsDataSource.EventsRemoteDataSource {

    private static EventsRemoteDataSource sIntance;
    private ApiClient mApiClient;

    private EventsRemoteDataSource(ApiClient apiClient) {
        mApiClient = apiClient;
    }

    public static EventsRemoteDataSource getInstance(ApiClient apiClient) {
        if (sIntance == null) {
            sIntance = new EventsRemoteDataSource(apiClient);
        }
        return sIntance;
    }

    @Override
    public Flowable<EventsResponse> getListEvents(int pageIndex, int pageSize) {
        return mApiClient.getListEvents(pageIndex, pageSize);
    }

    @Override
    public Flowable<GenresResponse> getListGenres() {
        return mApiClient.getListGenres();
    }
}
