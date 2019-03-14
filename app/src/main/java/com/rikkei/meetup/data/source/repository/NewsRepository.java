package com.rikkei.meetup.data.source.repository;

import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.source.NewsDataSource;
import com.rikkei.meetup.data.source.local.NewsLocalDataSource;
import com.rikkei.meetup.data.source.remote.NewsRemoteDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class NewsRepository implements NewsDataSource.NewsRemoteDataSource,
        NewsDataSource.NewsLocalDataSource {

    private static NewsRepository sIntance;
    private NewsDataSource.NewsRemoteDataSource mRemoteDataSource;
    private NewsDataSource.NewsLocalDataSource mLocalDataSource;

    private NewsRepository(
            NewsRemoteDataSource remoteDataSource, NewsLocalDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static NewsRepository getIntance(
            NewsRemoteDataSource remoteDataSource,  NewsLocalDataSource localDataSource) {
        if(sIntance == null) {
            sIntance = new NewsRepository(remoteDataSource, localDataSource);
        }
        return sIntance;
    }

    @Override
    public Flowable<NewsResponse> getListNews(int pageIndex, int pageSize) {
        return mRemoteDataSource.getListNews(pageIndex, pageSize);
    }

    @Override
    public Flowable<NewsResponse> getListNews() {
        return mRemoteDataSource.getListNews();
    }

    @Override
    public Flowable<List<News>> getListNewsDB(int pageIndex, int pageSize) {
        return mLocalDataSource.getListNewsDB(pageIndex, pageSize);
    }

    @Override
    public void insertNews(List<News> news) {
        mLocalDataSource.insertNews(news);
    }

    @Override
    public void deleteNews() {
        mLocalDataSource.deleteNews();
    }

    @Override
    public Flowable<Integer> getCount() {
        return mLocalDataSource.getCount();
    }
}
