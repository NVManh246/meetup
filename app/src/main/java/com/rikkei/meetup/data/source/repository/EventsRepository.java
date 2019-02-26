package com.rikkei.meetup.data.source.repository;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.venue.VenuesResponse;
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
    public Flowable<EventsResponse> getListEvents(String token, int pageIndex, int pageSize) {
        return mRemoteDataSource.getListEvents(token, pageIndex, pageSize);
    }

    @Override
    public Flowable<EventsResponse> getEventsByCategory(String token, int categoryId, int pageIndex, int pageSize) {
        return mRemoteDataSource.getEventsByCategory(token, categoryId, pageIndex, pageSize);
    }

    @Override
    public Flowable<EventsResponse> getEventsByCategory(String token, int categoryId) {
        return mRemoteDataSource.getEventsByCategory(token, categoryId);
    }

    @Override
    public Flowable<EventsResponse> getEventsByKeyword(String token, String keyword, int pageIndex, int pageSize) {
        return mRemoteDataSource.getEventsByKeyword(token, keyword, pageIndex, pageSize);
    }

    @Override
    public Flowable<Message> updateStatusEvent(String token, long status, long evetnId) {
        return mRemoteDataSource.updateStatusEvent(token, status, evetnId);
    }

    @Override
    public Flowable<Message> followVenue(String token, long venueId) {
        return mRemoteDataSource.followVenue(token, venueId);
    }

    @Override
    public Flowable<GenresResponse> getListGenres() {
        return mRemoteDataSource.getListGenres();
    }

    @Override
    public Flowable<EventsResponse> getMyEvents(String token, int status) {
        return mRemoteDataSource.getMyEvents(token, status);
    }

    @Override
    public Flowable<VenuesResponse> getVenuesFollowed(String token) {
        return mRemoteDataSource.getVenuesFollowed(token);
    }

    @Override
    public Flowable<EventsResponse> getNearEvents(String token, int radius, String geoLong, String geoLat) {
        return mRemoteDataSource.getNearEvents(token, radius, geoLong, geoLat);
    }
}
