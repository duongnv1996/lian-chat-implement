package com.skynet.lian.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Seen implements Parcelable {
    @SerializedName("user_id")
    String user_id;
    @SerializedName("room_id")
    String room_id;
    @SerializedName("message_id")
    String message_id;
    @SerializedName("user_name")
    String user_name;
    @SerializedName("time")
    String time;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.room_id);
        dest.writeString(this.message_id);
        dest.writeString(this.user_name);
        dest.writeString(this.time);
    }

    public Seen() {
    }

    protected Seen(Parcel in) {
        this.user_id = in.readString();
        this.room_id = in.readString();
        this.message_id = in.readString();
        this.user_name = in.readString();
        this.time = in.readString();
    }

    public static final Creator<Seen> CREATOR = new Creator<Seen>() {
        @Override
        public Seen createFromParcel(Parcel source) {
            return new Seen(source);
        }

        @Override
        public Seen[] newArray(int size) {
            return new Seen[size];
        }
    };
}
