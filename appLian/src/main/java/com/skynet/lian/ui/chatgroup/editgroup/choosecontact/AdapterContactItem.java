package com.skynet.lian.ui.chatgroup.editgroup.choosecontact;

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
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Profile;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContactItem extends RecyclerView.Adapter<AdapterContactItem.SimpleViewHolder> {

    private final Context mContext;
    SparseBooleanArray cacheOnline;

    private List<Profile> mData;
    SparseBooleanArray cacheChecked;
    ICallback iCallback;
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

    public  class SimpleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.circleImageView)
        CircleImageView circleImageView;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.imgStatus)
        ImageView imgStatus;
        @BindView(R2.id.imgChecked)
        ImageView imgChecked;
        public SimpleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public AdapterContactItem(Context context, List<Profile> data, ICallback iCallback) {
        this.iCallback = iCallback;
        cacheOnline = new SparseBooleanArray();

        cacheChecked =new SparseBooleanArray();
        mContext = context;
        this.mData = data;
        for (int i = 0; i < mData.size(); i++) {
            cacheChecked.put(i,mData.get(i).isChecked());
            cacheOnline.put(i,mData.get(i).getOnline()==1);

        }
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.tab_item_contact, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        holder.tvName.setText(mData.get(position).getName());
        if(mData.get(position).getAvatar() != null && !mData.get(position).getAvatar().isEmpty()){
            Picasso.with(mContext).load(mData.get(position).getAvatar()).fit().into(holder.circleImageView);
        }else{

        }
        holder.imgChecked.setVisibility(cacheChecked.get(position) ? View.VISIBLE : View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCallback.onCallBack(position);
                mData.get(position).setChecked(!mData.get(position).isChecked());
                cacheChecked.put(position,mData.get(position).isChecked());
                holder.imgChecked.setVisibility(cacheChecked.get(position) ? View.VISIBLE : View.INVISIBLE);
            }
        });
        holder.imgStatus.setVisibility(View.VISIBLE);
        if(cacheOnline.get(position)){
            holder.imgStatus.setImageResource(R.drawable.dot_green_stock);
        }else{
            holder.imgStatus.setImageResource(R.drawable.dot_gray_stock);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}