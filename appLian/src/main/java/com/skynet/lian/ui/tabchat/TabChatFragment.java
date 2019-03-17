package com.skynet.lian.ui.tabchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.socket.SocketConstants;
import com.skynet.lian.network.socket.SocketResponse;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.chatgroup.ChatGroupActivity;
import com.skynet.lian.ui.chatting.ChatActivity;
import com.skynet.lian.ui.contact.ContactActivity;
import com.skynet.lian.ui.main.MainActivity;
import com.skynet.lian.ui.makepost.MakePostActivity;
import com.skynet.lian.ui.profile.ProfileActivity;
import com.skynet.lian.ui.profileFriend.ProfileFriendActivity;
import com.skynet.lian.ui.setting.SettingActivity;
import com.skynet.lian.ui.views.AdapterMenu;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class TabChatFragment extends BaseFragment implements AdapterChatItem.CallBack, ICallback, TabChatContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener {

    @BindView(R2.id.imgHome)
    CircleImageView imgHome;
    @BindView(R2.id.imgStausToolbar)
    ImageView imgStausToolbar;
    @BindView(R2.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R2.id.tvStatusToolbar)
    TextView tvStatusToolbar;
    @BindView(R2.id.imgMore)
    ImageView imgMore;
    @BindView(R2.id.imgSearch)
    ImageView imgSearch;
    @BindView(R2.id.view)
    View view;
    @BindView(R2.id.textView6)
    TextView textView6;
    @BindView(R2.id.rcvFrientOnline)
    RecyclerView rcvFrientOnline;
    @BindView(R2.id.textView7)
    TextView textView7;
    @BindView(R2.id.view2)
    View view2;
    @BindView(R2.id.rcvChatItem)
    RecyclerView rcvChatItem;

    @BindView(R2.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R2.id.swipeTabChat)
    SwipeRefreshLayout swipeTabChat;
    //    @BindView(R2.id.layoutToolbar)
//    ConstraintLayout layoutToolbar;
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.layoutSearch)
    ConstraintLayout layoutSearch;
    @BindView(R2.id.layoutMenuChat)
    LinearLayout layoutMenuChat;
    ArrayList<String> data = new ArrayList<>();
    ListPopupWindow window;
    @BindView(R2.id.include)
    ConstraintLayout include;
    Unbinder unbinder;

    private List<ChatItem> chatItems;
    private AdapterChatItem adapterChatItem;
    private List<Profile> listFriend;
    private AdapterFriendOnline adapterFriend;
    private int posEdit;
    private ChatItem itemChatEdit;
    private TabChatContract.Presenter presenter;
    private Profile profile;
    private Timer timer;

    private ICallback callbackClickFriendOnline = new ICallback() {
        @Override
        public void onCallBack(int pos) {
            if (pos == 0) {
                Intent i = new Intent(getActivity(), MakePostActivity.class);
                startActivityForResult(i, 1000);
                return;
            }
            Intent i = new Intent(getActivity(), ProfileFriendActivity.class);
            i.putExtra(AppConstant.MSG, listFriend.get(pos).getId());
            startActivity(i);
        }
    };

    public static TabChatFragment newInstance() {
        Bundle args = new Bundle();
        TabChatFragment fragment = new TabChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

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

    @Override
    public void doAction() {
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra(AppConstant.MSG, ContactActivity.SINGLE_MSG_CODE);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketConstants.SOCKET_CHAT);
        getMyContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getMyContext().unregisterReceiver(receiver);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_tab_chat;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        swipeTabChat.setOnRefreshListener(this);
        rcvChatItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvChatItem.setHasFixedSize(true);
        rcvFrientOnline.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rcvFrientOnline.setHasFixedSize(true);
        rcvChatItem.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        rcvChatItem.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                    yourViewPager.setPagerEnabled(false);
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    yourViewPager.setPagerEnabled(true);
//                }
//                return true;
//            }
//        });
    }

    @Override
    protected void initVariables() {
        presenter = new TabChatPresenter(this);
        chatItems = new ArrayList<>();
        setupDialog();
        adapterChatItem = new AdapterChatItem(chatItems, getContext(), this);
        rcvChatItem.setAdapter(adapterChatItem);
        listFriend = new ArrayList<>();
        adapterFriend = new AdapterFriendOnline(listFriend, getContext(), callbackClickFriendOnline);
        rcvFrientOnline.setAdapter(adapterFriend);
        presenter.getListChat();
        presenter.getListFriendOnline();
        bindUiProfile();
        search.addTextChangedListener(new TextWatcher() {
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
                if (s.toString().isEmpty()) return;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatItems.clear();
                                adapterChatItem.notifyDataSetChanged();
                                presenter.getListChat(s.toString());
                            }
                        });
                    }
                }, 600);
            }
        });

        swipeTabChat.setOnTouchListener(this);
        rcvChatItem.setOnTouchListener(this);
        rcvFrientOnline.setOnTouchListener(this);
    }

    private void setupDialog() {
        data.add("Trò chyện mới");
        data.add("Tạo nhóm mới");
        data.add("Danh bạ");
        data.add("Thiết lập");
        window = new ListPopupWindow(getContext());
        AdapterMenu adapter = new AdapterMenu(getContext(),
                R.layout.popup_menu_chat, data);
        /* use ur custom layout which has only TextView along with style required*/
        window.setAdapter(adapter);
        View view = LayoutInflater.from(getMyContext()).inflate(R.layout.popup_menu_chat, null, false);
        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setHeight(1);
//        window.setWidth(1);
//window.getListView();
        window.setModal(false);
        window.setAnchorView(imgMore);/*it will be the overflow view of yours*/
        window.setHorizontalOffset(-430);
        window.setContentWidth(600);/* set width based on ur requirement*/
        window.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        showAtLocation((View) parent.getWindowToken(), gravity, x, y);
    }

