package com.skynet.lian.ui.main;

import android.content.ContentResolver;

import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface ContactContract {
    interface View extends BaseView {
        void onSucessAddFriend();

        void onSucessListContact(List<Profile> list);
    }

    interface Presenter extends IBasePresenter ,Listener{
        void getListBussinessContact();
        void getListContact(ContentResolver cr);
        void updateToken();
        void acceptFriend(String id);
        void getListFriend();

    }

    interface Interactor {
        void getListBussinessContact();
        void updateToken();
        void acceptFriend(String id);
        void getListFriend();

        void getListContact(ContentResolver cr);
    }

    interface Listener extends OnFinishListener {
        void onSucessAddFriend();

        void onSucessListContact(List<Profile> list);
    }
}
