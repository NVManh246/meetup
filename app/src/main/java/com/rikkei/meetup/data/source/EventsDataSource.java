package com.rikkei.meetup.data.source;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.venue.VenuesResponse;

import io.reactivex.Flowable;

public interface EventsDataSource {
    interface EventsRemoteDataSource {
        Flowable<EventsResponse> getListEvents(String token, int pageIndex, int pageSize);
        Flowable<EventsResponse> getEventsByCategory(String token, int categoryId, int pageIndex, int pageSize);
        Flowable<EventsResponse> getEventsByCategory(String token, int categoryId);
        Flowable<EventsResponse> getEventsByKeyword(String token, String keyword, int pageIndex, int pageSize);
        Flowable<Message> updateStatusEvent(String token, long status, long evetnId);
        Flowable<Message> followVenue(String token, long venueId);
        Flowable<GenresResponse> getListGenres();
        Flowable<EventsResponse> getMyEvents(String token, int status);
        Flowable<VenuesResponse> getVenuesFollowed(String token);
        Flowable<EventsResponse> getNearEvents(String token, int radius, String geoLong, String geoLat);
    }
}
