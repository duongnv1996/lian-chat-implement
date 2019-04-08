package com.skynet.lian.ui.detailpost;


import com.skynet.lian.models.Comment;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.Post;

import java.util.ArrayList;
import java.util.List;

public class DetailPostPresenter implements DetailPostContract.Presenter {
    DetailPostContract.View view;
    DetailPostContract.Interactor interactor;
    private int currentOffset = 0;

    public DetailPostPresenter(DetailPostContract.View view) {
        this.view = view;
        interactor = new DetailPostRemoteImpl(this);
    }

    @Override
    public void getDetail(int id) {
        view.showProgress();
        interactor.doGetDetail(id);
    }

    @Override
    public void onSuccessGetDetail(Post Post) {
        if (view == null) return;
        view.hiddenProgress();
        if (Post.getList_image() != null) {
            prepareMovieData(Post.getList_image());
        }
        view.onSuccessGetDetail(Post);
    }

    @Override
    public void onSucessShare(int pos) {
        if (view == null) return;
        view.hiddenProgress();
        view.onSucessShare(pos);
    }

    @Override
    public void onSucessEditPost() {
        if (view == null) return;
        view.hiddenProgress();
        view.onSucessEditPost();
    }

    @Override
    public void onSucessComment(Comment comment, int pos) {
        if (view == null) return;
        view.onSucessComment(comment, pos);
    }

    private List<Image> prepareMovieData(List<Image> list) {

        for (int i = 0; i < list.size(); i++) {
            ArrayList<Image> mPathitems = new ArrayList<>();
            boolean isCol2Avail = false;

            Image i1 = list.get(i);
            int colSpan1 = Math.random() < 0.2f ? 2 : 1;
            int rowSpan1 = colSpan1;
            if (colSpan1 == 2 && !isCol2Avail)
                isCol2Avail = true;
            else if (colSpan1 == 2 && isCol2Avail)
                colSpan1 = 1;

            i1.setColumnSpan(colSpan1);
            i1.setRowSpan(rowSpan1);
            i1.setPosition(currentOffset + i);
            currentOffset += mPathitems.size();
        }


        return list;
    }

    @Override
    public void toggleLike(int id, int toogle) {
        if (view != null) {
            interactor.toggleLike(id, toogle);
        }
    }

    @Override
    public void toggleComment(int id, int toogle) {
        if (view != null) {
            interactor.toggleComment(id, toogle);
        }
    }

    @Override
    public void deletePost(int id) {
        if (view != null) {
            interactor.deletePost(id);
        }
    }

    @Override
    public void deleteComment(int id) {
        interactor.deleteComment(id);
    }

    @Override
    public void comment(int idPost, String conent, int pos) {
        if (view == null) return;
        interactor.comment(idPost, conent, pos);
    }

    @Override
    public void editPost(int idPost, String content, int type) {
        if (view == null) return;
        view.showProgress();
        interactor.editPost(idPost, content, type);
    }

    @Override
    public void editComment(int idPost, String content) {
        interactor.editComment(idPost,content);

    }

    @Override
    public void shareContent(int idPost, String content, int type) {
        if (view == null) return;
        view.showProgress();
        interactor.shareContent(idPost, content, type);
    }

    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onErrorApi(String message) {
        if (view == null) return;
        view.hiddenProgress();
        view.onErrorApi(message);
    }

    @Override
    public void onError(String message) {
        if (view == null) return;
        view.hiddenProgress();
        view.onErrorApi(message);
    }

    @Override
    public void onErrorAuthorization() {
        if (view == null) return;
        view.hiddenProgress();
        view.onErrorAuthorization();
    }
}
