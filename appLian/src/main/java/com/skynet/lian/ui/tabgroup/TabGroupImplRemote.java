package com.skynet.lian.ui.tabgroup;

import com.skynet.lian.application.AppController;
import com.skynet.lian.models.ChatItem;
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

public class TabGroupImplRemote extends Interactor implements TabGoupContract.Interactor {
    TabGoupContract.Listener listener;

    public TabGroupImplRemote(TabGoupContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }


    @Override
    public void getListChat(String query) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().getListGroupChats(profile.getId(),query).enqueue(new CallBackBase<ApiResponse<List<ChatItem>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<ChatItem>>> call, Response<ApiResponse<List<ChatItem>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSucessListChat(response.body().getData());
                    } else {
                        new ExceptionHandler<List<ChatItem>>(listener, response.body()).excute();
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<List<ChatItem>>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }
    @Override
    public void deleteChat(String id) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().deleteGroupChat(id,profile.getId()).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {

            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
    @Override
    public void getListChat() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().getListGroup(profile.getId()).enqueue(new CallBackBase<ApiResponse<List<ChatItem>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<ChatItem>>> call, Response<ApiResponse<List<ChatItem>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSucessListChat(response.body().getData());
                    } else {
                        new ExceptionHandler<List<ChatItem>>(listener, response.body()).excute();
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<List<ChatItem>>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }
}
