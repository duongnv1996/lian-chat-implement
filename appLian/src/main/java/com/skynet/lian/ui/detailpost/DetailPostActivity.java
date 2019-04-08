package com.skynet.lian.ui.detailpost;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.interfaces.SnackBarCallBack;
import com.skynet.lian.models.Comment;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.Post;
import com.skynet.lian.ui.chatting.BaseEmojiActivity;
import com.skynet.lian.ui.detailpost.viewphoto.ViewPhotoPostActivity;
import com.skynet.lian.ui.main.OptionBottomSheet;
import com.skynet.lian.ui.profileFriend.ProfileFriendActivity;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.instachat.emojilibrary.model.layout.EmojiEditText;
import br.com.instachat.emojilibrary.model.layout.EmojiKeyboardLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPostActivity extends BaseEmojiActivity implements DetailPostContract.View, ICallback, MaterialDialog.ListCallback, SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener, EditShareBottomSheet.MoreOptionCallback {
    @BindView(R2.id.imageView10)
    ImageView imageView10;
    @BindView(R2.id.imageView13)
    ImageView imageView13;
    @BindView(R2.id.circleImageView3)
    CircleImageView circleImageView3;
    @BindView(R2.id.tvName)
    TextView tvName;
    @BindView(R2.id.tvTime)
    TextView tvTime;
    @BindView(R2.id.asymmetricGridView)
    RecyclerView rcvPhoto;
    @BindView(R2.id.tvContent)
    TextView tvContent;
    @BindView(R2.id.view6)
    View view6;
    @BindView(R2.id.view7)
    View view7;
    @BindView(R2.id.checkBox)
    CheckBox checkBox;
    @BindView(R2.id.tvNumbershare)
    TextView tvNumbershare;
    @BindView(R2.id.view8)
    View view8;
    @BindView(R2.id.tvNumberComment)
    TextView tvNumberComment;
    @BindView(R2.id.rcv)
    RecyclerView rcv;
    @BindView(R2.id.img_smile)
    ImageView imgSmile;
    @BindView(R2.id.message_txt)
    EmojiEditText messageTxt;
    @BindView(R2.id.send_imv)
    ImageView sendImv;
    @BindView(R2.id.swipelayout)
    SwipeRefreshLayout swipelayout;
    @BindView(R2.id.emojiKeyboardLayout)
    EmojiKeyboardLayout emojiKeyboardLayout;
    @BindView(R2.id.layoutCommentBottom)
    ConstraintLayout layoutCommentBottom;
    @BindView(R2.id.cardMenu)
    CardView expandMenu;
    @BindView(R2.id.scrollDetailPost)
    NestedScrollView scrollDetailPost;
    List<Image> listImage = new ArrayList<>();
    List<Comment> listComment = new ArrayList<>();
    @BindView(R2.id.tvDelete)
    TextView tvDelete;
    @BindView(R2.id.tvSharePostMenu)
    TextView tvSharePostMenu;
    @BindView(R2.id.tvEdit)
    TextView tvEdit;
    @BindView(R2.id.tvOffComment)
    TextView tvOffComment;
    @BindView(R2.id.layoutRootDetailPost)
    ConstraintLayout layoutRootDetailPost;
    @BindView(R2.id.avtShare)
    CircleImageView avtShare;
    @BindView(R2.id.tvNameShare)
    TextView tvNameShare;
    @BindView(R2.id.tvTimeShare)
    TextView tvTimeShare;
    @BindView(R2.id.rcvShare)
    RecyclerView rcvShare;
    @BindView(R2.id.tvContentShare)
    TextView tvContentShare;
    @BindView(R2.id.layoutShare)
    ConstraintLayout layoutShare;
    @BindView(R2.id.tvShareName)
    TextView tvShareName;
    MaterialDialog.Builder builder;
    private int commentEditPosition = 0;
    private AdapterComment adapterComment;
    private Post post;
    private DetailPostContract.Presenter presenter;
    private OptionBottomSheet dialogConfirmDelete;
    private EditShareBottomSheet dialogShare;
    private EditPostBottomSheet dialogEditPost;
    private EditPostBottomSheet.MoreOptionCallback callBackEditPost = new EditPostBottomSheet.MoreOptionCallback() {
        @Override
        public void onMoreOptionCallback(String msg, int type) {
            if (post != null) {
                post.setType_share(type);
                presenter.editPost(post.getId(), msg, type);
            }
        }
    };
    private boolean isEdit = false;
    private MaterialDialog dialogOption;

    @Override
    protected int initLayout() {
        return R.layout.activity_detailpost;
    }

    public static final int RESULT_CHANGE_LIKE = 10;
    public static final int RESULT_CHANGE_SHARE = 11;
    public static final int RESULT_CHANGE_COMMENT = 12;
    public static final int RESULT_DELETE = 13;

    @Override
    protected void initVariables() {
        dialogShare = new EditShareBottomSheet(this, this);
        dialogEditPost = new EditPostBottomSheet(this, callBackEditPost);
        adapterComment = new AdapterComment(listComment, this, new ICallback() {
            @Override
            public void onCallBack(int pos) {
                commentEditPosition = pos;
                if (dialogOption != null && !dialogOption.isShowing())
                    dialogOption.show();
            }
        });
        rcv.setAdapter(adapterComment);
        presenter = new DetailPostPresenter(this);
        presenter.getDetail(getIntent().getExtras().getInt(AppConstant.MSG));
        dialogConfirmDelete = new OptionBottomSheet(this, R.drawable.ic_delete,
                Html.fromHtml(getString(R.string.confirm_delete_post)),
                "Quay lại", "Xoá ",
                new OptionBottomSheet.MoreOptionCallback() {
                    @Override
                    public void onMoreOptionCallback() {
                        presenter.deletePost(post.getId());
                        showToast("Bài viết đã bị xoá thành công!", AppConstant.POSITIVE, new SnackBarCallBack() {
                            @Override
                            public void onClosedSnackBar() {
                                setResult(RESULT_DELETE);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancleCallback() {

                    }
                });
    }

    @Override
    protected void initViews() {
        builder = new MaterialDialog.Builder(this);
        builder.items(R.array.option);
        builder.title("Comment");
        builder.itemsCallback(this);
        dialogOption = builder.build();
        messageTxt.setEmojiSize(48);
        emojiKeyboardLayout.prepareKeyboard(this, messageTxt);
        rcvPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rcvPhoto.setHasFixedSize(true);
        rcvShare.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rcvShare.setHasFixedSize(true);
        rcv.setHasFixedSize(true);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcvPhoto);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        rcv.setLayoutManager(layoutManager);
        rcv.setNestedScrollingEnabled(false);
        rcv.setOnTouchListener(this);
        rcvPhoto.setOnTouchListener(this);
        layoutRootDetailPost.setOnTouchListener(this);

        swipelayout.setOnRefreshListener(this);
        swipelayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                swipelayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = swipelayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                Log.d(TAG, "keypadHeight = " + keypadHeight);
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    scrollToBottom();
                } else {
                    // keyboard is closed
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (expandMenu.getVisibility() == View.VISIBLE) {
            expandMenu.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
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

    //
//    @OnClick({R2.id.imageView10, R2.id.imageView13, R2.id.circleImageView3,
//            R2.id.tvName, R2.id.tvTime, R2.id.tvNumbershare, R2.id.img_smile,
//            R2.id.send_imv})
//    public void onViewClicked(View view) {
//        Intent iProfile = new Intent(DetailPostActivity.this, ProfileFriendActivity.class);
//        iProfile.putExtra(AppConstant.MSG, post.getUser_id());
//        switch (view.getId()) {
//            case R2.id.imageView10:
//                onBackPressed();
//                break;
//            case R2.id.imageView13:
//                if (expandMenu.getVisibility() == View.VISIBLE) {
//                    expandMenu.setVisibility(View.GONE);
//                } else {
//                    expandMenu.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R2.id.circleImageView3:
//                startActivity(iProfile);
//
//                break;
//            case R2.id.tvName:
//                startActivity(iProfile);
//
//                break;
//            case R2.id.tvTime:
//                startActivity(iProfile);
//                break;
//            case R2.id.tvNumbershare:
//                if (dialogShare != null && !dialogShare.isShowing())
//                    dialogShare.show();
//                break;
//            case R2.id.img_smile:
//                if (emojiKeyboardLayout.getVisibility() == View.VISIBLE) {
//                    openKeyboard();
//                } else {
//                    onpenEmojiKeyboard();
//                }
//
//                break;
//            case R2.id.send_imv:
//                hideKeyboard();
//                String content = messageTxt.getText().toString();
//                try {
//                    content = URLEncoder.encode(content, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                if (content.isEmpty()) {
//                    Toast.makeText(this, "Bạn phải nhập bình luận ", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (post == null) return;
//
//                Comment message = new Comment();
//                message.setName(AppController.getInstance().getmProfileUser().getName());
//                message.setComment(content);
//                message.setAvatar(AppController.getInstance().getmProfileUser().getAvatar());
//                messageTxt.setText("");
//                listComment.add(message);
//                adapterComment.notifyItemInserted(adapterComment.getItemCount());
//                presenter.comment(post.getId(), content, listComment.size() - 1);
//                if (adapterComment.getItemCount() > 0)
//                    rcv.smoothScrollToPosition(adapterComment.getItemCount());
//                post.setNumber_comment(post.getNumber_comment() + 1);
//                tvNumberComment.setText(post.getNumber_comment() + " bình luận");
//                Intent i = new Intent();
//                i.putExtra(AppConstant.MSG, post.getNumber_comment());
//                setResult(RESULT_CHANGE_COMMENT, i);
//
//                break;
//        }
//    }
    @OnClick(R2.id.layoutShare)
    public void onViewClicked() {
    }

    @OnClick(R2.id.imageView10)
    public void onImageView10Clicked() {
        onBackPressed();
    }

    @OnClick(R2.id.imageView13)
    public void onImageView13Clicked() {
        if (expandMenu.getVisibility() == View.VISIBLE) {
            expandMenu.setVisibility(View.GONE);
        } else {
            expandMenu.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R2.id.circleImageView3)
    public void onCircleImageView3Clicked() {
        Intent iProfile = new Intent(DetailPostActivity.this, ProfileFriendActivity.class);
        iProfile.putExtra(AppConstant.MSG, post.getUser_id());
        startActivity(iProfile);
    }

    @OnClick(R2.id.tvName)
    public void onTvNameClicked() {
        Intent iProfile = new Intent(DetailPostActivity.this, ProfileFriendActivity.class);
        iProfile.putExtra(AppConstant.MSG, post.getUser_id());
        startActivity(iProfile);
    }

    @OnClick(R2.id.tvTime)
    public void onTvTimeClicked() {
        Intent iProfile = new Intent(DetailPostActivity.this, ProfileFriendActivity.class);
        iProfile.putExtra(AppConstant.MSG, post.getUser_id());
        startActivity(iProfile);
    }

    @OnClick(R2.id.tvNumbershare)
    public void onTvNumbershareClicked() {
        if (dialogShare != null && !dialogShare.isShowing())
            dialogShare.show();
    }

    @OnClick(R2.id.img_smile)
    public void onImgSmileClicked() {
        if (emojiKeyboardLayout.getVisibility() == View.VISIBLE) {
            openKeyboard();
        } else {
            onpenEmojiKeyboard();
        }
    }

    @OnClick(R2.id.send_imv)
    public void onSendImvClicked() {
        hideKeyboard();
        String content = messageTxt.getText().toString();
        try {
            content = URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (content.isEmpty() || content.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(this, "Bạn phải nhập bình luận ", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (content.isEmpty()) {
//            Toast.makeText(this, "Bạn phải nhập bình luận ", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (post == null) return;
        if (commentEditPosition < listComment.size() && isEdit) {
            listComment.get(commentEditPosition).setComment(content);
            isEdit = false;
            adapterComment.notifyItemChanged(commentEditPosition);
            presenter.editComment(listComment.get(commentEditPosition).getId(), content);
            messageTxt.setText("");

        } else {
            Comment message = new Comment();
            message.setName(AppController.getInstance().getmProfileUser().getName());
            message.setComment(content);
            message.setAvatar(AppController.getInstance().getmProfileUser().getAvatar());
            messageTxt.setText("");
            listComment.add(message);
            adapterComment.notifyItemInserted(adapterComment.getItemCount());
            presenter.comment(post.getId(), content, listComment.size() - 1);
            if (adapterComment.getItemCount() > 0)
                rcv.smoothScrollToPosition(adapterComment.getItemCount());
            post.setNumber_comment(post.getNumber_comment() + 1);
            tvNumberComment.setText(post.getNumber_comment() + " bình luận");
            Intent i = new Intent();
            i.putExtra(AppConstant.MSG, post.getNumber_comment());
            setResult(RESULT_CHANGE_COMMENT, i);
        }

    }

    @Override
    public void onSuccessGetDetail(Post notification) {
        this.post = notification;
        int idImageClicked = getIntent().getIntExtra("idImage", -1);
        if (post.getList_image() != null) {
            this.listImage = post.getList_image();
            rcvPhoto.setAdapter(new AdapterPhoto(post.getList_image(), this, this));
            if (idImageClicked != -1) {
                int i = 0;
                for (Image img : this.listImage) {
                    if (img.getId() == idImageClicked) {
                        rcvPhoto.scrollToPosition(i);
                        break;
                    }
                    i++;
                }
            }
        }
        if (post.getList_comment() != null) {
            this.listComment.clear();
            this.listComment.addAll(post.getList_comment());
            adapterComment.notifyDataSetChanged();
        }
        if (post.getUser() != null) {
            if (post.getUser().getAvatar() != null && !post.getUser().getAvatar().isEmpty()) {
                Picasso.with(this).load(post.getUser().getAvatar()).fit().centerCrop().into(circleImageView3);
            }
            tvName.setText(post.getUser().getName());
            if (!post.getUser().getId().equals(AppController.getInstance().getmProfileUser().getId())) {
                tvEdit.setVisibility(View.GONE);
            }
        }
        if (post.getOff_comment() == 1) {
            layoutCommentBottom.setVisibility(View.GONE);
            tvOffComment.setText("Bật bình luận");
        } else {
            tvOffComment.setText("Tắt bình luận");
        }
        if (post.getIs_comment() == 0) {
            tvOffComment.setVisibility(View.GONE);
        }
        if (post.getIs_delete() == 0) {
            tvDelete.setVisibility(View.GONE);
        }
        String time = post.getDate() + (post.getAddress() != null && !post.getAddress().isEmpty() ? ", " + post.getAddress() : "");
        tvTime.setText(time);
        tvContent.setText(post.getContent());
        checkBox.setChecked(post.getIs_like() == 1);
        checkBox.setText(post.getNumber_like() + "");
        tvNumbershare.setText(post.getNumber_share() + "");
        tvNumberComment.setText(post.getNumber_comment() + " bình luận");
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (post != null) {
                    int numberLike = post.getNumber_like();
                    numberLike = isChecked ? ++numberLike : --numberLike;
                    post.setNumber_like(numberLike);
                    checkBox.setText(numberLike + "");
                    presenter.toggleLike(post.getId(), isChecked ? 1 : 0);
                    Intent i = new Intent();
                    i.putExtra(AppConstant.MSG, numberLike);
                    i.putExtra("is_like", isChecked);
                    setResult(RESULT_CHANGE_LIKE, i);
                }
            }
        });

        //todo share post here
        if (post.getPostShare() != null) {
            if (post.getPostShare().getList_image() != null) {
                this.listImage = post.getPostShare().getList_image();
                rcvShare.setAdapter(new AdapterPhoto(post.getPostShare().getList_image(), this, this));
            }
            if (post.getPostShare().getUser() != null) {
                if (post.getPostShare().getUser().getAvatar() != null && !post.getPostShare().getUser().getAvatar().isEmpty()) {
                    Picasso.with(this).load(post.getPostShare().getUser().getAvatar()).fit().centerCrop().into(avtShare);
                }
                tvNameShare.setText(post.getPostShare().getUser().getName());
            }
            tvShareName.setText(" đã chia sẻ bài viết");
            String timeShare = post.getPostShare().getDate() + (post.getPostShare().getAddress() != null && !post.getPostShare().getAddress().isEmpty() ? ", " + post.getPostShare().getAddress() : "");
            tvTimeShare.setText(timeShare);
            tvContentShare.setText(post.getPostShare().getContent());

            layoutShare.setVisibility(View.VISIBLE);
        } else {
            layoutShare.setVisibility(View.GONE);

        }

    }

    @OnClick(R2.id.layoutShare)
    public void onClickLayoutshare() {
        if (post != null && post.getPostShare() != null) {
            Intent i = new Intent(DetailPostActivity.this, DetailPostActivity.class);
            i.putExtra(AppConstant.MSG, post.getPostShare().getId());
            startActivity(i);
        }
    }

    @Override
    public void onSucessShare(final int pos) {
        setResult(RESULT_OK);
        showToast("Chia sẻ thành công", AppConstant.POSITIVE, new SnackBarCallBack() {
            @Override
            public void onClosedSnackBar() {
                Intent i = new Intent(DetailPostActivity.this, DetailPostActivity.class);
                i.putExtra(AppConstant.MSG, pos);
                startActivity(i);
            }
        });

        onRefresh();
    }

    @Override
    public void onSucessEditPost() {
        setResult(RESULT_OK);
        showToast("Chỉnh sửa thành công", AppConstant.POSITIVE);
        onRefresh();
    }

    @Override
    public void onSucessComment(Comment comment, int pos) {
        listComment.set(pos, comment);
        adapterComment.notifyItemChanged(pos);
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void showProgress() {
        swipelayout.setRefreshing(true);
    }

    @Override
    protected void onStop() {
        if (dialogConfirmDelete != null && dialogConfirmDelete.isShowing())
            dialogConfirmDelete.dismiss();
        super.onStop();
    }

    @Override
    public void hiddenProgress() {
        swipelayout.setRefreshing(false);
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
        Intent i = new Intent(DetailPostActivity.this, ViewPhotoPostActivity.class);
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) listImage);
        i.putExtra(AppConstant.BUNDLE, b);
        startActivity(i);
    }

    private void openKeyboard() {
        messageTxt.requestFocus();

        messageTxt.showSoftKeyboard();
        imgSmile.setImageResource(R.drawable.ic_smile);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                emojiKeyboardLayout.startAnimation(slide);
                emojiKeyboardLayout.setVisibility(View.GONE);
            }
        });
        scrollToBottom();

    }

    private void onpenEmojiKeyboard() {
        messageTxt.requestFocus();

        messageTxt.hideSoftKeyboard();
        emojiKeyboardLayout.setVisibility(View.VISIBLE);
        imgSmile.setImageResource(R.drawable.ic_keyboard_black_24dp);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollDetailPost.post(new Runnable() {
            @Override
            public void run() {
                rcv.smoothScrollToPosition(listComment.size());
                scrollDetailPost.fullScroll(ScrollView.FOCUS_DOWN);
                messageTxt.requestFocus();

            }
        });
    }

    private void hideKeyboard() {
        KeyboardUtils.hideSoftInput(this);
        emojiKeyboardLayout.setVisibility(View.GONE);
        imgSmile.setImageResource(R.drawable.ic_smile);
    }

    @Override
    public void onRefresh() {
        presenter.getDetail(getIntent().getExtras().getInt(AppConstant.MSG));
    }

    //    @OnClick({R2.id.tvDelete, R2.id.tvSharePostMenu, R2.id.tvEdit, R2.id.tvOffComment})
//    public void onViewMenuClicked(View view) {
//        switch (view.getId()) {
//            case R2.id.tvDelete:
//                dialogConfirmDelete.show();
//                break;
//            case R2.id.tvSharePostMenu:
//                if (dialogShare != null && !dialogShare.isShowing())
//                    dialogShare.show();
//                break;
//            case R2.id.tvEdit:
//                if (dialogEditPost != null && !dialogEditPost.isShowing()) {
//                    dialogEditPost.setTypeShare(post.getType_share());
//                    dialogEditPost.show();
//                }
//                break;
//            case R2.id.tvOffComment:
//                if (post.getOff_comment() == 0) { // dc binh luan
//                    layoutCommentBottom.setVisibility(View.GONE);
//                    post.setOff_comment(1);
//                    tvOffComment.setText("Bật bình luận");
//                } else {
//                    // k dc binh luan
//                    layoutCommentBottom.setVisibility(View.VISIBLE);
//                    post.setOff_comment(0);
//                    tvOffComment.setText("Tắt bình luận");
//                }
//                presenter.toggleComment(post.getId(), post.getOff_comment());
//                break;
//        }
//
//        expandMenu.setVisibility(View.GONE);
//    }
    @OnClick({R2.id.tvOffComment})
    public void onViewMenutvOffCommentClicked(View view) {

        if (post.getOff_comment() == 0) { // dc binh luan
            layoutCommentBottom.setVisibility(View.GONE);
            post.setOff_comment(1);
            tvOffComment.setText("Bật bình luận");
        } else {
            // k dc binh luan
            layoutCommentBottom.setVisibility(View.VISIBLE);
            post.setOff_comment(0);
            tvOffComment.setText("Tắt bình luận");
        }
        presenter.toggleComment(post.getId(), post.getOff_comment());

        expandMenu.setVisibility(View.GONE);
    }

    @OnClick({R2.id.tvEdit})
    public void onViewMenutvEditClicked(View view) {

        if (dialogEditPost != null && !dialogEditPost.isShowing()) {
            dialogEditPost.setTypeShare(post.getType_share());
            dialogEditPost.show();
        }

        expandMenu.setVisibility(View.GONE);
    }

    @OnClick({R2.id.tvSharePostMenu})
    public void onViewMenutvSharePostMenuClicked(View view) {
        if (dialogShare != null && !dialogShare.isShowing())
            dialogShare.show();
        expandMenu.setVisibility(View.GONE);
    }

    @OnClick({R2.id.tvDelete})
    public void onViewMenutvDeleteClicked(View view) {
        dialogConfirmDelete.show();

        expandMenu.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideKeyboard();
        expandMenu.setVisibility(View.GONE);
        return false;
    }

    @Override
    public void onMoreOptionCallback(String msg, int typeShare) {
        if (post == null) {
            showToast("Bài viết đã bị chỉnh sửa hoặc không tồn tại!", AppConstant.NEGATIVE);
            return;
        }
        //todo share content here
        presenter.shareContent(post.getId(), msg, typeShare);
    }


    @Override
    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
        if (commentEditPosition < listComment.size()) {
            switch (position) {
                case 0: {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = null;
                    try {
                        clip = ClipData.newPlainText("text",
                                URLDecoder.decode(listComment.get(commentEditPosition).getComment(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    clipboard.setPrimaryClip(clip);
                    showToast("Đã sao chép!", AppConstant.POSITIVE);
                    break;
                }
                case 1: {
                    isEdit = true;
                    messageTxt.setText(listComment.get(commentEditPosition).getComment());
                    break;
                }
                case 2: {
                    presenter.deleteComment(listComment.get(commentEditPosition).getId());
                    adapterComment.remove(commentEditPosition);

                    break;
                }
            }
        }
    }
}
