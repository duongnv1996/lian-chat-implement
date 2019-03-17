package com.skynet.lian.ui.profile;

import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.Presenter;

public class ProfilePresenter extends Presenter<ProfileContract.View> implements ProfileContract.Presenter {
    ProfileContract.Interactor interactor;

    public ProfilePresenter(ProfileContract.View view) {
        super(view);
        interactor = new ProfileImplRemote(this);
    }


    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onErrorApi(String message) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onErrorApi(message);
        }
    }

    @Override
    public void onError(String message) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onError(message);
        }
    }

    @Override
    public void onSucessShare(int pos) {
        if (view == null) return;
        view.hiddenProgress();
        view.onSucessShare(pos);
    }


    @Override
    public void shareContent(int idPost, String content, int type) {
        if (view == null) return;
        view.showProgress();
        interactor.shareContent(idPost, content, type);
    }
    @Override
    public void onErrorAuthorization() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onErrorAuthorization();
        }
    }

    @Override
    public void getTimeline(int index, int type,String id) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getTimeline(index,type,id);
        }
    }

    @Override
    public void toggleLike(int id, int toogle) {
        if (isAvaliableView()) {
            interactor.toggleLike(id,toogle);
        }
    }

    @Override
    public void onSucessGetTimeLine(Timeline timeline) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if (timeline != null)
                view.onSucessGetTimeline(timeline);
        }
    }
}
