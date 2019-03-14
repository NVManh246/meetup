package com.rikkei.meetup.ultis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public static final int NOT_CONNECTED = 0;
    public static final int CONNECTED = 1;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null ? CONNECTED : NOT_CONNECTED;
    }
}
