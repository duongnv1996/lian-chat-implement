package com.skynet.lian.ui.chatgroup.editgroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.interfaces.SnackBarCallBack;
import com.skynet.lian.models.Room;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.chatgroup.editgroup.choosecontact.ChooseContactActivity;
import com.skynet.lian.ui.contact.EditBottomSheet;
import com.skynet.lian.ui.main.OptionBottomSheet;
import com.skynet.lian.ui.profileFriend.ProfileFriendActivity;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditGroupActivity extends BaseActivity implements EditContract.View, EditBottomSheet.MoreOptionCallback, ICallback {
    public static final int RESULT_CODE_DELETE_GROUP = 12;
    public static final int RESULT_CODE_LEAVE_GROUP = 13;
    public static final int RESULT_CODE_EDIT_GROUP = 14;
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
    @BindView(R2.id.imgAdd)
    LinearLayout imgAdd;
    @BindView(R2.id.btnLeave)
    TextView btnLeave;
    @BindView(R2.id.btnDelete)
    TextView btnDelete;
    @BindView(R2.id.textView9)
    TextView textView9;
    EditBottomSheet editBottomSheet;
    Room room;
    ProgressDialogCustom dialog;
    private EditContract.Presenter presenter;

    @Override
    protected int initLayout() {
        return R.layout.activity_edit_group;
    }

    @Override
    protected void initVariables() {
        dialog = new ProgressDialogCustom(this);
        presenter = new EditPresenter(this);
        editBottomSheet = new EditBottomSheet(this, this);
        if (getIntent() != null && getIntent().getBundleExtra(AppConstant.BUNDLE) != null) {
            room = getIntent().getBundleExtra(AppConstant.BUNDLE).getParcelable(AppConstant.MSG);
            bindUI();

        }
    }

    private void bindUI() {
        if (room != null) {
            editBottomSheet.setText(room.getTitle());
            rcv.setAdapter(new AdapterContactItem(this, room.getListUser(), this));
            textView4.setText(room.getTitle());
        }
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

    @Override
    protected void onStop() {
        if (editBottomSheet != null && editBottomSheet.isShowing()) {
            editBottomSheet.dismiss();
        }
        super.onStop();
    }
//
//    @OnClick({R2.id.imgBack, R2.id.btnEdit, R2.id.imgAdd, R2.id.btnLeave, R2.id.btnDelete})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.imgBack:
//                break;
//            case R.id.btnEdit:
//                break;
//            case R.id.imgAdd:
//
//                break;
//            case R2.id.btnLeave:
//
//                break;
//            case R.id.btnDelete:
//
//                break;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            presenter.addUserToGroup(room.getRoomInfo() + "", data.getExtras().getString(AppConstant.MSG));
        }
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

    @Override
    public void onMoreOptionCallback(String msg) {
        if (msg == null || msg.isEmpty()) {
            showToast("Vui lòng nhập tên nhóm", AppConstant.NEGATIVE);
            return;
        }
        if (room != null)
            presenter.edtChatGroup(msg, room.getRoomInfo() + "");

        textView4.setText(msg);
    }

    @Override
    public void onSucessEdtChatGroup() {
        showToast("Đã đổi tên nhóm thành công!", AppConstant.POSITIVE);
        setResult(RESULT_CODE_EDIT_GROUP);
        if (getmSocket() != null) {
            getmSocket().sendMessage("", "", "", room.getRoomInfo() + "", AppConstant.GROUP);
        }
    }

    @Override
    public void onSucessDeleteChat() {
        if (getmSocket() != null) {
            getmSocket().sendMessage("", "", "", room.getRoomInfo() + "", AppConstant.GROUP);
        }
        setResult(RESULT_CODE_DELETE_GROUP);
        showToast("Đã xoá nhóm thành công!", AppConstant.POSITIVE, new SnackBarCallBack() {
            @Override
            public void onClosedSnackBar() {
                finish();
            }
        });
    }

    @Override
    public void onSucessLeaveGroupChat() {
        setResult(RESULT_CODE_LEAVE_GROUP);
        if (getmSocket() != null) {
            getmSocket().sendMessage("", "", "", room.getRoomInfo() + "", AppConstant.GROUP);
            getmSocket().leaveRoom(room.getRoomInfo() + "");
        }
        showToast("Đã rời khỏi nhóm thành công!", AppConstant.POSITIVE, new SnackBarCallBack() {
            @Override
            public void onClosedSnackBar() {
                finish();
            }
        });

    }

    @Override
    public void onSucessAddUserToGroup(Room room) {
        this.room = room;
        bindUI();
        setResult(RESULT_CODE_EDIT_GROUP);

    }

    @Override
    public void onCallBack(int pos) {
        Intent i = new Intent(EditGroupActivity.this, ProfileFriendActivity.class);
        i.putExtra(AppConstant.MSG, room.getListUser().get(pos).getId());
        startActivity(i);
    }


    @OnClick(R2.id.imgBack)
    public void onImgBackClicked() {
        onBackPressed();

    }

    @OnClick(R2.id.btnEdit)
    public void onBtnEditClicked() {
        editBottomSheet.show();

    }

    @OnClick(R2.id.btnLeave)
    public void onBtnLeaveClicked() {
        new OptionBottomSheet(this, R.drawable.ic_sign_out_alt_solid,
                Html.fromHtml(String.format(getString(R.string.confirm_leave), room.getTitle())),
                "Quay lại", "Rời khởi nhóm",
                new OptionBottomSheet.MoreOptionCallback() {
                    @Override
                    public void onMoreOptionCallback() {
                        presenter.leaveGroupChat(room.getRoomInfo() + "");
                    }

                    @Override
                    public void onCancleCallback() {

                    }
                }).show();
    }

    @OnClick(R2.id.btnDelete)
    public void onBtnDeleteClicked() {
        new OptionBottomSheet(this, R.drawable.ic_delete,
                Html.fromHtml(String.format(getString(R.string.confirm_delete), room.getTitle())),
                "Quay lại", "Xoá ",
                new OptionBottomSheet.MoreOptionCallback() {
                    @Override
                    public void onMoreOptionCallback() {
                        presenter.deleteChat(room.getRoomInfo() + "");

                    }

                    @Override
                    public void onCancleCallback() {

                    }
                }).show();

    }

    @OnClick(R2.id.imgAdd)
    public void onImgAddClicked() {
        Intent i = new Intent(EditGroupActivity.this, ChooseContactActivity.class);
        startActivityForResult(i, 1000);
    }
}
