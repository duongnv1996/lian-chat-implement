package com.skynet.lian.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.google.gson.Gson;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.SnackBarCallBack;
import com.skynet.lian.models.NotificationFCM;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.chatgroup.ChatGroupActivity;
import com.skynet.lian.ui.chatting.ChatActivity;
import com.skynet.lian.ui.tabchat.TabChatFragment;
import com.skynet.lian.ui.tabgroup.TabGroupFragment;
import com.skynet.lian.ui.views.ViewpagerNotSwipe;
import com.skynet.lian.utils.AppConstant;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import q.rorbin.badgeview.Badge;

public class MainActivity extends BaseActivity implements OptionBottomSheet.MoreOptionCallback, ContactContract.View {


    @BindView(R2.id.viewpager)
    ViewpagerNotSwipe viewpager;
    private AdapterMainViewpager adapter;
    private boolean doubleBackToExitPressedOnce;
    private Badge badge;
    private ContactContract.Presenter presenter;
    private OptionBottomSheet optionBottomSheet;
    private OptionBottomSheet bottomAddFriendRequest;
    private String userIdRequestFriend;
    private OptionBottomSheet.MoreOptionCallback addFriendCallback = new OptionBottomSheet.MoreOptionCallback() {
        @Override
        public void onMoreOptionCallback() {
            if (userIdRequestFriend != null) {
                presenter.acceptFriend(userIdRequestFriend);
            }
        }

        @Override
        public void onCancleCallback() {

        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_main_lian;
    }

    @Override
    protected void initVariables() {
//        showDialogExpired();
        bottomAddFriendRequest = new OptionBottomSheet(this, addFriendCallback);
        if (getIntent() != null && getIntent().getExtras() != null) {
            String data = getIntent().getExtras().getString(AppConstant.NOTIFICATION_SOCKET);
            if (data != null) {
//                startActivity(new Intent(MainActivityLian.this, ChatActivity.class));
                NotificationFCM dataObj = new Gson().fromJson(data, NotificationFCM.class);
                if (dataObj.getType() == 1) {
                    Intent i = new Intent(MainActivity.this, ChatActivity.class);
                    i.putExtra(AppConstant.MSG, dataObj.getObject().getUser_id());
                    i.putExtra("typeRoom", AppConstant.SINGLE);
                    startActivityForResult(i, 1000);
                } else if (dataObj.getType() == 6 || dataObj.getType() == 3 || dataObj.getType() == 5 || dataObj.getType() == 7) {
                    Intent i = new Intent(MainActivity.this, ChatGroupActivity.class);
                    i.putExtra(AppConstant.MSG, dataObj.getRoomID());
                    i.putExtra("typeRoom", AppConstant.GROUP);
                    startActivityForResult(i, 1000);
                } else if (dataObj.getType() == 4) {
//                    bnve.setCurrentItem(2); todo
                } else if (dataObj.getType() == 2) {
                    //addfriend
                    userIdRequestFriend = dataObj.getIdUserRequestFriend();
                    bottomAddFriendRequest.setData(R.drawable.ic_mail,
                            Html.fromHtml(String.format(getString(R.string.confirm_add), dataObj.getNameUserRequestFriend())),
                            "Xoá bỏ ", "Đồng ý ");
                    bottomAddFriendRequest.show();
                } else if (dataObj.getType() == 8) {
                    //accept friend
                    Intent i = new Intent(MainActivity.this, ChatActivity.class);
                    i.putExtra(AppConstant.MSG, dataObj.getIdUserRequestFriend());
                    i.putExtra("typeRoom", AppConstant.SINGLE);
                    startActivityForResult(i, 1000);
                }

            }
        }

        presenter = new ContactPresenter(this);
        optionBottomSheet = new OptionBottomSheet(this, this);
        presenter.getListBussinessContact();
        presenter.updateToken();
        getPermision();

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //todo check Home or Call seleted here
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getmSocket().joinRoom();
                getmSocket().online();
            }
        });

    }

    private void getPermision() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
