package com.rikkei.meetup.data.networking;

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

}
