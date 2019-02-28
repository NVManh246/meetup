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
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.EndLessScrollListener;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class PopularFragment extends Fragment implements PopularContract.View,
        EventAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int SPACING = 40;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private static final int FIRST_PAGE_INDEX = 1;

    private RecyclerView mRecyclerEvent;
    private SwipeRefreshLayout mSwipeRefreshEvent;
    private EventAdapter mEventAdapter;
    private List<Event> mEvents;

    private PopularContract.Presenter mPresenter;
    private int mPageIndex = FIRST_PAGE_INDEX;
    private int mPageSize = PAGE_SIZE_DEFAULT;

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
        initView(view);
        setupRecyclerEvent();
        mPresenter = new PopularPresenter(this);
        mPresenter.getEvents(mPageIndex, mPageSize);

    }

    private void initView(View view) {
        mRecyclerEvent = view.findViewById(R.id.recycler_event);
        mSwipeRefreshEvent = view.findViewById(R.id.swipe_refresh_event);
        mSwipeRefreshEvent.setOnRefreshListener(this);
        mSwipeRefreshEvent.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
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
                mPresenter.getEvents(++mPageIndex, mPageSize);
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
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent
                = EventDetailActivity.getEventDetailIntent(getContext(), mEvents.get(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPageIndex = FIRST_PAGE_INDEX;
        mEventAdapter.clearAll();
        mPresenter.getEvents(mPageIndex, mPageSize);
    }
}
