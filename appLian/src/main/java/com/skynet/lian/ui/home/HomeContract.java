package com.skynet.lian.ui.home;


import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

public interface HomeContract {
    interface View extends BaseView {
        void onSuccessGetInfor();
    }

    interface PresenterI extends IBasePresenter ,Listener{
        void getInfor();
    }

    interface Interactor {
        void doGetInfor(String profileInfor);
    }

    interface Listener extends OnFinishListener {
        void onSuccessGetInfor(Profile profile);
    }
}
