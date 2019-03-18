package com.rikkei.meetup.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.data.model.news.NewsResponse;
import com.rikkei.meetup.data.networking.ApiUtils;
import com.rikkei.meetup.data.source.local.Database.NewsDAO;
import com.rikkei.meetup.data.source.local.Database.NewsDatabase;
import com.rikkei.meetup.data.source.local.NewsLocalDataSource;
import com.rikkei.meetup.data.source.remote.NewsRemoteDataSource;
import com.rikkei.meetup.data.source.repository.NewsRepository;
import com.rikkei.meetup.screen.main.MainActivity;
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

public class UpdateJobIntentService extends JobIntentService {

    private static final String CHANNEL_ID = "channel_update";
    private static final int NOTIFICATION_ID = 1;
    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, UpdateJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        showNotification(getString(R.string.update_data));
        updateData(getApplicationContext());
        AlarmReceiver.setAlarm(getApplicationContext(), false);
        stopSelf();
    }

    private void showNotification(String content) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_app_noti)
                .setContentTitle(getString(R.string.update))
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setChannelId(CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.update), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_ID, notificationCompatBuilder.build());
    }

    private void updateData(Context context) {
        System.out.println(getString(R.string.update));
        NewsDAO newsDAO = NewsDatabase.getInstance(context).newsDao();
        NewsRepository newsRepository = NewsRepository.getIntance(
                NewsRemoteDataSource.getInstance(ApiUtils.getApi()),
                NewsLocalDataSource.getInstance(newsDAO));
        saveNewsDB(newsRepository);
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
                        showNotification(StringUtils
                                .getContentNotification(getApplicationContext(), newsList.size()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.toString());
                        showNotification(getString(R.string.error));
                    }
                });

        new CompositeDisposable().add(disposable);
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
        new CompositeDisposable().add(disposable);
    }
}
