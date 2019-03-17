package com.skynet.lian.ui.searchfriend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Profile;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContactItem extends RecyclerView.Adapter<AdapterContactItem.SimpleViewHolder> {

    private final Context mContext;

    private List<Profile> mData;
    com.skynet.lian.ui.contact.AdapterContactItem.CallBackContact iCallback;
    SparseBooleanArray cacheChecked;
    SparseBooleanArray cacheInvite;
    SparseBooleanArray cacheOnline;
    SparseBooleanArray cacheFriend;
    SparseBooleanArray cacheCall;

    public void add(Profile s, int position) {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void refreshCache() {
        for (int i = 0; i < mData.size(); i++) {
            cacheChecked.put(i, mData.get(i).isChecked());
            cacheInvite.put(i, mData.get(i).getId() == null || mData.get(i).getId().isEmpty());
            cacheOnline.put(i, mData.get(i).getOnline() == 1);
            cacheFriend.put(i, mData.get(i).getIs_friend() == 0);
            cacheCall.put(i,mData.get(i).getIs_friend() == 2 && mData.get(i).getId() != null && !mData.get(i).getId().isEmpty() && mData.get(i).getOnline() == 1);
        }
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.circleImageView)
        CircleImageView circleImageView;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.tvPhone)
        TextView tvPhone;
        @BindView(R2.id.tvAddFriend)
        TextView tvAddFriend;
        @BindView(R2.id.imgStatus)
        ImageView imgStatus;
        @BindView(R2.id.tvInvite)
        TextView tvInvite;
        @BindView(R2.id.call)
        ImageView call;
        @BindView(R2.id.video)
        ImageView video;

        public SimpleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AdapterContactItem(Context context, List<Profile> data, com.skynet.lian.ui.contact.AdapterContactItem.CallBackContact iCallback) {
        this.iCallback = iCallback;
        mContext = context;
        cacheOnline = new SparseBooleanArray();
        cacheInvite = new SparseBooleanArray();
        cacheCall = new SparseBooleanArray();
        cacheFriend = new SparseBooleanArray();

        cacheChecked = new SparseBooleanArray();
        this.mData = data;
        refreshCache();
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.search_item_contact, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.tvName.setText(mData.get(position).getName());
        holder.tvPhone.setText("Số điện thoại: " + mData.get(position).getPhone());
        if (mData.get(position).getAvatar() != null && !mData.get(position).getAvatar().isEmpty()) {
            Picasso.with(mContext).load(mData.get(position).getAvatar()).fit().into(holder.circleImageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onCallBack(position);
            }
        });

        if (cacheOnline.get(position)) {
            holder.imgStatus.setImageResource(R.drawable.dot_green_stock);
        } else {
            holder.imgStatus.setImageResource(R.drawable.dot_gray_stock);
        }
        if (cacheFriend.get(position)) {
            holder.tvAddFriend.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddFriend.setVisibility(View.GONE);
        }
        if (cacheCall.get(position)) {
            holder.call.setVisibility(View.VISIBLE);
            holder.video.setVisibility(View.VISIBLE);
        } else {
            holder.call.setVisibility(View.GONE);
            holder.video.setVisibility(View.GONE);
        }

        holder.tvInvite.setVisibility(cacheInvite.get(position) ? View.VISIBLE : View.GONE);
        holder.tvInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickInvite(position, mData.get(position));
            }
        });
        holder.tvAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickAddfriend(position, mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}