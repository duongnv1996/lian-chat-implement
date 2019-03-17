package com.skynet.lian.ui.chatgroup.editgroup;

import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Room;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import retrofit2.Call;
import retrofit2.Response;

public class EditImplRemote extends Interactor implements EditContract.Interactor {
    EditContract.Listener listener;

    public EditImplRemote(EditContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }



    @Override
    public void edtChatGroup(String name,String idGroup) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().editGroup(idGroup,name).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getCode() == AppConstant.CODE_API_SUCCESS){
                        listener.onSucessEdtChatGroup();
                    }else listener.onError(response.body().getMessage());
                }else{
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());

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
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getCode() == AppConstant.CODE_API_SUCCESS){
                        listener.onSucessDeleteChat();
                    }else listener.onError(response.body().getMessage());
                }else{
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());

            }
        });
    }

    @Override
    public void leaveGroupChat(String id) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().leaveGroupChat(id,profile.getId()).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getCode() == AppConstant.CODE_API_SUCCESS){
                        listener.onSucessLeaveGroupChat();
                    }else listener.onError(response.body().getMessage());
                }else{
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());

            }
        });
    }

    @Override
    public void addUserToGroup(String idGroup,String idUser) {
        getmService().addUserGroup(idGroup,idUser).enqueue(new CallBackBase<ApiResponse<Room>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Room>> call, Response<ApiResponse<Room>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getCode() == AppConstant.CODE_API_SUCCESS){
                        listener.onSucessAddUserToGroup(response.body().getData());
                    }else listener.onError(response.body().getMessage());
                }else{
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Room>> call, Throwable t) {
                listener.onError(t.getMessage());

            }
        });

    }

}
