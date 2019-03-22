package com.rikkei.meetup.data.source.local.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rikkei.meetup.data.model.news.News;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface NewsDAO {
    @Query("SELECT * FROM tblNews ORDER BY id LIMIT :pageIndex * :pageSize, :pageSize")
    Flowable<List<News>> getNews(int pageIndex, int pageSize);

    @Query("SELECT * FROM tblNews")
    Flowable<List<News>> getAllNews();

    @Insert
    void insertNews(List<News> news);

    @Query("DELETE FROM tblNews")
    void deleteAllNews();

    @Query("SELECT COUNT(*) FROM tblNews")
    Flowable<Integer> getCount();
}
