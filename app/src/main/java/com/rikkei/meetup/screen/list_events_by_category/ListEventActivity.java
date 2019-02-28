package com.rikkei.meetup.screen.list_events_by_category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventPagerAdapter;
import com.rikkei.meetup.data.model.genre.Genre;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListEventActivity extends AppCompatActivity {

    public static final String BUNDLE_CATEGORY_ID = "bundle_category_id";
    private static final String EXTRA_CATEFORY = "category";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewpager_list_event) ViewPager mViewPager;
    private EventPagerAdapter mEventPagerAdapter;

    private Genre mGenre;

    public static Intent getListEventIntent(Context context, Genre genre) {
        Intent intent = new Intent(context, ListEventActivity.class);
        intent.putExtra(EXTRA_CATEFORY, genre);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event_by_category);
        ButterKnife.bind(this);
        mGenre = getIntent().getParcelableExtra(EXTRA_CATEFORY);
        initView();
        setupPager();
    }

    private void initView() {
        mToolbar.setTitle(mGenre.getName());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupPager() {
        mEventPagerAdapter
                = new EventPagerAdapter(getSupportFragmentManager(), this, mGenre.getId());
        mViewPager.setAdapter(mEventPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
