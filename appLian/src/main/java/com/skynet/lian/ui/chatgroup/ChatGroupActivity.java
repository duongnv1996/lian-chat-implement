package com.skynet.lian.ui.chatgroup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Message;
import com.skynet.lian.models.OnLoadMoreListener;
import com.skynet.lian.models.Profile;
import com.skynet.lian.models.Room;
import com.skynet.lian.models.Seen;
import com.skynet.lian.network.socket.SocketConstants;
import com.skynet.lian.network.socket.SocketResponse;
import com.skynet.lian.ui.DownloadService;
import com.skynet.lian.ui.chatgroup.editgroup.EditGroupActivity;
import com.skynet.lian.ui.chatting.ChatActivity;
import com.skynet.lian.ui.profileFriend.ProfileFriendActivity;
import com.skynet.lian.ui.viewphoto.ViewPhotoActivity;
import com.skynet.lian.ui.views.ChatParentLayout;
import com.skynet.lian.utils.AppConstant;
import com.skynet.lian.utils.CommomUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImage;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import br.com.instachat.emojilibrary.model.layout.EmojiEditText;
import br.com.instachat.emojilibrary.model.layout.EmojiKeyboardLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.iwf.photopicker.PhotoPicker;


public class ChatGroupActivity extends BaseEmojiActivity implements ChattingContract.View, SwipeRefreshLayout.OnRefreshListener, ICallback,MediaPlayer.OnCompletionListener
{
    private static final int TYPE_TO = 2;
    @BindView(R2.id.rcv_chat)
    RecyclerView mRcv;
//    @BindView(R2.id.tvTitle_toolbar)
//    TextView textToolbar;

    @BindView(R2.id.message_txt)
    EmojiEditText mMessage;

    @BindView(R2.id.row_chat_ll)
    ChatParentLayout mChatLL;
    @BindView(R2.id.imageView3)
    ImageView imageView3;
    @BindView(R2.id.img_smile)
    ImageView imgSwitchKeyboard;
    @BindView(R2.id.circleImageView2)
    CircleImageView imgAvatar;
    @BindView(R2.id.imageView4)
    ImageView imgStatus;
    @BindView(R2.id.textView3)
    TextView tvTitle;
    @BindView(R2.id.btnChoosePhoto)
    FloatingActionButton btnChoosePhoto;
    @BindView(R2.id.imageView6)
    ImageView imageView6;
    @BindView(R2.id.imageView8)
    ImageView imageView8;
    @BindView(R2.id.expandableLayout2)
    ExpandableLayout expandableLayout2;
    //    @BindView(R2.id.expandEmoji)
//    ExpandableLayout expandEmoji;
    @BindView(R2.id.send_imv)
    ImageView sendImv;
    @BindView(R2.id.bottom_chat_layout)
    ConstraintLayout bottomChatLayout;
    @BindView(R2.id.layoutAvt)
    ConstraintLayout layoutAvt;
    @BindView(R2.id.emojiKeyboardLayout)
    EmojiKeyboardLayout emojiKeyboardLayout;
    AppBarLayout appBar;
    @BindView(R2.id.rcvPhoto)
    RecyclerView rcvPhoto;
    @BindView(R2.id.mute)
    CheckBox mute;
    @BindView(R2.id.expandKeyboardLayout)
    ExpandableLayout expandKeyboardLayout;
    @BindView(R2.id.layoutKeyboard)
    ConstraintLayout layoutKeyboard;
    private ChattingContract.Presenter presenter;
    private AdapterChat mAdapterChat;
    private List<Message> mList = new ArrayList<>();
    private int indexList = 0;
    private PhotoAdapter adapterPhoto;
    List<String> listpathPhoto;
    private int chatRoomId;

