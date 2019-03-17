package com.skynet.lian.ui.profileFriend;

import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.Presenter;

public class ProfileFriendPresenter extends Presenter<ProfileFriendContract.View> implements ProfileFriendContract.Presenter {
    ProfileFriendContract.Interactor interactor;

    public ProfileFriendPresenter(ProfileFriendContract.View view) {
        super(view);
        interactor = new ProfileFriendImplRemote(this);
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
    public void onErrorAuthorization() {
        if (isAvaliableView()) {
            view.hiddenProgress();
            view.onErrorAuthorization();
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

    @Override
    public void onSucessGetInfo(Profile profile) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if (profile != null)
                view.onSucessGetInfo(profile);
        }
    }

    @Override
    public void getTimeline(String id, int index, int type) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getTimeline(id,index,type);
        }
    }

    @Override
    public void getInfo(String id) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getInfo(id);
        }
    }

    @Override
    public void toggleFollow(String id, int toggle) {
        if (isAvaliableView()) {
            interactor.toggleFollow(id,toggle);
        }
    }

    @Override
    public void addFriend(String idFriend,int type) {
        if (isAvaliableView()) {
            interactor.addFriend(idFriend,type);
        }
    }
}
