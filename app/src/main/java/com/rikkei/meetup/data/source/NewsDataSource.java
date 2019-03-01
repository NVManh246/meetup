package com.rikkei.meetup.data.source;

import com.rikkei.meetup.data.model.news.NewsResponse;

import io.reactivex.Flowable;

public interface NewsDataSource {
    interface NewsRemoteDataSource {
        Flowable<NewsResponse> getListNews(int pageIndex, int pageSize);
    }
}
