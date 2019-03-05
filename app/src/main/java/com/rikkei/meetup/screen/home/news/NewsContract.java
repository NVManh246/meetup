package com.rikkei.meetup.screen.home.news;

import com.rikkei.meetup.data.model.news.News;

import java.util.List;

public interface NewsContract {
    interface View {
        void showListNews(List<News> news);
        void showError();
    }

    interface Presenter {
        void getListNews(int pageIndex, int pageSize);
    }
}
