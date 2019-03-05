package com.rikkei.meetup.screen.home.news;

import android.util.Log;

import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.remote.NewsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.NewsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenter implements NewsContract.Presenter {

    private NewsContract.View mView;
    private NewsRepository mNewsRepository;
    private CompositeDisposable mCompositeDisposable;

    public NewsPresenter(NewsContract.View view) {
        mView = view;
        mNewsRepository
                = NewsRepository.getIntance(NewsRemoteDataSource.getInstance(ApiUtils.getApi()));
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getListNews(int pageIndex, int pageSize) {
        Disposable disposable = mNewsRepository.getListNews(pageIndex, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NewsResponse>() {
                    @Override
                    public void accept(NewsResponse newsResponse) throws Exception {
                        mView.showListNews(newsResponse.getListNews().getNews());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showError();
                    }
                });

        mCompositeDisposable.add(disposable);
    }
}
