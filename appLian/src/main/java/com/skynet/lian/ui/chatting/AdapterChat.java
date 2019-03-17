package com.skynet.lian.ui.chatting;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Message;
import com.skynet.lian.models.OnLoadMoreListener;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import br.com.instachat.emojilibrary.model.layout.EmojiTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Huy on 8/31/2017.
 */

public class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> mMessages;
    Context context;
    ICallback onResponse;
    String ava_user;
    String ava_driver;
    int layoutId;
    private final int VIEW_PROG = 3;
    CallBackChat iCallback;
    private int pastVisiblesItems, totalItemCount, visibleItemCount;
    private boolean loading = false;
    private OnLoadMoreListener onLoadMoreListener;

    private SparseBooleanArray cache;
    private SparseBooleanArray cachePlay;
    private ViewHolerMyMessage oldHolder;
    private int oldPosition;

    public AdapterChat(List<Message> list, Context applicationContext, RecyclerView recyclerView, CallBackChat iCallback) {
        this.mMessages = list;
        this.context = applicationContext;
        this.cache = new SparseBooleanArray();
        this.cachePlay = new SparseBooleanArray();
        this.iCallback = iCallback;
//        addList(list);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutParams = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
//                            LogUtils.e(" dy " + dy);
//                            if (dy > 0) {
                            visibleItemCount = layoutParams.getChildCount();
                            totalItemCount = layoutParams.getItemCount();
                            pastVisiblesItems = layoutParams.findFirstVisibleItemPosition();
                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                }
                            }
                        }
//                        }
                    });
        }

    }

    public void addList(List<Message> list) {
        this.mMessages.addAll(list);
        int length = this.mMessages.size();
        for (int i = 0; i < length - 1; i++) {
            if (!this.mMessages.get(i).getUser_id().equals(this.mMessages.get(i + 1).getUser_id())) {
                cache.put(i, true);
            }
            cachePlay.put(i, this.mMessages.get(i).isPlaying());
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AppConstant.TYPE_USER) {
            return new ViewHolerMyMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_chat, parent, false));
        } else if (viewType == 2) {
            return new ViewHolerMyMessage(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_partner_chat, parent, false));

        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_loadmore, parent, false));

        }
