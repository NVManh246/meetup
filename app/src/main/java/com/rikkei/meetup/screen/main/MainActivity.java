package com.rikkei.meetup.screen.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.browser.BrowserFragment;
import com.rikkei.meetup.screen.home.HomeFragment;
import com.rikkei.meetup.screen.mypage.MyPageFragment;
import com.rikkei.meetup.screen.near.NearFragment;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView mBottomTabBar;
    private HomeFragment mHomeFragment;
    private NearFragment mNearFragment;
    private BrowserFragment mBrowserFragment;
    private MyPageFragment mMyPageFragment;
    private FragmentManager mFragmentManager;
    private Fragment mCurruntFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomTabBar = findViewById(R.id.navigation_bottom_bar);
        mBottomTabBar.setOnNavigationItemSelectedListener(this);
        initFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                showFragment(mHomeFragment);
                break;
            case R.id.menu_near:
                showFragment(mNearFragment);
                break;
            case R.id.menu_browser:
                showFragment(mBrowserFragment);
                break;
            case R.id.menu_my_page:
                showFragment(mMyPageFragment);
                break;
        }
        return true;
    }

    private void initFragment() {
        mHomeFragment = HomeFragment.newInstance();
        mNearFragment = NearFragment.newInstance();
        mBrowserFragment = BrowserFragment.newInstance();
        mMyPageFragment = MyPageFragment.newInstance();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.frame_container, mHomeFragment)
                .add(R.id.frame_container, mNearFragment)
                .add(R.id.frame_container, mBrowserFragment)
                .add(R.id.frame_container, mMyPageFragment)
                .hide(mNearFragment)
                .hide(mBrowserFragment)
                .hide(mMyPageFragment)
                .show(mHomeFragment)
                .commit();
        mCurruntFragment = mHomeFragment;
    }

    private void showFragment(Fragment fragmentShow) {
        mFragmentManager.beginTransaction()
                .hide(mCurruntFragment)
                .show(fragmentShow)
                .commit();
        mCurruntFragment = fragmentShow;
    }
}
