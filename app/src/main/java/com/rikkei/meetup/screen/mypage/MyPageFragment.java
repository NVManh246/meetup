package com.rikkei.meetup.screen.mypage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkei.meetup.R;
import com.rikkei.meetup.screen.signup.RegisterFragment;

public class MyPageFragment extends Fragment {

    public static MyPageFragment newInstance() {
        MyPageFragment fragment = new MyPageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_my_page, RegisterFragment.newInstance())
                .commit();
    }
}
