package com.rikkei.meetup.data.networking;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.user.TokenResponse;
import com.rikkei.meetup.data.model.venue.VenuesResponse;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("listNews")
    Flowable<NewsResponse> getListNews(
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @GET("listPopularEvents")
    Flowable<EventsResponse> getListEvents(
            @Header("Authorization") String token,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @GET("listCategories")
    Flowable<GenresResponse> getListGenres();

    @GET("listEventsByCategory")
    Flowable<EventsResponse> getEventsByCategory(
            @Header("Authorization") String token,
            @Query("category_id") int categoryId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @GET("listEventsByCategory")
    Flowable<EventsResponse> getEventsByCategory(
            @Header("Authorization") String token,
            @Query("category_id") int categoryId
    );

    @GET("search")
    Flowable<EventsResponse> getEventsByKeyword(
            @Header("Authorization") String token,
            @Query("keyword") String keyword,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @POST("register")
    @FormUrlEncoded
    Flowable<TokenResponse> register(
            @Field("name") String fullname,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("login")
    @FormUrlEncoded
    Flowable<TokenResponse> login(@Field("email") String email, @Field("password") String password);

    @POST("resetPassword")
    @FormUrlEncoded
    Flowable<Message> resetPassword(@Field("email") String email);

    @POST("doUpdateEvent")
    @FormUrlEncoded
    Flowable<Message> updateEvent(
            @Header("Authorization") String token,
            @Field("status") long status,
            @Field("event_id") long eventId
    );

    @POST("doFollowVenue")
    @FormUrlEncoded
    Flowable<Message> followVenue(
            @Header("Authorization") String token,
            @Field("venue_id") long venueId
    );

    @GET("listMyEvents")
    Flowable<EventsResponse> getMyEvents(
            @Header("Authorization") String token,
            @Query("status") int status
    );

    @GET("listVenueFollowed")
    Flowable<VenuesResponse> getVenuesFollowed(@Header("Authorization") String token);

    @GET("listNearlyEvents")
    Flowable<EventsResponse> getNearEvents(
            @Header("Authorization") String token,
            @Query("radius") int radius,
            @Query("longitue") String geoLong,
            @Query("latitude") String geoLat
    );
}
