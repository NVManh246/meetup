package com.rikkei.meetup.screen.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventAdapter;
import com.rikkei.meetup.adapter.VenueAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.data.model.event.Venue;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;
import com.rikkei.meetup.ultis.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventStatusActivity extends AppCompatActivity
        implements EventAdapter.OnItemClickListener, EventStatusContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_STATUS = "status";
    public static final int STATUS_GOING = 1;
    public static final int STATUS_WENT = 2;
    public static final int VENUE = 3;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private List<Event> mEvents;
    private EventAdapter mEventAdapter;

    private List<Venue> mVenues;
    private VenueAdapter mVenueAdapter;

    private EventStatusContract.Presenter mPresenter;
    private int mStatus;
    private String mToken;

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
        switch (mStatus) {
            case STATUS_GOING:
                mPresenter.getMyEvetns(mToken, STATUS_GOING);
                break;
            case STATUS_WENT:
                mPresenter.getMyEvetns(mToken, STATUS_WENT);
                break;
            case VENUE:
                mPresenter.getVenuesFollowed(mToken);
                break;
        }
    }

    private void setupToolbar() {
        switch (mStatus) {
            case STATUS_GOING:
                mToolbar.setTitle(R.string.going);
                break;
            case STATUS_WENT:
                mToolbar.setTitle(R.string.went);
                break;
            case VENUE:
                mToolbar.setTitle(R.string.venue);
                break;
        }
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecycler() {
        if(mStatus == VENUE) {
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
        if(mStatus != VENUE) {
            Intent intent = EventDetailActivity
                    .getEventDetailIntent(this, mEvents.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void showEvents(List<Event> events) {
        if(mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        mEventAdapter.insertData(events);
        mEventAdapter.removeItemNull();
    }

    @Override
    public void showVenues(List<Venue> venues) {
        if(mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        mVenueAdapter.insertData(venues);
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
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
