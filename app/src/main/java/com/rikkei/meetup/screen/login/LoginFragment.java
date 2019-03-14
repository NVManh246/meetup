package com.rikkei.meetup.screen.login;

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
import com.rikkei.meetup.screen.forgotpassword.ForgotPasswordFragment;
import com.rikkei.meetup.screen.profile.ProfileFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class LoginFragment extends Fragment implements LoginContract.View {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.button_login) Button mButtonLogin;
    @BindView(R.id.progress_login) ProgressBar mProgress;

    private Unbinder mUnbinder;
    private AppCompatActivity mActivity;

    private LoginContract.Presenter mPresenter;
    private String mEmail;
    private String mPassword;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new LoginPresenter(this);
        mUnbinder = ButterKnife.bind(this, view);
        setupToolbar();
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

    @OnClick(R.id.text_forgot_password)
    public void onForgotPasswordClick() {
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_my_page, ForgotPasswordFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.button_login)
    public void onButtonLoginClick() {
        mPresenter.login(mEmail, mPassword);
    }

    @OnTextChanged(R.id.edit_email)
    public void onEmailTextChanged(CharSequence c) {
        mEmail = String.valueOf(c);
        setEnableButtonRegister(mEmail, mPassword);
    }

    @OnTextChanged(R.id.edit_password)
    public void onPasswordTextChanged(CharSequence c) {
        mPassword = String.valueOf(c);
        setEnableButtonRegister(mEmail, mPassword);
    }

    private void setEnableButtonRegister(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mButtonLogin.setEnabled(true);
        } else {
            mButtonLogin.setEnabled(false);
        }
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
        mButtonLogin.setText("");
        mButtonLogin.setClickable(false);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
        mButtonLogin.setText(R.string.login);
        mButtonLogin.setClickable(true);
    }

    @Override
    public void showErrorEmail() {
        Toast.makeText(getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorPassword() {
        Toast.makeText(getContext(), R.string.invalid_password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.please_check_again, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigationToProfile() {
        getFragmentManager().popBackStack();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_my_page, ProfileFragment.newInstance())
                .commit();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
}
