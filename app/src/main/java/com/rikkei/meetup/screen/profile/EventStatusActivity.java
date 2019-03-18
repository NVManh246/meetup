package com.rikkei.meetup.screen.profile;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventAdapter;
import com.rikkei.meetup.adapter.VenueAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.NetworkChangeReceiver;
import com.rikkei.meetup.common.OnNetworkChangedListener;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.data.model.event.Venue;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;
import com.rikkei.meetup.ultis.AnimUtils;
import com.rikkei.meetup.ultis.NetworkUtils;
import com.rikkei.meetup.ultis.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventStatusActivity extends AppCompatActivity
        implements EventAdapter.OnItemClickListener, EventStatusContract.View,
        SwipeRefreshLayout.OnRefreshListener, OnNetworkChangedListener {

    private static final String EXTRA_STATUS = "status";
    public static final int STATUS_GOING = 1;
    public static final int STATUS_WENT = 2;
    public static final int VENUE = 3;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_title_toolbar)
    TextView mTextTitleToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.text_alert_network)
    TextView mTextAlertNetWork;
    @BindView(R.id.text_alert_connection_error)
    TextView mTextAlertConnectionError;

    private List<Event> mEvents;
    private EventAdapter mEventAdapter;
    private List<Venue> mVenues;
    private VenueAdapter mVenueAdapter;

    private EventStatusContract.Presenter mPresenter;
    private int mStatus;
    private String mToken;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private IntentFilter mIntentFilterNetwork;
    private int mHeightAlert;
    private boolean mIsShowAlert;
    private boolean mIsLoadingError;

    public static Intent getEventStatusIntent(Context context, int status) {
        Intent intent = new Intent(context, EventStatusActivity.class);
        intent.putExtra(EXTRA_STATUS, status);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_event);
        ButterKnife.bind(this);
        mStatus = getIntent().getIntExtra(EXTRA_STATUS, -1);
        setupToolbar();
        setupRecycler();
        mRefreshLayout.setOnRefreshListener(this);
        mPresenter = new EventStatusPresenter(this);
        mToken = StringUtils.getToken(this);
        loadData();
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

    private void setupToolbar() {
        switch (mStatus) {
            case STATUS_GOING:
                mTextTitleToolbar.setText(R.string.going);
                break;
            case STATUS_WENT:
                mTextTitleToolbar.setText(R.string.went);
                break;
            case VENUE:
                mTextTitleToolbar.setText(R.string.venue);
                break;
        }
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

    private void setupRecycler() {
        if (mStatus == VENUE) {
            mVenues = new ArrayList<>();
            mVenueAdapter = new VenueAdapter(mVenues);
            mRecycler.setAdapter(mVenueAdapter);
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.addItemDecoration(new CustomItemDecoration(20));
        } else {
            mEvents = new ArrayList<>();
            mEventAdapter = new EventAdapter(mEvents, this);
            mRecycler.setAdapter(mEventAdapter);
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.addItemDecoration(new CustomItemDecoration(20));
        }
    }

    @Override
    public void onItemClick(int position) {
        if (mStatus != VENUE) {
            Intent intent = EventDetailActivity
                    .getEventDetailIntent(this, mEvents.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEvents(List<Event> events) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        mEventAdapter.insertData(events);
        mEventAdapter.removeItemNull();
        mTextAlertConnectionError.setVisibility(View.GONE);
    }

    @Override
    public void showVenues(List<Venue> venues) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        mTextAlertConnectionError.setVisibility(View.GONE);
        mVenueAdapter.insertData(venues);
    }

    @Override
    public void showError() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        mTextAlertConnectionError.setVisibility(View.VISIBLE);
        mIsLoadingError = true;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onNetworkChanged(int status) {
        if (status == NetworkUtils.NOT_CONNECTED) {
            if (!mIsShowAlert) {
                mTextAlertNetWork.setText(R.string.not_connect);
                mTextAlertNetWork.setBackgroundColor(getResources().getColor(R.color.color_milano_red));
                AnimUtils.translateY(mTextAlertNetWork, 0, -mHeightAlert);
                mIsShowAlert = true;
            }
        } else {
            if (mIsShowAlert) {
                mTextAlertNetWork.setBackgroundColor(
                        getResources().getColor(R.color.color_japanese_laurel));
                mTextAlertNetWork.setText(R.string.connecting);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimUtils.translateY(mTextAlertNetWork, -mHeightAlert, 0);
                    }
                }, 2000);
                mIsShowAlert = false;
            }
            if (mIsLoadingError) {
                loadData();
                mIsLoadingError = false;
            }
        }
    }

    private void loadData() {
        switch (mStatus) {
            case STATUS_GOING:
                mEventAdapter.clearAll();
                mPresenter.getMyEvetns(mToken, STATUS_GOING);
                break;
            case STATUS_WENT:
                mEventAdapter.clearAll();
                mPresenter.getMyEvetns(mToken, STATUS_WENT);
                break;
            case VENUE:
                mVenueAdapter.clearAll();
                mPresenter.getVenuesFollowed(mToken);
                break;
        }
    }
}
