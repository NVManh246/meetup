package com.rikkei.meetup.screen.search;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.EndLessScrollListener;
import com.rikkei.meetup.common.NetworkChangeReceiver;
import com.rikkei.meetup.common.OnNetworkChangedListener;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;
import com.rikkei.meetup.ultis.AnimUtils;
import com.rikkei.meetup.ultis.NetworkUtils;
import com.rikkei.meetup.ultis.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements SearchContract.View,
        EventAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        OnNetworkChangedListener {

    private static final int SPACING = 40;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private static final int FIRST_PAGE_INDEX = 1;
    private String mToken;

    @BindView(R.id.edit_search)
    EditText mEditSearch;
    @BindView(R.id.swipe_refresh_event)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_event_upcoming)
    RecyclerView mRecyclerEventUpComing;
    @BindView(R.id.recycler_event_past)
    RecyclerView mRecyclerEventPast;
    @BindView(R.id.text_lable_msg_result)
    TextView mTextMsg;
    @BindView(R.id.radio_group_filter)
    RadioGroup mRadioGroupFilter;
    @BindView(R.id.radio_button_upcoming)
    RadioButton mRadioButtonUpComing;
    @BindView(R.id.radio_button_past)
    RadioButton mRadioButtonPast;
    @BindView(R.id.text_alert_network)
    TextView mTextAlertNetWork;
    @BindView(R.id.text_alert_connection_error)
    TextView mTextAlertConnectionError;
    @BindView(R.id.progress)
    ProgressBar mProgress;

    private List<Event> mEventsUpComing;
    private EventAdapter mEventsAdapterUpComing;

    private List<Event> mEventsPast;
    private EventAdapter mEventsAdapterPast;

    private SearchContract.Presenter mPresenter;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private IntentFilter mIntentFilterNetwork;
    private String mKeyword;
    private int mPageIndex = FIRST_PAGE_INDEX;
    private int mPageSize = PAGE_SIZE_DEFAULT;
    private boolean mIsShowAlert;
    private boolean mIsLoadingError;
    private int mHeightAlert;

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
        mEditSearch.requestFocus();
        search();
        mHeightAlert = (int) getResources().getDimension(R.dimen.dp_20);
        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
        mIntentFilterNetwork = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mNetworkChangeReceiver, mIntentFilterNetwork);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkChangeReceiver);
    }

    @OnClick(R.id.image_back)
    public void onButtonBackClick() {
        finish();
    }

    @OnCheckedChanged({R.id.radio_button_upcoming, R.id.radio_button_past})
    public void onRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if(checked) {
            switch (button.getId()) {
                case R.id.radio_button_upcoming:
                    mRecyclerEventUpComing.setVisibility(View.VISIBLE);
                    mRecyclerEventPast.setVisibility(View.GONE);
                    break;
                case R.id.radio_button_past:
                    mRecyclerEventUpComing.setVisibility(View.GONE);
                    mRecyclerEventPast.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void showEventsUpComing(List<Event> events) {
        if (events.isEmpty()) {
            mPresenter.getEventsByKeyword(mToken, mKeyword, ++mPageIndex, mPageSize);
        } else {
            mEventsAdapterUpComing.insertData(events);
            showRecycler();
        }
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showEventsPass(List<Event> events) {
        if (events.isEmpty()) {
            mPresenter.getEventsByKeyword(mToken, mKeyword, ++mPageIndex, mPageSize);
        } else {
            mEventsAdapterPast.insertData(events);
            showRecycler();
        }
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showError() {
        if(mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        if(mRadioButtonPast.isChecked()){
            mEventsAdapterPast.removeItemNull();
        } else {
            mEventsAdapterUpComing.removeItemNull();
        }
        mIsLoadingError = true;
    }

    @Override
    public void noResultSearching() {
        if (mEventsUpComing.isEmpty() && mEventsPast.isEmpty()) {
            mRecyclerEventUpComing.setVisibility(View.GONE);
            mRecyclerEventPast.setVisibility(View.GONE);
            mTextMsg.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, getString(R.string.final_result), Toast.LENGTH_SHORT).show();
            mEventsAdapterUpComing.removeItemNull();
            mEventsAdapterPast.removeItemNull();
        }
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showAlertConnectionError() {
        if(mEventsUpComing.isEmpty() && mEventsPast.isEmpty()) {
            mTextAlertConnectionError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideAlertConnectionError() {
        mTextAlertConnectionError.setVisibility(View.GONE);
    }

    @Override
    public void setCount() {
        mRadioButtonUpComing.setText(StringUtils
                .getCount(getString(R.string.current_upcoming), mEventsUpComing));
        mRadioButtonPast.setText(StringUtils.getCount(getString(R.string.past), mEventsPast));
    }

    @Override
    public void onItemClick(int position) {
        Event event;
        if(mRadioButtonPast.isChecked()){
            event = mEventsPast.get(position);
        } else {
            event = mEventsUpComing.get(position);
        }
        Intent intent = EventDetailActivity.getEventDetailIntent(this, event);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPageIndex = FIRST_PAGE_INDEX;
        mEventsAdapterUpComing.clearAll();
        mEventsAdapterPast.clearAll();
        mPresenter.getEventsByKeyword(mToken, mKeyword, mPageIndex, mPageSize);
    }

    private void setupRecyclerEvent() {
        mEventsUpComing = new ArrayList<>();
        mEventsPast = new ArrayList<>();
        mEventsAdapterUpComing = new EventAdapter(mEventsUpComing, this);
        mEventsAdapterPast = new EventAdapter(mEventsPast, this);
        mRecyclerEventUpComing.setAdapter(mEventsAdapterUpComing);
        mRecyclerEventPast.setAdapter(mEventsAdapterPast);

        mRecyclerEventUpComing.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerEventUpComing.addItemDecoration(new CustomItemDecoration(SPACING));
        mRecyclerEventUpComing.addOnScrollListener(new EndLessScrollListener() {
            @Override
            public boolean onLoadMore() {
                mPresenter.getEventsByKeyword(mToken, mKeyword, ++mPageIndex, mPageSize);
                return true;
            }
        });

        mRecyclerEventPast.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerEventPast.addItemDecoration(new CustomItemDecoration(SPACING));
        mRecyclerEventPast.addOnScrollListener(new EndLessScrollListener() {
            @Override
            public boolean onLoadMore() {
                mPresenter.getEventsByKeyword(mToken, mKeyword, ++mPageIndex, mPageSize);
                return true;
            }
        });
    }

    private void search() {
        mToken = StringUtils.getToken(this);
        mEditSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    mKeyword = mEditSearch.getText().toString();
                    mPageIndex = FIRST_PAGE_INDEX;
                    mEventsAdapterUpComing.clearAll();
                    mEventsAdapterPast.clearAll();
                    mPresenter.getEventsByKeyword(mToken, mKeyword, mPageIndex, mPageSize);
                    hideSoftKeyboard();
                    mProgress.setVisibility(View.VISIBLE);
                    mTextMsg.setVisibility(View.GONE);
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

    private void showRecycler() {
        if(!mEventsPast.isEmpty() || !mEventsUpComing.isEmpty()) {
            mTextMsg.setVisibility(View.GONE);
            if(mRadioButtonPast.isChecked()) {
                mRecyclerEventPast.setVisibility(View.VISIBLE);
            } else {
                mRecyclerEventUpComing.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNetworkChanged(int status) {
        if (status == NetworkUtils.NOT_CONNECTED) {
            if(!mIsShowAlert) {
                mTextAlertNetWork.setText(R.string.not_connect);
                mTextAlertNetWork.setBackgroundColor(getResources().getColor(R.color.color_milano_red));
                AnimUtils.translateY(mTextAlertNetWork, 0, -mHeightAlert);
                mIsShowAlert = true;
            }
        } else {
            if (mIsShowAlert) {
                mTextAlertNetWork.setBackgroundColor(
                        getResources().getColor(R.color.color_japanese_laurel));
                mTextAlertNetWork.setText(R.string.connecting);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimUtils.translateY(mTextAlertNetWork, -mHeightAlert, 0);
                    }
                }, 2000);
                mIsShowAlert = false;
            }
            if(!TextUtils.isEmpty(mKeyword) && mIsLoadingError) {
                mPresenter.getEventsByKeyword(mToken, mKeyword, mPageIndex, mPageSize);
                mIsLoadingError = false;
            }
        }
    }
}
