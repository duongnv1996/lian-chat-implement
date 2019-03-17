package com.skynet.lian.models;

import com.google.gson.annotations.SerializedName;

public class Contact  {
    @SerializedName("name")
    String name;
    @SerializedName(value = "phone",alternate = "mobile")
    String phone;
    @SerializedName("id")
    String id;
    @SerializedName("online")
    int online;
    @SerializedName("avatar")
    String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
