package com.skynet.lian.ui.makepost;

import com.google.gson.annotations.SerializedName;
import com.skynet.lian.models.PlaceNearby;

import java.util.List;

public class ResponseRout {
//
//    @SerializedName(value = "errorId", alternate = "status")
//    int code;
//    @SerializedName(value = "message", alternate = "content")
//    String message;

    @SerializedName("results")
    List<PlaceNearby> placeNearBy;

//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }


    public List<PlaceNearby> getPlaceNearBy() {
        return placeNearBy;
    }

    public void setPlaceNearBy(List<PlaceNearby> placeNearBy) {
        this.placeNearBy = placeNearBy;
    }

}
