package com.skynet.lian.ui.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Notification;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.DetailNotificationActivity.DetailNotificationActivity;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends BaseFragment implements ICallback, NotificationContract.View, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R2.id.swipe)
    SwipeRefreshLayout swipe;
    //    @BindView(R2.id.swipe)
//    SwipeRefreshLayout swipe;
    private NotificationContract.Presenter presenter;
    private ProgressDialogCustom dialogLoading;
    @BindView(R2.id.rcvNotification)
    RecyclerView rcvNotification;
    Profile profileModel;
    List<Notification> listGroupServices = new ArrayList<>();
    private ICallback mListener;

    public static NotificationActivity newInstance() {

        Bundle args = new Bundle();

        NotificationActivity fragment = new NotificationActivity();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        rcvNotification.setLayoutManager(new LinearLayoutManager(getMyContext()));
        rcvNotification.setHasFixedSize(true);
        swipe.setOnRefreshListener(this);
    }

    @Override
    protected void initVariables() {
        dialogLoading = new ProgressDialogCustom(getMyContext());
        presenter = new NotificationPresenter(this);
        profileModel = AppController.getInstance().getmProfileUser();
        if (profileModel == null) {
            showDialogExpiredToken();
            return;
        }
        presenter.getAllService(profileModel.getId());
    }


    @Override
    public Context getMyContext() {
        return getContext();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();

        super.onDestroyView();


    }

    @Override
    public void showProgress() {
//        dialogLoading.showDialog();
        swipe.setRefreshing(true);
    }

    @Override
    public void hiddenProgress() {
//        dialogLoading.hideDialog();
        swipe.setRefreshing(false);
    }


    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
        Toast.makeText(getMyContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);
        Toast.makeText(getMyContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorAuthorization() {

    }


    @Override
    public void onSuccessGetServices(List<Notification> listGroupServices) {
        if (listGroupServices != null) {
            this.listGroupServices.clear();
            this.listGroupServices.addAll(listGroupServices);
        }
        rcvNotification.setAdapter(new NotificationAdapter(this.listGroupServices, getMyContext(), this));
    }


    @Override
    public void onRefresh() {
        presenter.getAllService(profileModel.getId());
    }

    // Click to item to fire this
    @Override
    public void onCallBack(int pos) {
        Intent i = new Intent(getActivity(), DetailNotificationActivity.class);
        if (listGroupServices.get(pos).isRead() == 0) {
            AppController.getInstance().getmProfileUser().setNoty(AppController.getInstance().getmProfileUser().getNoty() - 1);
//            ((MainActivityLian) getActivity()).setBadget(AppController.getInstance().getmProfileUser().getNoty());
//            mListener.onCallBack(1);
        }
        i.putExtra(AppConstant.MSG, this.listGroupServices.get(pos).getId());
        startActivityForResult(i, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == getActivity().RESULT_OK) {
            //     onRefresh();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof HomeFragment.OnFragmentHomeCallBack) {
//            mListener = (ICallback) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentHomeCallBack");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void doAction() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getCallBackTitle().setTilte(null);

    }
}
