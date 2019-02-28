package com.rikkei.meetup.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.home.news.NewsFragment;
import com.rikkei.meetup.screen.home.popular.PopularFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private static final int PAGER_NUMBER = 2;
    private Context mContext;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0 ? NewsFragment.newInstance() : PopularFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGER_NUMBER;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? mContext.getString(R.string.news)
                : mContext.getString(R.string.popular);
    }
}
