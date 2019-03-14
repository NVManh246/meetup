package com.rikkei.meetup.data.source.local.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.rikkei.meetup.data.model.news.News;

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "db_meetup";

    private static NewsDatabase sIntance;

    public static NewsDatabase getInstance(Context context) {
        if(sIntance == null) {
            sIntance = Room.databaseBuilder(context, NewsDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sIntance;
    }

    public abstract NewsDAO newsDao();
}
