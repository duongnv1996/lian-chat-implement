package com.skynet.lian.ui.contact;

import android.content.ContentResolver;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.network.api.ExceptionHandler;
import com.skynet.lian.network.socket.SocketClient;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import java.lang.reflect.Type;
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
        if(AppController.getInstance().getmSetting().getString("contact_business",null) != null ){
            String json = AppController.getInstance().getmSetting().getString("contact_business",null);
            Type listType = new TypeToken<List<Profile>>() {}.getType();
            List<Profile> list = new Gson().fromJson(json,listType);
            if(list!=null) {
                listener.onSucessListContact(list);
                return;
            }
        }
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
                        listener.onSucessListContact(response.body().getData());
                        AppController.getInstance().getmSetting().put("contact_business",new Gson().toJson(response.body().getData()));

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
    public void createChatGroup(List<Profile> listUser, String name, final SocketClient socketClient) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        String id="";
        for (Profile prfile :listUser) {
            if(prfile.isChecked()){
                id+=prfile.getId()+",";
            }
        }
        if(!id.isEmpty()){
            id=id.substring(0,id.length()-1) + ","+profile.getId();
        }else{
            listener.onError("Vui lòng chọn thành viên nhóm");
            return;
        }
        final String finalId = id;
        getmService().createGroup(id,name,profile.getId()).enqueue(new CallBackBase<ApiResponse<ChatItem>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<ChatItem>> call, Response<ApiResponse<ChatItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.code() == AppConstant.CODE_API_SUCCESS && response.body().getData() != null) {
                        listener.onSucessCreateChatGroup(response.body().getData());
                        LogUtils.e("let user join room "+ response.body().getData().getId() +" : "+ finalId);
                        socketClient.sendMessageToGroup("",AppController.getInstance().getmProfileUser().getId()
                                ,"",response.body().getData().getId(),AppConstant.GROUP, finalId);
                    } else {
                        listener.onError(response.body().getMessage());
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<ChatItem>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }

    @Override
    public void getListFriend() {
        if(AppController.getInstance().getmSetting().getString("contact_friend",null) != null ){
            String json = AppController.getInstance().getmSetting().getString("contact_friend",null);
            Type listType = new TypeToken<List<Profile>>() {}.getType();
            List<Profile> list = new Gson().fromJson(json,listType);
            if(list!=null && !list.isEmpty()) {
                listener.onSucessListContact(list);
                return;
            }
        }
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
    public void getListContact(ContentResolver cr) {
        if(AppController.getInstance().getmSetting().getString("contact",null) != null ){
            String json = AppController.getInstance().getmSetting().getString("contact",null);
            Type listType = new TypeToken<List<Profile>>() {}.getType();
            List<Profile> list = new Gson().fromJson(json,listType);
            if(list!=null) {
                listener.onSucessListContact(list);
                return;
            }
        }
         new GetContactTask(new GetContactTask.CallBack() {
             @Override
             public void onCallBack(List<Profile> listProfile) {
                 Profile profile = AppController.getInstance().getmProfileUser();
                 if (profile == null) {
                     listener.onErrorAuthorization();
                     return;
                 }
                 getmService().checkContact(profile.getId(),new Gson().toJson(listProfile)).enqueue(new CallBackBase<ApiResponse<List<Profile>>>() {
                     @Override
                     public void onRequestSuccess(Call<ApiResponse<List<Profile>>> call, Response<ApiResponse<List<Profile>>> response) {
                         if (response.isSuccessful() && response.body() != null) {
                             if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                                 listener.onSucessListContact(response.body().getData());
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
         }).execute(cr);



    }
}
