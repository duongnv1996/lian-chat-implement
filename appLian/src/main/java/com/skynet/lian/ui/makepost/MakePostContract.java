package com.skynet.lian.ui.makepost;

import com.skynet.lian.models.Image;
import com.skynet.lian.models.MyPlace;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface MakePostContract {
    interface View extends BaseView {
        void onSuccessSubmit(int idPost);
        void onSucessGetPlaces(List<MyPlace> myPlaces);
    }

    interface Presenter extends IBasePresenter ,Listener{
        void submitPost(String content, int type, String address, List<Image> listImage);
        void getPlaceNearby(float lat, float lng);
    }

    interface Interactor {
        void submitPost(String content, int type, String address, List<Image> listImage);
        void getPlaceNearby(float lat, float lng);
        void getPlace(float lat, float lng);

    }

    interface Listener extends OnFinishListener {
        void onSuccessSubmit(int idPost);
        void onSucessGetPlaces(List<MyPlace> myPlaces);
    }
}
