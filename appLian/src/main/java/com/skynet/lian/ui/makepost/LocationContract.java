package com.skynet.lian.ui.makepost;

import com.google.android.gms.maps.model.LatLng;
import com.skynet.lian.models.MyPlace;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

public interface LocationContract {
    interface View extends BaseView {
        void onSuccessGetMyAddress(MyPlace response);
    }

    interface Presenter extends IBasePresenter, Listener {
        void getMyAddress(LatLng latLng);
    }

    interface Interactor {
        void getMyAddress(LatLng latLng);

    }

    interface Listener extends OnFinishListener {
        void onSuccessGetMyAddress(MyPlace response);
    }
}
