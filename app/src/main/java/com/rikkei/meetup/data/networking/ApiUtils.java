package com.rikkei.meetup.data.networking;

public class ApiUtils {
    private static final String BASE_URL
            = "http://172.16.14.61/18175d1_mobile_100_fresher/public/api/v0/";

    public static ApiClient getApi() {
       return ApiConfig.getRetrofit(BASE_URL).create(ApiClient.class);
    }
}
