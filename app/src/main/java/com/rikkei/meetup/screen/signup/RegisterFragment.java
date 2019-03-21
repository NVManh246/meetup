package com.rikkei.meetup.screen.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.login.LoginFragment;
import com.rikkei.meetup.screen.profile.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class RegisterFragment extends Fragment implements RegisterContract.View {

    @BindView(R.id.edit_fullname) EditText mEditFullname;
    @BindView(R.id.button_register) Button mButtonRegister;
    @BindView(R.id.progress_register) ProgressBar mProgressBar;
    private Unbinder mUnbinder;

    private String mFullname;
    private String mEmail;
    private String mPassword;

    private RegisterContract.Presenter mPresenter;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter = new RegisterPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.text_login)
    public void onLoginClick() {
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_my_page, LoginFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.button_register)
    public void onButtonRegisterClick() {
        String fullname = mEditFullname.getText().toString();
        mPresenter.register(fullname, mEmail, mPassword);
    }

    @OnTextChanged(R.id.edit_fullname)
    public void onTextFullnameChanged(CharSequence c) {
        mFullname = String.valueOf(c);
        setEnableButtonRegister(mFullname, mEmail, mPassword);
    }

    @OnTextChanged(R.id.edit_email)
    public void onTextEmailChanged(CharSequence c) {
        mEmail = String.valueOf(c);
        setEnableButtonRegister(mFullname, mEmail, mPassword);
    }

    @OnTextChanged(R.id.edit_password)
    public void onTextPasswordChanged(CharSequence c) {
        mPassword = String.valueOf(c);
        setEnableButtonRegister(mFullname, mEmail, mPassword);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mButtonRegister.setText("");
        mButtonRegister.setClickable(false);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mButtonRegister.setText(R.string.register);
        mButtonRegister.setClickable(true);
    }

    @Override
    public void showErrorEmail() {
        Toast.makeText(getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorPassword() {
        Toast.makeText(getContext(),
                R.string.invalid_password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(), R.string.please_check_again, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigationToProfile() {
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_my_page, ProfileFragment.newInstance())
                .commit();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    private void setEnableButtonRegister(String fullname, String email, String password) {
        if(!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)) {
            mButtonRegister.setEnabled(true);
        } else {
            mButtonRegister.setEnabled(false);
        }
    }
}
