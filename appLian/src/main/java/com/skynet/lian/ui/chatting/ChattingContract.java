package com.skynet.lian.ui.chatting;


import com.skynet.lian.models.Message;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Room;
import com.skynet.lian.network.socket.SocketClient;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.io.File;
import java.util.List;

public interface ChattingContract {
    interface View extends BaseView {
        void onSuccessgetRoomInfo(List<Message> list, Profile user1, Profile user2, int indexList, Room room);
        void onSuccessgetLoadmoreChat(List<Message> list, int indexList);

        void onSuccessSendMessage(Message mess, int positionMessage);
        void onSuccuessUploadImages(List<Message> mess, int positionMessage);
        void onSuccuessUploadFiles(Message mess, int positionMessage);
        void onSucessGetInfo(Profile profile, int type);
    }

    interface Presenter extends IBasePresenter, ChattingListener {
        void getRoomInfo(String id, int indexList, boolean isLoadmore);

        void sendMessage(String sendToId, String message, SocketClient socketClient, int positionMessage);

        void getInfo(String id, int type);
        void toggle(String id, boolean toggle);
        void uploadImages(List<File> listFile, int idRoom, int positionMessage, SocketClient socketClient);
        void uploadFiles(File file, int idRoom, int positionMessage, SocketClient socketClient);

    }
    interface Interactor {
        void getRoomInfo(String id, int indexList, boolean isLoadmore);
        void toggle(String id, int toggle);
        void sendMessage(String sendToId, String message, SocketClient socketClient, int positionMessage);

        //        void sendMessage(String from, String to, String idDriverBooking, String content, SocketClient socketClient, String name);
        void getInfo(String id, int type);
        void uploadImages(List<File> listFile, int idRoom, int positionMessage, SocketClient socketClient);
        void uploadFiles(File file, int idRoom, int positionMessage, SocketClient socketClient);
    }

    interface ChattingListener extends OnFinishListener {
        void onSuccessgetRoomInfo(Room room, boolean isLoadmore);

        void onSuccessSendMessage(String sendToId, SocketClient socketClient, Message messageObject, int positionMessage);

        void onSucessGetInfo(Profile profile, int type);
        void onSuccuessUploadImages(List<Message> message, int positionMessage, SocketClient socketClient, String idRoom);
        void onSuccuessUploadFiles(Message url, int positionMessage, SocketClient socketClient, String idRoom);
    }
}
