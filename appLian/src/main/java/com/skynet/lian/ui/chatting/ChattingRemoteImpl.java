package com.skynet.lian.ui.chatting;


import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Message;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Room;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiService;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.network.api.ExceptionHandler;
import com.skynet.lian.network.socket.SocketClient;
import com.skynet.lian.ui.base.Interactor;
import com.skynet.lian.utils.AppConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class ChattingRemoteImpl extends Interactor implements ChattingContract.Interactor {
    ChattingContract.ChattingListener listener;

    public ChattingRemoteImpl(ChattingContract.ChattingListener listener) {
        this.listener = listener;
    }

    @Override
    public ApiService createService() {
        return ApiUtil.createNotTokenApi();
    }


    @Override
    public void getRoomInfo(String id, int index, final boolean isLoadmore) {
        //
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().getRoomInfo(profile.getId(), id, index).enqueue(new CallBackBase<ApiResponse<Room>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Room>> call, Response<ApiResponse<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.code() == AppConstant.CODE_API_SUCCESS && response.body().getData() != null) {
                        listener.onSuccessgetRoomInfo(response.body().getData(), isLoadmore);
                    } else {
                        listener.onError(response.body().getMessage());
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Room>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }

    @Override
    public void toggle(String id, int toggle) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        LogUtils.e("toggle to noti = "+ toggle);
        getmService().toggleMuteChat(profile.getId(), id, toggle).enqueue(new CallBackBase<ApiResponse>() {
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
    public void sendMessage(final String sendToId, String message, final SocketClient socketClient, final int positionMessage) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }
        getmService().chatSingle(profile.getId(), sendToId, message).enqueue(new CallBackBase<ApiResponse<Message>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.code() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSuccessSendMessage(sendToId, socketClient, response.body().getData(), positionMessage);
                    } else {
                        listener.onError(response.body().getMessage());
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Message>> call, Throwable t) {
                listener.onErrorApi(t.getMessage());
            }
        });
    }

    @Override
    public void getInfo(String id, int type) {

    }

    @Override
    public void uploadImages(List<File> listFile, final int idRoom, final int positionMessage, final SocketClient socketClient) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }

        RequestBody paramidUser = RequestBody.create(MediaType.parse("text/plain"), profile.getId());
        RequestBody paramType = RequestBody.create(MediaType.parse("text/plain"), 2 + "");
        RequestBody paramidRoom = RequestBody.create(MediaType.parse("text/plain"), idRoom + "");

        List<MultipartBody.Part> parts;
        Call<ApiResponse<List<Message>>> call;

        if (listFile != null) {
            parts = new ArrayList<>();
            for (File img : listFile) {
                RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), img);
                parts.add(MultipartBody.Part.createFormData("img[]", img.getName(), requestImageFile));
            }
            call = getmService().uploadImages(paramidUser, paramidRoom, paramType, parts);
        } else {
            return;
        }

        call.enqueue(new CallBackBase<ApiResponse<List<Message>>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<List<Message>>> call, Response<ApiResponse<List<Message>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSuccuessUploadImages(response.body().getData(), positionMessage, socketClient, idRoom + "");
                    } else {
                        new ExceptionHandler<List<Message>>(listener, response.body()).excute();
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<List<Message>>> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }
        });
    }

    @Override
    public void uploadFiles(File file, final int idRoom, final int positionMessage, final SocketClient socketClient) {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile == null) {
            listener.onErrorAuthorization();
            return;
        }

        RequestBody paramidUser = RequestBody.create(MediaType.parse("text/plain"), profile.getId());
        RequestBody paramType = RequestBody.create(MediaType.parse("text/plain"), 4 + "");
        RequestBody paramidRoom = RequestBody.create(MediaType.parse("text/plain"), idRoom + "");

        MultipartBody.Part parts;
        Call<ApiResponse<Message>> call;


        RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), file);
        try {
            parts = (MultipartBody.Part.createFormData("file", file.getName(), requestImageFile));
        }catch (Exception e){
            parts = (MultipartBody.Part.createFormData("file", "Tep tin" + file.getName().substring(file.getName().indexOf("."), file.getName().length()), requestImageFile));

            e.printStackTrace();
        }

        call = getmService().uploadFile(paramidUser, paramidRoom, paramType, parts);

        call.enqueue(new CallBackBase<ApiResponse<Message>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        listener.onSuccuessUploadFiles(response.body().getData(), positionMessage, socketClient, idRoom + "");
                    } else {
                        new ExceptionHandler<Message>(listener, response.body()).excute();
                    }
                } else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Message>> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }
        });
    }

//    @Override
//    public void sendMessage(final String from, final String to, final String idDriverBooking, String content, SocketClient socketClient, final String name) {
//
//        getmService().sendMessageTo(from, to, idDriverBooking, new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date()), content, AppConstant.TYPE_USER).enqueue(new CallBackBase<ApiResponse<Message>>() {
//            @Override
//            public void onRequestSuccess(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.code() == AppConstant.CODE_API_SUCCESS) {
//                        listener.onSuccessSendMessage(from,to,idDriverBooking,response.body().getData(),AppConstant.TYPE_USER,name);
//                    } else {
//                        listener.onError(response.body().getMessage());
//                    }
//                } else {
//                    listener.onError(response.message());
//                }
//            }
//
//            @Override
//            public void onRequestFailure(Call<ApiResponse<Message>> call, Throwable t) {
//                listener.onErrorApi(t.getMessage());
//            }
//        });
//    }

//
//    @Override
//    public void getInfo(String id, final int type) {
//        getmService().getProfile(id, type).enqueue(new CallBackBase<ApiResponse<Profile>>() {
//            @Override
//            public void onRequestSuccess(Call<ApiResponse<Profile>> call, Response<ApiResponse<Profile>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS && response.body().getData() != null) {
//                        listener.onSucessGetInfo(response.body().getData(),type);
//                    } else {
//                        LogUtils.e(response.body().getMessage());
//                    }
//                } else {
//                    LogUtils.e(response.message());
//                }
//            }
//
//            @Override
//            public void onRequestFailure(Call<ApiResponse<Profile>> call, Throwable t) {
//                LogUtils.e(t.getMessage());
//
//            }
//        });
//    }
}
