package com.skynet.lian.ui.DetailNotificationActivity;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Notification;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailNotificationActivity extends BaseActivity implements DetailNotificationContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R2.id.tvTitle_toolbar)
    TextView tvTitleToolbar;
    @BindView(R2.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R2.id.tvTitle)
    TextView tvTitle;
    @BindView(R2.id.tvContent)
    TextView tvContent;
    @BindView(R2.id.tvTime)
    TextView tvTime;

    private DetailNotificationContract.Presenter presenter;
    private ProgressDialogCustom dialogLoading;


    @Override
    protected int initLayout() {
        return R.layout.activity_detail_notification;
    }

    @Override
    protected void initVariables() {
        dialogLoading = new ProgressDialogCustom(this);
        presenter = new DetailNotificationPresenter(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            presenter.getDetail(getIntent().getExtras().getString(AppConstant.MSG));
        }
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        tvTitleToolbar.setText("Thông báo");

        swipe.setOnRefreshListener(this);
    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }


    @OnClick(R2.id.imgBtn_back_toolbar)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        dialogLoading.showDialog();
    }

    @Override
    public void hiddenProgress() {
        dialogLoading.hideDialog();
        swipe.setRefreshing(false);
    }

    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorAuthorization() {

    }


    @Override
    public void onRefresh() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            presenter.getDetail(getIntent().getExtras().getString(AppConstant.MSG));
        }
    }


    @Override
    public void onSuccessGetDetail(Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvContent.setText(Html.fromHtml(notification.getName(), Html.FROM_HTML_MODE_COMPACT));

        } else {
            tvContent.setText(Html.fromHtml(notification.getName()));
        }
        tvTitle.setText(notification.getTitle());
        tvTime.setText(notification.getTime());

        setResult(RESULT_OK);
    }


}
