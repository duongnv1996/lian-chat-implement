package com.skynet.lian.ui.profile;

import android.net.Uri;

import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.ui.base.Presenter;

import java.io.File;


public class UploadPresenter extends Presenter<UploadContract.View> implements UploadContract.Presenter {
    UploadContract.Interactor interactor;

    public UploadPresenter(UploadContract.View view) {
        super(view);
        interactor = new UploadInteractor(this);
    }

    @Override
    public void upload(File file, int type) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.upload(file, ApiUtil.prepareFilePart("img", Uri.fromFile(file)), type);
        }
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onErrorApi(String message) {
        if(isAvaliableView()){
            view.hiddenProgress();
            view.onErrorApi(message);
        }
    }

    @Override
    public void onError(String message) {
        if(isAvaliableView()){
            view.hiddenProgress();
            view.onError(message);
        }
    }

    @Override
    public void onErrorAuthorization() {
        if(isAvaliableView()){
            view.hiddenProgress();
            view.onErrorAuthorization();
        }
    }

    @Override
    public void onSucessUploadAvat() {
        if(isAvaliableView()){
            view.hiddenProgress();
            view.onSucessUploadAvat();
        }
    }
}
