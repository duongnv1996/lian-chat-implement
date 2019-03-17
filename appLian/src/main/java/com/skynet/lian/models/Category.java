package com.skynet.lian.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category implements Parcelable {

    @Expose
    @SerializedName("active")
    private String active;
    @Expose
    @SerializedName("date")
    private String date;
    @Expose
    @SerializedName("img")
    private String img;
    @Expose
    @SerializedName("content")
    private String content;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("number_ex")
    private int number_ex;
    @Expose
    @SerializedName("ex")
    private List<Excercise> listEx;

    public List<Excercise> getListEx() {
        return listEx;
    }

    public void setListEx(List<Excercise> listEx) {
        this.listEx = listEx;
    }

    public String getActive() {
        return active;
    }

    public int getNumber_ex() {
        return number_ex;
    }

    public void setNumber_ex(int number_ex) {
        this.number_ex = number_ex;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.active);
        dest.writeString(this.date);
        dest.writeString(this.img);
        dest.writeString(this.content);
        dest.writeString(this.title);
        dest.writeInt(this.id);
        dest.writeInt(this.number_ex);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.active = in.readString();
        this.date = in.readString();
        this.img = in.readString();
        this.content = in.readString();
        this.title = in.readString();
        this.id = in.readInt();
        this.number_ex = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
