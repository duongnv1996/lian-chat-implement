package com.skynet.lian.ui.profile;

import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.io.File;

import okhttp3.MultipartBody;

public interface UploadContract {

    interface Presenter extends IBasePresenter,Listener {
        void upload(File file, int type);
    }

    interface Interactor {
        void upload(File file, MultipartBody.Part part, int type);
    }

    interface View extends BaseView{
        void onSucessUploadAvat();
    }
    interface Listener extends OnFinishListener {
        void onSucessUploadAvat();

    }

}
