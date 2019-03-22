package com.rikkei.meetup.screen.home.news;

import android.os.Handler;
import android.util.Log;

import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.local.Database.NewsDatabase;
import com.rikkei.meetup.data.source.local.NewsLocalDataSource;
import com.rikkei.meetup.data.source.remote.NewsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.NewsRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
        NewsDatabase newsDatabase = NewsDatabase.getInstance(mView.getViewContext());
        mNewsRepository = NewsRepository.getIntance(
                NewsRemoteDataSource.getInstance(ApiUtils.getApi()),
                NewsLocalDataSource.getInstance(newsDatabase.newsDao()));
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
                        List<News> newsList = newsResponse.getListNews().getNews();
                        if(newsList.isEmpty()) {
                            mView.showEndData();
                        } else {
                            mView.showListNews(newsResponse.getListNews().getNews());
                        }
                        mView.hideProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.hideProgress();
                        mView.showError();
                        System.out.println(throwable.toString());
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListNewsDB(int pageIndex, int pageSize) {
        Disposable disposable = mNewsRepository.getListNewsDB(pageIndex, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<News>>() {
                    @Override
                    public void accept(final List<News> news) throws Exception {
                        if(news.isEmpty()) {
                            mView.showEndData();
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mView.showListNews(news);
                                }
                            }, 1000);
                        }
                        mView.hideProgress();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void saveNewsDB() {
        Disposable disposable = mNewsRepository.getListNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NewsResponse>() {
                    @Override
                    public void accept(NewsResponse newsResponse) throws Exception {
                        List<News> newsList = newsResponse.getListNews().getNews();
                        saveAll(newsList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.toString());
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getCount() {
        Disposable disposable = mNewsRepository.getCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.toString());
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void saveAll(final List<News> news) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                mNewsRepository.insertNews(news);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
