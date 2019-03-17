package com.skynet.lian.ui.tabchat;

import com.blankj.utilcode.util.LogUtils;
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

public class TabChatImplRemote extends Interactor implements TabChatContract.Interactor {
    TabChatContract.Listener listener;

    public TabChatImplRemote(TabChatContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }

    @Override
    public void getListFriendOnline() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().getListFriends(profile.getId()).enqueue(new CallBackBase<ApiResponse<List<Profile>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<Profile>>> call, Response<ApiResponse<List<Profile>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSucessListFriendOnline(response.body().getData());

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
    public void getListChat() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().getListChats(profile.getId()).enqueue(new CallBackBase<ApiResponse<List<ChatItem>>>() {
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
        LogUtils.e(id + " delete chat");
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().deleteChat(profile.getId(),id).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {

            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void getListChat(String query) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().getListChats(profile.getId(),query).enqueue(new CallBackBase<ApiResponse<List<ChatItem>>>() {
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
