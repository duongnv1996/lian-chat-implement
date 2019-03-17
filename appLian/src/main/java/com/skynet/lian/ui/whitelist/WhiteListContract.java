package com.skynet.lian.ui.whitelist;

import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface WhiteListContract {
    interface View extends BaseView {
        void onSucessGetList(List<Profile> list);

    }

    interface Presenter extends IBasePresenter,Listener {
        void getList();
        void deleteFromWhitelist(String id, int type);
    }

    interface Interactor {
        void getList();
        void deleteFromWhitelist(String id, int type);
    }

    interface Listener extends OnFinishListener {
        void onSucessGetList(List<Profile> list);
    }
}