    private Room room;
    private String filename;
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    private String urlStreaming = "";
    private int oldPostPlaying=-1;
    private com.skynet.lian.ui.chatting.AdapterChat.CallBackChat callBackClickMessage = new com.skynet.lian.ui.chatting.AdapterChat.CallBackChat() {

        @Override
        public void onClickAvatar(int pos, String idProfile) {
            Intent i = new Intent(ChatGroupActivity.this,ProfileFriendActivity.class);
            i.putExtra(AppConstant.MSG,idProfile);
            startActivity(i);
        }


        @Override
        public void onCallBack(int pos) {
            if (mList.get(pos).getImage() != null && !mList.get(pos).getImage().isEmpty()) {
                Intent intent = new Intent(ChatGroupActivity.this, ViewPhotoActivity.class);
                intent.putExtra(AppConstant.MSG, mList.get(pos).getImage());
                startActivity(intent);
                return;
            }
            String url = mList.get(pos).getFile();
            if (!url.contains("http")) return;
            if (url.substring(url.lastIndexOf('/') + 1, url.length()).contains(".wav") ||url.substring(url.lastIndexOf('/') + 1, url.length()).contains(".m4a")) {
                if (!url.equals(urlStreaming)) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause = false;
                    initialStage = true;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }

                if (!playPause) {
                    mList.get(pos).setPlaying(true);
                    mAdapterChat.notifyItemChanged(pos);
                    oldPostPlaying = pos;
                    if (initialStage) {
                        new ChatGroupActivity.Player().execute(url);
                    } else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();

                        }
                    }
                    playPause = true;
                    urlStreaming = url;

                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    playPause = false;
                }
                return;
            }
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
            showToast("Downloading....",AppConstant.POSITIVE);
            startService(DownloadService.getDownloadService(ChatGroupActivity.this, url, Environment.DIRECTORY_DOWNLOADS+ "/lian/" ));
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("OnReceive from UI ");
            if (intent != null) {
                SocketResponse data = new Gson().fromJson(intent.getExtras().getString(AppConstant.MSG), SocketResponse.class);
                if (data != null && data.getIdRoom() != null && data.getIdRoom().equals(chatRoomId + "")) {
                    indexList = 0;
                    presenter.getRoomInfo(chatRoomId + "", indexList, false);
                }
            }
        }
    };

    private String oldIdMessageSeen = "";
    private BroadcastReceiver receiverSeen = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("OnReceive seen from UI ");
            if (intent != null) {
                Seen data = new Gson().fromJson(intent.getExtras().getString(AppConstant.MSG), Seen.class);
                if (data != null && data.getRoom_id() != null && data.getRoom_id().equals(chatRoomId + "") && mList != null && !mList.isEmpty()) {
                    int index = -1;
                    int oldIndex = -1;

                    for (int i = 0; i < mList.size(); i++) {

                        if (!oldIdMessageSeen.isEmpty() && mList.get(i).getId().equals(oldIdMessageSeen)) {
                            oldIndex = i;
                            if (index != -1) break;
                        }
                        if (data.getMessage_id().equals(mList.get(i).getId())) {
                            index = i;
                            if (oldIndex != -1) break;
                        }
                    }
                    if (oldIndex != -1 && oldIndex != index) {
                        mList.get(oldIndex).setTime(mList.get(oldIndex).getTime().replace("Đã xem", ""));
                        mAdapterChat.notifyItemChanged(oldIndex);
                    }
                    mList.get(index).setTime("Đã xem " + data.getTime());
                    mAdapterChat.notifyItemChanged(index);
                    oldIdMessageSeen = data.getMessage_id();

                }
            }
        }
    };

    private boolean isRecording = false;
//    private ICallback callBackClickMessage = new ICallback() {
//        @Override
//        public void onCallBack(int pos) {
//            if (mList.get(pos).getImage() != null && !mList.get(pos).getImage().isEmpty()) {
//                Intent intent = new Intent(ChatGroupActivity.this, ViewPhotoActivity.class);
//                intent.putExtra(AppConstant.MSG, mList.get(pos).getImage());
//                startActivity(intent);
//                return;
//            }
//            String url = mList.get(pos).getFile();
//            if (!URLUtil.isHttpUrl(url)) return;
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        }
//    };

    @Override
    protected int initLayout() {
        return R.layout.activity_chatting_group;
    }


    @Override
    protected void initVariables() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        mMessage.setEmojiSize(48);
        emojiKeyboardLayout.prepareKeyboard(this, mMessage);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
//        layoutParams.setReverseLayout(true);
//        layoutParams.setStackFromEnd(true);
        mRcv.setLayoutManager(layoutParams);
