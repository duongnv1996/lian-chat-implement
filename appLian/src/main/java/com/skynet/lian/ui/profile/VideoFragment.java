package com.skynet.lian.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.interfaces.SnackBarCallBack;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.Timeline;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.detailpost.DetailPostActivity;
import com.skynet.lian.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends BaseFragment implements ProfileContract.View ,ICallback , XRecyclerView.LoadingListener{
    @BindView(R2.id.rcv)
    XRecyclerView rcv;
    private AdapterVideo adapterGrid;
    private static int TYPE_LOADMORE = 1;
    private static int TYPE_REFREESH = 0;
    private ProfileContract.Presenter presenter;
    private int index=0;
    private List<Image> list;
    private int requestType;
    public static VideoFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(AppConstant.MSG,id);
        VideoFragment fragment = new VideoFragment();
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
        ButterKnife.bind(this,view);
//        rcv.setLoadingMoreEnabled(true);
//        rcv.setHasFixedSize(false);
        ;
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
    protected void initVariables() {
        presenter = new ProfilePresenter(this);
        list = new ArrayList<>();
        adapterGrid = new AdapterVideo(list,getContext(),this);
        rcv.setLayoutManager(new GridLayoutManager(getContext(),3));
        rcv.setHasFixedSize(true);
        presenter.getTimeline(index,AppConstant.TYPE_VIDEO,getArguments().getString(AppConstant.MSG));
        rcv.setAdapter(adapterGrid);
        rcv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rcv.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rcv.setLimitNumberToCallLoadMore(10);
        rcv.setLoadingMoreEnabled(true);
        rcv.setLoadingListener(this);
    }

    @Override
    public void onSucessGetTimeline(Timeline timeline) {
        if(timeline != null){
            if (requestType == TYPE_REFREESH) {
                this.list.clear();
            }
            if(timeline.getList_video()!=null && !timeline.getList_video().isEmpty()) {
                int pos = this.list.size()+1;
                this.list.addAll(timeline.getList_video());
//                adapterGrid.notifyItemRangeInserted(pos,timeline.getList_video().size());
                adapterGrid.notifyDataSetChanged();
            }else{
                rcv.setNoMore(true);
            }
        }
//        adapterGrid.notifyItemInserted(this.index);
        this.index= timeline.getIndex();

        rcv.loadMoreComplete();
        rcv.refreshComplete();

    }

    @Override
    public Context getMyContext() {
        return getContext();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hiddenProgress() {

    }

    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);
        showToast(message,AppConstant.NEGATIVE);
    }

    @Override
    public void onErrorAuthorization() {
        showDialogExpiredToken();
    }

    @Override
    public void onCallBack(int pos) {
        Intent i =new Intent(getActivity(),DetailPostActivity.class);
        i.putExtra(AppConstant.MSG,list.get(pos).getPostID());
        startActivityForResult(i,1000);
    }

    @Override
    public void onRefresh() {
        index = 0;
        requestType = TYPE_REFREESH;
        presenter.getTimeline(index, AppConstant.TYPE_VIDEO,getArguments().getString(AppConstant.MSG));
    }

    @Override
    public void onLoadMore() {
        requestType = TYPE_LOADMORE;
        presenter.getTimeline(index, AppConstant.TYPE_VIDEO,getArguments().getString(AppConstant.MSG));

    }

}
