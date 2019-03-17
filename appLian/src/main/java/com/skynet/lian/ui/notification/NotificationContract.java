package com.skynet.lian.ui.notification;


import com.skynet.lian.models.Notification;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface NotificationContract {
    interface View extends BaseView {
        void onSuccessGetServices(List<Notification> listGroupServices);
    }

    interface Presenter extends IBasePresenter, OnHomeRequestFinish {
        void getAllService(String idShop);
    }

    interface Interactor {
        void doGetAllService(String idShop);
    }

    interface OnHomeRequestFinish extends OnFinishListener {
        void onSuccessGetServices(List<Notification> listGroupServices);
    }
}
