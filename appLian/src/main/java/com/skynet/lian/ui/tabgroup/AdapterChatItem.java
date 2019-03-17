package com.skynet.lian.ui.tabgroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.ChatItem;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChatItem extends RecyclerView.Adapter<AdapterChatItem.ViewHolder> {

    List<ChatItem> chatItems;
    Context context;
    CallBack callBack;


    public AdapterChatItem(List<ChatItem> chatItems, Context context, CallBack callBack) {
        this.chatItems = chatItems;
        this.context = context;
        this.callBack = callBack;
    }
    public void remove(int position) {
        chatItems.remove(position);
//        cacheOnline.put(position,false);
//        cacheRead.put(position,false);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, chatItems.size());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_group_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        mItemManger.bindView(holder.itemView, position);
        holder.tvOptionLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onClickDelete(position, chatItems.get(position));
//                mItemManger.closeItem(position);
            }
        });
        holder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onClickDetail(position, chatItems.get(position));
//                mItemManger.closeItem(position);
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
            holder.tvContent.setText(URLDecoder.decode(chatItems.get(position).getLast_message(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.tvTime.setText(chatItems.get(position).getTime_updated());
        if(chatItems.get(position).getListAvatar() != null && chatItems.get(position).getListAvatar().size() > 0){
            if(chatItems.get(position).getListAvatar().size() > 0){
                Picasso.with(context).load(chatItems.get(position).getListAvatar().get(0)).fit().centerCrop().into(holder.imgAvt);
            }if(chatItems.get(position).getListAvatar().size() > 1){
                Picasso.with(context).load(chatItems.get(position).getListAvatar().get(1)).fit().centerCrop().into(holder.imgAvt1);
            }if (chatItems.get(position).getListAvatar().size() > 2) {
                Picasso.with(context).load(chatItems.get(position).getListAvatar().get(2)).fit().centerCrop().into(holder.imgAvt2);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.swipe;
//    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
//        @BindView(R2.id.swipe)
//        SwipeLayout swipe;
        @BindView(R2.id.imgAvt)
        CircleImageView imgAvt;
        @BindView(R2.id.imgAvt1)
        CircleImageView imgAvt1;
        @BindView(R2.id.imgAvt2)
        CircleImageView imgAvt2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface CallBack {
        void onClickDelete(int pos, ChatItem item);

        void onClickCall(int pos, ChatItem item);

        void onClickVideo(int pos, ChatItem item);

        void onClickDetail(int pos, ChatItem item);
    }
}
