package com.skynet.lian.ui.tabexplore;

import com.skynet.lian.models.Post;
import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface TimeLineContract {
    interface View extends BaseView{
        void onSucessGetListPost(List<Post> list, int index);
    }
    interface Presenter extends IBasePresenter,Listener{
        void getTimeLine(int index);
    }
    interface Interactor{
        void getTimeLine(int index);
    }
    interface Listener extends OnFinishListener{
        void onSucessGetTimeline(Timeline timeline);
    }
}
