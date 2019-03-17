package com.skynet.lian.ui.tabgroup;

import com.skynet.lian.models.ChatItem;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface TabGoupContract {
    interface View extends BaseView {


        void onSucessListChat(List<ChatItem> list);
    }

    interface Presenter extends IBasePresenter,Listener{
        void getListChat(String query);
        void deleteChat(String id);

        void getListChat();
    }

    interface Interactor {
        void getListChat(String query);
        void deleteChat(String id);

        void getListChat();
    }

    interface Listener extends OnFinishListener {

        void onSucessListChat(List<ChatItem> list);
    }
}
