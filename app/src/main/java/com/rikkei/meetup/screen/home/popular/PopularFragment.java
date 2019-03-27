package com.rikkei.meetup.screen.home.popular;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.EndLessScrollListener;
import com.rikkei.meetup.common.observer.Observer;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;
import com.rikkei.meetup.ultis.StringUtils;
import com.rikkei.meetup.ultis.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PopularFragment extends Fragment implements PopularContract.View,
        EventAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, Observer {

    private static final int SPACING = 40;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private static final int FIRST_PAGE_INDEX = 1;

    @BindView(R.id.recycler_event) RecyclerView mRecyclerEvent;
    @BindView(R.id.swipe_refresh_event) SwipeRefreshLayout mSwipeRefreshEvent;
    @BindView(R.id.text_alert_connection_error) TextView mTextAlertConnectionError;
    private Unbinder mUnbinder;
    private EventAdapter mEventAdapter;
    private List<Event> mEvents;

    private PopularContract.Presenter mPresenter;
    private int mPageIndex = FIRST_PAGE_INDEX;
    private int mPageSize = PAGE_SIZE_DEFAULT;
    private String mToken;
    private boolean mIsLoadingError;

    public static PopularFragment newInstance() {
        PopularFragment fragment = new PopularFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        setupRecyclerEvent();
        mPresenter = new PopularPresenter(this);
        mToken = StringUtils.getToken(getContext());
        mPresenter.getEvents(mToken, mPageIndex, mPageSize);
        mSwipeRefreshEvent.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    private void setupRecyclerEvent() {
        mEvents = new ArrayList<>();
        mEventAdapter = new EventAdapter(mEvents, this);
        mRecyclerEvent.setAdapter(mEventAdapter);
        mRecyclerEvent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerEvent.addItemDecoration(new CustomItemDecoration(SPACING));
        mRecyclerEvent.addOnScrollListener(new EndLessScrollListener() {
            @Override
            public boolean onLoadMore() {
                mPresenter.getEvents(mToken, ++mPageIndex, mPageSize);
                return true;
            }
        });
    }

    @Override
    public void showEvents(List<Event> events) {
        mEventAdapter.insertData(events);
        if (mSwipeRefreshEvent.isRefreshing()) {
            mSwipeRefreshEvent.setRefreshing(false);
        }
        mTextAlertConnectionError.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        if (mSwipeRefreshEvent.isRefreshing()) {
            mSwipeRefreshEvent.setRefreshing(false);
        }
        mIsLoadingError = true;
        mEventAdapter.removeItemNull();
        mTextAlertConnectionError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent
                = EventDetailActivity.getEventDetailIntent(getContext(), mEvents.get(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mToken = StringUtils.getToken(getContext());
        mPageIndex = FIRST_PAGE_INDEX;
        mEventAdapter.clearAll();
        mPresenter.getEvents(mToken, mPageIndex, mPageSize);
    }

    @Override
    public void update(int status) {
        if(status == NetworkUtils.CONNECTED) {
            if(mIsLoadingError) {
                mPresenter.getEvents(mToken, mPageIndex, mPageSize);
                mIsLoadingError = false;
            }
        }
    }

    @Subscribe
    public void onEvent(String s) {
        mRecyclerEvent.getLayoutManager().smoothScrollToPosition(mRecyclerEvent,
                new RecyclerView.State(), 0);
    }
}
