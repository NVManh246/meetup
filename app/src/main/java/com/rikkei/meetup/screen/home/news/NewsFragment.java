package com.rikkei.meetup.screen.home.news;

import android.content.Intent;
import android.net.Uri;
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
import com.rikkei.meetup.adapter.NewsAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.EndLessScrollListener;
import com.rikkei.meetup.data.model.news.News;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements NewsContract.View,
        SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    private static final int SPACING = 40;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private static final int FIRST_PAGE_INDEX = 1;

    private RecyclerView mRecyclerNews;
    private SwipeRefreshLayout mSwipeRefreshNews;
    private NewsAdapter mNewsAdapter;
    private List<News> mListNews;

    private NewsContract.Presenter mPresenter;
    private int mPageIndex = FIRST_PAGE_INDEX;
    private int mPageSize = PAGE_SIZE_DEFAULT;

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
        initView(view);
        setupRecyclerNews();
        mPresenter = new NewsPresenter(this);
        mPresenter.getListNews(mPageIndex, mPageSize);
    }

    private void initView(View view) {
        mRecyclerNews = view.findViewById(R.id.recycler_news);
        mSwipeRefreshNews = view.findViewById(R.id.swipe_refresh_news);
        mSwipeRefreshNews.setOnRefreshListener(this);
        mSwipeRefreshNews.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
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
                mPresenter.getListNews(++mPageIndex, mPageSize);
                return true;
            }
        });
    }

    @Override
    public void showListNews(List<News> news) {
        mNewsAdapter.insertData(news);
        if(mSwipeRefreshNews.isRefreshing()) {
            mSwipeRefreshNews.setRefreshing(false);
        }
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mPageIndex = FIRST_PAGE_INDEX;
        mNewsAdapter.clearAll();
        mPresenter.getListNews(mPageIndex, mPageSize);
    }

    @Override
    public void onItemClick(int position) {
        String url = mListNews.get(position).getDetailUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        startActivity(browserIntent);
    }
}
