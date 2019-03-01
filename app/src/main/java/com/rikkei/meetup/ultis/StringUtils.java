package com.rikkei.meetup.ultis;

import android.content.Context;
import android.text.TextUtils;

import com.rikkei.meetup.R;
import com.rikkei.meetup.data.model.event.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final int TIME_MILLIS_OF_DAY = 86400000;

    public static String covertDate(Context context, String strDate) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(strDate);
        Long publicTime = date.getTime();
        Long currentTime = Calendar.getInstance().getTimeInMillis();
        Long timeOffset = currentTime - publicTime;
        int dayOffet = (int) (timeOffset/TIME_MILLIS_OF_DAY);
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

        if(!TextUtils.isEmpty(event.getSchedulePermanent())) {
            return context.getString(R.string.permanent);
        }
        if(currentDate.before(startDate)) {
            return event.getScheduleStartDate();
        } else {
            return event.getScheduleEndDate();
        }
    }
}
