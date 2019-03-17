package com.skynet.lian.ui.searchfriend;

import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.network.api.ExceptionHandler;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchContactImplRemote extends Interactor implements SearchContactContract.Interactor {
    SearchContactContract.Listener listener;

    public SearchContactImplRemote(SearchContactContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }



    @Override
    public void getListContact(String cr) {
                 Profile profile = AppController.getInstance().getmProfileUser();
                 if (profile == null) {
                     listener.onErrorAuthorization();
                     return;
                 }
                 getmService().queryContact(profile.getId(),cr).enqueue(new CallBackBase<ApiResponse<List<Profile>>>() {
                     @Override
                     public void onRequestSuccess(Call<ApiResponse<List<Profile>>> call, Response<ApiResponse<List<Profile>>> response) {
                         if (response.isSuccessful() && response.body() != null) {
                             if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                                 listener.onSucessListContact(response.body().getData());
                             } else {
                                 new ExceptionHandler<List<Profile>>(listener, response.body()).excute();
                             }
                         } else {
                             listener.onError(response.message());
                         }
                     }
                     @Override
                     public void onRequestFailure(Call<ApiResponse<List<Profile>>> call, Throwable t) {
                         listener.onErrorApi(t.getMessage());
                     }
                 });
             }

    @Override
    public void addFriend(String idFriend) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().addFried(profile.getId(),idFriend).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSucessAddFriend();
                    } else {
                        new ExceptionHandler<List<Profile>>(listener, response.body()).excute();
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
