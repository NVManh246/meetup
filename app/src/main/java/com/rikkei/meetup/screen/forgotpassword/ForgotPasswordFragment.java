package com.rikkei.meetup.screen.forgotpassword;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.ultis.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class ForgotPasswordFragment extends Fragment implements ForgotPasswordContract.View {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.button_forgot_password) Button mButtonForgotPassword;
    @BindView(R.id.progress) ProgressBar mProgress;
    private Unbinder mUnbinder;

    private ForgotPasswordContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private String mEmail;

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        setupToolbar();
        mPresenter = new ForgotPasswordPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void setupToolbar() {
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
    }

    @OnTextChanged(R.id.edit_email)
    public void onEmailChanged(CharSequence c) {
        mEmail = String.valueOf(c);
        if(!TextUtils.isEmpty(mEmail)) {
            mButtonForgotPassword.setEnabled(true);
        } else {
            mButtonForgotPassword.setEnabled(false);
        }
    }

    @OnClick(R.id.button_forgot_password)
    public void onButtonForgotPasswordClick() {
        mPresenter.forgotPassword(mEmail);
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
        mButtonForgotPassword.setText("");
        mButtonForgotPassword.setClickable(false);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
        mButtonForgotPassword.setText(R.string.reset_password);
        mButtonForgotPassword.setClickable(true);
    }

    @Override
    public void showErrorEmail() {
        Toast.makeText(getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess() {
        Toast.makeText(getContext(), R.string.reset_email_success, Toast.LENGTH_SHORT).show();
    }
}
