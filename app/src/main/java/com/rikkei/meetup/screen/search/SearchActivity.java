package com.rikkei.meetup.screen.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.EndLessScrollListener;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements SearchContract.View,
        EventAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int SPACING = 40;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private static final int FIRST_PAGE_INDEX = 1;

    @BindView(R.id.edit_search)
    EditText mEditSearch;
    @BindView(R.id.swipe_refresh_event)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_event)
    RecyclerView mRecyclerEvent;
    @BindView(R.id.text_lable_msg_result)
    TextView mTextMsg;

    private List<Event> mEvents;
    private EventAdapter mEventAdapter;

    private SearchContract.Presenter mPresenter;

    private String mKeyword;
    private int mPageIndex = FIRST_PAGE_INDEX;
    private int mPageSize = PAGE_SIZE_DEFAULT;

    public static Intent getSearchIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mRefreshLayout.setOnRefreshListener(this);
        setupRecyclerEvent();
        mPresenter = new SearchPresenter(this);
        search();
    }

    @OnClick(R.id.image_back)
    public void onButtonBackClick() {
        finish();
    }

    @Override
    public void showEvents(List<Event> events) {
        if (mRecyclerEvent.getVisibility() == View.GONE) {
            mRecyclerEvent.setVisibility(View.VISIBLE);
            mTextMsg.setVisibility(View.GONE);
        }
        mEventAdapter.insertData(events);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void noResultSearching() {
        if (mEvents.size() == 0) {
            mRecyclerEvent.setVisibility(View.GONE);
            mTextMsg.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, getString(R.string.final_result), Toast.LENGTH_SHORT).show();
            mEventAdapter.removeItemNull();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = EventDetailActivity.getEventDetailIntent(this, mEvents.get(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPageIndex = FIRST_PAGE_INDEX;
        mEventAdapter.clearAll();
        mPresenter.getEventsByKeyword(mKeyword, mPageIndex, mPageSize);
    }

    private void setupRecyclerEvent() {
        mEvents = new ArrayList<>();
        mEventAdapter = new EventAdapter(mEvents, this);
        mRecyclerEvent.setAdapter(mEventAdapter);
        mRecyclerEvent.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerEvent.addItemDecoration(new CustomItemDecoration(SPACING));
        mRecyclerEvent.addOnScrollListener(new EndLessScrollListener() {
            @Override
            public boolean onLoadMore() {
                mPresenter.getEventsByKeyword(mKeyword, ++mPageIndex, mPageSize);
                return true;
            }
        });
    }

    private void search() {
        mEditSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    mKeyword = mEditSearch.getText().toString();
                    mPageIndex = FIRST_PAGE_INDEX;
                    mEventAdapter.clearAll();
                    mPresenter.getEventsByKeyword(mKeyword, mPageIndex, mPageSize);
                    hideSoftKeyboard();
                }
                return false;
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
