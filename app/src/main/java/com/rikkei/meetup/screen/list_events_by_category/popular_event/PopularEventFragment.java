package com.rikkei.meetup.screen.list_events_by_category.popular_event;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.EndLessScrollListener;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;
import com.rikkei.meetup.screen.list_events_by_category.ListEventActivity;
import com.rikkei.meetup.screen.list_events_by_category.ListEventContract;
import com.rikkei.meetup.screen.list_events_by_category.ListEventPresenter;
import com.rikkei.meetup.ultis.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PopularEventFragment extends Fragment implements ListEventContract.View,
        EventAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int SPACING = 40;

    @BindView(R.id.recycler_event) RecyclerView mRecyclerEvent;
    @BindView(R.id.swipe_refresh_event) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progress) ProgressBar mProgressBar;
    private Unbinder mUnbinder;

    private EventAdapter mEventAdapter;
    private List<Event> mEvents;

    private int mCategoryId;
    private int mPageIndex = 1;
    private int mPageSize = 10;
    private String mToken;

    private ListEventContract.Presenter mPresenter;

    public static PopularEventFragment newInstance(int categoryId) {
        Bundle args = new Bundle();
        args.putInt(ListEventActivity.BUNDLE_CATEGORY_ID, categoryId);
        PopularEventFragment fragment = new PopularEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mRefreshLayout.setOnRefreshListener(this);
        setupRecyclerEvent();
        mCategoryId = getArguments().getInt(ListEventActivity.BUNDLE_CATEGORY_ID);
        mPresenter = new ListEventPresenter(this);
        mToken = StringUtils.getToken(getContext());
        mPresenter.getEventsByCategory(mToken, mCategoryId, mPageIndex, mPageSize);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
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
                mPresenter.getEventsByCategory(mToken, mCategoryId, ++mPageIndex, mPageSize);
                return true;
            }
        });
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEvents(List<Event> events) {
        mEventAdapter.insertData(events);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if(mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEventsToday(List<Event> events) {

    }

    @Override
    public void showEventsTomorrow(List<Event> events) {

    }

    @Override
    public void showEventsWeekend(List<Event> events) {

    }

    @Override
    public void showEventsNextWeek(List<Event> events) {

    }

    @Override
    public void showEventsEndMonth(List<Event> events) {

    }

    @Override
    public void showEventsNextMonth(List<Event> events) {

    }

    @Override
    public void showEventsForever(List<Event> events) {

    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endData() {
        Toast.makeText(getContext(), R.string.end, Toast.LENGTH_SHORT).show();
        if (mEvents.size() != 0) {
            mEventAdapter.removeItemNull();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = EventDetailActivity.getEventDetailIntent(getContext(), mEvents.get(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        mEventAdapter.clearAll();
        mPresenter.getEventsByCategory(mToken, mCategoryId, mPageIndex, mPageSize);
    }
}
