package com.skynet.lian.ui.whitelist;

import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class WhiteListImplRemote extends Interactor implements WhiteListContract.Interactor {
    WhiteListContract.Listener listener;

    public WhiteListImplRemote(WhiteListContract.Listener listener) {
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
        getmService().getListWhitelist(profile.getId()).enqueue(new CallBackBase<ApiResponse<List<Profile>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<Profile>>> call, Response<ApiResponse<List<Profile>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSucessGetList(response.body().getData());
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
    public void deleteFromWhitelist(String id,int type) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().block(profile.getId(),id,type).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                    }
                } else {
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
            }
        });
    }
}
