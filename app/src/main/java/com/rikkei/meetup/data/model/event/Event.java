package com.rikkei.meetup.data.model.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event implements Parcelable {
    @SerializedName("id")
    private long mId;
    @SerializedName("status")
    private int mStatus;
    @SerializedName("photo")
    private String mPhoto;
    @SerializedName("name")
    private String mName;
    @SerializedName("description_raw")
    private String mDescriptionRaw;
    @SerializedName("description_html")
    private String mDescriptionHtml;
    @SerializedName("schedule_permanent")
    private String mSchedulePermanent;
    @SerializedName("schedule_date_warning")
    private String mScheduleDateWarning;
    @SerializedName("schedule_time_alert")
    private String mScheduleTimeAlert;
    @SerializedName("schedule_start_date")
    private String mScheduleStartDate;
    @SerializedName("schedule_start_time")
    private String mScheduleStartTime;
    @SerializedName("schedule_end_date")
    private String mScheduleEndDate;
    @SerializedName("schedule_end_time")
    private String mScheduleEndTime;
    @SerializedName("schedule_one_day_event")
    private boolean mScheduleOndayEvent;
    @SerializedName("schedule_extra")
    private String mScheduleExtra;
    @SerializedName("going_count")
    private int mGoingCount;
    @SerializedName("went_count")
    private int mWentCount;
    @SerializedName("my_status")
    private int mMyStatus;
    @SerializedName("venue")
    private Venue mVenue;

    public Event() {
    }

    protected Event(Parcel in) {
        mId = in.readLong();
        mStatus = in.readInt();
        mPhoto = in.readString();
        mName = in.readString();
        mDescriptionRaw = in.readString();
        mDescriptionHtml = in.readString();
        mSchedulePermanent = in.readString();
        mScheduleDateWarning = in.readString();
        mScheduleTimeAlert = in.readString();
        mScheduleStartDate = in.readString();
        mScheduleStartTime = in.readString();
        mScheduleEndDate = in.readString();
        mScheduleEndTime = in.readString();
        mScheduleOndayEvent = in.readByte() != 0;
        mScheduleExtra = in.readString();
        mGoingCount = in.readInt();
        mWentCount = in.readInt();
        mMyStatus = in.readInt();
        mVenue = in.readParcelable(Venue.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public long getId() {
        return mId;
    }

    public int getStatus() {
        return mStatus;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public String getName() {
        return mName;
    }

    public String getDescriptionRaw() {
        return mDescriptionRaw;
    }

    public String getDescriptionHtml() {
        return mDescriptionHtml;
    }

    public String getSchedulePermanent() {
        return mSchedulePermanent;
    }

    public String getScheduleDateWarning() {
        return mScheduleDateWarning;
    }

    public String getScheduleTimeAlert() {
        return mScheduleTimeAlert;
    }

    public String getScheduleStartDate() {
        return mScheduleStartDate;
    }

    public String getScheduleStartTime() {
        return mScheduleStartTime;
    }

    public String getScheduleEndDate() {
        return mScheduleEndDate;
    }

    public String getScheduleEndTime() {
        return mScheduleEndTime;
    }

    public boolean isScheduleOndayEvent() {
        return mScheduleOndayEvent;
    }

    public String getScheduleExtra() {
        return mScheduleExtra;
    }

    public int getGoingCount() {
        return mGoingCount;
    }

    public int getWentCount() {
        return mWentCount;
    }

    public int getMyStatus() {
        return mMyStatus;
    }

    public Venue getVenue() {
        return mVenue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mStatus);
        dest.writeString(mPhoto);
        dest.writeString(mName);
        dest.writeString(mDescriptionRaw);
        dest.writeString(mDescriptionHtml);
        dest.writeString(mSchedulePermanent);
        dest.writeString(mScheduleDateWarning);
        dest.writeString(mScheduleTimeAlert);
        dest.writeString(mScheduleStartDate);
        dest.writeString(mScheduleStartTime);
        dest.writeString(mScheduleEndDate);
        dest.writeString(mScheduleEndTime);
        dest.writeByte((byte) (mScheduleOndayEvent ? 1 : 0));
        dest.writeString(mScheduleExtra);
        dest.writeInt(mGoingCount);
        dest.writeInt(mWentCount);
        dest.writeInt(mMyStatus);
        dest.writeParcelable(mVenue, flags);
    }

    public static final int STATUS_GOING = 1;
    public static final int STATUS_WENT = 2;
}
