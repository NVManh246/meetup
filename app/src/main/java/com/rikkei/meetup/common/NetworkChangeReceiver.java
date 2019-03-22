package com.rikkei.meetup.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rikkei.meetup.ultis.NetworkUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private OnNetworkChangedListener mListener;

    public NetworkChangeReceiver(OnNetworkChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtils.getConnectivityStatus(context);
        mListener.onNetworkChanged(status);
    }
}
