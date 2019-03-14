package com.rikkei.meetup.screen.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.PagerAdapter;
import com.rikkei.meetup.common.observer.NetworkData;
import com.rikkei.meetup.common.observer.Observer;
import com.rikkei.meetup.screen.home.news.NewsFragment;
import com.rikkei.meetup.screen.home.popular.PopularFragment;

public class HomeFragment extends Fragment implements Observer {

    private TabLayout mTabLayout;
    private ViewPager mPagerHome;
    private NetworkData mNetworkData;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNetworkData = new NetworkData();
        initView(view);
    }

    private void initView(View view) {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mPagerHome = view.findViewById(R.id.pager_home);
        PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager());
        NewsFragment newsFragment = NewsFragment.newInstance();
        PopularFragment popularFragment = PopularFragment.newInstance();
        pagerAdapter.addFragment(newsFragment, getString(R.string.news));
        pagerAdapter.addFragment(popularFragment, getString(R.string.popular));
        mPagerHome.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mPagerHome);
        mNetworkData.registerObserver(newsFragment);
        mNetworkData.registerObserver(popularFragment);
    }

    @Override
    public void update(int status) {
        mNetworkData.notifyObservers(status);
    }
}
