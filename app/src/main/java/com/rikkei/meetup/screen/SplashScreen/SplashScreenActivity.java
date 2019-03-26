package com.rikkei.meetup.screen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.main.MainActivity;
import com.rikkei.meetup.service.AlarmReceiver;
import com.rikkei.meetup.ultis.StringUtils;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenContract.View {

    private SplashScreenContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mPresenter = new SplashScreenPresenter(this);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        boolean isRunFirstTime = StringUtils.isRunFirstTime(this);
        if(!isRunFirstTime) {
            mPresenter.saveNewsIfRunFirstTime();
            StringUtils.saveIsRunFirstTime(this, true);
        }
        AlarmReceiver.setAlarm(this, false);
        String token = StringUtils.getToken(this);
        if (token != null) {
            mPresenter.checkTokenExpired(token);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = MainActivity.getMainIntent(SplashScreenActivity.this);
                    startActivity(intent);
                    finish();
                }
            }, 1500);
        }
    }

    @Override
    public void showResult() {
        Toast.makeText(this, R.string.token_expired, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigationToMain() {
        Intent intent = MainActivity.getMainIntent(this);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
