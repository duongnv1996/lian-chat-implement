package com.skynet.lian.ui.searchfriend;

import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface SearchContactContract {
    interface View extends BaseView {
        void onSucessAddFriend();


        void onSucessListContact(List<Profile> list);
    }

    interface Presenter extends IBasePresenter,Listener{
        void getListContact(String cr);
        void addFriend(String idFriend);
    }

    interface Interactor {
        void getListContact(String cr);
        void addFriend(String idFriend);

    }

    interface Listener extends OnFinishListener {
        void onSucessAddFriend();
        void onSucessListContact(List<Profile> list);
    }
}