//        mRcv.setPullRefreshEnabled(false);
//        mRcv.setLoadingMoreEnabled(false);

        mAdapterChat = new AdapterChat(mList, ChatGroupActivity.this, mRcv, callBackClickMessage);
        mRcv.setAdapter(mAdapterChat);
        mRcv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyboardUtils.hideSoftInput(ChatGroupActivity.this);
                expandableLayout2.expand();
                emojiKeyboardLayout.setVisibility(View.GONE);
                imgSwitchKeyboard.setImageResource(R.drawable.ic_smile);
                return false;
            }
        });
        presenter = new ChattingPresenter(this);
        chatRoomId = Integer.parseInt(getIntent().getStringExtra(AppConstant.MSG));
        presenter.getRoomInfo(chatRoomId + "", indexList, false);
        mChatLL.setOnKeyBoardChangeListener(new ChatParentLayout.OnKeyBoardChangeListener() {
            @Override
            public void onShow(int keyboardHeight) {
                expandKeyboardLayout.collapse();
                try {
                    if (mAdapterChat.getItemCount() > 0)
                        mRcv.scrollToPosition(0);
                } catch (Exception e) {

                }
                expandableLayout2.collapse();
                layoutAvt.setVisibility(View.GONE);
                emojiKeyboardLayout.setVisibility(View.GONE);
//                appBar.setExpanded(false,false);
            }

            @Override
            public void onHide(int keyboardHeight) {
                try {
                    if (mAdapterChat.getItemCount() > 0)
                        mRcv.scrollToPosition(0);
                } catch (Exception e) {
                }
                expandableLayout2.expand();
                layoutAvt.setVisibility(View.VISIBLE);
            }
        });

        mMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                expandableLayout2.collapse();
            }
        });


        mAdapterChat.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                mList.add(null);
                mAdapterChat.notifyItemInserted(mList.size() - 1);
                onRefresh();
            }
        });

        expandKeyboardLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                if (state == ExpandableLayout.State.EXPANDING) {
                    KeyboardUtils.hideSoftInput(ChatGroupActivity.this);
                }
            }
        });

    }


    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisiblesItems;
    private boolean loading = true;


    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        appBar = findViewById(R.id.appBarLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true);
        layoutManager.setStackFromEnd(true);
        rcvPhoto.setLayoutManager(layoutManager);


        rcvPhoto.setHasFixedSize(true);
    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }


    //
    @OnClick({R2.id.send_imv})
    public void onClicked() {
        String content = mMessage.getText().toString();

        if (content.isEmpty() || content.replaceAll(" ","").isEmpty()) {
            Toast.makeText(this, "Bạn phải nhập tin nhắn", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            content = URLEncoder.encode(content, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Message message = new Message();
        message.setUser_id(AppController.getInstance().getmProfileUser().getId());
        message.setContent(content);
        message.setAvatar(AppController.getInstance().getmProfileUser().getAvatar());
        mMessage.setText("");
        mList.add(0, message);
        String listUserId = "";
        if (room != null) {
            for (Profile pr : room.getListUser()) {
                listUserId += pr.getId() + ",";
            }
        }
        if (!listUserId.isEmpty()) {
            listUserId = listUserId.substring(0, listUserId.length() - 1) +","+AppController.getInstance().getmProfileUser().getId();
        }
        presenter.sendMessage(chatRoomId + "", content, getmSocket(), 0,listUserId);
        mAdapterChat.notifyItemInserted(0);
//        mRcv.setAdapter(mAdapterChat);
        if (mAdapterChat.getItemCount() > 0)
            mRcv.smoothScrollToPosition(0);
        setResult(RESULT_OK);
    }


    @Override
    protected void onDestroy() {

        KeyboardUtils.hideSoftInput(this);
        presenter.onDestroyView();
        super.onDestroy();
        AppConstant.ID_CHAT = "0";
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppConstant.ID_CHAT = "0";
        unregisterReceiver(receiverSeen);

        initialStage = true;
        playPause = false;
        //  btn.setText("Launch Streaming");
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
        LogUtils.e("unregister receiver");
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketConstants.SOCKET_CHAT);
        registerReceiver(receiver, intentFilter);
        LogUtils.e("register receiver");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilterSeen = new IntentFilter();
        intentFilterSeen.addAction(SocketConstants.SOCKET_SEEN_CHAT);
        registerReceiver(receiverSeen, intentFilterSeen);
        LogUtils.e("register receiver seen");

    }
    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        if (getmSocket() != null) {
            getmSocket().joinRoom(chatRoomId + "");
            if (mList != null && mList.size() > 0) {
                Message message = mList.get(0);
                getmSocket().seenMessage(AppController.getInstance().getmProfileUser().getId(), chatRoomId + "", message.getId(), AppController.getInstance().getmProfileUser().getName());
            }
        }
    }


    @Override
    public void onSuccessgetRoomInfo(List<Message> list, Profile user1, Profile user2, int index, Room room) {
        this.room = room;
        this.chatRoomId = room.getRoomInfo();
        if (getmSocket() != null){
            getmSocket().joinRoom(chatRoomId + "");
            if (list != null && list.size() > 0) {
                Message message = list.get(0);
                getmSocket().seenMessage(AppController.getInstance().getmProfileUser().getId(), chatRoomId + "", message.getId(), AppController.getInstance().getmProfileUser().getName());
            }
        }
        setResult(RESULT_OK);
        this.indexList = index;
        mList.clear();
//        mList.addAll(list);
        mAdapterChat.addList(list);
        mAdapterChat.setLoad(true);
        mRcv.setAdapter(mAdapterChat);
//        mAdapterChat.notifyDataSetChanged();
//        mAdapterChat.notifyItemInserted(mAdapterChat.getItemCount() - 1);
//        if (mAdapterChat.getItemCount() > 0)
//            mRcv.smoothScrollToPosition(mAdapterChat.getItemCount());
        tvTitle.setText(room.getTitle());
        mute.setChecked(room.getIs_mute() == 0 ? true : false);
        mute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.toggle(chatRoomId + "", isChecked);
            }
        });
    }

    @Override
    public void onSuccessgetLoadmoreChat(List<Message> list, int indexList) {
        mAdapterChat.setLoad(true);
        loading = true;
        int posInsert = mList.size() - 1;
        mList.remove(posInsert);
        mAdapterChat.notifyItemRemoved(posInsert);
        if (list.size() == 0) return;
        mAdapterChat.addList(list);

//        mList.addAll(list);
//        mAdapterChat.notifyItemRangeInserted(0,list.size());
//        mRcv.setAdapter(mAdapterChat);
        mRcv.smoothScrollToPosition(posInsert);
        this.indexList = indexList;
    }

    @Override
    public void onSuccessSendMessage(Message message, int positionMessage) {
        mList.set(positionMessage, message);
        chatRoomId = Integer.parseInt(message.getChat_id());

        if (getmSocket() != null && message != null) {
            getmSocket().joinRoom(message.getChat_id());
        }
        mAdapterChat.notifyItemChanged(positionMessage);
    }

    @Override
    public void onSuccuessUploadImages(List<Message> mess, int positionMessage) {
        for (int i = 0; i < mess.size(); i++) {
            if (positionMessage < mList.size()) {
                mList.set(positionMessage, mess.get(i));
                mAdapterChat.notifyItemChanged(positionMessage++);

            }
        }
    }

    @Override
    public void onSuccuessUploadFiles(Message mess, int positionMessage) {
        mList.set(positionMessage, mess);
        mAdapterChat.notifyItemChanged(positionMessage);

    }


    @Override
    public void onSucessGetInfo(Profile profile, int type) {
        //textToolbar.setText(title );

    }

    @Override
    public Context getMyContext() {
        return this;
    }


    @Override
    public void showProgress() {
//        swipeLayout.setRefreshing(true);
    }

    @Override
    public void hiddenProgress() {
//        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
        mAdapterChat.setLoad(true);

    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);
        showToast(message, AppConstant.NEGATIVE);
        mAdapterChat.setLoad(true);

    }

    @Override
    public void onErrorAuthorization() {
        mAdapterChat.setLoad(true);
        showDialogExpired();
    }

    @OnClick(R2.id.btnChoosePhoto)
    public void onClickChoosePhoto() {
        choosePhoto();
    }

    @OnClick(R2.id.imgPicker)
    public void onClickPickerPhoto() {
//        choosePhoto();
        btnChoosePhoto.setVisibility(View.VISIBLE);
        rcvPhoto.setVisibility(View.VISIBLE);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    List<String> paths = CommomUtils.getCameraImages(ChatGroupActivity.this);
                    if (listpathPhoto == null) {
                        listpathPhoto = paths;
                        adapterPhoto = new PhotoAdapter(listpathPhoto, ChatGroupActivity.this, ChatGroupActivity.this);
                        rcvPhoto.setAdapter(adapterPhoto);
                    } else {
                        listpathPhoto.clear();
                        listpathPhoto.addAll(paths);
                        adapterPhoto.notifyDataSetChanged();
                    }
                    expandKeyboardLayout.toggle();
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
    public void onBackPressed() {
        //  super.onBackPressed();
        finish();
    }

    @OnClick(R2.id.imageView3)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R2.id.imageView6)
    public void onClickInforBack() {
        if (room == null) return;
        Intent i = new Intent(ChatGroupActivity.this, EditGroupActivity.class);
        Bundle b = new Bundle();
        b.putParcelable(AppConstant.MSG, room);
        i.putExtra(AppConstant.BUNDLE, b);
        startActivityForResult(i, 1002);
    }

    @OnClick(R2.id.imgVoice)
    public void onClicVoicekBack() {
        startRecording();
    }

    @OnClick(R2.id.imgAttact)
    public void onClickAttactBack() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
