package com.rikkei.meetup.screen.browser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.GenreAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.data.model.genre.Genre;
import com.rikkei.meetup.screen.list_events_by_category.ListEventActivity;
import com.rikkei.meetup.screen.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BrowserFragment extends Fragment implements BrowserContract.View,
        GenreAdapter.OnItemClickListener {

    private static final int SPACING = 20;

    private Unbinder mUnbinder;
    @BindView(R.id.recycler_genre) RecyclerView mRecyclerGenre;
    @BindView(R.id.progress) ProgressBar mProgressBar;

    private List<Genre> mGenres;
    private GenreAdapter mGenreAdapter;

    private BrowserContract.Presenter mPresenter;

    public static BrowserFragment newInstance() {
        BrowserFragment fragment = new BrowserFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        setupRecyclerGenre();
        mPresenter = new BrowserPresenter(this);
        mPresenter.getGenres();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.image_search)
    public void onButtonSearchClick() {
        Intent intent = SearchActivity.getSearchIntent(getContext());
        startActivity(intent);
    }

    private void setupRecyclerGenre() {
        mGenres = new ArrayList<>();
        mGenreAdapter = new GenreAdapter(mGenres, this);
        mRecyclerGenre.setAdapter(mGenreAdapter);
        mRecyclerGenre.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerGenre.addItemDecoration(new CustomItemDecoration(SPACING));
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEvents(List<Genre> genres) {
        mGenreAdapter.insertData(genres);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = ListEventActivity.getListEventIntent(getContext(), mGenres.get(position));
        startActivity(intent);
    }
}
