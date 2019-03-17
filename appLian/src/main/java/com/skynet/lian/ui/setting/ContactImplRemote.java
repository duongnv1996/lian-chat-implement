package com.skynet.lian.ui.setting;

import android.content.ContentResolver;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.network.api.ExceptionHandler;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.ui.contact.GetContactTask;
import com.skynet.lian.utils.AppConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ContactImplRemote extends Interactor implements ContactContract.Interactor {
    ContactContract.Listener listener;

    public ContactImplRemote(ContactContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }


    @Override
    public void getListBussinessContact() {
//        if(AppController.getInstance().getmSetting().getString("contact_business",null) != null ){
//            String json = AppController.getInstance().getmSetting().getString("contact",null);
//            Type listType = new TypeToken<List<Profile>>() {}.getType();
//            List<Profile> list = new Gson().fromJson(json,listType);
//            if(list!=null) {
//                listener.onS(list);
//                return;
//            }
//        }
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        ApiUtil.getAPILIAN().getBussinessContact("5b724156eabf267f2b8b456c").enqueue(new CallBackBase<ApiResponse<List<Profile>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<Profile>>> call, Response<ApiResponse<List<Profile>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
//                        listener.onSucessListContact(response.body().getData());
                        AppController.getInstance().getmSetting().put("contact_business",new Gson().toJson(response.body().getData()));
                        getListFriend();
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
    public void updateToken() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        LogUtils.e("update token firebase ",AppController.getInstance().getmSetting().getString(AppConstant.KEY_TOKEN_FCM));
        getmService().updateFCM(profile.getU_id(), AppController.getInstance().getmSetting().getString(AppConstant.KEY_TOKEN_FCM)).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {

            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }

    @Override
    public void acceptFriend(String id) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().acceptFriend(id,profile.getId()).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSucessAddFriend();
                    } else {
                        new ExceptionHandler<>(listener, response.body()).excute();
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

    @Override
    public void getListFriend() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().getContact(profile.getId()).enqueue(new CallBackBase<ApiResponse<List<Profile>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<Profile>>> call, Response<ApiResponse<List<Profile>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSucessListContact(response.body().getData());
                        AppController.getInstance().getmSetting().put("contact_friend",new Gson().toJson(response.body().getData()));
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
    public void toggleOnline(int type) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().toggleOnline(profile.getId(),  type).enqueue(new CallBackBase<ApiResponse>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.code() == AppConstant.CODE_API_SUCCESS && response.body().getData() != null) {
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse> call, Throwable t) {
            }
        });
    }


    @Override
    public void getListContact(ContentResolver cr) {
//        if(AppController.getInstance().getmSetting().getString("contact",null) != null ){
//            String json = AppController.getInstance().getmSetting().getString("contact",null);
//            Type listType = new TypeToken<List<Profile>>() {}.getType();
//            List<Profile> list = new Gson().fromJson(json,listType);
//            if(list!=null) {
//                syncContact(list);
//                return;
//            }
//        }
         new GetContactTask(new GetContactTask.CallBack() {
             @Override
             public void onCallBack(List<Profile> listProfile) {
                 syncContact(listProfile);
             }
         }).execute(cr);



    }

    private void syncContact(List<Profile> listProfile) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        String jsn = new Gson().toJson(listProfile);
        LogUtils.e(jsn);
        getmService().checkContact(profile.getId(),jsn).enqueue(new CallBackBase<ApiResponse<List<Profile>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<Profile>>> call, Response<ApiResponse<List<Profile>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
//                        listener.onSucessListContact(response.body().getData());
                        AppController.getInstance().getmSetting().put("contact",new Gson().toJson(response.body().getData()));
                        getListBussinessContact();

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
}
