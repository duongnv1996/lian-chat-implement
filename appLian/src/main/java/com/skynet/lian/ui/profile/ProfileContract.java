package com.skynet.lian.ui.profile;

import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

public interface ProfileContract {
    interface View extends BaseView {
        void onSucessGetTimeline(Timeline timeline);
        void onSucessShare(int pos);

    }
    interface Presenter extends IBasePresenter,Listener {
        void getTimeline(int index, int type, String id);
        void toggleLike(int id, int toogle);
        void shareContent(int idPost, String content, int type);

    }
    interface Interactor {
        void getTimeline(int index, int type, String id);
        void toggleLike(int id, int toogle);
        void shareContent(int idPost, String content, int type);

    }
    interface Listener extends OnFinishListener {
        void onSucessGetTimeLine(Timeline timeline);
        void onSucessShare(int pos);

    }
}
