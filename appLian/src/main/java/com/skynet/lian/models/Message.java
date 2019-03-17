package com.skynet.lian.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Huy on 4/21/2018.
 */

public class Message implements Parcelable {

    @Expose
    @SerializedName("active")
    private String active;
    @Expose
    @SerializedName("list_user_id_seen")
    private String list_user_id_seen;
    @Expose
    @SerializedName("avatar")
    private String avatar;
    @Expose
    @SerializedName("content_type")
    private int content_type;
    @Expose
    @SerializedName("type")
    private int type;
    @Expose
    @SerializedName("content")
    private String content;
    @Expose
    @SerializedName("time")
    private String time;
    @Expose
    @SerializedName("chat_id")
    private String chat_id;
    @Expose
    @SerializedName("user_id")
    private String user_id;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("file")
    private String file;
    @Expose
    @SerializedName("image")
    private String image;

    @SerializedName("list_image")
    private List<String> listImage;

    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Message() {
    }

    ;

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getList_user_id_seen() {
        return list_user_id_seen;
    }

    public void setList_user_id_seen(String list_user_id_seen) {
        this.list_user_id_seen = list_user_id_seen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.active);
        dest.writeString(this.list_user_id_seen);
        dest.writeString(this.avatar);
        dest.writeInt(this.content_type);
        dest.writeInt(this.type);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeString(this.chat_id);
        dest.writeString(this.user_id);
        dest.writeString(this.id);
        dest.writeString(this.file);
        dest.writeString(this.image);
        dest.writeStringList(this.listImage);
    }

    protected Message(Parcel in) {
        this.active = in.readString();
        this.list_user_id_seen = in.readString();
        this.avatar = in.readString();
        this.content_type = in.readInt();
        this.type = in.readInt();
        this.content = in.readString();
        this.time = in.readString();
        this.chat_id = in.readString();
        this.user_id = in.readString();
        this.id = in.readString();
        this.file = in.readString();
        this.image = in.readString();
        this.listImage = in.createStringArrayList();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
