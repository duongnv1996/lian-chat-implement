package com.skynet.lian.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R2.id.swipe)
    SwipeRefreshLayout swipe;
    Unbinder unbinder;

    HomeContract.PresenterI presenter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        swipe.setOnRefreshListener(this);
    }

    @Override
    protected void initVariables() {
        presenter = new HomePresenterI(this);
       // bindData();
    }



    private void bindData() {
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile != null) {

        } else {
            showDialogExpiredToken();
            return;
        }
    }

    private void setupChart(Profile profile) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void doAction() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onRefresh() {
        presenter.getInfor();
    }

    @Override
    public void onSuccessGetInfor() {
        bindData();
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


}
