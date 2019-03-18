package com.rikkei.meetup.screen.list_events_by_category;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.PagerAdapter;
import com.rikkei.meetup.common.NetworkChangeReceiver;
import com.rikkei.meetup.common.OnNetworkChangedListener;
import com.rikkei.meetup.common.observer.NetworkData;
import com.rikkei.meetup.data.model.genre.Genre;
import com.rikkei.meetup.screen.list_events_by_category.date_event.DateEventFragment;
import com.rikkei.meetup.screen.list_events_by_category.popular_event.PopularEventFragment;
import com.rikkei.meetup.ultis.AnimUtils;
import com.rikkei.meetup.ultis.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListEventActivity extends AppCompatActivity implements OnNetworkChangedListener {

    public static final String BUNDLE_CATEGORY_ID = "bundle_category_id";
    private static final String EXTRA_CATEFORY = "category";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.text_title_toolbar) TextView mTextTitleToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewpager_list_event) ViewPager mViewPager;
    @BindView(R.id.text_alert_network) TextView mTextAlertNetwork;

    private PagerAdapter mEventPagerAdapter;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private IntentFilter mIntentFilterNetwork;
    private NetworkData mNetworkData;

    private Genre mGenre;
    private int mHeightAlert;
    private boolean mIsShowAlert;

    public static Intent getListEventIntent(Context context, Genre genre) {
        Intent intent = new Intent(context, ListEventActivity.class);
        intent.putExtra(EXTRA_CATEFORY, genre);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event_by_category);
        ButterKnife.bind(this);
        mGenre = getIntent().getParcelableExtra(EXTRA_CATEFORY);
        mNetworkData = new NetworkData();
        initView();
        setupPager();
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
        mIntentFilterNetwork = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mHeightAlert = (int) getResources().getDimension(R.dimen.dp_20);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mNetworkChangeReceiver, mIntentFilterNetwork);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }

    private void initView() {
        mTextTitleToolbar.setText(mGenre.getName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupPager() {
        mEventPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        PopularEventFragment popularEventFragment = PopularEventFragment.newInstance(mGenre.getId());
        mEventPagerAdapter.addFragment(popularEventFragment, getString(R.string.sort_by_popular));
        DateEventFragment dateEventFragment = DateEventFragment.newInstance(mGenre.getId());
        mEventPagerAdapter.addFragment(dateEventFragment, getString(R.string.sort_by_date));
        mViewPager.setAdapter(mEventPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mNetworkData.registerObserver(popularEventFragment);
        mNetworkData.registerObserver(dateEventFragment);
    }

    @Override
    public void onNetworkChanged(int status) {
        mNetworkData.notifyObservers(status);
        if (status == NetworkUtils.NOT_CONNECTED) {
            if(!mIsShowAlert) {
                mTextAlertNetwork.setText(R.string.not_connect);
                mTextAlertNetwork.setBackgroundColor(getResources().getColor(R.color.color_milano_red));
                AnimUtils.translateY(mTextAlertNetwork, 0, -mHeightAlert);
                mIsShowAlert = true;
            }
        } else {
            if (mIsShowAlert) {
                mTextAlertNetwork.setBackgroundColor(
                        getResources().getColor(R.color.color_japanese_laurel));
                mTextAlertNetwork.setText(R.string.connecting);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimUtils.translateY(mTextAlertNetwork, -mHeightAlert, 0);
                    }
                }, 2000);
                mIsShowAlert = false;
            }
        }
    }
}
