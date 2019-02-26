package com.rikkei.meetup.data.source.repository;

import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.source.NewsDataSource;
import com.rikkei.meetup.data.source.remote.NewsRemoteDataSource;

import io.reactivex.Flowable;

public class NewsRepository implements NewsDataSource.NewsRemoteDataSource {

    private static NewsRepository sIntance;
    private NewsDataSource.NewsRemoteDataSource mRemoteDataSource;

    private NewsRepository(NewsRemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public static NewsRepository getIntance(NewsRemoteDataSource remoteDataSource) {
        if(sIntance == null) {
            sIntance = new NewsRepository(remoteDataSource);
        }
        return sIntance;
    }

    @Override
    public Flowable<NewsResponse> getListNews(int pageIndex, int pageSize) {
        return mRemoteDataSource.getListNews(pageIndex, pageSize);
    }
}
