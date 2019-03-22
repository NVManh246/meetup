package com.rikkei.meetup.screen.home.news;

import android.content.Context;

import com.rikkei.meetup.data.model.news.News;

import java.util.List;

public interface NewsContract {
    interface View {
        void hideProgress();
        void showListNews(List<News> news);
        void showError();
        void showEndData();
        Context getViewContext();
    }

    interface Presenter {
        void getListNews(int pageIndex, int pageSize);
        void getListNewsDB(int pageIndex, int pageSize);
        void saveNewsDB();
        void getCount();
    }
}
