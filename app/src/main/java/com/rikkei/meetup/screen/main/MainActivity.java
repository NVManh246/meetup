package com.rikkei.meetup.screen.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.common.NetworkChangeReceiver;
import com.rikkei.meetup.common.OnNetworkChangedListener;
import com.rikkei.meetup.common.observer.NetworkData;
import com.rikkei.meetup.screen.browser.BrowserFragment;
import com.rikkei.meetup.screen.home.HomeFragment;
import com.rikkei.meetup.screen.mypage.MyPageFragment;
import com.rikkei.meetup.screen.near.NearFragment;
import com.rikkei.meetup.ultis.AnimUtils;
import com.rikkei.meetup.ultis.NetworkUtils;
import com.rikkei.meetup.ultis.StringUtils;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, OnNetworkChangedListener {

    private TextView mTextAlertNetwork;
    private BottomNavigationView mBottomTabBar;
    private HomeFragment mHomeFragment;
    private NearFragment mNearFragment;
    private BrowserFragment mBrowserFragment;
    private MyPageFragment mMyPageFragment;
    private FragmentManager mFragmentManager;
    private Fragment mCurruntFragment;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private IntentFilter mIntentFilterNetwork;
    private NetworkData mNetworkData;

    private int mHeightAlert;
    private boolean mIsShowAlert;

    public static Intent getMainIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomTabBar = findViewById(R.id.navigation_bottom_bar);
        mTextAlertNetwork = findViewById(R.id.text_alert_network);
        mBottomTabBar.setOnNavigationItemSelectedListener(this);
        mFragmentManager = getSupportFragmentManager();

        mHomeFragment = HomeFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.frame_container, mHomeFragment)
                .commit();
        mCurruntFragment = mHomeFragment;

        mNetworkChangeReceiver = new NetworkChangeReceiver(this);
        mIntentFilterNetwork = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkData = new NetworkData();
        mNetworkData.registerObserver(mHomeFragment);

        mHeightAlert = (int) getResources().getDimension(R.dimen.dp_20);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                showFragment(0);
                break;
            case R.id.menu_near:
                showFragment(1);
                break;
            case R.id.menu_browser:
                showFragment(2);
                break;
            case R.id.menu_my_page:
                showFragment(3);
                break;
        }
        return true;
    }

    private void showFragment(int i) {
        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .show(mCurruntFragment)
                            .add(R.id.frame_container, mHomeFragment)
                            .commit();
                } else {
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .show(mHomeFragment)
                            .commit();
                }
                mCurruntFragment = mHomeFragment;
                break;
            case 1:
                if (mNearFragment == null) {
                    mNearFragment = NearFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .add(R.id.frame_container, mNearFragment)
                            .commit();
                    mNetworkData.registerObserver(mNearFragment);
                } else {
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .show(mNearFragment)
                            .commit();
                }
                mCurruntFragment = mNearFragment;
                break;
            case 2:
                if (mBrowserFragment == null) {
                    mBrowserFragment = BrowserFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .add(R.id.frame_container, mBrowserFragment)
                            .commit();
                    mNetworkData.registerObserver(mBrowserFragment);
                } else {
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .show(mBrowserFragment)
                            .commit();
                }
                mCurruntFragment = mBrowserFragment;
                break;
            case 3:
                if (mMyPageFragment == null) {
                    mMyPageFragment = MyPageFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .add(R.id.frame_container, mMyPageFragment)
                            .commit();
                    mNetworkData.registerObserver(mMyPageFragment);
                } else {
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .show(mMyPageFragment)
                            .commit();
                }
                mCurruntFragment = mMyPageFragment;
                break;
        }
    }

    public void onNetworkChanged(int status) {
        mNetworkData.notifyObservers(status);
        if (status == NetworkUtils.NOT_CONNECTED) {
            if (!mIsShowAlert) {
                mTextAlertNetwork.setText(R.string.not_connect);
                mTextAlertNetwork.setBackgroundColor(getResources().getColor(R.color.color_milano_red));
                AnimUtils.translateY(mTextAlertNetwork, 0, -mHeightAlert);
                mIsShowAlert = true;
            }
        } else {
            if (mIsShowAlert) {
                mTextAlertNetwork.setBackgroundColor(
                        getResources().getColor(R.color.color_japanese_laurel));
                mTextAlertNetwork.setText(R.string.connecting);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimUtils.translateY(mTextAlertNetwork, -mHeightAlert, 0);
                    }
                }, 2000);
                mIsShowAlert = false;
            }
        }
    }
}
