package com.skynet.lian.ui.profile;

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

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.ViewHolder> {
    List<Image> list;
    Context context;
    ICallback iCallback;


    public AdapterVideo(List<Image> list, Context context, ICallback iCallback) {
        this.list = list;
        this.context = context;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_timeline_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(list.get(position).getVideo()!=null &&!list.get(position).getVideo().isEmpty()){
//            Picasso.with(context).load(list.get(position).getVideo()).into(holder.imageview);
            Glide.with(context).asBitmap().load(list.get(position).getVideo()).into(holder.imageview);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onCallBack(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.imageview)
        ImageView imageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
