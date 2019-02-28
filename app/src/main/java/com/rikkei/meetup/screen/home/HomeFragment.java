package com.rikkei.meetup.screen.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.HomePagerAdapter;

public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mPagerHome;

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
        initView(view);
    }

    private void initView(View view) {
        mTabLayout = view.findViewById(R.id.tab_layout);
        mPagerHome = view.findViewById(R.id.pager_home);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getFragmentManager(), getContext());
        mPagerHome.setAdapter(homePagerAdapter);
        mTabLayout.setupWithViewPager(mPagerHome);
    }
}