//                .withFilter(Pattern.compile(".*\\.txt$")) // Filtering files and directories by file name using regexp
//                .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    @OnClick(R2.id.img_smile)
    public void onClickSmile() {
        if (emojiKeyboardLayout.getVisibility() == View.VISIBLE) {
            mMessage.showSoftKeyboard();
//            KeyboardUtils.showSoftInput(mMessage);
            Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
            emojiKeyboardLayout.startAnimation(slide);
            emojiKeyboardLayout.setVisibility(View.GONE);
//            KeyboardUtils.showSoftInput(mMessage);
            imgSwitchKeyboard.setImageResource(R.drawable.ic_smile);
        } else {
            mMessage.hideSoftKeyboard();
            emojiKeyboardLayout.setVisibility(View.VISIBLE);
            imgSwitchKeyboard.setImageResource(R.drawable.ic_keyboard_black_24dp);
//            KeyboardUtils.hideSoftInput(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh() {
        // loadmore here
        presenter.getRoomInfo(chatRoomId + "", indexList, true);
    }


    private void choosePhoto() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PhotoPicker.builder()
                            .setPhotoCount(10)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .start(ChatGroupActivity.this, PhotoPicker.REQUEST_CODE);
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

    private boolean checkPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 111:
                if (grantResults.length > 2 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissionGranted();
                    return;
                } else {
                    choosePhoto();
                }
                return;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            File source = new File(filePath);
//            String filename = uri.getLastPathSegment();
//            File destination = new File(Environment.getExternalStorageDirectory() + "/" + filename);
//            copy(source, destination);
            if (source.exists()) {
                sendFileToUI(source, 5);
                presenter.uploadFiles(source, chatRoomId, 0,getmSocket());
            }
        }
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                List<File> listPickup = new ArrayList<>();
                for (String urlPath : photos) {
                    File fileImage = new File(urlPath);
                    if (!fileImage.exists()) {
                        Toast.makeText(this, "File không tồn tại.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listPickup.add(fileImage);
                    sendFileToUI(fileImage, 2);
                }
//                expandKeyboardLayout.collapse();
                presenter.uploadImages(listPickup, chatRoomId, 0, getmSocket());


//                listImage.addAll(listPickup);
//                adapterPhoto.notifyItemInserted(adapterPhoto.getItemCount() - 1);
//                recyclerView2.smoothScrollToPosition(adapterPhoto.getItemCount() - 1);
//                if (postToEdit != null && postToEdit.getPost() != null) {
//                    presenter.addPhoto(listPickup, postToEdit.getPost().getId());
//                }
//                CropImage.activity(Uri.fromFile(fileImage))
//                        .setAspectRatio(2, 1)
//                        .setRequestedSize(800, 400, CropImageView.RequestSizeOptions.RESIZE_EXACT)
//
//                        .start(this);
            }

//            File fileImage = CommomUtils.scanFileMetisse(this, Matisse.obtainResult(data).get(0));
            // File fileImage = new File(Matisse.obtainPathResult(data).get(0));

            //  LogUtils.d("Matisse", "mSelected: " + (fileImage.exists() ? fileImage.getAbsolutePath() : "null"));

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                File file = new File(resultUri.getPath());
//                listImage.add(file);
//                adapterPhoto.notifyItemInserted(adapterPhoto.getItemCount() - 1);
//                recyclerView2.smoothScrollToPosition(adapterPhoto.getItemCount() - 1);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }

        }

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String filePath = Environment.getExternalStorageDirectory() + filename;
                File file = new File(filePath);
                if (file.exists()) {
                    sendFileToUI(file, 4);
                    presenter.uploadFiles(file, chatRoomId, 0, getmSocket());
                }

                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
            return;
        }

        if (requestCode == 1001) {
            Uri uri = data.getData();
            String src = getPath(uri);
            File source = new File(src);
//            String filename = uri.getLastPathSegment();
//            File destination = new File(Environment.getExternalStorageDirectory() + "/" + filename);
//            copy(source, destination);
            if (source.exists()) {
                sendFileToUI(source, 5);
                presenter.uploadFiles(source, chatRoomId, 0, getmSocket());
            }
            return;
        }

        if (requestCode == 1002) {
            if (resultCode == EditGroupActivity.RESULT_CODE_EDIT_GROUP) {
                indexList = 0;
                presenter.getRoomInfo(chatRoomId + "", indexList, false);
            } else if (resultCode == EditGroupActivity.RESULT_CODE_DELETE_GROUP || resultCode == EditGroupActivity.RESULT_CODE_LEAVE_GROUP) {
                finish();
            }
        }
    }

    private void copy(File source, File destination) {
        try {
            if (!destination.exists())
                destination.createNewFile();
            FileChannel in = null;
            in = new FileInputStream(source).getChannel();

            FileChannel out = new FileOutputStream(destination).getChannel();

            try {
                in.transferTo(0, in.size(), out);
            } catch (Exception e) {
                // post to log
                e.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
        } catch (Exception e) {
            // post to log
            e.printStackTrace();

        }
    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    @Override
    public void onCallBack(int pos) {
        File file = new File(listpathPhoto.get(pos));
        if (file.exists()) {

            sendFileToUI(file, 2);

            ArrayList<File> list = new ArrayList<>();
            list.add(file);
            presenter.uploadImages(list, chatRoomId, 0, getmSocket());


        }
    }

    private void sendFileToUI(File file, int type) {
        Message message = new Message();
        message.setUser_id(AppController.getInstance().getmProfileUser().getId());
        message.setContent("");
        message.setContent_type(type);
        if (type == 2) {
            message.setImage(file.getPath());
        } else {
            message.setFile(file.getPath());
        }
        message.setAvatar(AppController.getInstance().getmProfileUser().getAvatar());
        mList.add(0, message);
        mAdapterChat.notifyItemInserted(0);
        if (mAdapterChat.getItemCount() > 0)
            mRcv.smoothScrollToPosition(0);
        expandKeyboardLayout.collapse();
        setResult(RESULT_OK);
    }

    private void startRecording() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    filename = "/audio_"+System.currentTimeMillis()+".wav";

                    String filePath = Environment.getExternalStorageDirectory() + filename;
                    int color = getResources().getColor(R.color.colorAccent);
                    int requestCode = 0;
                    AndroidAudioRecorder.with(ChatGroupActivity.this)
                            // Required
                            .setFilePath(filePath)
                            .setColor(color)
                            .setRequestCode(requestCode)

                            // Optional
                            .setSource(AudioSource.MIC)
                            .setChannel(AudioChannel.STEREO)
                            .setSampleRate(AudioSampleRate.HZ_48000)
                            .setAutoStart(true)
                            .setKeepDisplayOn(true)

                            // Start recording
                            .record();
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
    public void onCompletion(MediaPlayer mp) {
        initialStage = true;
        playPause = false;
        //  btn.setText("Launch Streaming");
        mediaPlayer.stop();
        mediaPlayer.reset();
        if(oldPostPlaying != -1 && mList != null && mList.size() >oldPostPlaying){
            mList.get(oldPostPlaying).setPlaying(false);
            mAdapterChat.stopPlaying(oldPostPlaying);
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
//
//            if (progressDialog.isShowing()) {
//                progressDialog.cancel();
//            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog.setMessage("Buffering...");
//            progressDialog.show();
        }
    }
}
