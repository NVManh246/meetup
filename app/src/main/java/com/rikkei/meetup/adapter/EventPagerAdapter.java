package com.rikkei.meetup.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.list_events_by_category.date_event.DateEventFragment;
import com.rikkei.meetup.screen.list_events_by_category.popular_event.PopularEventFragment;

public class EventPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGER_NUMBER = 2;
    private Context mContext;
    private int mCategoryId;

    public EventPagerAdapter(FragmentManager fm, Context context, int categoryId) {
        super(fm);
        mContext = context;
        mCategoryId = categoryId;
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0 ? PopularEventFragment
                .newInstance(mCategoryId) : DateEventFragment.newInstance(mCategoryId);
    }

    @Override
    public int getCount() {
        return PAGER_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? mContext.getString(R.string.sort_by_popular)
                : mContext.getString(R.string.sort_by_date);
    }
}
