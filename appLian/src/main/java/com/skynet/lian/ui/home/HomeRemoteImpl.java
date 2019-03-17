package com.skynet.lian.ui.home;

import com.google.gson.Gson;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import retrofit2.Call;
import retrofit2.Response;

public class HomeRemoteImpl extends Interactor implements HomeContract.Interactor {
    HomeContract.Listener presenter;

    public HomeRemoteImpl(HomeContract.Listener presenter) {
        this.presenter = presenter;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }

    @Override
    public void doGetInfor(String idUser) {
        Profile profile = new Gson().fromJson(idUser, Profile.class);
        if (profile == null) {
            presenter.onErrorAuthorization();
            return;
        }
        getmService().getProfile(profile.getId()).enqueue(new CallBackBase<ApiResponse<Profile>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Profile>> call, final Response<ApiResponse<Profile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS && response.body().getData() != null) {
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                presenter.onSuccessGetInfor(response.body().getData());
//                                AppController.getInstance().setListBanner(response.body().getData().getBanners());
                            }
                        }, 2000);
                    } else {
                        presenter.onErrorAuthorization();
                    }
                } else {
                    presenter.onErrorAuthorization();
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Profile>> call, Throwable t) {
                presenter.onErrorAuthorization();

            }
        });
    }
}