//        layoutId = viewType  == AppConstant.TYPE_USER ? R.layout.row_my_chat : R.layout.row_partner_chat;
//        return new ViewHolerMessage(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderRaw, final int position) {
        if (holderRaw instanceof ViewHolerMyMessage) {
            final ViewHolerMyMessage holder = (ViewHolerMyMessage) holderRaw;
            final Message message = mMessages.get(position);
            if (holder != null) {
//                if(cache.get(position)){
////                    holder.contentTxt.setBackgroundResource(R.drawable.background_button_3);
//
//                    holder.avt.setVisibility(View.VISIBLE);
//                    holder.timeTt.setVisibility(View.VISIBLE);
//                }else{
//                    holder.avt.setVisibility(View.INVISIBLE);
//                    holder.timeTt.setVisibility(View.GONE);
////                    holder.contentTxt.setBackgroundResource(R.drawable.background_button_1);
//                }
                holder.contentTxt.setEmojiconSize(48);
                try {
                 String content =    message.getContent().replaceAll("\\+","```");
                    holder.contentTxt.setText(URLDecoder.decode(content,"UTF_8").replaceAll("```","+"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                holder.timeTt.setText(mMessages.get(position).getTime());
//            if(position == getItemCount()-1){
//                holder.mTime.setVisibility(View.VISIBLE);
//            }else{
//                holder.mTime.setVisibility(View.INVISIBLE);
//
//            }

                if (message.getImage() != null && !message.getImage().isEmpty()) {
                    if (message.getImage().contains("http")) {
                        Picasso.with(context).load(message.getImage()).fit().centerCrop().into(holder.roundedImageView);
                    } else {
                        File file = new File(message.getImage());
                        if (file.exists())
                            Picasso.with(context).load(file).fit().centerCrop().into(holder.roundedImageView);
                    }
                    holder.roundedImageView.setVisibility(View.VISIBLE);
                    holder.contentTxt.setVisibility(View.GONE);


                } else {
                    holder.roundedImageView.setVisibility(View.GONE);
                    holder.contentTxt.setVisibility(View.VISIBLE);
                }

                holder.roundedImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                            if((message.getFile().contains("http")))
                        iCallback.onCallBack(position);

                    }
                });
                if (message.getFile() != null && !message.getFile().isEmpty()) {
                    String fileName = message.getFile().substring(message.getFile().lastIndexOf('/') + 1, message.getFile().length());
                    holder.contentTxt.setText(fileName);
                    if (fileName.contains(".wav") || fileName.contains(".m4a")) {
                        if (cachePlay.get(position)) {
                            if(getItemViewType(position)==2) {
                                holder.contentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_pause_circle_filled_black_24dp), null, null, null);
                            }else{
                                holder.contentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_pause_circle_filled_white_24dp), null, null, null);
                            }
                        } else {
                            if(getItemViewType(position)==2) {
                                holder.contentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_play_circle_filled_black_24dp), null, null, null);
                            }else{
                                holder.contentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_play_circle_filled_white_24dp), null, null, null);
                            }
                        }
                    } else {
                        holder.contentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_file_download_black_24dp), null, null, null);
                    }
                    holder.contentTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((message.getFile().contains("http"))) {

                                if (message.getFile().contains(".wav") || message.getFile().contains(".m4a")) {
                                    if (oldHolder != null) {
                                        mMessages.get(oldPosition).setPlaying(false);
                                        cachePlay.put(oldPosition, false);
                                        notifyItemChanged(oldPosition);
                                    }
                                    oldHolder = holder;
                                    oldPosition = position;
                                } else {

                                }
                                cachePlay.put(position, true);
                                iCallback.onCallBack(position);
                            }

                        }
                    });
                } else {
                    holder.contentTxt.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                    holder.contentTxt.setOnClickListener(null);
                }
                if (message.getAvatar() != null && !message.getAvatar().isEmpty()) {
                    Picasso.with(context).load(message.getAvatar()).fit().centerCrop().into(holder.avt);
                }

                holder.avt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iCallback.onClickAvatar(position, message.getUser_id());

                    }
                });
            }

        } else {
            ((ProgressViewHolder) holderRaw).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void setLoad(boolean isLoading) {
        loading = isLoading;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position) != null) {
//            if(position != getItemCount()-1){
//
//            }else{
            return mMessages.get(position).getUser_id().equals(AppController.getInstance().getmProfileUser().getId()) ? 1 : 2;
//            }
        } else return VIEW_PROG;
    }

    public void stopPlaying(int oldPostPlaying) {
        cachePlay.put(oldPostPlaying, false);
        notifyItemChanged(oldPostPlaying);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public class ViewHolerMyMessage extends RecyclerView.ViewHolder {
        @BindView(R2.id.avt)
        CircleImageView avt;
        @BindView(R2.id.content_txt)
        EmojiTextView contentTxt;
        @BindView(R2.id.time_tt)
        TextView timeTt;
        @BindView(R2.id.layoutPost)
        ConstraintLayout layoutPost;
        @BindView(R2.id.roundedImageView)
        RoundedImageView roundedImageView;

        public ViewHolerMyMessage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolerPartnerMessage extends RecyclerView.ViewHolder {
        @BindView(R2.id.avt)
        CircleImageView avt;
        @BindView(R2.id.content_txt)
        EmojiTextView contentTxt;
        @BindView(R2.id.time_tt)
        TextView timeTt;

        public ViewHolerPartnerMessage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CallBackChat extends ICallback {
        void onClickAvatar(int pos, String idProfile);
    }
}
