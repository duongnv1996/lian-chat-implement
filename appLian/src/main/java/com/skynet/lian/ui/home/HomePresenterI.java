package com.skynet.lian.ui.home;


import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.utils.AppConstant;

public class HomePresenterI implements HomeContract.PresenterI {
    HomeContract.View view;
    HomeContract.Interactor interactor;

    public HomePresenterI(HomeContract.View view) {
        this.view = view;
        interactor = new HomeRemoteImpl(this);
    }

    @Override
    public void getInfor() {

        String profileStr = AppController.getInstance().getmSetting().getString(AppConstant.KEY_PROFILE, "");
        if (profileStr.isEmpty()) {
            view.onError("not found");
        } else {
            view.showProgress();
            interactor.doGetInfor(profileStr);
        }

    }

    @Override
    public void onSuccessGetInfor(Profile profile) {
        if(view ==null ) return;
        view.hiddenProgress();
        AppController.getInstance().setmProfileUser(profile);
        view.onSuccessGetInfor();
    }



    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onErrorApi(String message) {
        if(view ==null ) return;
        view.hiddenProgress();
        view.onError("not found");

    }

    @Override
    public void onError(String message) {
        if(view ==null ) return;
        view.hiddenProgress();
        view.onError("not found");

    }

    @Override
    public void onErrorAuthorization() {
        if(view ==null ) return;
        view.hiddenProgress();
        view.onError("not found");

    }


}
