package com.skynet.lian.ui.makepost;

import com.skynet.lian.models.Image;
import com.skynet.lian.models.MyPlace;
import com.skynet.lian.ui.base.Presenter;

import java.util.List;

public class MakePostPresenter extends Presenter<MakePostContract.View> implements MakePostContract.Presenter {
    MakePostContract.Interactor interactor;

    public MakePostPresenter(MakePostContract.View view) {
        super(view);
        interactor = new MakePostImplRemote(this);
    }

    @Override
    public void submitPost(String content, int type, String address, List<Image> listImage) {
        if(isAvaliableView()){
            view.showProgress();
            interactor.submitPost(content,type,address,listImage);
        }
    }

    @Override
    public void getPlaceNearby(float lat,float lng) {
        if(isAvaliableView()){
            interactor.getPlaceNearby(lat,lng);
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
    public void onSuccessSubmit(int idPost) {
        if(isAvaliableView()){
            view.hiddenProgress();
            view.onSuccessSubmit(idPost);
        }
    }

    @Override
    public void onSucessGetPlaces(List<MyPlace> myPlaces) {
        if(isAvaliableView()){
            view.hiddenProgress();
            view.onSucessGetPlaces(myPlaces);
        }
    }
}
