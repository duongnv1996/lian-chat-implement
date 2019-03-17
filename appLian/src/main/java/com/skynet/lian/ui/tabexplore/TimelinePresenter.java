package com.skynet.lian.ui.tabexplore;

import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.Presenter;

public class TimelinePresenter extends Presenter<TimeLineContract.View> implements TimeLineContract.Presenter {
    TimeLineContract.Interactor interactor;

    public TimelinePresenter(TimeLineContract.View view) {
        super(view);
        interactor = new TimelineImplRemote(this);
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
    public void getTimeLine(int index) {
        if (isAvaliableView()) {
            view.showProgress();
            interactor.getTimeLine(index);
        }
    }


    @Override
    public void onSucessGetTimeline(Timeline timeline) {
        if (isAvaliableView()) {
            view.hiddenProgress();
            if (timeline != null)
                view.onSucessGetListPost(timeline.getListPost(),timeline.getIndex());
        }
    }
}
