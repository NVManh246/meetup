package com.rikkei.meetup.screen.list_events_by_category.date_event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventSmallAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;
import com.rikkei.meetup.screen.list_events_by_category.ListEventActivity;
import com.rikkei.meetup.screen.list_events_by_category.ListEventContract;
import com.rikkei.meetup.screen.list_events_by_category.ListEventPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DateEventFragment extends Fragment
        implements ListEventContract.View, EventSmallAdapter.OnItemClickListener {

    private static final int STT_EVENT_TODAY = 0;
    private static final int STT_EVENT_TOMORROW = 1;
    private static final int STT_EVENT_WEEKEND = 2;
    private static final int STT_EVENT_NEXT_WEEK = 3;
    private static final int STT_EVENT_END_MONTH = 4;
    private static final int STT_EVENT_NEXT_MONTH = 5;
    private static final int STT_EVENT_FOREVER = 6;
    private static final int SPACING = 30;

    private List<RecyclerView> mRecyclerEvents;
    private List<List<Event>> mEvents;
    private List<EventSmallAdapter> mAdapters;
    private List<CardView> mLayouts;

    private int mCategoryId;
    private ListEventContract.Presenter mPresenter;

    public static DateEventFragment newInstance(int categoryId) {
        Bundle args = new Bundle();
        args.putInt(ListEventActivity.BUNDLE_CATEGORY_ID, categoryId);
        DateEventFragment fragment = new DateEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        setupRecyclerEvents();
        mCategoryId = getArguments().getInt(ListEventActivity.BUNDLE_CATEGORY_ID);
        mPresenter = new ListEventPresenter(this);
        mPresenter.getEventsByCategory(mCategoryId, -1, -1);
    }

    private void setupRecyclerEvents() {
        for (int i = 0; i < mEvents.size(); i++) {
            mRecyclerEvents.get(i).setAdapter(mAdapters.get(i));
            mRecyclerEvents.get(i).setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            mRecyclerEvents.get(i).addItemDecoration(new CustomItemDecoration(SPACING));
        }
    }

    private void initView(View view) {
        mRecyclerEvents = new ArrayList<>();
        mRecyclerEvents.add((RecyclerView) view.findViewById(R.id.recycler_event_today));
        mRecyclerEvents.add((RecyclerView) view.findViewById(R.id.recycler_event_tomorrow));
        mRecyclerEvents.add((RecyclerView) view.findViewById(R.id.recycler_event_weekend));
        mRecyclerEvents.add((RecyclerView) view.findViewById(R.id.recycler_event_next_week));
        mRecyclerEvents.add((RecyclerView) view.findViewById(R.id.recycler_event_end_month));
        mRecyclerEvents.add((RecyclerView) view.findViewById(R.id.recycler_event_next_month));
        mRecyclerEvents.add((RecyclerView) view.findViewById(R.id.recycler_event_forever));

        mLayouts = new ArrayList<>();
        mLayouts.add((CardView) view.findViewById(R.id.layout_event_today));
        mLayouts.add((CardView) view.findViewById(R.id.layout_event_tomorrow));
        mLayouts.add((CardView) view.findViewById(R.id.layout_event_weekend));
        mLayouts.add((CardView) view.findViewById(R.id.layout_event_next_week));
        mLayouts.add((CardView) view.findViewById(R.id.layout_event_end_of_month));
        mLayouts.add((CardView) view.findViewById(R.id.layout_event_next_month));
        mLayouts.add((CardView) view.findViewById(R.id.layout_event_forever));
    }

    private void initData() {
        mEvents = new ArrayList<>();
        mEvents.add(new ArrayList<Event>());
        mEvents.add(new ArrayList<Event>());
        mEvents.add(new ArrayList<Event>());
        mEvents.add(new ArrayList<Event>());
        mEvents.add(new ArrayList<Event>());
        mEvents.add(new ArrayList<Event>());
        mEvents.add(new ArrayList<Event>());

        mAdapters = new ArrayList<>();
        for (int i = 0; i < mEvents.size(); i++) {
            mAdapters.add(new EventSmallAdapter(mEvents.get(i), this));
        }
    }

    @Override
    public void showEvents(List<Event> events) {
    }

    @Override
    public void showEventsToday(List<Event> events) {
        mLayouts.get(STT_EVENT_TODAY).setVisibility(View.VISIBLE);
        mAdapters.get(STT_EVENT_TODAY).insertData(events);
    }

    @Override
    public void showEventsTomorrow(List<Event> events) {
        mLayouts.get(STT_EVENT_TOMORROW).setVisibility(View.VISIBLE);
        mAdapters.get(STT_EVENT_TOMORROW).insertData(events);
    }

    @Override
    public void showEventsWeekend(List<Event> events) {
        mLayouts.get(STT_EVENT_WEEKEND).setVisibility(View.VISIBLE);
        mAdapters.get(STT_EVENT_WEEKEND).insertData(events);
    }

    @Override
    public void showEventsNextWeek(List<Event> events) {
        mLayouts.get(STT_EVENT_NEXT_WEEK).setVisibility(View.VISIBLE);
        mAdapters.get(STT_EVENT_NEXT_WEEK).insertData(events);
    }

    @Override
    public void showEventsEndMonth(List<Event> events) {
        mLayouts.get(STT_EVENT_END_MONTH).setVisibility(View.VISIBLE);
        mAdapters.get(STT_EVENT_END_MONTH).insertData(events);
    }

    @Override
    public void showEventsNextMonth(List<Event> events) {
        mLayouts.get(STT_EVENT_NEXT_MONTH).setVisibility(View.VISIBLE);
        mAdapters.get(STT_EVENT_NEXT_MONTH).insertData(events);
    }

    @Override
    public void showEventsForever(List<Event> events) {
        mLayouts.get(STT_EVENT_FOREVER).setVisibility(View.VISIBLE);
        mAdapters.get(STT_EVENT_FOREVER).insertData(events);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endData() {

    }

    @Override
    public void onItemClick(Event event) {
        Intent intent = EventDetailActivity.getEventDetailIntent(getContext(), event);
        startActivity(intent);
    }
}
