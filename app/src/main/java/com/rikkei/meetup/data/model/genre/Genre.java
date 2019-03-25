package com.rikkei.meetup.data.model.genre;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Genre implements Parcelable {
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("slug")
    private String mSlug;

    public Genre(int id, String name, String slug) {
        mId = id;
        mName = name;
        mSlug = slug;
    }

    public Genre() {
    }

    protected Genre(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mSlug = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getSlug() {
        return mSlug;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mSlug);
    }
}
