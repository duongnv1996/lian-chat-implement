package com.skynet.lian.ui.tabexplore;

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

public class TimelineImplRemote extends Interactor implements TimeLineContract.Interactor {
    TimeLineContract.Listener listener;

    public TimelineImplRemote(TimeLineContract.Listener listener) {
        this.listener = listener;
    }


    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }

    @Override
    public void getTimeLine(int index) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        Call<ApiResponse<Timeline>> call ;
            call =getmService().getNewsfeed(profile.getId(),    profile.getId(),index);
        call.enqueue(new CallBackBase<ApiResponse<Timeline>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Timeline>> call, Response<ApiResponse<Timeline>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getCode() == AppConstant.CODE_API_SUCCESS){
                        listener.onSucessGetTimeline(response.body().getData());
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



}
