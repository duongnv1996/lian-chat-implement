package com.skynet.lian.ui.whitelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.chatgroup.editgroup.choosecontact.ChooseContactActivity;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WhiteListActivity extends BaseActivity implements WhiteListContract.View, AdapterWhitelistItem.CallBack {
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.textView4)
    TextView textView4;
    @BindView(R2.id.btnEdit)
    ImageView btnEdit;
    @BindView(R2.id.constraintLayout2)
    ConstraintLayout constraintLayout2;
    @BindView(R2.id.rcv)
    RecyclerView rcv;
    @BindView(R2.id.search)
    EditText search;
    private WhiteListContract.Presenter presenter;
    private ProgressDialogCustom dialog;
    private List<Profile> list;
    private AdapterWhitelistItem adapterWhitelistItem;

    @Override
    protected int initLayout() {
        return R.layout.activity_white_list;
    }

    @Override
    protected void initVariables() {
        dialog = new ProgressDialogCustom(this);
        presenter = new WhiteListPresenter(this);
        presenter.getList();
        list = new ArrayList<>();
        adapterWhitelistItem = new AdapterWhitelistItem(list, this, this);
        rcv.setAdapter(adapterWhitelistItem);
    }

    @Override
    protected void initViews() {
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterWhitelistItem.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapterWhitelistItem.getFilter().filter(s);
            }
        });
    }

    @OnClick(R2.id.imgBack)
    public void onClick() {
        onBackPressed();
    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }

    @Override
    public void onSucessGetList(List<Profile> list) {
        this.list.addAll(list);
        adapterWhitelistItem.notiDateChange();
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
    }

    @Override
    public void onErrorAuthorization() {
        showDialogExpired();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClickDelete(int pos, Profile item) {
        presenter.deleteFromWhitelist(item.getIdBlocked(), 2);
        adapterWhitelistItem.remove(pos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String id = data.getStringExtra(AppConstant.MSG);
            Bundle b = data.getBundleExtra(AppConstant.BUNDLE);
            if (b != null) {
                Profile profile = b.getParcelable("profile");
                list.add(profile);
                adapterWhitelistItem.notiDateChange();
            }
            presenter.deleteFromWhitelist(id, 1);

        }
    }

    @OnClick(R2.id.btnEdit)
    public void onViewClicked() {
        Intent i = new Intent(WhiteListActivity.this, ChooseContactActivity.class);
        startActivityForResult(i, 100);
    }
}
