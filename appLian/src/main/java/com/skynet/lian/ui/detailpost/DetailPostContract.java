package com.skynet.lian.ui.detailpost;


import com.skynet.lian.models.Comment;
import com.skynet.lian.models.Post;
import com.skynet.lian.ui.base.BaseView;
import com.skynet.lian.ui.base.IBasePresenter;
import com.skynet.lian.ui.base.OnFinishListener;

public interface DetailPostContract {
    interface View extends BaseView {
        void onSuccessGetDetail(Post notification);

        void onSucessShare(int pos);
        void onSucessEditPost();
        void onSucessComment(Comment comment, int pos);
    }

    interface Presenter extends IBasePresenter, Listener {
        void getDetail(int id);

        void toggleLike(int id, int toogle);

        void toggleComment(int id, int toogle);

        void deletePost(int id);
        void deleteComment(int id);

        void comment(int idPost, String conent, int pos);

        void editPost(int idPost, String content, int type);
        void editComment(int idPost, String content);

        void shareContent(int idPost, String content, int type);
    }

    interface Interactor {
        void doGetDetail(int id);

        void toggleLike(int id, int toogle);

        void toggleComment(int id, int toogle);
        void editComment(int idPost, String content);
        void deleteComment(int id);

        void deletePost(int id);

        void shareContent(int idPost, String content, int type);

        void editPost(int idPost, String content, int type);

        void comment(int idPost, String conent, int pos);
    }

    interface Listener extends OnFinishListener {
        void onSuccessGetDetail(Post notification);

        void onSucessShare(int pos);

        void onSucessEditPost();

        void onSucessComment(Comment comment, int pos);
    }
}
