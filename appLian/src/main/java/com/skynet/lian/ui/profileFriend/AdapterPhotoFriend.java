package com.skynet.lian.ui.profileFriend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Image;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterPhotoFriend extends RecyclerView.Adapter<AdapterPhotoFriend.ViewHolder> {
    List<Image> list;
    Context context;
    ICallback iCallback;

    SparseBooleanArray cacheVideo;


    public AdapterPhotoFriend(List<Image> list, Context context, ICallback iCallback) {
        this.list = list;
        this.context = context;
        this.iCallback = iCallback;
        cacheVideo = new SparseBooleanArray();
        refreshCache();

    }

    public void refreshCache() {
        for (int i = 0; i < list.size(); i++) {
            cacheVideo.put(i, list.get(i).getType() == 2);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_friend_photo_timeline_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (list.get(position).getImg() != null && !list.get(position).getImg().isEmpty()) {
//            Picasso.with(context).load(list.get(position).getVideo()).into(holder.imageview);
            Glide.with(context).asBitmap().load(list.get(position).getImg()).into(holder.imageview);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onCallBack(position);
            }
        });

        holder.imgPlay.setVisibility(cacheVideo.get(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.imageview)
        ImageView imageview;
        @BindView(R2.id.imageView12)
        ImageView imgPlay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
