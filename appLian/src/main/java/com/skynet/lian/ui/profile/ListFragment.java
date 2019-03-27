package com.skynet.lian.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.SnackBarCallBack;
import com.skynet.lian.models.Post;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.detailpost.DetailPostActivity;
import com.skynet.lian.ui.detailpost.EditShareBottomSheet;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends BaseFragment implements ProfileContract.View, AdapterList.CallBackPost, XRecyclerView.LoadingListener, EditShareBottomSheet.MoreOptionCallback {
    @BindView(R2.id.rcv)
    XRecyclerView rcv;
    private AdapterList adapter;
    private static int TYPE_LOADMORE = 1;
    private static int TYPE_REFREESH = 0;
    private int requestType;
    private int index = 0;
    private List<Post> list;
    private ProfileContract.Presenter presenter;
    boolean isShowDialog = false;

    private int posEdit;
    private Post postEditing;
    private EditShareBottomSheet dialogShare;
    private ProgressDialogCustom dialogLoading;

    public static ListFragment newInstance(String id, Profile profile) {
        Bundle args = new Bundle();
        args.putString(AppConstant.MSG,id);
        args.putParcelable("profile",profile);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void doAction() {
        onRefresh();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_timeline_grid;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        dialogLoading = new ProgressDialogCustom(getContext());
//        rcv.setLoadingMoreEnabled(true);
//        rcv.setHasFixedSize(false);
        ;
    }

    @Override
    protected void initVariables() {
        dialogShare = new EditShareBottomSheet(getContext(), this);

        presenter = new ProfilePresenter(this);
        list = new ArrayList<>();
        adapter = new AdapterList(list, getContext(), this);
        adapter.setProfile(getArguments().getParcelable("profile"));
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setHasFixedSize(false);
        presenter.getTimeline(index, AppConstant.TYPE_POST,getArguments().getString(AppConstant.MSG));
        rcv.setAdapter(adapter);
        rcv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rcv.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rcv.setLimitNumberToCallLoadMore(10);
        rcv.setLoadingMoreEnabled(true);
        rcv.setLoadingListener(this);

    }

    @Override
    public void onSucessGetTimeline(Timeline timeline) {
        if (timeline != null) {
            if (requestType == TYPE_REFREESH) {
                this.list.clear();
            }
            if (!timeline.getListPost().isEmpty()) {
                this.list.addAll(timeline.getListPost());
                adapter.refreshCache();
                adapter.notifyDataSetChanged();
            } else {
                rcv.setNoMore(true);
            }
        }
        this.index = timeline.getIndex();
        rcv.loadMoreComplete();
        rcv.refreshComplete();
    }

    @Override
    public Context getMyContext() {
        return getContext();
    }

    @Override
    public void showProgress() {
        if (!isShowDialog) {
            dialogLoading.showDialog();
            isShowDialog = false;
        }
    }

    @Override
    public void hiddenProgress() {
        dialogLoading.hideDialog();
        isShowDialog = true;
    }

    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);
        showToast(message, AppConstant.NEGATIVE);
    }

    @Override
    public void onErrorAuthorization() {
        showDialogExpiredToken();
    }


    @Override
    public void toggleLike(int pos, Post post) {
        presenter.toggleLike(post.getId(), post.getIs_like());
    }

    @Override
    public void onClickDetail(int pos, Post post) {
        this.posEdit = pos;
        this.postEditing = post;
        Intent i = new Intent(getActivity(), DetailPostActivity.class);
        i.putExtra(AppConstant.MSG, post.getId());
        startActivityForResult(i, 1000);
    }

    @Override
    public void onClickComment(int pos, Post post) {
        this.posEdit = pos;
        this.postEditing = post;
        Intent i = new Intent(getActivity(), DetailPostActivity.class);
        i.putExtra(AppConstant.MSG, post.getId());
        startActivityForResult(i, 1000);
    }

    @Override
    public void onClickShare(int pos, Post post) {
        if (dialogShare != null && !dialogShare.isShowing()) {
            dialogShare.show();
        }
    }

    @Override
    public void onClickPostShare(int pos, Post post) {
        Intent i = new Intent(getActivity(), DetailPostActivity.class);
        i.putExtra(AppConstant.MSG, post.getPostShare().getId());
        startActivityForResult(i, 1000);
    }

    @Override
    public void onRefresh() {
        index = 0;
        requestType = TYPE_REFREESH;
        presenter.getTimeline(index, AppConstant.TYPE_POST,getArguments().getString(AppConstant.MSG));
    }

    @Override
    public void onLoadMore() {
        requestType = TYPE_LOADMORE;
        presenter.getTimeline(index, AppConstant.TYPE_POST,getArguments().getString(AppConstant.MSG));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && postEditing != null) {
            if (resultCode == DetailPostActivity.RESULT_CHANGE_LIKE) {
                int number = data.getIntExtra(AppConstant.MSG, postEditing.getNumber_like());
                boolean isLike = data.getBooleanExtra("is_like", false);
                postEditing.setNumber_like(number);
                postEditing.setIs_like(isLike ? 1 : 0);
                list.set(posEdit, postEditing);
                adapter.refrestItemAt(posEdit);
            } else if (resultCode == DetailPostActivity.RESULT_CHANGE_COMMENT) {
                int number = data.getIntExtra(AppConstant.MSG, postEditing.getNumber_comment());
                postEditing.setNumber_comment(number);
                list.set(posEdit, postEditing);
                adapter.refrestItemAt(posEdit);
            } else if (resultCode == DetailPostActivity.RESULT_CHANGE_SHARE) {
                int number = data.getIntExtra(AppConstant.MSG, postEditing.getNumber_share());
                postEditing.setNumber_share(number);
                list.set(posEdit, postEditing);
                adapter.refrestItemAt(posEdit);
            } else if (resultCode == DetailPostActivity.RESULT_DELETE) {
                adapter.remove(posEdit);

            }
        }
    }

    @Override
    public void onSucessShare(final int pos) {
        showToast("Chia sẻ thành công", AppConstant.POSITIVE, new SnackBarCallBack() {
            @Override
            public void onClosedSnackBar() {
                Intent i = new Intent(getActivity(), DetailPostActivity.class);
                i.putExtra(AppConstant.MSG, pos);
                startActivity(i);
            }
        });

    }

    @Override
    public void onMoreOptionCallback(String msg, int type) {
        if (postEditing == null) {
            showToast("Bài viết đã bị chỉnh sửa hoặc không tồn tại!", AppConstant.NEGATIVE);
            return;
        }
        //todo share content here
        presenter.shareContent(postEditing.getId(), msg, type);
    }
}
