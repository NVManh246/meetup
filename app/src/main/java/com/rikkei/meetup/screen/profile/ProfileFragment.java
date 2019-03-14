package com.rikkei.meetup.screen.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkei.meetup.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

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
}
