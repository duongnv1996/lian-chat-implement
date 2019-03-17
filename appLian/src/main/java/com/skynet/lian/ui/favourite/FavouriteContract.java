package com.skynet.lian.ui.favourite;

import com.skynet.lian.models.Excercise;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

import java.util.List;

public interface FavouriteContract {
    interface View extends BaseView {
        void onSucessGetList(List<Excercise> list);
    }
    interface Presenter extends IBasePresenter, Listener {
        void getList();
        void toggleFav(int idPost, boolean isFav);
    }
    interface Interactor {
        void getList();
        void toggleFav(int idPost, int isFav);
    }
    interface Listener extends OnFinishListener {
        void onSucessGetList(List<Excercise> list);
    }
}
