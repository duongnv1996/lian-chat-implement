package com.skynet.lian.ui.chatgroup.editgroup.choosecontact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.searchfriend.AdapterContactItem;
import com.skynet.lian.ui.searchfriend.SearchContactContract;
import com.skynet.lian.ui.searchfriend.SearchContactPresenter;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchForChooseFriendActivity extends BaseActivity implements SearchContactContract.View, com.skynet.lian.ui.contact.AdapterContactItem.CallBackContact {
    private static final int DIVIDER_SIZE = 2;
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.textView4)
    TextView textView4;
    @BindView(R2.id.imgSearch)
    ImageView imgSearch;
    @BindView(R2.id.constraintLayout2)
    ConstraintLayout constraintLayout2;
    @BindView(R2.id.edtPhone)
    EditText edtPhone;
    @BindView(R2.id.rcv)
    RecyclerView rcv;
    @BindView(R2.id.imageView9)
    ImageView imageView9;
    @BindView(R2.id.tablelayout)
    TableLayout tablelayout;
    @BindView(R2.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R2.id.number1)
    LinearLayout number1;
    @BindView(R2.id.number2)
    LinearLayout number2;
    @BindView(R2.id.number3)
    LinearLayout number3;
    @BindView(R2.id.number4)
    LinearLayout number4;
    @BindView(R2.id.number5)
    LinearLayout number5;
    @BindView(R2.id.number6)
    LinearLayout number6;
    @BindView(R2.id.number7)
    LinearLayout number7;
    @BindView(R2.id.number8)
    LinearLayout number8;
    @BindView(R2.id.number9)
    LinearLayout number9;
    @BindView(R2.id.numberstar)
    LinearLayout numberstar;
    @BindView(R2.id.number0)
    LinearLayout number0;
    @BindView(R2.id.numberT)
    LinearLayout numberT;
    @BindView(R2.id.progressBar)
    ProgressBar progressBar;

    private ProgressDialogCustom dialogLoading;
    private SearchContactContract.Presenter presenter;
    private Timer timer;
    private List<Profile> list;
    private AdapterContactItem adapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_search_contact;
    }

    @Override
    protected void initVariables() {
        dialogLoading = new ProgressDialogCustom(this);
        presenter = new SearchContactPresenter(this);
        list = new ArrayList<>();
        adapter = new AdapterContactItem(this, list, this);
        rcv.setAdapter(adapter);

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list.clear();
                                adapter.notifyDataSetChanged();
                                presenter.getListContact(s.toString());
                            }
                        });
                    }
                }, 600);
            }
        });
    }

    @Override
    protected void initViews() {
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);

    }


    @Override
    protected int initViewSBAnchor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.imgBack)
    public void onViewBackClicked() {

        onBackPressed();


    }

    @OnClick(R2.id.imgSearch)
    public void onViewClicked() {

        if (list.size() == 1) {
            presenter.addFriend(list.get(0).getId());
        }

    }

    @Override
    public void onSucessAddFriend() {
        showToast("Đã thêm bạn thành công!", AppConstant.POSITIVE);
        setResult(RESULT_OK);
    }

    @Override
    public void onSucessListContact(List<Profile> list) {
        this.list.clear();
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hiddenProgress() {
        progressBar.setVisibility(View.GONE);

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

    @OnClick( R2.id.imageView9)
    public void onViewNumberPadClicked() {
       
                if (edtPhone.getText().toString().isEmpty()) return;
                edtPhone.setText(edtPhone.getText().subSequence(0, edtPhone.getText().toString().length() - 1));
        
    }

    @Override
    public void onCallBack(int pos) {
        Intent i = new Intent();
        i.putExtra(AppConstant.MSG, list.get(pos).getId());
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onClickInvite(int pos, Profile profile) {

    }

    @Override
    public void onClickAddfriend(int pos, Profile profile) {
        presenter.addFriend(profile.getId());
    }

    @Override
    public void onClickCall(int pos, Profile profile) {

    }

    @Override
    public void onClickVideo(int pos, Profile profile) {

    }

    @OnClick(R2.id.number1)
    public void onNumber1Clicked() {
        edtPhone.setText(edtPhone.getText() + "1");
    }

    @OnClick(R2.id.number2)
    public void onNumber2Clicked() {
        edtPhone.setText(edtPhone.getText() + "2");


    }

    @OnClick(R2.id.number3)
    public void onNumber3Clicked() {
        edtPhone.setText(edtPhone.getText() + "3");

    }

    @OnClick(R2.id.number4)
    public void onNumber4Clicked() {
        edtPhone.setText(edtPhone.getText() + "4");

    }

    @OnClick(R2.id.number5)
    public void onNumber5Clicked() {
        edtPhone.setText(edtPhone.getText() + "5");

    }

    @OnClick(R2.id.number6)
    public void onNumber6Clicked() {
        edtPhone.setText(edtPhone.getText() + "6");

    }

    @OnClick(R2.id.number7)
    public void onNumber7Clicked() {
        edtPhone.setText(edtPhone.getText() + "7");

    }

    @OnClick(R2.id.number8)
    public void onNumber8Clicked() {
        edtPhone.setText(edtPhone.getText() + "8");

    }

    @OnClick(R2.id.number9)
    public void onNumber9Clicked() {
        edtPhone.setText(edtPhone.getText() + "9");

    }

    @OnClick(R2.id.numberstar)
    public void onNumberstarClicked() {
        edtPhone.setText(edtPhone.getText() + "*");

    }

    @OnClick(R2.id.number0)
    public void onNumber0Clicked() {
        edtPhone.setText(edtPhone.getText() + "0");

    }

    @OnClick(R2.id.numberT)
    public void onNumberTClicked() {
        edtPhone.setText(edtPhone.getText() + "#");

    }
}
