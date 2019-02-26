package com.rikkei.meetup.data.source;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.venue.VenuesResponse;

import io.reactivex.Flowable;

public interface EventsDataSource {
    interface EventsRemoteDataSource {
        Flowable<EventsResponse> getListEvents(int pageIndex, int pageSize);
        Flowable<EventsResponse> getEventsByCategory(int categoryId, int pageIndex, int pageSize);
        Flowable<EventsResponse> getEventsByCategory(int categoryId);
        Flowable<EventsResponse> getEventsByKeyword(String keyword, int pageIndex, int pageSize);
        Flowable<Message> updateStatusEvent(String token, long status, long evetnId);
        Flowable<Message> followVenue(String token, long venueId);
        Flowable<GenresResponse> getListGenres();
        Flowable<EventsResponse> getMyEvents(String token, int status);
        Flowable<VenuesResponse> getVenuesFollowed(String token);
    }
}
