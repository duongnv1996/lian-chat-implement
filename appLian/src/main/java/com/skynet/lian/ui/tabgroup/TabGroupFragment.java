package com.skynet.lian.ui.tabgroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.ChatItem;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.chatgroup.ChatGroupActivity;
import com.skynet.lian.ui.contact.ContactActivity;
import com.skynet.lian.ui.main.MainActivity;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class TabGroupFragment extends BaseFragment implements AdapterChatItem.CallBack,SwipeRefreshLayout.OnRefreshListener, ICallback,TabGoupContract.View
{

    @BindView(R2.id.imgHome)
    CircleImageView imgHome;
    @BindView(R2.id.imgStausToolbar)
    ImageView imgStausToolbar;
    @BindView(R2.id.tvNameToolbar)
    TextView tvNameToolbar;

    @BindView(R2.id.imgMore)
    ImageView imgMore;
    @BindView(R2.id.imgSearch)
    ImageView imgSearch;
    @BindView(R2.id.view)
    View view;

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
    @BindView(R2.id.layoutToolbar)
    ConstraintLayout layoutToolbar;
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.layoutSearch)
    ConstraintLayout layoutSearch;
    @BindView(R2.id.imgAddNewMessage)
    ImageButton imgAddNewMessage;
    ArrayList<String > data = new ArrayList<>();
    ListPopupWindow window;
    private List<ChatItem> chatItems;
    private AdapterChatItem adapterChatItem;
    private ChatItem itemChatEdit;
    private int posEdit;

    private TabGoupContract.Presenter presenter;
    private Timer timer;

    public static TabGroupFragment newInstance() {
        Bundle args = new Bundle();
        TabGroupFragment fragment = new TabGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void doAction() {

    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_tab_group;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);

        swipeTabChat.setOnRefreshListener(this);
        rcvChatItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvChatItem.setHasFixedSize(true);
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
    private void setupDialog() {
        data.add("Trò chyện mới");
        data.add("Tạo nhóm mới");
        data.add("Danh bạ");
        data.add("Thiết lập");
        data.add("Trang chủ");
        window = new ListPopupWindow(getContext());
        AdapterMenu adapter = new AdapterMenu(getContext(),
                R.layout.popup_menu_chat, data);
        /* use ur custom layout which has only TextView along with style required*/
        window.setAdapter(adapter);
        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
//window.getListView();
        window.setModal(false);
        window.setAnchorView(imgMore);/*it will be the overflow view of yours*/
        window.setHorizontalOffset(-430);
        window.setContentWidth(500);/* set width based on ur requirement*/
        window.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        Intent intent = new Intent(getActivity(), ContactActivity.class);
                        intent.putExtra(AppConstant.MSG,ContactActivity.SINGLE_MSG_CODE);
                        startActivityForResult(intent,1000);
                        break;
                    }  case 1: {
                        Intent intent = new Intent(getActivity(), ContactActivity.class);
                        intent.putExtra(AppConstant.MSG,ContactActivity.GROUP_MSG_CODE);
                        startActivityForResult(intent,1000);
                        break;
                    }
                    case 2: {
                        startActivity(new Intent(getActivity(), ContactActivity.class));
                        break;
                    } case 3: {
                        startActivity(new Intent(getActivity(), SettingActivity.class));
                        break;
                    }
                    case 4: {
                        // TODO: 1/3/19 start Main here
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void initVariables() {
        presenter = new TabGroupPresenter(this);
        chatItems = new ArrayList<>();
        setupDialog();
        adapterChatItem = new AdapterChatItem(chatItems, getContext(), this);
        rcvChatItem.setAdapter(adapterChatItem);
       Profile profile = AppController.getInstance().getmProfileUser();
        if (profile != null) {
            if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
                Picasso.with(getContext()).load(profile.getAvatar()).fit().centerCrop().into(imgHome);
            }
//            tvNameToolbar.setText("Hi, " + profile.getName());
//            tvStatusToolbar.setText(profile.getLast_status());
        }
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
                if(s.toString().isEmpty()) return;

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
        presenter.getListChat();

    }
//
//    @OnClick({R2.id.imgHome, R2.id.tvNameToolbar, R2.id.imgMore, R2.id.imgBack,
//            R2.id.imgSearch, R2.id.imgAddNewMessage})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R2.id.imgHome:
//                break;
//
//            case R2.id.tvNameToolbar:
//                break;
//
//            case R2.id.imgMore:
//                window.show();
//                break;
//            case R2.id.imgBack:
//                layoutToolbar.setVisibility(View.VISIBLE);
//                layoutSearch.setVisibility(View.INVISIBLE);
//                onRefresh();
//                KeyboardUtils.hideSoftInput(getActivity());
//
//                break;
//            case R2.id.imgSearch:
//                layoutToolbar.setVisibility(View.INVISIBLE);
//                layoutSearch.setVisibility(View.VISIBLE);
//                search.requestFocus();
//                KeyboardUtils.showSoftInput(search);
//                break;
//            case R2.id.imgAddNewMessage:
//                Intent intent = new Intent(getActivity(), ContactActivity.class);
//                intent.putExtra(AppConstant.MSG,ContactActivity.GROUP_MSG_CODE);
//                startActivityForResult(intent,1000);
//                break;
//        }
//    }
    public void deleteChat() {
        if (itemChatEdit != null && presenter != null) {
            presenter.deleteChat(itemChatEdit.getId());
            adapterChatItem.remove(posEdit);
        }
    }
    @Override
    public void onClickDelete(int pos, ChatItem item) {
        this.posEdit = pos;
        this.itemChatEdit = item;
        ((MainActivity) getActivity()).showDialogConfirm(R.drawable.ic_delete,
                Html.fromHtml(String.format(getString(R.string.confirm_delete),item.getTitle())),
                "Quay lại", "Xóa bỏ");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            onRefresh();
        }
    }
    @Override
    public void onClickCall(int pos, ChatItem item) {

    }

    @Override
    public void onClickVideo(int pos, ChatItem item) {

    }

    @Override
    public void onClickDetail(int pos, ChatItem item) {
        Intent i  = new Intent(getActivity(),ChatGroupActivity.class);
        i.putExtra(AppConstant.MSG,item.getId());
        startActivityForResult(i,1000);
    }

    @Override
    public void onCallBack(int pos) {

    }


    @Override
    public void onSucessListChat(List<ChatItem> list) {
        this.chatItems.clear();
        this.chatItems.addAll(list);
        adapterChatItem.notifyDataSetChanged();
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
        presenter.getListChat();
       // presenter.getListFriendOnline();
    }
}
