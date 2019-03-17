package com.skynet.lian.ui.profile;

import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Timeline;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileImplRemote extends Interactor implements ProfileContract.Interactor {
    ProfileContract.Listener listener;

    public ProfileImplRemote(ProfileContract.Listener listener) {
        this.listener = listener;
    }


    @Override
    public void shareContent(int idPost, String content, int type) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().sharePost(idPost,profile.getId(),content,type).enqueue(new CallBackBase<ApiResponse<Integer>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS && response.body().getData() != null) {
                        listener.onSucessShare(response.body().getData());
                    } else {
                        listener.onError(response.body().getMessage());
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());

            }
        });
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }

    @Override
    public void getTimeline(int index,int type,String id) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        Call<ApiResponse<Timeline>> call ;
        if(type == AppConstant.TYPE_POST){
            call =getmService().getListTimeline(profile.getId(),id == null ? profile.getId() : id,index);
        }else if(type == AppConstant.TYPE_IMAGE){
            call =getmService().getListPhotos(profile.getId(),id == null ? profile.getId() : id,index);

        }else{
            call =getmService().getListVideos(profile.getId(),id == null ? profile.getId() : id,index);

        }
        call.enqueue(new CallBackBase<ApiResponse<Timeline>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Timeline>> call, Response<ApiResponse<Timeline>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getCode() == AppConstant.CODE_API_SUCCESS){
                        listener.onSucessGetTimeLine(response.body().getData());
                    }else{
                        listener.onError(response.body().getMessage());
                    }
                }else{
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Timeline>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }

    @Override
    public void toggleLike(int id, int toogle) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().toggleLikePost(profile.getId(),id,toogle).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getCode() == AppConstant.CODE_API_SUCCESS){
                    }else{
                    }
                }else{
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }

}
