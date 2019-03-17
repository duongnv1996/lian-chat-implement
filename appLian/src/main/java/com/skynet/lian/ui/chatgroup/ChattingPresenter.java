package com.skynet.lian.ui.chatgroup;


import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Message;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Room;
import com.skynet.lian.network.socket.SocketClient;
import com.skynet.lian.ui.base.Presenter;
import com.skynet.lian.utils.AppConstant;

import java.io.File;
import java.util.List;


public class ChattingPresenter extends Presenter<ChattingContract.View> implements ChattingContract.Presenter {
    ChattingContract.Interactor interactor;

    public ChattingPresenter(ChattingContract.View view) {
        super(view);
        interactor = new ChattingRemoteImpl(this);

    }
//    SocketClient socketClient;


//
//    @Override
//    public void sendMessage(String from, String to, String idDriverBooking, String content, SocketClient socketClient, String name) {
//        view.showProgress();
//        this.socketClient = socketClient;
//        Profile profile = AppController.getInstance().getmProfileUser();
//        if (profile == null) {
//            onErrorAuthorization();
//            return;
//        }
//        interactor.sendMessage(from, to, idDriverBooking, content, socketClient, name);
//    }

    @Override
    public void getRoomInfo(String id, int index, boolean isLoadmore) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getRoomInfo(id, index, isLoadmore);
        }
    }

    @Override
    public void sendMessage(String sendToId, String message, SocketClient socketClient, int positionMessage,String listUserIds) {
        if (isAvaliableView()) {
            interactor.sendMessage(sendToId, message, socketClient, positionMessage,listUserIds);
        }
    }

    @Override
    public void getInfo(String id, int type) {
        if (view == null) return;
        view.showProgress();
        interactor.getInfo(id, type);
    }

    @Override
    public void uploadImages(List<File> listFile, int idRoom, int positionMessage,SocketClient socketClient) {
        if (view == null) return;
        view.showProgress();
        interactor.uploadImages(listFile, idRoom,positionMessage,socketClient);
    }

    @Override
    public void uploadFiles(File file, int idRoom, int positionMessage,SocketClient socketClient) {
        if (view == null) return;
        view.showProgress();
        interactor.uploadFiles(file, idRoom,positionMessage,socketClient);
    }

//    @Override
//    public void sendSocketJoin(String from, String to, String idDriverBooking, String idUserBooking, SocketClient socketClient, String name) {
//        socketClient.joinBooking(from, to, idDriverBooking, idUserBooking, name);
//    }

    @Override
    public void onDestroyView() {
        view = null;
    }


//    @Override
//    public void onSuccessSendMessage(String from, String to, String idBooking, Message message, int type, String name) {
//        if (view == null) return;
//        view.hiddenProgress();
//        if (socketClient != null) {
//            socketClient.sendMessage(from, to, idBooking, type, name, message.getContent());
//        }
//        view.onSuccessSendMessage(message);
//    }


    @Override
    public void onSuccessgetRoomInfo(Room room, boolean isLoadmore) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if (!isLoadmore)
                view.onSuccessgetRoomInfo(room.getListMessage(), room.getUser1(), room.getUser2(), room.getIndex(),room);
            else
                view.onSuccessgetLoadmoreChat(room.getListMessage(), room.getIndex());

        }
    }

    @Override
    public void onSuccessSendMessage(String sendToId, SocketClient socketClient, Message messageObject, int positionMessage,String listUserIds) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if(socketClient != null){
                socketClient.sendMessageToGroup(messageObject.getContent(),AppController.getInstance().getmProfileUser().getId()
                        ,sendToId,messageObject.getChat_id(),AppConstant.GROUP,listUserIds);
            }
            view.onSuccessSendMessage(messageObject, positionMessage);
        }

//        view.hiddenProgress();
//        if (socketClient != null) {
//            socketClient.sendMessage(from, to, idBooking, type, name, message.getContent());
//        }
//        view.onSuccessSendMessage(message);
    }

    @Override
    public void onSucessGetInfo(Profile profile, int type) {
        if (view == null) return;
        view.hiddenProgress();
        view.onSucessGetInfo(profile, type);
    }

    @Override
    public void onSuccuessUploadImages(List<Message> message, int positionMessage,SocketClient socketClient,String idRoom) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if(socketClient != null){
                socketClient.sendMessage("Hình ảnh",AppController.getInstance().getmProfileUser().getId(),"",idRoom,AppConstant.GROUP);
            }
            view.onSuccuessUploadImages(message, positionMessage);
        }
    }

    @Override
    public void onSuccuessUploadFiles(Message url, int positionMessage,SocketClient socketClient,String idRoom) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if(socketClient != null){
                socketClient.sendMessage("Tệp tin",AppController.getInstance().getmProfileUser().getId(),"",idRoom,AppConstant.GROUP);
            }
            view.onSuccuessUploadFiles(url, positionMessage);
        }
    }

//    public SocketClient getSocketClient() {
//        return socketClient;
//    }
//
//    public void setSocketClient(SocketClient socketClient) {
//        this.socketClient = socketClient;
//    }

    @Override
    public void onErrorApi(String message) {
        if (view == null) return;
        view.hiddenProgress();
        view.onErrorApi(message);
    }

    @Override
    public void onError(String message) {
        if (view == null) return;
        view.hiddenProgress();
        view.onError(message);
    }

    @Override
    public void onErrorAuthorization() {
        if (view == null) return;
        view.hiddenProgress();
        view.onErrorAuthorization();
    }
}
