package com.skynet.lian.ui.tabcall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skynet.lian.R;
import com.skynet.lian.models.CallItemModel;

import java.util.List;

public class AdapterCallItem extends RecyclerView.Adapter<AdapterCallItem.SimpleViewHolder> {

    private final Context mContext;
    private List<CallItemModel> mData;

    public void add(CallItemModel s,int position) {
        position = position == -1 ? getItemCount()  : position;
        mData.add(position,s);
        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View view) {
            super(view);
        }
    }

    public AdapterCallItem(Context context,List<CallItemModel> data) {
        mContext = context;
        this.mData = data;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.tab_call_item_list, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
//        holder.title.setText(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}