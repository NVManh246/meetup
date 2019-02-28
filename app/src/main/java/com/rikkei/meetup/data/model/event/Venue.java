package com.rikkei.meetup.data.model.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Venue implements Parcelable {
    @SerializedName("id")
    private long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("type")
    private String mType;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("schedule_openinghour")
    private String mScheduleOpeningHour;
    @SerializedName("schedule_closinghour")
    private String mScheduleClosingHour;
    @SerializedName("schedule_closed")
    private String mScheduleClosed;

    public Venue() {
    }

    protected Venue(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mType = in.readString();
        mDescription = in.readString();
        mScheduleOpeningHour = in.readString();
        mScheduleClosingHour = in.readString();
        mScheduleClosed = in.readString();
    }

    public static final Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getScheduleOpeningHour() {
        return mScheduleOpeningHour;
    }

    public String getScheduleClosingHour() {
        return mScheduleClosingHour;
    }

    public String getScheduleClosed() {
        return mScheduleClosed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mType);
        dest.writeString(mDescription);
        dest.writeString(mScheduleOpeningHour);
        dest.writeString(mScheduleClosingHour);
        dest.writeString(mScheduleClosed);
    }
}
