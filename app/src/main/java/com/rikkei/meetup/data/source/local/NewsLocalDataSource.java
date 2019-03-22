package com.rikkei.meetup.data.source.local;

import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.data.source.NewsDataSource;
import com.rikkei.meetup.data.source.local.Database.NewsDAO;

import java.util.List;

import io.reactivex.Flowable;

public class NewsLocalDataSource implements NewsDataSource.NewsLocalDataSource {

    private static NewsLocalDataSource sIntance;
    private NewsDAO mNewsDAO;

    private NewsLocalDataSource(NewsDAO newsDAO) {
        mNewsDAO = newsDAO;
    }

    public static NewsLocalDataSource getInstance(NewsDAO newsDAO) {
        if(sIntance == null) {
            sIntance = new NewsLocalDataSource(newsDAO);
        }
        return sIntance;
    }

    @Override
    public Flowable<List<News>> getListNewsDB(int pageIndex, int pageSize) {
        return mNewsDAO.getNews(pageIndex, pageSize);
    }

    @Override
    public void insertNews(List<News> news) {
        mNewsDAO.insertNews(news);
    }

    @Override
    public void deleteNews() {
        mNewsDAO.deleteAllNews();
    }

    @Override
    public Flowable<Integer> getCount() {
        return mNewsDAO.getCount();
    }
}