//    @OnClick({R2.id.imgHome, R2.id.tvNameToolbar, R2.id.imgMore, R2.id.imgBack, R2.id.imgSearch, R2.id.imgAddNewMessage})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R2.id.imgHome:
//                startActivityForResult(new Intent(getActivity(), ProfileActivity.class), 100);
//                break;
//
//            case R2.id.tvNameToolbar:
//                break;
//
//            case R2.id.imgMore:
//                if (layoutMenuChat.getVisibility() == View.VISIBLE) {
//                    layoutMenuChat.setVisibility(View.GONE);
//
//                } else {
//                    layoutMenuChat.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R2.id.imgBack:
////                layoutToolbar.setVisibility(View.VISIBLE);
//                layoutSearch.setVisibility(View.INVISIBLE);
//                onRefresh();
//                KeyboardUtils.hideSoftInput(getActivity());
//                break;
//            case R2.id.imgSearch:
////                layoutToolbar.setVisibility(View.INVISIBLE);
//                layoutSearch.setVisibility(View.VISIBLE);
//                layoutMenuChat.setVisibility(View.GONE);
//                search.requestFocus();
//                KeyboardUtils.showSoftInput(search);
//                break;
//            case R2.id.imgAddNewMessage:
//                Intent intent = new Intent(getActivity(), ContactActivity.class);
//                intent.putExtra(AppConstant.MSG, ContactActivity.SINGLE_MSG_CODE);
//                startActivityForResult(intent, 1000);
//                break;
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {
            bindUiProfile();
        }
        if (resultCode == getActivity().RESULT_OK) {
            onRefresh();
        }
    }

    private void bindUiProfile() {
        profile = AppController.getInstance().getmProfileUser();
        if (profile != null) {
            if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
                Picasso.with(getContext()).load(profile.getAvatar()).fit().centerCrop().into(imgHome);
            }
            tvNameToolbar.setText("Hi, " + profile.getName());
            tvStatusToolbar.setText(profile.getLast_status());
            if (profile.getOnline() == 0) {
                imgStausToolbar.setImageResource(R.drawable.dot_gray_stock);
            } else {
                imgStausToolbar.setImageResource(R.drawable.dot_green_stock);
            }
        }
    }

    @Override
    public void onClickDelete(int pos, ChatItem item) {
        this.posEdit = pos;
        this.itemChatEdit = item;
        ((MainActivity) getActivity()).showDialogConfirm(R.drawable.ic_delete,
                Html.fromHtml(String.format(getString(R.string.confirm_delete), item.getUser_name() == null ? item.getTitle() : item.getUser_name())),
                "Quay lại", "Xóa bỏ");
    }

    @Override
    public void onClickCall(int pos, ChatItem item) {

    }

    @Override
    public void onClickVideo(int pos, ChatItem item) {

    }

    @Override
    public void onClickViewProfile(int pos, ChatItem item) {
        if (item.getType() != 1) return; //todo filter single chat
        Intent i = new Intent(getActivity(), ProfileFriendActivity.class);
        i.putExtra(AppConstant.MSG, item.getUser_id_send());
        startActivity(i);
    }

    public void deleteChat() {
        if (itemChatEdit != null && presenter != null) {
            presenter.deleteChat(itemChatEdit.getId());
            adapterChatItem.remove(posEdit);
        }
    }

    @Override
    public void onClickDetail(int pos, ChatItem item) {
        Intent i;
        if (item.getType() == 1) {
            i = new Intent(getActivity(), ChatActivity.class);
            i.putExtra(AppConstant.MSG, item.getUser_id_send());
        } else {
            i = new Intent(getActivity(), ChatGroupActivity.class);
            i.putExtra(AppConstant.MSG, item.getId());

        }
        i.putExtra("typeRoom", AppConstant.SINGLE);
        startActivityForResult(i, 1000);
    }

    @Override
    public void onCallBack(int pos) {
        if (pos == 0) return; // dang trang thai
        Intent i = new Intent(getActivity(), ChatActivity.class);
        i.putExtra(AppConstant.MSG, listFriend.get(pos).getId());
        i.putExtra("typeRoom", AppConstant.SINGLE);
        startActivityForResult(i, 1000);
    }

    @Override
    public void onSucessListFriendOnline(List<Profile> list) {
        list.add(0, AppController.getInstance().getmProfileUser());
        this.listFriend.clear();
        listFriend.addAll(list);
        adapterFriend.notifyDataSetChanged();
    }

    @Override
    public void onSucessListChat(List<ChatItem> list) {
        this.chatItems.clear();
        this.chatItems.addAll(list);
        adapterChatItem.notiDateChange();
    }

    @Override
    public Context getMyContext() {
        return getContext();
    }

    @Override
    public void showProgress() {
        swipeTabChat.setRefreshing(true);
    }

    @Override
    public void hiddenProgress() {
        swipeTabChat.setRefreshing(false);
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

    @Override
    public void onRefresh() {
        bindUiProfile();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                presenter.getListChat();
                presenter.getListFriendOnline();
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        layoutMenuChat.setVisibility(View.GONE);
        return false;
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

    @OnClick(R2.id.imgHome)
    public void onImgHomeClicked() {
        startActivityForResult(new Intent(getActivity(), ProfileActivity.class), 100);

    }

    @OnClick(R2.id.tvNameToolbar)
    public void onTvNameToolbarClicked() {
    }

    @OnClick(R2.id.imgMore)
    public void onImgMoreClicked() {
        if (layoutMenuChat.getVisibility() == View.VISIBLE) {
            layoutMenuChat.setVisibility(View.GONE);

        } else {
            layoutMenuChat.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R2.id.imgSearch)
    public void onImgSearchClicked() {
        layoutSearch.setVisibility(View.VISIBLE);
        layoutMenuChat.setVisibility(View.GONE);
        search.requestFocus();
        KeyboardUtils.showSoftInput(search);
    }

    @OnClick(R2.id.imgBack)
    public void onImgBackClicked() {
        layoutSearch.setVisibility(View.INVISIBLE);
        onRefresh();
        KeyboardUtils.hideSoftInput(getActivity());
    }

    @OnClick(R2.id.imgAddNewMessage)
    public void onImgAddNewMessageClicked() {
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra(AppConstant.MSG, ContactActivity.SINGLE_MSG_CODE);
        startActivityForResult(intent, 1000);
        layoutMenuChat.setVisibility(View.GONE);

    }

    @OnClick(R2.id.menu_newchat)
    public void onMenuNewchatClicked() {
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra(AppConstant.MSG, ContactActivity.SINGLE_MSG_CODE);
        startActivityForResult(intent, 1000);
        layoutMenuChat.setVisibility(View.GONE);

    }

    @OnClick(R2.id.menu_new_group_chat)
    public void onMenuNewGroupChatClicked() {
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra(AppConstant.MSG, ContactActivity.GROUP_MSG_CODE);
        startActivityForResult(intent, 1000);
        layoutMenuChat.setVisibility(View.GONE);

    }

    @OnClick(R2.id.menu_contact)
    public void onMenuContactClicked() {
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra(AppConstant.MSG, ContactActivity.VIEW_CONTACT);
        startActivityForResult(intent, 1000);
        layoutMenuChat.setVisibility(View.GONE);

    }

    @OnClick(R2.id.menu_setting)
    public void onMenuSettingClicked() {
        startActivityForResult(new Intent(getActivity(), SettingActivity.class), 1000);

    }
//
//    @OnClick({R2.id.menu_newchat, R2.id.menu_new_group_chat, R2.id.menu_contact, R2.id.menu_setting})
//    public void onViewMenuClicked(View view) {
//        switch (view.getId()) {
//            case R2.id.menu_newchat: {
//                Intent intent = new Intent(getActivity(), ContactActivity.class);
//                intent.putExtra(AppConstant.MSG, ContactActivity.SINGLE_MSG_CODE);
//                startActivityForResult(intent, 1000);
//                break;
//            }
//            case R2.id.menu_new_group_chat: {
//                Intent intent = new Intent(getActivity(), ContactActivity.class);
//                intent.putExtra(AppConstant.MSG, ContactActivity.GROUP_MSG_CODE);
//                startActivityForResult(intent, 1000);
//                break;
//            }
//            case R2.id.menu_contact: {
//                Intent intent = new Intent(getActivity(), ContactActivity.class);
//                intent.putExtra(AppConstant.MSG, ContactActivity.VIEW_CONTACT);
//                startActivityForResult(intent, 1000);
//                break;
//            }
//            case R2.id.menu_setting: {
//                startActivityForResult(new Intent(getActivity(), SettingActivity.class), 1000);
//                break;
//            }
//
//        }
//        layoutMenuChat.setVisibility(View.GONE);
//    }

}
