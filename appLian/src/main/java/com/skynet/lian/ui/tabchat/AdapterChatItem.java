package com.skynet.lian.ui.tabchat;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.ChatItem;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import br.com.instachat.emojilibrary.model.layout.EmojiTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChatItem extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    List<ChatItem> chatItems;
    Context context;
    CallBack callBack;
    SparseBooleanArray cacheOnline;
    SparseBooleanArray cacheRead;

    public AdapterChatItem(List<ChatItem> chatItems, Context context, CallBack callBack) {
        this.chatItems = chatItems;
        this.context = context;
        this.callBack = callBack;
        this.cacheOnline = new SparseBooleanArray();
        this.cacheRead = new SparseBooleanArray();
        for (int i = 0; i < chatItems.size(); i++) {
            cacheOnline.put(i, chatItems.get(i).getUser_online() == 1);
            cacheRead.put(i, chatItems.get(i).getIs_read() == 2);
        }
    }

    public void notiDateChange() {
        cacheOnline.clear();
        cacheRead.clear();
        for (int i = 0; i < chatItems.size(); i++) {
            cacheOnline.put(i, chatItems.get(i).getUser_online() == 1);
            cacheRead.put(i, chatItems.get(i).getIs_read() == 2);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1)
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_chat_item_list, parent, false));
        else
            return new ViewGroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_group_item_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder oldholder, final int position) {
        if(oldholder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) oldholder;
            mItemManger.bindView(holder.itemView, position);
            holder.tvOptionLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onClickDelete(position, chatItems.get(position));
                    mItemManger.closeItem(position);
                    // remove(position);
                }
            });
            holder.layoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onClickDetail(position, chatItems.get(position));
                  //  mItemManger.closeItem(position);
                }
            });
            holder.circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onClickViewProfile(position, chatItems.get(position));
                    mItemManger.closeItem(position);
                }
            });

            holder.tvName.setText(chatItems.get(position).getUser_name());
            try {
//                holder.tvContent.setText(URLDecoder.decode(chatItems.get(position).getLast_message(), "UTF-8"));
                String content =    chatItems.get(position).getLast_message().replaceAll("\\+","```");
                holder.tvContent.setText(URLDecoder.decode(content, "UTF-8").replaceAll("```","+"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            holder.tvTime.setText(chatItems.get(position).getTime_updated());
            if (chatItems.get(position).getUser_avatar() != null && !chatItems.get(position).getUser_avatar().isEmpty()) {
                Picasso.with(context).load(chatItems.get(position).getUser_avatar()).resize(100,100).centerCrop().into(holder.circleImageView);
            }
            if (cacheOnline.get(position)) {
                holder.imgStatus.setImageResource(R.drawable.dot_green_stock);
            } else {
                holder.imgStatus.setImageResource(R.drawable.dot_gray_stock);

            }
            if (cacheRead.get(position)) {
                holder.imgRead.setImageResource(R.drawable.ic_double_checked);
                holder.tvContent.setTypeface( holder.tvContent.getTypeface(),Typeface.NORMAL);

            } else {
                holder.imgRead.setImageResource(R.drawable.ic_path_copy_3);
                holder.tvContent.setTypeface( holder.tvContent.getTypeface(),Typeface.BOLD);
            }
        }else{
            final ViewGroupHolder holder = (ViewGroupHolder) oldholder;
            mItemManger.bindView(holder.itemView, position);
            holder.tvOptionLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onClickDelete(position, chatItems.get(position));
                    mItemManger.closeItem(position);
                }
            });
            holder.layoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onClickDetail(position, chatItems.get(position));
                    mItemManger.closeItem(position);
                }
            });
//        holder.tvName.setText(chatItems.get(position).getUser_name());
//        try {
//            holder.tvContent.setText(URLDecoder.decode(chatItems.get(position).getLast_message(), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        holder.tvTime.setText(chatItems.get(position).getTime_updated());
            holder.tvName.setText(chatItems.get(position).getTitle());
            try {
//                holder.tvContent.setText(URLDecoder.decode(chatItems.get(position).getLast_message(), "UTF-8"));
                String content =    chatItems.get(position).getLast_message().replaceAll("\\+","```");
                holder.tvContent.setText(URLDecoder.decode(content, "UTF-8").replaceAll("```","+"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            holder.tvTime.setText(chatItems.get(position).getTime_updated());
            new android.os.Handler().post(new Runnable() {
                @Override
                public void run() {

                    if(chatItems.get(position).getListAvatar() != null && chatItems.get(position).getListAvatar().size() > 0){
                        if(chatItems.get(position).getListAvatar().size() > 0){
                            Picasso.with(context).load(chatItems.get(position).getListAvatar().get(0)).resize(100,100).centerCrop().into(holder.imgAvt);
                        }if(chatItems.get(position).getListAvatar().size() > 1){
                            Picasso.with(context).load(chatItems.get(position).getListAvatar().get(1)).resize(100,100).centerCrop().into(holder.imgAvt1);
                        }if (chatItems.get(position).getListAvatar().size() > 2) {
                            Picasso.with(context).load(chatItems.get(position).getListAvatar().get(2)).resize(100,100).centerCrop().into(holder.imgAvt2);
                        }
                    }
                }
            });
        }
    }

    public void remove(int position) {
        chatItems.remove(position);
        cacheOnline.put(position,false);
        cacheRead.put(position,false);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, chatItems.size());
    }

    @Override
    public int getItemViewType(int position) {
        return chatItems.get(position).getType();

    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tvOptionLeft)
        FrameLayout tvOptionLeft;
        @BindView(R2.id.tvOptionCenter)
        FrameLayout tvOptionCenter;
        @BindView(R2.id.tvOptionRight)
        FrameLayout tvOptionRight;
        @BindView(R2.id.circleImageView)
        CircleImageView circleImageView;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.tvTime)
        TextView tvTime;
        @BindView(R2.id.tvContent)
        EmojiTextView tvContent;
        @BindView(R2.id.imgRead)
        ImageView imgRead;
        @BindView(R2.id.imgStatus)
        ImageView imgStatus;
        @BindView(R2.id.layoutContent)
        LinearLayout layoutContent;
        @BindView(R2.id.swipe)
        SwipeLayout swipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewGroupHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tvOptionLeft)
        FrameLayout tvOptionLeft;
        @BindView(R2.id.tvOptionCenter)
        FrameLayout tvOptionCenter;
        @BindView(R2.id.tvOptionRight)
        FrameLayout tvOptionRight;
        @BindView(R2.id.circleImageView)
        RelativeLayout circleImageView;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.tvTime)
        TextView tvTime;
        @BindView(R2.id.tvContent)
        TextView tvContent;
        @BindView(R2.id.imgRead)
        ImageView imgRead;
        @BindView(R2.id.imgStatus)
        ImageView imgStatus;
        @BindView(R2.id.layoutContent)
        LinearLayout layoutContent;
        @BindView(R2.id.swipe)
        SwipeLayout swipe;
        @BindView(R2.id.imgAvt)
        CircleImageView imgAvt;
        @BindView(R2.id.imgAvt1)
        CircleImageView imgAvt1;
        @BindView(R2.id.imgAvt2)
        CircleImageView imgAvt2;
        public ViewGroupHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface CallBack {
        void onClickDelete(int pos, ChatItem item);

        void onClickCall(int pos, ChatItem item);

        void onClickVideo(int pos, ChatItem item);
        void onClickViewProfile(int pos, ChatItem item);

        void onClickDetail(int pos, ChatItem item);
    }
}
