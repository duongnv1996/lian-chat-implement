package com.skynet.lian.ui.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Post;
import com.skynet.lian.models.Profile;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {
    List<Post> list;
    Context context;
    CallBackPost iCallback;
    SparseBooleanArray cacheImage;
    SparseBooleanArray cacheShare;
    Profile profile;
    SparseBooleanArray cacheLike;


    public AdapterList(List<Post> list, Context context, CallBackPost iCallback) {
        this.list = list;
        this.context = context;
        this.iCallback = iCallback;
        cacheImage = new SparseBooleanArray();
        cacheShare = new SparseBooleanArray();
        cacheLike = new SparseBooleanArray();
        refreshCache();
    }

    public void refreshCache() {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getPostShare()!=null && list.get(i).getPostShare().getList_image()!=null && !list.get(i).getPostShare().getList_image().isEmpty()){
                list.get(i).setList_image(list.get(i).getPostShare().getList_image());
            }
            cacheImage.put(i, list.get(i).getList_image() != null && !list.get(i).getList_image().isEmpty());
            cacheLike.put(i, list.get(i).getIs_like() == 1);
            cacheShare.put(i, list.get(i).getPostShare() != null);
        }
    }

    public void remove(int position) {
        list.remove(position);
        cacheLike.put(position, false);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void refrestItemAt(int pos) {
        if (pos < getItemCount()) {
            cacheLike.put(pos, list.get(pos).getIs_like() == 1);
            cacheShare.put(pos, list.get(pos).getPostShare() != null);
            notifyItemChanged(pos + 1);   //todo must +1
        }
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timeline_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (list.get(position).getList_image() != null && !list.get(position).getList_image().isEmpty()) {
//            Picasso.with(context).load(list.get(position).getList_image().get(0).getImg()).fit().centerCrop().into(holder.imageview);
            Glide.with(context).asBitmap().load(list.get(position).getList_image().get(0).getImg()).thumbnail(0.5f).into(holder.imageview);
        }
        holder.card.setVisibility(cacheImage.get(position) ? View.VISIBLE : View.GONE);
        if (profile != null) {
            holder.tvName.setText(profile.getName());
            if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
                Picasso.with(context).load(profile.getAvatar()).fit().centerCrop().into(holder.circleImageView3);
            }
        }
        final Post post = list.get(position);
        String timeAndAddress = post.getDate() + (post.getAddress() != null && post.getAddress().isEmpty() ? "" : ", " + post.getAddress());
        holder.tvTime.setText(timeAndAddress);
        holder.textView14.setText(post.getContent() + "");
        holder.tvLike.setText(post.getNumber_like() + "");
        holder.tvComment.setText(post.getNumber_comment() + "");
        holder.tvShare.setText(post.getNumber_share() + "");

        if (cacheShare.get(position)) {
            if (post.getPostShare() != null) {
                holder.tvShareName.setText("đã chia sẻ bài viết");
                holder.tvShareName.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tvShareName.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickDetail(position, post);
            }
        });

        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickComment(position, post);
            }
        });
        holder.tvLike.setOnCheckedChangeListener(null);
        holder.tvLike.setChecked(cacheLike.get(position));
        holder.tvLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cacheLike.put(position, isChecked);
                post.setIs_like(isChecked ? 1 : 0);
                int number = post.getNumber_like();
                post.setNumber_like(isChecked ? ++number : --number);
                holder.tvLike.setText(post.getNumber_like() + "");
                iCallback.toggleLike(position, post);
            }
        });
        holder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickShare(position, post);
            }
        });
        holder.tvShareName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onClickPostShare(position, post);
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
        @BindView(R2.id.circleImageView3)
        CircleImageView circleImageView3;

        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.card)
        CardView card;
        @BindView(R2.id.tvTime)
        TextView tvTime;
        @BindView(R2.id.imageView11)
        ImageView imageView11;
        @BindView(R2.id.textView14)
        TextView textView14;
        @BindView(R2.id.tvLike)
        CheckBox tvLike;
        @BindView(R2.id.tvComment)
        TextView tvComment;
        @BindView(R2.id.tvShare)
        TextView tvShare;
        @BindView(R2.id.tvShareName)
        TextView tvShareName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CallBackPost {

        void toggleLike(int pos, Post post);

        void onClickDetail(int pos, Post post);

        void onClickComment(int pos, Post post);

        void onClickShare(int pos, Post post);

        void onClickPostShare(int pos, Post post);
    }
}
