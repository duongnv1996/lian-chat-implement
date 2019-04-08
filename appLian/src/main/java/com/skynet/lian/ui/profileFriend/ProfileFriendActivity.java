package com.skynet.lian.ui.profileFriend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Timeline;
import com.skynet.lian.network.socket.SocketResponse;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.chatting.ChatActivity;
import com.skynet.lian.ui.detailpost.DetailPostActivity;
import com.skynet.lian.ui.main.OptionBottomSheet;
import com.skynet.lian.ui.profile.AdapterProfileViewpager;
import com.skynet.lian.ui.profile.ProfileActivity;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.ui.views.ViewpagerNotSwipe;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

//import android.support.v7.widget.Toolbar;

public class ProfileFriendActivity extends BaseActivity implements ProfileFriendContract.View, ICallback, XRecyclerView.LoadingListener {

    @BindView(R2.id.imgCover)
    ImageView imgCover;
    @BindView(R2.id.tvFollow)
    TextView tvFollow;
    @BindView(R2.id.tvName)
    TextView tvName;
    @BindView(R2.id.imgAvatar)
    CircleImageView imgAvatar;
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.tvTitle)
    TextView tvTitle;
    @BindView(R2.id.imgVideo)
    ImageView imgVideo;
    @BindView(R2.id.imgChat)
    ImageView imgChat;
    @BindView(R2.id.imgCall)
    ImageView imgCall;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.appBarLayout3)
    AppBarLayout appBarLayout3;
    @BindView(R2.id.tvFriend)
    TextView tvFriend;
    @BindView(R2.id.tvAddress)
    TextView tvAddress;
    @BindView(R2.id.tvMakeFriendRQ)
    TextView tvMakeFriendRQ;
    @BindView(R2.id.rcv)
    XRecyclerView rcv;
    private ProfileFriendContract.Presenter presenter;
    private ProgressDialogCustom dialogLoading;
    private AdapterPhotoFriend adapterGrid;
    private static int TYPE_LOADMORE = 1;
    private static int TYPE_REFREESH = 0;
    private int index = 0;
    private int requestType;
    Profile profileFriend;
    List<Image> list;
    @BindView(R2.id.viewpager)
    ViewpagerNotSwipe viewpager;
    @BindView(R2.id.bottomNavigationViewEx)
    BottomNavigationViewEx bnve;
    private AdapterProfileViewpager adapter;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("OnReceive from UI ");
            if (intent != null) {
                SocketResponse data = new Gson().fromJson(intent.getExtras().getString(AppConstant.MSG), SocketResponse.class);
                if (data != null) {
                    onRefresh();
                }
            }
        }
    };
    private String idFriend;
    private boolean isFirtShow = true;
    private OptionBottomSheet.MoreOptionCallback addFriendCallback = new OptionBottomSheet.MoreOptionCallback() {
        @Override
        public void onMoreOptionCallback() {
            presenter.addFriend(profileFriend.getId(), 1);
            profileFriend.setIs_friend(2);
        }

        @Override
        public void onCancleCallback() {
            presenter.addFriend(profileFriend.getId(), 2);
            profileFriend.setIs_friend(0);
            onSucessGetInfo(profileFriend);
        }
    };
    private OptionBottomSheet bottomAddFriendRequest;

    @Override
    protected int initLayout() {
        return R.layout.activity_profile_friend;
    }

    @Override
    protected void initVariables() {
        bottomAddFriendRequest = new OptionBottomSheet(this, addFriendCallback);
        idFriend = getIntent().getStringExtra(AppConstant.MSG);
        if (idFriend == null) return;
        if (idFriend.equals(AppController.getInstance().getmProfileUser().getId())) {
            startActivity(new Intent(ProfileFriendActivity.this, ProfileActivity.class));
            finish();
        }
        list = new ArrayList<>();
        adapterGrid = new AdapterPhotoFriend(list, this, this);
        rcv.setAdapter(adapterGrid);
        dialogLoading = new ProgressDialogCustom(this);
        presenter = new ProfileFriendPresenter(this);
//        profileFriend = AppController.getInstance().getmProfileUser();
        rcv.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rcv.setRefreshProgressStyle(ProgressStyle.BallPulse);
        rcv.setLimitNumberToCallLoadMore(10);
        rcv.setLoadingMoreEnabled(true);
        rcv.setPullRefreshEnabled(true);
        rcv.setLoadingListener(this);

        onRefresh();

    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);

        rcv.setLayoutManager(new GridLayoutManager(this, 3));
        rcv.setHasFixedSize(true);

        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.enableItemShiftingMode(false);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Text-Medium.otf");
        bnve.setTypeface(custom_font);

    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }


    @OnClick(R2.id.imgBack)
    public void onViewimgBackClicked() {

                onBackPressed();

    }
