package com.rikkei.meetup.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.rikkei.meetup.ultis.StringUtils;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String CUSTOM_INTENT = "com.rikkie.intent.action.ALARM";
    public static final int HOUR_UPDATE = 17;
    public static final int MINUTE_UPDATE = 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateJobIntentService.enqueueWork(context, intent);
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    public static void setAlarm(Context context, boolean force) {
        cancelAlarm(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long delay = getTimeDelay(HOUR_UPDATE, MINUTE_UPDATE);
        long when = System.currentTimeMillis();
        if (!force) {
            when += delay;
        }

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, when, getPendingIntent(context));
        } else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, when, getPendingIntent(context));
        } else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    when, getPendingIntent(context));
        }
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction(CUSTOM_INTENT);
        return PendingIntent.getBroadcast(context,
                0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static long getTimeDelay(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long delayTime = calendar.getTimeInMillis();
        return delayTime > currentTime ?
                delayTime - currentTime : delayTime - currentTime + StringUtils.TIME_MILLIS_OF_DAY;
    }
}
