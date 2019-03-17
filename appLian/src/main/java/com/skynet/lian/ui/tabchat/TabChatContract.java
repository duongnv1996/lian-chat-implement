package com.skynet.lian.ui.tabchat;

import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface TabChatContract {
    interface View extends BaseView {
        void onSucessListFriendOnline(List<Profile> list);

        void onSucessListChat(List<ChatItem> list);
    }

    interface Presenter extends IBasePresenter,Listener{
        void getListFriendOnline();
        void getListChat();
        void getListChat(String query);
        void deleteChat(String id);
    }

    interface Interactor {
        void getListFriendOnline();
        void getListChat();
        void deleteChat(String id);
        void getListChat(String query);

    }

    interface Listener extends OnFinishListener {
        void onSucessListFriendOnline(List<Profile> list);

        void onSucessListChat(List<ChatItem> list);
    }
}
