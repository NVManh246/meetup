package com.rikkei.meetup.screen.SplashScreen;

import android.os.Handler;
import android.util.Log;

import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.model.venue.VenuesResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.local.Database.NewsDAO;
import com.rikkei.meetup.data.source.local.Database.NewsDatabase;
import com.rikkei.meetup.data.source.local.NewsLocalDataSource;
import com.rikkei.meetup.data.source.remote.EventsRemoteDataSource;
import com.rikkei.meetup.data.source.remote.NewsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.EventsRepository;
import com.rikkei.meetup.data.source.repository.NewsRepository;
import com.rikkei.meetup.ultis.StringUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenPresenter implements SplashScreenContract.Presenter {

    private SplashScreenContract.View mView;
    private EventsRepository mEventsRepository;
    private NewsRepository mNewsRepository;
    private CompositeDisposable mCompositeDisposable;

    public SplashScreenPresenter(SplashScreenContract.View view) {
        mView = view;
        mEventsRepository = EventsRepository
                .getInstance(EventsRemoteDataSource.getInstance(ApiUtils.getApi()));
        NewsDAO newsDAO = NewsDatabase.getInstance(mView.getViewContext()).newsDao();
        mNewsRepository = NewsRepository.getIntance(NewsRemoteDataSource.getInstance(ApiUtils.getApi()),
                NewsLocalDataSource.getInstance(newsDAO));
        mCompositeDisposable = new CompositeDisposable();
    }
    @Override
    public void checkTokenExpired(String token) {
        Disposable disposable = mEventsRepository.getVenuesFollowed(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<VenuesResponse>() {
                    @Override
                    public void accept(VenuesResponse venuesResponse) throws Exception {
                        if(venuesResponse.getStatus() == 0) {
                            mView.showResult();
                            StringUtils.saveToken(mView.getViewContext(), null, null);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.navigationToMain();
                            }
                        }, 1500);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.toString());
                        StringUtils.saveToken(mView.getViewContext(), null, null);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mView.navigationToMain();
                            }
                        }, 1500);
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void saveNewsIfRunFirstTime() {
        saveNewsDB(mNewsRepository);
    }


    private void saveNewsDB(final NewsRepository newsRepository) {
        Disposable disposable = newsRepository.getListNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NewsResponse>() {
                    @Override
                    public void accept(NewsResponse newsResponse) throws Exception {
                        List<News> newsList = newsResponse.getListNews().getNews();
                        saveAll(newsRepository, newsList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.toString());
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    private void saveAll(final NewsRepository newsRepository, final List<News> news) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                newsRepository.insertNews(news);
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
                        System.out.println(throwable.toString());
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
