package com.skynet.lian.ui.chatgroup.editgroup.choosecontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.contact.AdapterContactItem;
import com.skynet.lian.ui.contact.ContactContract;
import com.skynet.lian.ui.contact.ContactPresenter;
import com.skynet.lian.ui.contact.EditBottomSheet;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.ui.views.SimpleSectionedRecyclerViewAdapter;
import com.skynet.lian.utils.AppConstant;
import com.skynet.lian.utils.CommomUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChooseContactActivity extends BaseActivity implements ContactContract.View, AdapterContactItem.CallBackContact,EditBottomSheet.MoreOptionCallback{
    public static final int VIEW_CONTACT = 3;
    @BindView(R2.id.textView4)
    TextView textView4;
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.tab)
    TabLayout tab;
    @BindView(R2.id.constraintLayout2)
    ConstraintLayout constraintLayout2;
    @BindView(R2.id.rcv)
    RecyclerView rcv;
    @BindView(R2.id.imgAdd)
    ImageView imgAdd;
    @BindView(R2.id.btnCreateGroup)
    TextView btnCreateGroup;
    private ProgressDialogCustom dialogLoading;
    ContentResolver cr;
    private ContactContract.Presenter presenter;
    private AdapterContactItem adapterChatItem;
    private List<Profile> listContact;
    List<SimpleSectionedRecyclerViewAdapter.Section> sections =
            new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
    private int code_excute;
    public final static int SINGLE_MSG_CODE = 1;
    public final static int GROUP_MSG_CODE = 2;
    EditBottomSheet bottomSheet;
    @Override
    protected int initLayout() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initVariables() {
        bottomSheet = new EditBottomSheet(this,this);
        if (getIntent() != null)
            code_excute = getIntent().getIntExtra(AppConstant.MSG, 0);
        if (code_excute == GROUP_MSG_CODE) {
            imgAdd.setVisibility(View.GONE);
            btnCreateGroup.setVisibility(View.VISIBLE);
        } else if (code_excute == SINGLE_MSG_CODE) {
            imgAdd.setVisibility(View.VISIBLE);
        }
        presenter = new ContactPresenter(this);
        cr = getContentResolver();
        getPermision();
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(listContact!=null && adapterChatItem!=null) {
                    listContact.clear();
                    adapterChatItem.notifyDataSetChanged();
                }
                if (tab.getPosition() == 0) {
                    getPermision();
                }else if (tab.getPosition() == 1) {
                    presenter.getListFriend();
                } else {
                    presenter.getListBussinessContact();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterChatItem.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapterChatItem.getFilter().filter(s);
            }
        });
    }

    private void getPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            presenter.getListContact(cr);
            return;
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.READ_CONTACTS).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    presenter.getListContact(cr);
                } else {

                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        dialogLoading = new ProgressDialogCustom(this);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        imgAdd.setVisibility(View.GONE);
        btnCreateGroup.setVisibility(View.GONE);
    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
    }

    @OnClick(R2.id.imgBack)
    public void onViewClicked() {

                onBackPressed();

    }

    @OnClick( R2.id.imgSearch)
    public void onViewSearchClicked() {

                startActivityForResult(new Intent(ChooseContactActivity.this, SearchForChooseFriendActivity.class), 1000);

    }

    @Override
    public void onSucessCreateChatGroup(ChatItem chatItem) {
//        if(getmSocket() != null){
//            getmSocket().joinRoom(chatItem.getId());
//
//        }
//        Intent i = new Intent(ContactActivity.this,ChatGroupActivity.class);
//        i.putExtra(AppConstant.MSG,chatItem.getId());
//        startActivity(i);
//        setResult(RESULT_OK);
//        finish();
    }

    @Override
    public void onSucessListContact(List<Profile> list) {
        getHeaderListLatter(list);
        this.listContact = list;
        adapterChatItem = new AdapterContactItem(this, listContact, this,code_excute);
        //This is the code to provide a sectioned list
        //Sections
        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(this, R.layout.section_contact, R.id.section_text, adapterChatItem);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        //Apply this adapter to the RecyclerView
        rcv.setAdapter(mSectionedAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Intent i = new Intent();
            i.putExtra(AppConstant.MSG, data.getStringExtra(AppConstant.MSG));
            setResult(RESULT_OK, i);
            finish();

        }
    }

    private void getHeaderListLatter(List<Profile> usersList) {

        Collections.sort(usersList, new Comparator<Profile>() {
            @Override
            public int compare(Profile user1, Profile user2) {
                return String.valueOf(user1.getName().charAt(0)).toUpperCase().compareTo(String.valueOf(user2.getName().charAt(0)).toUpperCase());
            }
        });

        String lastHeader = "";

        int size = usersList.size();
        sections.clear();
        for (int i = 0; i < size; i++) {

            Profile user = usersList.get(i);
            String header = String.valueOf(user.getName().charAt(0)).toUpperCase();

            if (!TextUtils.equals(lastHeader, header)) {
                lastHeader = header;
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(i, lastHeader));
            }

        }
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void showProgress() {
        dialogLoading.showDialog();
    }

    @Override
    public void hiddenProgress() {
        dialogLoading.hideDialog();
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

    @Override
    public void onCallBack(int pos) {
//        if (code_excute == SINGLE_MSG_CODE) {
            if (listContact.get(pos).getId() == null || listContact.get(pos).getId().equals("0") || listContact.get(pos).getId().isEmpty()) {
                showToast("Người bạn này chưa cài app Lian!", AppConstant.NEGATIVE);
                return;
            }
            Intent i = new Intent();
            i.putExtra(AppConstant.MSG, listContact.get(pos).getId());
            Bundle b = new Bundle();
            b.putParcelable("profile",  listContact.get(pos));
            i.putExtra(AppConstant.BUNDLE,b);
            setResult(RESULT_OK, i);
            finish();
//        } else if(code_excute == VIEW_CONTACT){
//            Intent i = new Intent(ChooseContactActivity.this, ProfileFriendActivity.class);
//            i.putExtra(AppConstant.MSG, listContact.get(pos).getId());
//            startActivity(i);
//        }
    }

    @OnClick({ R2.id.btnCreateGroup})
    public void onViewbtnCreateGroupClicked(View view) {

                for (Profile person :listContact) {
                    if(person.isChecked() && (person.getId() == null || person.getId().equals("0") || person.getId().isEmpty())){
                        person.setChecked(false);
                    }
                }
                bottomSheet.show();

    }

    @OnClick({R2.id.imgAdd})
    public void onViewAddClicked(View view) {

                imgAdd.setVisibility(View.INVISIBLE);
                code_excute=GROUP_MSG_CODE;
                btnCreateGroup.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStop() {
        if(bottomSheet != null && bottomSheet.isShowing()){
            bottomSheet.dismiss();
        }
        super.onStop();

    }

    @Override
    public void onMoreOptionCallback(String msg) {
        presenter.createChatGroup(listContact,msg,getmSocket());
    }

    @Override
    public void onClickInvite(int pos, Profile profile) {
        CommomUtils.itentToSMS(this,profile.getPhone(),String.format(getString(R.string.invite_form),AppController.getInstance().getmProfileUser().getName(),getPackageName()));

    }

    @Override
    public void onClickAddfriend(int pos, Profile profile) {

    }

    @Override
    public void onClickCall(int pos, Profile profile) {

    }

    @Override
    public void onClickVideo(int pos, Profile profile) {

    }
}
