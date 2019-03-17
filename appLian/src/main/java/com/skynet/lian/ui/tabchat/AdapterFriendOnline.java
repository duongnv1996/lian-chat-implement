package com.skynet.lian.ui.tabchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Profile;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFriendOnline extends RecyclerView.Adapter<AdapterFriendOnline.ViewHolder> {

    List<Profile> list;
    Context context;
    ICallback callBack;
    SparseBooleanArray cacheOnline;


    public AdapterFriendOnline(List<Profile> chatItems, Context context, ICallback callBack) {
        this.list = chatItems;
        this.context = context;
        this.callBack = callBack;
        this.cacheOnline = new SparseBooleanArray();
        for (int i = 0; i < chatItems.size(); i++) {
            cacheOnline.put(i, false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AppConstant.TYPE_HEADER) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_chat_friend_header_list, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_chat_friend_item_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (getItemViewType(position) == AppConstant.TYPE_HEADER) {
            if (AppController.getInstance().getmProfileUser().getAvatar() != null && !AppController.getInstance().getmProfileUser().getAvatar().isEmpty())
                Picasso.with(context).load(list.get(position).getAvatar()).fit().centerCrop().into(holder.circleImageView);
        } else {
            holder.tvName.setText(list.get(position).getName());
            if (list.get(position).getAvatar() != null && !list.get(position).getAvatar().isEmpty())
                Picasso.with(context).load(list.get(position).getAvatar()).fit().centerCrop().into(holder.circleImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onCallBack(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return AppConstant.TYPE_HEADER;

        } else {
            return AppConstant.TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notiDateChange() {
        cacheOnline.clear();
        for (int i = 0; i < list.size(); i++) {
            cacheOnline.put(i, false);
        }
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.circleImageView)
        CircleImageView circleImageView;
        @BindView(R2.id.imgStatus)
        ImageView imgStatus;
        @BindView(R2.id.tvName)
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
