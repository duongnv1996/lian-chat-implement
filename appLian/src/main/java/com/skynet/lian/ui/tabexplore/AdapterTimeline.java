package com.skynet.lian.ui.tabexplore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.CallbackClickPhotoItem;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterTimeline extends RecyclerView.Adapter<AdapterTimeline.ViewHolder> {
    List<Post> list;
    Context context;
    ICallBackPost iCallback;

    CallbackClickPhotoItem callbackClickPhotoItem;



    public AdapterTimeline(List<Post> list, Context context, ICallBackPost iCallback, CallbackClickPhotoItem callbackClickPhotoItem) {
        this.list = list;
        this.context = context;
        this.iCallback = iCallback;
        this.callbackClickPhotoItem = callbackClickPhotoItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }
    public void refreshCache() {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getPostShare()!=null && list.get(i).getPostShare().getList_image()!=null && !list.get(i).getPostShare().getList_image().isEmpty()){
                list.get(i).setList_image(list.get(i).getPostShare().getList_image());
            }
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Post post = list.get(position);
        if (post.getAvatar() != null && !post.getAvatar().isEmpty()) {
            Picasso.with(context).load(post.getAvatar()).fit().centerCrop().into(holder.circleImageView);
        }
        holder.tvName.setText(post.getFullname());
        holder.tvTime.setText(post.getDate());
        holder.tvNumberSeen.setText(post.getNumber_seen() + "");
        holder.rcvPhoto.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.rcvPhoto.setHasFixedSize(true);
        holder.rcvPhoto.setAdapter(new AdapterPhotoNewfeeds(post.getList_image(), context, post, callbackClickPhotoItem));
        holder.tvContent.setText(post.getContent());
        if (post.getPostShare() != null) {
            holder.tvShare.setText("đã chia sẻ bài viết");
        } else {
            holder.tvShare.setText("");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onCallBack(position);
            }
        });

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickProfile(post.getUser_id());
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickProfile(post.getUser_id());
            }
        });
        holder.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickProfile(post.getUser_id());
            }
        });
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.circleImageView)
        CircleImageView circleImageView;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.tvTime)
        TextView tvTime;
        @BindView(R2.id.tvNumberSeen)
        TextView tvNumberSeen;
        @BindView(R2.id.rcvPhoto)
        RecyclerView rcvPhoto;
        @BindView(R2.id.tvContent)

        TextView tvContent;
        @BindView(R2.id.tvShare)
        TextView tvShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ICallBackPost extends ICallback {
        void onClickProfile(String id);
    }
}
