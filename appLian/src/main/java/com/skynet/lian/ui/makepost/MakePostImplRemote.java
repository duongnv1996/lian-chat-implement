package com.skynet.lian.ui.makepost;

import android.os.AsyncTask;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.MyPlace;
import com.skynet.lian.models.PlaceNearby;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.network.api.ExceptionHandler;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class MakePostImplRemote extends Interactor implements MakePostContract.Interactor {
    MakePostContract.Listener listener;

    public MakePostImplRemote(MakePostContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }

    @Override
    public void submitPost(String content, int type, String address, List<Image> listPhotos) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        RequestBody paramidHost = RequestBody.create(MediaType.parse("text/plain"), profile.getId());
        RequestBody paramContent = RequestBody.create(MediaType.parse("text/plain"), content + "");
        RequestBody paramType = RequestBody.create(MediaType.parse("text/plain"), type + "");
        RequestBody paramAddress = RequestBody.create(MediaType.parse("text/plain"), address + "");

        List<MultipartBody.Part> parts;
        Call<ApiResponse<Integer>> call;

        if (listPhotos != null) {
            parts = new ArrayList<>();
            for (Image img : listPhotos) {
                RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), img.getFile());
                parts.add(MultipartBody.Part.createFormData("file[]", img.getFile().getName(), requestImageFile));
            }
            call = getmService().submitPost(paramidHost,paramType,paramAddress,paramContent, parts);
        } else {
            call = getmService().submitPost(paramidHost,paramType,paramAddress,paramContent);
        }

        call.enqueue(new CallBackBase<ApiResponse<Integer>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSuccessSubmit(response.body().getData());
                    } else {
                        new ExceptionHandler<Integer>(listener, response.body()).excute();
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                LogUtils.e(t.getMessage());
                listener.onErrorApi(t.getMessage());

            }
        });
    }

    @Override
    public void getPlaceNearby(float lat,float lng) {
        final String latlngStr = lat + "," + lng;
//        Call<ApiResponse<List<PlaceNearby>>> getNear = ApiUtil.getAPIPLACE().getNearby(latlngStr, 100, "bus",5, AppConstant.GG_KEY);
//        Call<JsonObject>  call= ApiUtil.getAPIPLACE().getNearbyJson(latlngStr, 100, "bus",5, AppConstant.GG_KEY);
//        call.enqueue(new CallBackBase<JsonObject>() {
//            @Override
//            public void onRequestSuccess(Call<JsonObject> call, Response<JsonObject> response) {
//
//            }
//
//            @Override
//            public void onRequestFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                Request request = new Request.Builder()
                        .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latlngStr+"&radius=500&limit=5&key=AIzaSyD89SAwZAK3SuiyF9jfckdN0OKuQOop0x8")
                        .build();

                try (okhttp3.Response response = client.newCall(request).execute()) {
                   return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(String myPlaces) {
                super.onPostExecute(myPlaces);
                if(myPlaces != null){
                }
                ResponseRout response1= new Gson().fromJson(myPlaces,ResponseRout.class);
                if (response1 != null ) {
                    List<MyPlace> myPlaceList = new ArrayList<>();
                    for (PlaceNearby place : response1.getPlaceNearBy()) {
                        MyPlace mItem = new MyPlace();
                        mItem.setLat(place.getGeometry().getLocation().getLat());
                        mItem.setLng(place.getGeometry().getLocation().getLng());
                        mItem.setName(place.getName());
                        mItem.setDescription(place.getVicinity());
                        myPlaceList.add(mItem);
                        if(myPlaceList.size() == 3) break;
                    }
                    LogUtils.e("onsucess get nearby");
                    listener.onSucessGetPlaces(myPlaceList);
                } else {

                }
            }
        }.execute();
//
//
//        ApiUtil.getAPIPLACE().getNearby(lat+","+lng, 1000, "bus",5, AppConstant.GG_KEY).enqueue(new Callback<ApiResponse<List<PlaceNearby>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<PlaceNearby>>> call, Response<ApiResponse<List<PlaceNearby>>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().getCode() == 0) {
//                        List<MyPlace> myPlaceList = new ArrayList<>();
//                        for (PlaceNearby place : response.body().getPlaceNearBy()) {
//                            MyPlace mItem = new MyPlace();
//                            mItem.setLat(place.getGeometry().getLocation().getLat());
//                            mItem.setLng(place.getGeometry().getLocation().getLng());
//                            String[] vics = place.getVicinity().split(",");
//                            mItem.setName(place.getName());
//                            mItem.setDescription(place.getVicinity());
//                            myPlaceList.add(mItem);
//                        }
//                        listener.onSucessGetPlaces(myPlaceList);
//                    } else {
//                        listener.onError(response.body().getMessage());
//                    }
//                } else {
//                    listener.onError(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<PlaceNearby>>> call, Throwable t) {
//                listener.onErrorApi(t.getMessage());
//
//            }
//        });

    }
    OkHttpClient client = new OkHttpClient();

    @Override
    public void getPlace(float lat, float lng) {
        ApiUtil.getAPIPLACE().getNearby(lat+","+lng, 1000, "bus",5, AppConstant.GG_KEY).enqueue(new CallBackBase<ApiResponse<List<PlaceNearby>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<PlaceNearby>>> call, Response<ApiResponse<List<PlaceNearby>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 0) {
                        List<MyPlace> myPlaceList = new ArrayList<>();
                        for (PlaceNearby place : response.body().getPlaceNearBy()) {
                            MyPlace mItem = new MyPlace();
                            mItem.setLat(place.getGeometry().getLocation().getLat());
                            mItem.setLng(place.getGeometry().getLocation().getLng());
                            String[] vics = place.getVicinity().split(",");
                            mItem.setName(place.getName());
                            mItem.setDescription(place.getVicinity());
                            myPlaceList.add(mItem);
                        }
                        listener.onSucessGetPlaces(myPlaceList);
                    } else {
                        listener.onError(response.body().getMessage());
                    }
                } else {
                    listener.onError(response.message());
                }
            }
            @Override
            public void onRequestFailure(Call<ApiResponse<List<PlaceNearby>>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }
}
