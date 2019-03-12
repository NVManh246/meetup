package com.rikkei.meetup.data.networking;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.model.user.Message;
import com.rikkei.meetup.data.model.user.TokenResponse;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @GET("listCategories")
    Flowable<GenresResponse> getListGenres();

    @GET("listEventsByCategory")
    Flowable<EventsResponse> getEventsByCategory(
            @Query("category_id") int categoryId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @GET("listEventsByCategory")
    Flowable<EventsResponse> getEventsByCategory(@Query("category_id") int categoryId);

    @GET("search")
    Flowable<EventsResponse> getEventsByKeyword(
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
}
