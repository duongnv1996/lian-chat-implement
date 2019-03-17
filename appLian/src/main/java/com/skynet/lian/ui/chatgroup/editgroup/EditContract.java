package com.skynet.lian.ui.chatgroup.editgroup;

import com.skynet.lian.models.Room;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

public interface EditContract {
    interface View extends BaseView {
        void onSucessEdtChatGroup();
        void onSucessDeleteChat();
        void onSucessLeaveGroupChat();
        void onSucessAddUserToGroup(Room room);

    }

    interface Presenter extends IBasePresenter,Listener{
        void edtChatGroup(String name, String idGroup);
        void deleteChat(String id);
        void leaveGroupChat(String id);
        void addUserToGroup(String idGroup, String idUser);
    }

    interface Interactor {
        void edtChatGroup(String name, String idGroup);
        void deleteChat(String id);
        void leaveGroupChat(String id);
        void addUserToGroup(String idGroup, String idUser);


    }

    interface Listener extends OnFinishListener {
        void onSucessEdtChatGroup();
        void onSucessDeleteChat();
        void onSucessLeaveGroupChat();
        void onSucessAddUserToGroup(Room room);
    }
}
