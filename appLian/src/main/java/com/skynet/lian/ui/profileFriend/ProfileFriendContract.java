package com.skynet.lian.ui.profileFriend;

import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

public interface ProfileFriendContract {
    interface View extends BaseView{
        void onSucessGetTimeline(Timeline timeline);
        void onSucessGetInfo(Profile profile);

    }
    interface Presenter extends IBasePresenter,Listener {
        void getTimeline(String id, int index, int type);
        void getInfo(String id);
        void toggleFollow(String id, int toggle);
        void addFriend(String idFriend, int type);

    }
    interface Interactor {
        void getTimeline(String id, int index, int type);
        void getInfo(String id);
        void toggleFollow(String id, int toggle);
        void addFriend(String idFriend, int type);
    }
    interface Listener extends OnFinishListener{
        void onSucessGetTimeLine(Timeline timeline);
        void onSucessGetInfo(Profile profile);
    }
}
