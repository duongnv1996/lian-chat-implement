package com.skynet.lian.ui.favourite;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallbackTwoM;
import com.skynet.lian.models.Excercise;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.utils.AppConstant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

;

public class FavouriteFragment extends BaseFragment implements FavouriteContract.View, SwipeRefreshLayout.OnRefreshListener, ICallbackTwoM {

    @BindView(R2.id.rcv)
    RecyclerView rcv;
    @BindView(R2.id.swipe)
    SwipeRefreshLayout swipe;
    private List<Excercise> list;
    private FavouriteContract.Presenter presenter;

    public static FavouriteFragment newInstance() {
        Bundle args = new Bundle();
        FavouriteFragment fragment = new FavouriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_favourite;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        swipe.setOnRefreshListener(this);
        rcv.setLayoutManager(new GridLayoutManager(getMyContext(), 2));
        rcv.setHasFixedSize(true);
    }

    @Override
    protected void initVariables() {
        presenter = new FavouritePresenter(this);
        swipe.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void doAction() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getCallBackTitle().setTilte("Bài tập yêu thích  ");
    }

    @Override
    public void onSucessGetList(List<Excercise> list) {
        this.list = list;
        rcv.setAdapter(new AdapterFavourite(this.list, getMyContext(), this));
    }

    @Override
    public Context getMyContext() {
        return getContext();
    }

    @Override
    public void showProgress() {
        swipe.setRefreshing(true);

    }

    @Override
    public void hiddenProgress() {
        swipe.setRefreshing(false);

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
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();

    }

    @Override
    public void onRefresh() {
        presenter.getList();
    }

    @Override
    public void onCallBack(int pos) {

    }

    @Override
    public void onCallBackToggle(int pos, boolean isCheck) {
        presenter.toggleFav(list.get(pos).getId(), isCheck);
    }
}
