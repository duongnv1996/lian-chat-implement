package com.skynet.lian.ui.tabexplore;

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
import com.skynet.lian.interfaces.CallbackClickPhotoItem;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterPhotoNewfeeds extends RecyclerView.Adapter<AdapterPhotoNewfeeds.ViewHolder> {
    List<Image> list;
    Context context;
    CallbackClickPhotoItem iCallback;
    SparseBooleanArray cacheVideo;
    Post post;

    public AdapterPhotoNewfeeds(List<Image> list, Context context, Post post, CallbackClickPhotoItem iCallback) {
        this.post = post;
        this.list = list;
        this.context = context;
        this.iCallback = iCallback;
        cacheVideo = new SparseBooleanArray();
        for (int i = 0; i < list.size(); i++) {
            cacheVideo.put(i, list.get(i).getType() == 2);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_newfeed, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (list.get(position).getImg() != null && !list.get(position).getImg().isEmpty()) {
//            Picasso.with(context).load(list.get(position).getVideo()).into(holder.imageview);
            Glide.with(context).asBitmap().load(list.get(position).getImg()).into(holder.imageview);
        }
        holder.imgPlay.setVisibility(cacheVideo.get(position) ? View.VISIBLE : View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickPhoto(position,post);
            }
        });
//        if(getItemCount()==1){
//            ViewGroup.LayoutParams params = holder.imageview.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            holder.imageview.setLayoutParams(params);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.imageview)
        ImageView imageview;
        @BindView(R2.id.imgPlay)
        ImageView imgPlay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
