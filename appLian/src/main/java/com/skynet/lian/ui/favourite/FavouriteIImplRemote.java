package com.skynet.lian.ui.favourite;

import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import retrofit2.Call;
import retrofit2.Response;

public class FavouriteIImplRemote extends Interactor implements FavouriteContract.Interactor {
    FavouriteContract.Listener listener;

    public FavouriteIImplRemote(FavouriteContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }

    @Override
    public void getList() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
//        getmService().getFavouriteList(profile.getId()).enqueue(new CallBackBase<ApiResponse<List<Excercise>>>() {
//            @Override
//            public void onRequestSuccess(Call<ApiResponse<List<Excercise>>> call, Response<ApiResponse<List<Excercise>>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
//                        listener.onSucessGetList(response.body().getData());
//                    } else {
//                        new ExceptionHandler<List<Excercise>>(listener, response.body()).excute();
//                    }
//                } else {
//                    listener.onError(response.message());
//                }
//            }
//
//            @Override
//            public void onRequestFailure(Call<ApiResponse<List<Excercise>>> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    public void toggleFav(int idPost, int isFav) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().toggleFav(profile.getId(), idPost,isFav).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {

                    } else {
                        //  new ExceptionHandler<DetailPost>(listener, response.body()).excute();
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }
}
