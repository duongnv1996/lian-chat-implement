package com.skynet.lian.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.views.DialogCenterHelp;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.ui.whitelist.WhiteListActivity;
import com.skynet.lian.utils.AppConstant;
import com.skynet.lian.utils.CommomUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements DialogCenterHelp.DialogCenterHelpClickListener, ContactContract.View {

    DialogCenterHelp dialogCenterHelp;
    @BindView(R2.id.imgBtn_back_toolbar)
    ImageView imgBtnBackToolbar;
    @BindView(R2.id.tvTitle_toolbar)
    TextView tvTitleToolbar;
    @BindView(R2.id.textView11)
    TextView textView11;
    @BindView(R2.id.switchNoty)
    Switch switchNoty;
    @BindView(R2.id.layoutNoty)
    ConstraintLayout layoutNoty;
    @BindView(R2.id.layoutBlackList)
    ConstraintLayout layoutBlackList;
    @BindView(R2.id.layoutSyncContact)
    ConstraintLayout layoutSyncContact;
    @BindView(R2.id.switchStatus)
    Switch switchStatus;
    @BindView(R2.id.layoutStatusAccount)
    ConstraintLayout layoutStatusAccount;
    private ProgressDialogCustom dialog;
    private ContactContract.Presenter presenter;

    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initVariables() {
        tvTitleToolbar.setText("Cài đặt");
        dialog = new ProgressDialogCustom(this);
        presenter = new ContactPresenter(this);
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        switchNoty.setChecked(AppController.getInstance().getmSetting().getBoolean(AppConstant.NOTI_ON,true));
//        switchStatus.setChecked(AppController.getInstance().getmSetting().getBoolean(AppConstant.STATUS));
        switchNoty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppController.getInstance().getmSetting().put(AppConstant.NOTI_ON, isChecked);
            }
        });
        switchStatus.setChecked(AppController.getInstance().getmProfileUser().getOnline()==1);
        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.toggleOnline(isChecked);
                AppController.getInstance().getmSetting().put(AppConstant.STATUS, isChecked);
                AppController.getInstance().getmProfileUser().setOnline(isChecked ? 1 : 0);
                setResult(RESULT_OK);
            }
        });

    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }


//    @OnClick({R2.id.imgBtn_back_toolbar, R2.id.layoutNoty, R2.id.layoutBlackList,
//            R2.id.layoutSyncContact, R2.id.layoutStatusAccount})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R2.id.imgBtn_back_toolbar:
//                onBackPressed();
//                break;
//            case R2.id.layoutNoty:
//                switchNoty.toggle();
//                break;
//            case R2.id.layoutBlackList:
//                startActivity(new Intent(SettingActivity.this, WhiteListActivity.class));
//                break;
//            case R2.id.layoutSyncContact:
//                presenter.getListContact(getContentResolver());
//                break;
//
//            case R2.id.layoutStatusAccount:
//                switchStatus.toggle();
//                break;
//
//        }
//    }

    @Override
    public void onEmailClick() {
        CommomUtils.intentToMail(this, "htkh@vass.com.vn");
    }

    @Override
    public void onPhoneClick() {
        CommomUtils.dialPhoneNumber(this, "19009249");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onSucessAddFriend() {

    }

    @Override
    public void onSucessListContact(List<Profile> list) {
        showToast("Đồng bộ tài khoản thành công", AppConstant.POSITIVE);
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void showProgress() {
        dialog.showDialog();
    }

    @Override
    public void hiddenProgress() {
        dialog.hideDialog();

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
        showDialogExpired();
    }

   @OnClick(R2.id.imgBtn_back_toolbar)
    public void onImgBtnBackToolbarClicked() {
        onBackPressed();
    }

   @OnClick(R2.id.layoutNoty)
    public void onLayoutNotyClicked() {
        switchNoty.toggle();
    }

   @OnClick(R2.id.layoutBlackList)
    public void onLayoutBlackListClicked() {
        startActivity(new Intent(SettingActivity.this, WhiteListActivity.class));
    }

   @OnClick(R2.id.layoutSyncContact)
    public void onLayoutSyncContactClicked() {
        presenter.getListContact(getContentResolver());
    }

   @OnClick(R2.id.layoutStatusAccount)
    public void onLayoutStatusAccountClicked() {
        switchStatus.toggle();
    }
}
