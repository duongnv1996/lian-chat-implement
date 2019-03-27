package com.skynet.lian.ui.tabexplore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.CallbackClickPhotoItem;
import com.skynet.lian.models.Post;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.detailpost.DetailPostActivity;
import com.skynet.lian.ui.detailpost.viewphoto.ViewPhotoPostActivity;
import com.skynet.lian.ui.makepost.MakePostActivity;
import com.skynet.lian.ui.profile.ProfileActivity;
import com.skynet.lian.ui.profileFriend.ProfileFriendActivity;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

//import android.support.constraint.ConstraintLayout;

public class TabExploreFragment extends BaseFragment implements AdapterTimeline.ICallBackPost, TimeLineContract.View, XRecyclerView.LoadingListener, CallbackClickPhotoItem {
    @BindView(R2.id.imgHome)
    CircleImageView imgHome;
    @BindView(R2.id.imgStausToolbar)
    ImageView imgStausToolbar;
    @BindView(R2.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R2.id.tvStatusToolbar)
    TextView tvStatusToolbar;
    @BindView(R2.id.tvTimeLine)
    TextView tvTimeLine;
    @BindView(R2.id.view)
    View view;
    @BindView(R2.id.layoutToolbar)
    ConstraintLayout layoutToolbar;
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.layoutSearch)
    ConstraintLayout layoutSearch;
    @BindView(R2.id.textView6)
    TextView textView6;
    @BindView(R2.id.rcv)
    XRecyclerView rcv;
    //    @BindView(R2.id.scrollView)
//    NestedScrollView scrollView;
    @BindView(R2.id.include)
    ConstraintLayout include;
    //    @BindView(R2.id.swipeTabChat)
//    SwipeRefreshLayout swipeTabChat;
    private static int TYPE_LOADMORE = 1;
    private static int TYPE_REFREESH = 0;
    private int requestType;
    private int index = 0;
    private List<Post> list;
    private AdapterTimeline adapter;
    private TimeLineContract.Presenter presenter;
    private ProgressDialogCustom dialogLoading;
    private boolean isFirstShow = true;

    public static TabExploreFragment newInstance() {
        Bundle args = new Bundle();
        TabExploreFragment fragment = new TabExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void doAction() {
        Intent i = new Intent(getActivity(), MakePostActivity.class);
        startActivityForResult(i, 1000);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_tab_explore;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        rcv.setLayoutManager(layoutManager);
        rcv.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new AdapterTimeline(list, getContext(), this, this);
        rcv.setPullRefreshEnabled(true);
        rcv.setLoadingMoreEnabled(true);
        rcv.setAdapter(adapter);
        rcv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rcv.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rcv.setLimitNumberToCallLoadMore(10);
        rcv.setLoadingListener(this);
    }

    @Override
    protected void initVariables() {
        dialogLoading = new ProgressDialogCustom(getContext());
        presenter = new TimelinePresenter(this);
        presenter.getTimeLine(index);
        bindUi();
    }

    private void bindUi() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile != null) {
            if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
                Picasso.with(getContext()).load(profile.getAvatar()).fit().centerCrop().into(imgHome);
            }
            tvNameToolbar.setText("Hi, " + profile.getName());
            tvStatusToolbar.setText(profile.getLast_status());
            if(profile.getOnline() == 0 ){
                imgStausToolbar.setImageResource(R.drawable.dot_gray_stock);
            }else{
                imgStausToolbar.setImageResource(R.drawable.dot_green_stock);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bindUi();
    }

    @Override
    public void onCallBack(int pos) {
        Intent i = new Intent(getActivity(), DetailPostActivity.class);
        i.putExtra(AppConstant.MSG, list.get(pos).getId());
        startActivityForResult(i, 1000);
    }

    @OnClick({R2.id.layoutToolbar})
    public void onViewClicked(View view) {
        startActivityForResult(new Intent(getActivity(), ProfileActivity.class), 1000);
    }

    @Override
    public void onSucessGetListPost(List<Post> list, int index) {
        if (requestType == TYPE_REFREESH) {
            this.list.clear();
        }
        if (!list.isEmpty()) {
            this.list.addAll(list);
            adapter.refreshCache();
            adapter.notifyDataSetChanged();
        } else {
            rcv.setNoMore(true);
        }
        this.index = index;
        rcv.loadMoreComplete();
        rcv.refreshComplete();
    }

    @Override
    public Context getMyContext() {
        return null;
    }

    @Override
    public void showProgress() {
        if (isFirstShow) {
//            dialogLoading.showDialog();
            isFirstShow = false;
        }
    }

    @Override
    public void hiddenProgress() {
//        dialogLoading.hideDialog();
        rcv.loadMoreComplete();
        rcv.refreshComplete();
    }

    @Override
    public void onErrorApi(String message) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onErrorAuthorization() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == getActivity().RESULT_OK) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        requestType = TYPE_REFREESH;
        index = 0;

        presenter.getTimeLine(index);
    }

    @Override
    public void onLoadMore() {
        requestType = TYPE_LOADMORE;
        presenter.getTimeLine(index);
    }

    @Override
    public void onClickPhoto(int pos, Post post) {
        Intent i = new Intent(getActivity(), ViewPhotoPostActivity.class);
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) post.getList_image());
        i.putExtra(AppConstant.BUNDLE, b);
        startActivity(i);
    }

    @Override
    public void onClickProfile(String id) {
        Intent i = new Intent(getActivity(), ProfileFriendActivity.class);
        i.putExtra(AppConstant.MSG, id);
        startActivity(i);
    }
}
