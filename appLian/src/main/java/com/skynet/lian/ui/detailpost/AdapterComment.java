package com.skynet.lian.ui.detailpost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Comment;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import br.com.instachat.emojilibrary.model.layout.EmojiTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolder> {
    List<Comment> list;
    Context context;


    public AdapterComment(List<Comment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).getAvatar() != null && !list.get(position).getAvatar().isEmpty()) {
            Picasso.with(context).load(list.get(position).getAvatar()).fit().centerCrop().into(holder.circleImageView4);
          //  Glide.with(context).asBitmap().load(list.get(position).getAvatar()).into(holder.circleImageView4);
        }
        holder.tvName.setText(list.get(position).getName());
        holder.tvTime.setText(list.get(position).getDate());
//        holder.tvContent.setEmojiconSize(40);
        try {
            holder.tvContent.setText(URLDecoder.decode(list.get(position).getComment(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        holder.tvContent.setText(list.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.circleImageView4)
        CircleImageView circleImageView4;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.tvTime)
        TextView tvTime;
        @BindView(R2.id.tvContent)
        EmojiTextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
