package com.skynet.lian.ui.chatgroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    List<String> list;
    Context context;
    ICallback iCallback;
    SparseBooleanArray cache;

    public PhotoAdapter(List<String> list, Context context, ICallback iCallback) {
        this.list = list;
        this.context = context;
        this.iCallback = iCallback;
        this.cache = new SparseBooleanArray();
        for (int i = 0; i <list.size(); i++) {
            cache.put(i,false);
        }
    }
    public void clearCacheAndNoty(){
        cache.clear();
        for (int i = 0; i <list.size(); i++) {
            cache.put(i,false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_photo_picker_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        File file = new File(list.get(position));
        if (file.exists()) {
            Picasso.with(context).load(file).fit().centerCrop().into(holder.img);
        }
        holder.btnSend.setVisibility(cache.get(position) ? View.VISIBLE : View.GONE);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    holder.btnSend.setVisibility(View.VISIBLE);
                    cache.put(position,true);
            }
        });
        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnSend.setVisibility(View.GONE);
                cache.put(position,false);
            }
        });
        holder.tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnSend.setVisibility(View.GONE);
                cache.put(position,false);
                iCallback.onCallBack(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.img)
        ImageView img;
        @BindView(R2.id.btnSend)
        FrameLayout btnSend;
   @BindView(R2.id.tvSend)
   TextView tvSend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