@OnClick(R2.id.imgChat)
    public void onViewimgChatClicked() {

                Intent i;
                i = new Intent(ProfileFriendActivity.this, ChatActivity.class);
                i.putExtra(AppConstant.MSG, profileFriend.getId());
                i.putExtra("typeRoom", AppConstant.SINGLE);
                startActivityForResult(i, 1000);
    }


    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void showProgress() {
        if (isFirtShow) {
            dialogLoading.showDialog();
            isFirtShow = false;
        }
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
    public void onSucessGetTimeline(Timeline timeline) {
        if (timeline != null) {
            if (requestType == TYPE_REFREESH) {
                this.list.clear();

            }
            if (timeline.getList_image() != null && !timeline.getList_image().isEmpty()) {
                this.list.addAll(timeline.getList_image());
                if (requestType == TYPE_LOADMORE) {
                    int pos = this.list.size() + 1;
                    adapterGrid.refreshCache();
                    adapterGrid.notifyItemRangeInserted(pos, timeline.getList_image().size() - 1);
                } else
                    adapterGrid.refreshCache();
                adapterGrid.notifyDataSetChanged();
            } else {
                rcv.setNoMore(true);
            }
        }
//        adapterGrid.notifyItemInserted(this.index);
        this.index = timeline.getIndex();
        rcv.loadMoreComplete();
        rcv.refreshComplete();
    }

    @Override
    public void onSucessGetInfo(Profile profile) {
        this.profileFriend = profile;
        if (profile != null) {
            adapter = new AdapterProfileViewpager(getSupportFragmentManager(),idFriend,profileFriend);
            viewpager.setAdapter(adapter);
            bnve.setupWithViewPager(viewpager);
            viewpager.setPagingEnabled(true);
            viewpager.setCurrentItem(0);
            tvName.setText(profile.getName());
            tvFriend.setText(profile.getMutual_friend() + " bạn chung");
            tvAddress.setText(profile.getAddress());
            if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
                Picasso.with(this).load(profile.getAvatar()).fit().centerCrop().into(imgAvatar);
            }
            if (profile.getCover() != null && !profile.getCover().isEmpty()) {
                Picasso.with(this).load(profile.getCover()).fit().centerCrop().into(imgCover);
            } else {
                Picasso.with(this).load(R.drawable.background).fit().centerCrop().into(imgCover);
            }
            if (profile.getIs_following() == 1) {
                tvFollow.setText("Đang theo dõi");
            } else {
                tvFollow.setText("Theo dõi");
            }
            if (profile.getIs_friend() == 2) {
                tvMakeFriendRQ.setText("Huỷ kết bạn");
                tvMakeFriendRQ.setBackgroundResource(R.drawable.bg_round_profile_orange);
                tvMakeFriendRQ.setEnabled(true);
                tvMakeFriendRQ.setTextColor(ContextCompat.getColor(this, R.color.orange));
            } else if (profile.getIs_friend() == 0) {
                tvMakeFriendRQ.setText("Kết bạn");
                tvMakeFriendRQ.setBackgroundResource(R.drawable.bg_round_profile_orange);
                tvMakeFriendRQ.setEnabled(true);
                tvMakeFriendRQ.setTextColor(ContextCompat.getColor(this, R.color.orange));
            } else {
                tvMakeFriendRQ.setText("Đang chờ");
                tvMakeFriendRQ.setEnabled(false);
                tvMakeFriendRQ.setTextColor(ContextCompat.getColor(this, R.color.gray));
                tvMakeFriendRQ.setBackgroundResource(R.drawable.bg_round_profile_orange_gray);
            }
            if (profile.getUserIdRequestFriend().equals(profile.getId()) && profile.getIs_friend() == 1) {
                bottomAddFriendRequest.setData(R.drawable.ic_mail,
                        Html.fromHtml(String.format(getString(R.string.confirm_add), profile.getName())),
                        "Huỷ", "Đồng ý ");
                bottomAddFriendRequest.show();
            }
        }
    }

    @Override
    public void onCallBack(int pos) {
        Intent i = new Intent(ProfileFriendActivity.this, DetailPostActivity.class);
        i.putExtra(AppConstant.MSG, list.get(pos).getPostID());
        startActivityForResult(i, 1000);
    }

    @Override
    public void onRefresh() {
        index = 0;
        requestType = TYPE_REFREESH;
        presenter.getInfo(idFriend);
        presenter.getTimeline(idFriend, index, AppConstant.TYPE_IMAGE);
    }

    @Override
    public void onLoadMore() {
        requestType = TYPE_LOADMORE;
        presenter.getTimeline(idFriend, index, AppConstant.TYPE_IMAGE);
    }

    @OnClick(R2.id.tvFollow)
    public void onViewtvFollowClicked() {
        if (profileFriend == null) return;
                if (profileFriend.getIs_following() == 1) {
                    profileFriend.setIs_following(2);
                    tvFollow.setText("Theo dõi");
                } else {
                    profileFriend.setIs_following(1);
                    tvFollow.setText("Đang theo dõi");
                }
                presenter.toggleFollow(profileFriend.getId(), profileFriend.getIs_following());

    }
    @OnClick( R2.id.tvMakeFriendRQ)
    public void onViewtvMakeFriendRQClicked() {
        if (profileFriend == null) return;
                presenter.addFriend(profileFriend.getId(), profileFriend.getIs_friend());
                //todo update text after unfriend or addfriend
                if (profileFriend.getIs_friend() == 2) {

                    tvMakeFriendRQ.setText("Kết bạn");
                    tvMakeFriendRQ.setBackgroundResource(R.drawable.bg_round_profile_orange);
                    tvMakeFriendRQ.setEnabled(true);
                    tvMakeFriendRQ.setTextColor(ContextCompat.getColor(this, R.color.orange));
                } else if (profileFriend.getIs_friend() == 0) {
                    tvMakeFriendRQ.setText("Đang chờ");
                    tvMakeFriendRQ.setEnabled(false);
                    tvMakeFriendRQ.setTextColor(ContextCompat.getColor(this, R.color.gray));
                    tvMakeFriendRQ.setBackgroundResource(R.drawable.bg_round_profile_orange_gray);
                } else {
                    tvMakeFriendRQ.setText("Huỷ kết bạn");
                    tvMakeFriendRQ.setBackgroundResource(R.drawable.bg_round_profile_orange);
                    tvMakeFriendRQ.setEnabled(true);
                    tvMakeFriendRQ.setTextColor(ContextCompat.getColor(this, R.color.orange));


                }
//                tvMakeFriendRQ.setBackgroundResource(R.drawable.bg_round_profile);
//                tvMakeFriendRQ.setText("Đang chờ");
                setResult(RESULT_OK);

    }
}
