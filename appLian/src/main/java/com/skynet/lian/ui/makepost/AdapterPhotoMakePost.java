package com.skynet.lian.ui.makepost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class AdapterPhotoMakePost extends RecyclerView.Adapter<AdapterPhotoMakePost.ViewHolder> {

    List<Image> list;
    Context context;
    ICallback iCallback;

    public AdapterPhotoMakePost(List<Image> list, Context context, ICallback iCallback) {
        this.iCallback = iCallback;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.make_post_item_photo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if (list.get(i).getImg() != null && !list.get(i).getImg().isEmpty()) {
//            Picasso.with(context).load(list.get(i).getImg()).fit().centerCrop().into(viewHolder.img);
            Glide.with(context).asBitmap().thumbnail(0.7f).load(list.get(i).getImg()).into(viewHolder.img);

        } else if (list.get(i) != null && list.get(i).getFile() != null && list.get(i).getFile().exists()) {
//            Picasso.with(context).load(list.get(i).getFile()).fit().centerCrop().into(viewHolder.img);
            Glide.with(context).asBitmap().thumbnail(0.7f).load(list.get(i).getFile()).into(viewHolder.img);
        }
        viewHolder.clear.setVisibility(View.VISIBLE);
        viewHolder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onCallBack(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.imageView16)
        ImageView img;
        @BindView(R2.id.clear)
        ImageView clear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
