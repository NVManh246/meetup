package com.rikkei.meetup.data.source.remote;

import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.networking.ApiClient;
import com.rikkei.meetup.data.source.NewsDataSource;

import io.reactivex.Flowable;

public class NewsRemoteDataSource implements NewsDataSource.NewsRemoteDataSource {

    private static NewsRemoteDataSource sIntance;
    private ApiClient mApiClient;

    private NewsRemoteDataSource(ApiClient apiClient) {
        mApiClient = apiClient;
    }

    public static NewsRemoteDataSource getInstance(ApiClient apiClient) {
        if(sIntance == null) {
            sIntance = new NewsRemoteDataSource(apiClient);
        }
        return sIntance;
    }

    @Override
    public Flowable<NewsResponse> getListNews(int pageIndex, int pageSize) {
        return mApiClient.getListNews(pageIndex, pageSize);
    }
}
