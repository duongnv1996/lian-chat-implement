package com.skynet.lian.ui.notification;


import com.skynet.lian.models.Notification;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationRemoteImpl extends Interactor implements NotificationContract.Interactor {
    NotificationContract.Presenter presenter;

    public NotificationRemoteImpl(NotificationContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }



    @Override
    public void doGetAllService(String idShop) {
        getmService().getListNotification(idShop, 2).enqueue(new CallBackBase<ApiResponse<List<Notification>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS && response.body().getData() != null) {

                        presenter.onSuccessGetServices(response.body().getData());
                    } else {
                        presenter.onError(response.body().getMessage());
                    }
                } else {
                    presenter.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                presenter.onErrorApi(t.getMessage());
            }
        });
    }
}
