package com.skynet.lian.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatItem {
    @Expose
    @SerializedName("user_avatar")
    private String user_avatar;
    @Expose
    @SerializedName("list_avatar")
    private List<String> listAvatar;
    @Expose
    @SerializedName(value = "user_name")
    private String user_name;
    @Expose
    @SerializedName("active")
    private String active;
    @Expose
    @SerializedName("last_message")
    private String last_message;
    @Expose
    @SerializedName("time_updated")
    private String time_updated;
    @Expose
    @SerializedName("type")
    private int type;
    @Expose
    @SerializedName("list_user_id_del")
    private String list_user_id_del;
    @Expose
    @SerializedName("user_id_receive")
    private String user_id_receive;
    @Expose
    @SerializedName("user_id_send")
    private String user_id_send;
    @Expose
    @SerializedName("date")
    private String date;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("number_seen")
    private int is_read;
    @Expose
    @SerializedName("online")
    private int user_online;

    public List<String> getListAvatar() {
        return listAvatar;
    }

    public void setListAvatar(List<String> listAvatar) {
        this.listAvatar = listAvatar;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public int getUser_online() {
        return user_online;
    }

    public void setUser_online(int user_online) {
        this.user_online = user_online;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(String time_updated) {
        this.time_updated = time_updated;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getList_user_id_del() {
        return list_user_id_del;
    }

    public void setList_user_id_del(String list_user_id_del) {
        this.list_user_id_del = list_user_id_del;
    }

    public String getUser_id_receive() {
        return user_id_receive;
    }

    public void setUser_id_receive(String user_id_receive) {
        this.user_id_receive = user_id_receive;
    }

    public String getUser_id_send() {
        return user_id_send;
    }

    public void setUser_id_send(String user_id_send) {
        this.user_id_send = user_id_send;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
