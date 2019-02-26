package com.rikkei.meetup.screen.profile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.signup.RegisterFragment;
import com.rikkei.meetup.ultis.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    @BindView(R.id.text_email)
    TextView mTextEmail;
    private Unbinder mUnbinder;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mTextEmail.setText(StringUtils.getName(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.button_going)
    public void onGoingClick() {
        Intent intent = EventStatusActivity
                .getEventStatusIntent(getContext(), EventStatusActivity.STATUS_GOING);
        startActivity(intent);
    }

    @OnClick(R.id.button_went)
    public void onWentClick() {
        Intent intent = EventStatusActivity
                .getEventStatusIntent(getContext(), EventStatusActivity.STATUS_WENT);
        startActivity(intent);
    }

    @OnClick(R.id.button_venue)
    public void onVenueClick() {
        Intent intent = EventStatusActivity
                .getEventStatusIntent(getContext(), EventStatusActivity.VENUE);
        startActivity(intent);
    }

    @OnClick(R.id.button_logout)
    public void onLogoutClick() {
        showConfirmLogoutDialog();
    }

    private void showConfirmLogoutDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_confirm);
        TextView textTitle = dialog.findViewById(R.id.text_title);
        TextView textAlert = dialog.findViewById(R.id.text_alert);
        textTitle.setText(getString(R.string.logout));
        textAlert.setText(getString(R.string.comfirm_logout));
        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                StringUtils.saveToken(getContext(), null, null);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_my_page, RegisterFragment.newInstance())
                        .commit();
            }
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
