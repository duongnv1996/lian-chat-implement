package com.skynet.lian.ui.chatgroup.editgroup.choosecontact;

import android.content.ContentResolver;

import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface ContactContract {
    interface View extends BaseView {
        void onSucessCreateChatGroup(ChatItem chatItem);
        void onSucessListContact(List<Profile> list);
    }

    interface Presenter extends IBasePresenter,Listener{
        void getListBussinessContact();
        void getListContact(ContentResolver cr);
        void createChatGroup(List<Profile> listUser, String name);
    }

    interface Interactor {
        void getListBussinessContact();
        void createChatGroup(List<Profile> listUser, String name);

        void getListContact(ContentResolver cr);
    }

    interface Listener extends OnFinishListener {
        void onSucessCreateChatGroup(ChatItem chatItem);
        void onSucessListContact(List<Profile> list);
    }
}
