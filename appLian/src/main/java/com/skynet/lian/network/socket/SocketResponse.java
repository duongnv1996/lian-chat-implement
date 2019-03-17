package com.skynet.lian.network.socket;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/20/2017.
 */

public class SocketResponse implements Parcelable {

    @SerializedName("room_id")
    String idRoom;

    @SerializedName("idOrignal")
    String idOrignal;

    @SerializedName("idDestination")
    String idDestination;

    @SerializedName("message")
    String message;

    public String getIdOrignal() {
        return idOrignal;
    }

    public void setIdOrignal(String idOrignal) {
        this.idOrignal = idOrignal;
    }

    public String getIdDestination() {
        return idDestination;
    }

    public void setIdDestination(String idDestination) {
        this.idDestination = idDestination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public SocketResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idRoom);
        dest.writeString(this.idOrignal);
        dest.writeString(this.idDestination);
        dest.writeString(this.message);
    }

    protected SocketResponse(Parcel in) {
        this.idRoom = in.readString();
        this.idOrignal = in.readString();
        this.idDestination = in.readString();
        this.message = in.readString();
    }

    public static final Creator<SocketResponse> CREATOR = new Creator<SocketResponse>() {
        @Override
        public SocketResponse createFromParcel(Parcel source) {
            return new SocketResponse(source);
        }

        @Override
        public SocketResponse[] newArray(int size) {
            return new SocketResponse[size];
        }
    };
}
