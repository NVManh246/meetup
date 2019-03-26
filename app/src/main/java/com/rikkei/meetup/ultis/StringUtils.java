package com.rikkei.meetup.ultis;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String REGEX_EMAIL
            = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final int TIME_MILLIS_OF_DAY = 86400000;

    private static final String SHARED_PREF_TOKEN = "sptoken";
    private static final String SHARED_PREF_SETTING = "sp_setting";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_RUN_FIRST_TIME = "run_first_time";

    public static String covertDate(Context context, String strDate) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(strDate);
        Long publicTime = date.getTime();
        Long currentTime = Calendar.getInstance().getTimeInMillis();
        Long timeOffset = currentTime - publicTime;
        int dayOffet = (int) (timeOffset / TIME_MILLIS_OF_DAY);
        switch (dayOffet) {
            case 0:
                return context.getString(R.string.today);
            case 1:
                return context.getString(R.string.yesterday);
        }
        return dayOffet + " " + context.getString(R.string.day_ago);
    }

    public static String getDateEventField(Context context, Event event) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date currentDate = Calendar.getInstance().getTime();
        Date startDate = dateFormat.parse(event.getScheduleStartDate());

        if (!TextUtils.isEmpty(event.getSchedulePermanent())) {
            return context.getString(R.string.permanent);
        }
        if (currentDate.before(startDate)) {
            return event.getScheduleStartDate();
        } else {
            return event.getScheduleEndDate();
        }
    }

    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile(StringUtils.REGEX_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void saveToken(Context context, String token, String email) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        return token != null ? "Bearer " + sharedPreferences.getString(KEY_TOKEN, null) : null;
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        return email != null ? email.split("@")[0] : null;
    }

    public static String getDistance(int meter) {
        StringBuilder result = new StringBuilder();
        if (meter < 1000) {
            result.append(meter).append("m");
        } else {
            float kilometer = (float) meter / 1000;
            result.append(Math.round(kilometer * 10) / 10).append("km");
        }
        return result.toString();
    }

    public static String getCount(String pre, List<Event> events) {
        StringBuilder result = new StringBuilder();
        result.append(pre)
                .append("(")
                .append(events.isEmpty() ? 0 : (events.size() - 1))
                .append(")");
        return result.toString();
    }

    public static String getContentNotification(Context context, int size) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.update_success))
                .append(" ")
                .append(size)
                .append(" ")
                .append(context.getString(R.string.news));
        return builder.toString();
    }

    public static boolean isRunFirstTime(Context context) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREF_SETTING, Context.MODE_PRIVATE);
        boolean isRunFirstTime = sharedPreferences.getBoolean(KEY_RUN_FIRST_TIME, false);
        return isRunFirstTime;
    }

    public static void saveIsRunFirstTime(Context context, boolean isRunFirstTime) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(SHARED_PREF_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_RUN_FIRST_TIME, isRunFirstTime);
        editor.commit();
    }
}
