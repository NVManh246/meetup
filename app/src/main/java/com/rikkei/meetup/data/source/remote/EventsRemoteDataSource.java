package com.rikkei.meetup.data.source.remote;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.venue.VenuesResponse;
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
    public Flowable<EventsResponse> getListEvents(String token, int pageIndex, int pageSize) {
        return mApiClient.getListEvents(token, pageIndex, pageSize);
    }

    @Override
    public Flowable<EventsResponse> getEventsByCategory(String token, int categoryId, int pageIndex, int pageSize) {
        return mApiClient.getEventsByCategory(token, categoryId, pageIndex, pageSize);
    }

    @Override
    public Flowable<EventsResponse> getEventsByCategory(String token, int categoryId) {
        return mApiClient.getEventsByCategory(token, categoryId);
    }

    @Override
    public Flowable<EventsResponse> getEventsByKeyword(String token, String keyword, int pageIndex, int pageSize) {
        return mApiClient.getEventsByKeyword(token, keyword, pageIndex, pageSize);
    }

    @Override
    public Flowable<Message> updateStatusEvent(String token, long status, long evetnId) {
        return mApiClient.updateEvent(token, status, evetnId);
    }

    @Override
    public Flowable<Message> followVenue(String token, long venueId) {
        return mApiClient.followVenue(token, venueId);
    }

    @Override
    public Flowable<GenresResponse> getListGenres() {
        return mApiClient.getListGenres();
    }

    @Override
    public Flowable<EventsResponse> getMyEvents(String token, int status) {
        return mApiClient.getMyEvents(token, status);
    }

    @Override
    public Flowable<VenuesResponse> getVenuesFollowed(String token) {
        return mApiClient.getVenuesFollowed(token);
    }
}
