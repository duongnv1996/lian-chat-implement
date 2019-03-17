package com.skynet.lian.ui.whitelist;

import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.Presenter;

import java.util.List;

public class WhiteListPresenter extends Presenter<WhiteListContract.View> implements WhiteListContract.Presenter {
    WhiteListContract.Interactor interactor;
    public WhiteListPresenter(WhiteListContract.View view) {
        super(view);
        interactor =new WhiteListImplRemote(this);
    }

    @Override
    public void onSucessGetList(List<Profile> list) {
        if(isAvaliableView()){
            view.hiddenProgress();
            view.onSucessGetList(list);
        }
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
    public void getList() {
        if(isAvaliableView()){
            view.showProgress();
            interactor.getList();
        }
    }

    @Override
    public void deleteFromWhitelist(String id,int type) {
        if(isAvaliableView()){
//            view.showProgress();
            interactor.deleteFromWhitelist(id,type);
        }
    }

    @Override
    public void onDestroyView() {
        view = null;
    }
}
