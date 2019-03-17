package com.skynet.lian.ui.profile;

import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class UploadInteractor extends Interactor implements UploadContract.Interactor {
    UploadContract.Listener listener;

    public UploadInteractor(UploadContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }

    @Override
    public void upload(File file, MultipartBody.Part part, final int type) {
        Profile profile = AppController.getInstance().getmProfileUser();

        if (profile == null) {
            return;
        }
        RequestBody idRequest = ApiUtil.createPartFromString(AppController.getInstance().getmProfileUser().getId());
        RequestBody typeRequest = ApiUtil.createPartFromString(type + "");
        Map<String, RequestBody> map = new HashMap<>();
        map.put("user_id", idRequest);
        map.put("type", typeRequest);
        getmService().uploadAvatar(part, map).enqueue(new CallBackBase<ApiResponse<String>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        if (type == 1)
                            AppController.getInstance().getmProfileUser().setAvatar(response.body().getData());
                        else
                            AppController.getInstance().getmProfileUser().setCover(response.body().getData());

                        listener.onSucessUploadAvat();
                    } else {
                        listener.onError(response.body().getMessage());
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<String>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });

    }
}
