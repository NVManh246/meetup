package com.rikkei.meetup.screen.home.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.NewsAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.EndLessScrollListener;
import com.rikkei.meetup.common.observer.Observer;
import com.rikkei.meetup.data.model.news.News;
import com.rikkei.meetup.ultis.NetworkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsFragment extends Fragment implements NewsContract.View,
        SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener, Observer {

    private static final int SPACING = 40;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private static final int FIRST_PAGE_INDEX = 1;

    @BindView(R.id.recycler_news) RecyclerView mRecyclerNews;
    @BindView(R.id.swipe_refresh_news) SwipeRefreshLayout mSwipeRefreshNews;
    @BindView(R.id.layout_progress) FrameLayout mLayoutProgress;
    @BindView(R.id.text_alert_connection_error) TextView mTextAlertConnectionError;
    private Unbinder mUnbinder;

    private NewsAdapter mNewsAdapter;
    private List<News> mListNews;
    private NewsContract.Presenter mPresenter;
    private int mPageIndex = FIRST_PAGE_INDEX;
    private int mPageSize = PAGE_SIZE_DEFAULT;
    private boolean mIsLoadingError;
    private int statusNetwork;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mSwipeRefreshNews.setOnRefreshListener(this);
        setupRecyclerNews();
        mPresenter = new NewsPresenter(this);
        if(statusNetwork == NetworkUtils.CONNECTED) {
            mPresenter.getListNews(mPageIndex, mPageSize);
        } else {
            mPresenter.getListNewsDB(mPageIndex - 1, mPageSize);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void setupRecyclerNews() {
        mListNews = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(mListNews, this);
        mRecyclerNews.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerNews.setAdapter(mNewsAdapter);
        mRecyclerNews.addItemDecoration(new CustomItemDecoration(SPACING));
        mRecyclerNews.addOnScrollListener(new EndLessScrollListener() {
            @Override
            public boolean onLoadMore() {
                if(statusNetwork == NetworkUtils.CONNECTED) {
                    mPresenter.getListNews(++mPageIndex, mPageSize);
                } else {
                    mPresenter.getListNewsDB(++mPageIndex - 1, mPageSize);
                }
                return true;
            }
        });
    }

    @Override
    public void hideProgress() {
        if(mLayoutProgress.getVisibility() == View.VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLayoutProgress.setVisibility(View.GONE);
                }
            }, 1500);
        }
    }

    @Override
    public void showListNews(List<News> news) {
        mNewsAdapter.insertData(news);
        if(mSwipeRefreshNews.isRefreshing()) {
            mSwipeRefreshNews.setRefreshing(false);
        }
        mTextAlertConnectionError.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        if(mSwipeRefreshNews.isRefreshing()) {
            mSwipeRefreshNews.setRefreshing(false);
            mTextAlertConnectionError.setVisibility(View.VISIBLE);
        }
        if(mListNews.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  mTextAlertConnectionError.setVisibility(View.VISIBLE);
                }
            }, 1500);
        }
        mIsLoadingError = true;
        mNewsAdapter.removeItemNull();
        if(mPageIndex > 1) {
            mPageIndex--;
        }
    }

    @Override
    public void showEndData() {
        mNewsAdapter.removeItemNull();
        if(mSwipeRefreshNews.isRefreshing()) {
            mSwipeRefreshNews.setRefreshing(false);
        }
        Toast.makeText(getContext(), R.string.end, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void onRefresh() {
        mPageIndex = FIRST_PAGE_INDEX;
        mNewsAdapter.clearAll();
        if(statusNetwork == NetworkUtils.CONNECTED) {
            mPresenter.getListNews(mPageIndex, mPageSize);
        } else {
            mPresenter.getListNewsDB(mPageIndex - 1, mPageSize);
        }
    }

    @Override
    public void onItemClick(int position) {
        String url = mListNews.get(position).getDetailUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void update(int status) {
        statusNetwork = status;
        if(status == NetworkUtils.CONNECTED) {
            if(mIsLoadingError) {
                mPresenter.getListNews(mPageIndex, mPageSize);
                mIsLoadingError = false;
            }
        }
    }

    @Subscribe
    public void onEvent(String s) {
        mRecyclerNews.getLayoutManager().smoothScrollToPosition(mRecyclerNews,
                new RecyclerView.State(), 0);
    }
}
