package com.rikkei.meetup.data.source;

import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.data.model.news.NewsResponse;

import java.util.List;

import io.reactivex.Flowable;

public interface NewsDataSource {
    interface NewsRemoteDataSource {
        Flowable<NewsResponse> getListNews(int pageIndex, int pageSize);
        Flowable<NewsResponse> getListNews();
    }

    interface NewsLocalDataSource {
        Flowable<List<News>> getListNewsDB(int pageIndex, int pageSize);
        void insertNews(List<News> news);
        void deleteNews();
        Flowable<Integer> getCount();
    }
}
