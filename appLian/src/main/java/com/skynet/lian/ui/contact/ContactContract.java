package com.skynet.lian.ui.contact;

import android.content.ContentResolver;

import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.socket.SocketClient;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface ContactContract {
    interface View extends BaseView {
        void onSucessCreateChatGroup(ChatItem chatItem);
        void onSucessListContact(List<Profile> list);
    }

    interface Presenter extends IBasePresenter ,Listener{
        void getListBussinessContact();
        void getListFriend();
        void getListContact(ContentResolver cr);
        void createChatGroup(List<Profile> listUser, String name, SocketClient socketClient);
    }

    interface Interactor {
        void getListBussinessContact();
        void createChatGroup(List<Profile> listUser, String name, SocketClient socketClient);
        void getListFriend();

        void getListContact(ContentResolver cr);
    }

    interface Listener extends OnFinishListener {
        void onSucessCreateChatGroup(ChatItem chatItem);
        void onSucessListContact(List<Profile> list);
    }
}