//                == PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            presenter.getListContact(getContentResolver());
//
//            return;
//        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.READ_CONTACTS).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean)
                presenter.getListContact(getContentResolver());
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
        installButton90to180();
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Text-Medium.otf");
        adapter = new AdapterMainViewpager(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setPagingEnabled(false);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(1);
    }

    public void showDialogConfirm(int resIcon, Spanned content, String left, String right) {
        optionBottomSheet.setData(resIcon, content, left, right);
        optionBottomSheet.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        int count = AppController.getInstance().getmProfileUser().getMessage() + AppController.getInstance().getmProfileUser().getNoty();
//        if (count > 0)
//            addBadgeAt(2, count);
//        else if (badge != null)
//            badge.hide(true);

//        if (AppController.getInstance().getmProfileUser().getName().isEmpty() || AppController.getInstance().getmProfileUser().getEmail().isEmpty()) {
//            startActivity(new Intent(MainActivityLian.this, ProfileUpdateFragment.class));
//        }
    }

//    private void addBadgeAt(int position, int number) {
//        // add badge
//        if (badge == null)
//            badge = new QBadgeView(this)
//                    .setBadgeNumber(number)
//                    .setGravityOffset(12, 2, true)
//                    .bindTarget(bnve.getBottomNavigationItemView(position))
//                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
//                        @Override
//                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
////                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
////                            Toast.makeText(BadgeViewActivity.this, R.string.tips_badge_removed, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        else
//            badge.setBadgeNumber(number);
//    }

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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Nhấn BACK 2 lần để thoát", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }

    @Override
    public void onMoreOptionCallback() {
        if (viewpager.getCurrentItem() == 0)
            ((TabChatFragment) adapter.getRegisteredFragment(viewpager.getCurrentItem())).deleteChat();
        else if (viewpager.getCurrentItem() == 1) {
            ((TabGroupFragment) adapter.getRegisteredFragment(viewpager.getCurrentItem())).deleteChat();
        }
    }

    @Override
    public void onCancleCallback() {

    }

    private void setListener(AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
//                showToast("clicked index:" + index, AppConstant.POSITIVE);
                switch (index) {
                    case 1:{
                        ((BaseFragment)adapter.getRegisteredFragment(viewpager.getCurrentItem())).doAction();
                        break;
                    }
                    case 3: {
                        viewpager.setCurrentItem(0);
                        break;
                    }
                    case 4: {
//                        viewpager.setCurrentItem(4);

                        break;
                    }
                    case 5: {
                        viewpager.setCurrentItem(1);
                        break;
                    }

                }
            }

            @Override
            public void onExpand() {
//                showToast("onExpand");
            }

            @Override
            public void onCollapse() {
//                showToast("onCollapse");
            }
        });
    }

    private void installButton90to180() {
        final AllAngleExpandableButton button = (AllAngleExpandableButton) findViewById(R.id.button_expandable_90_180);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.plus, R.drawable.ic_new_chat,R.drawable.ic_home_48, R.drawable.ic_chat_48, R.drawable.ic_phone_48, R.drawable.plane_white_48};
        int[] color = {R.color.dusty_orange,R.color.orange, R.color.dusk, R.color.color_view, R.color.blue, R.color.green};
        for (int i = 0; i < 6; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 15);
            } else {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            }
            buttonData.setBackgroundColorId(this, color[i]);
            buttonDatas.add(buttonData);
        }
        button.setButtonDatas(buttonDatas);

        setListener(button);
//        buttonDatas.get(1).setIcon(getDrawable(  R.drawable.ic_smile));
    }

    @Override
    public void onSucessAddFriend() {
        showToast("Đã thêm vào danh sách bạn bè ", AppConstant.POSITIVE, new SnackBarCallBack() {
            @Override
            public void onClosedSnackBar() {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                i.putExtra(AppConstant.MSG, userIdRequestFriend);
                i.putExtra("typeRoom", AppConstant.SINGLE);
                startActivityForResult(i, 1000);
                userIdRequestFriend = null;

            }
        });
    }

    @Override
    public void onSucessListContact(List<Profile> list) {

    }

    @Override
    public Context getMyContext() {
        return null;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hiddenProgress() {

    }

    @Override
    public void onErrorApi(String message) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onErrorAuthorization() {

    }
}
