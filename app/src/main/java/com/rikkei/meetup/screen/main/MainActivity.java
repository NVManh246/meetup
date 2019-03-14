package com.rikkei.meetup.screen.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.browser.BrowserFragment;
import com.rikkei.meetup.screen.home.HomeFragment;
import com.rikkei.meetup.screen.mypage.MyPageFragment;
import com.rikkei.meetup.screen.near.NearFragment;
import com.rikkei.meetup.ultis.StringUtils;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private BottomNavigationView mBottomTabBar;
    private HomeFragment mHomeFragment;
    private NearFragment mNearFragment;
    private BrowserFragment mBrowserFragment;
    private MyPageFragment mMyPageFragment;
    private FragmentManager mFragmentManager;
    private Fragment mCurruntFragment;
    private String mToken;
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomTabBar = findViewById(R.id.navigation_bottom_bar);
        mBottomTabBar.setOnNavigationItemSelectedListener(this);
        mPresenter = new MainPresenter(this);
        mToken = StringUtils.getToken(this);
        if(mToken != null) {
            mPresenter.checkTokenExpired(mToken);
        }
        mFragmentManager = getSupportFragmentManager();

        mHomeFragment = HomeFragment.newInstance();
        mFragmentManager.beginTransaction()
                .add(R.id.frame_container, mHomeFragment)
                .commit();
        mCurruntFragment = mHomeFragment;

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
                if(mHomeFragment == null) {
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
                if(mNearFragment == null) {
                    mNearFragment = NearFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .add(R.id.frame_container, mNearFragment)
                            .commit();
                } else {
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .show(mNearFragment)
                            .commit();
                }
                mCurruntFragment = mNearFragment;
                break;
            case 2:
                if(mBrowserFragment == null) {
                    mBrowserFragment = BrowserFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .add(R.id.frame_container, mBrowserFragment)
                            .commit();
                } else {
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .show(mBrowserFragment)
                            .commit();
                }
                mCurruntFragment = mBrowserFragment;
                break;
            case 3:
                if(mMyPageFragment == null) {
                    mMyPageFragment = MyPageFragment.newInstance();
                    mFragmentManager.beginTransaction()
                            .hide(mCurruntFragment)
                            .add(R.id.frame_container, mMyPageFragment)
                            .commit();
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

    @Override
    public void showResult() {
        Toast.makeText(this, R.string.token_expired, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
