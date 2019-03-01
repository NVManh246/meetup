package com.rikkei.meetup.data.networking;

import com.rikkei.meetup.data.model.event.EventsResponse;
import com.rikkei.meetup.data.model.genre.GenresResponse;
import com.rikkei.meetup.data.model.news.NewsResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
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

    @GET("search")
    Flowable<EventsResponse> getEventsByKeyword(
            @Query("keyword") String keyword,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );
}
