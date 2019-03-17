package com.skynet.lian.ui.makepost;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.MyPlace;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterLocation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MyPlace> list;
    CallBackAddLocation iCallback;
    SparseBooleanArray cache;

    ViewHolder oldHoder;

    public AdapterLocation(List<MyPlace> list, CallBackAddLocation iCallback) {
        this.list = list;
        this.iCallback = iCallback;
        cache = new SparseBooleanArray();

    }

    public void refreshAt(int pos) {
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) cache.put(i, true);
            else

                cache.put(i, false);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 1)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_location_item, parent, false));
        else
            return new ViewFooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_location_item_footer, parent, false));
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return 2;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, final int position) {
        if (h instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) h;
            holder.cbLocation.setText(list.get(position).getName());
            holder.cbLocation.setOnCheckedChangeListener(null);
            holder.cbLocation.setChecked(cache.get(position));
            holder.cbLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        iCallback.onCallBack(position);
                    }
                    if (oldHoder != null) {
                        oldHoder.cbLocation.setChecked(false);
                    }

                    cache.put(position, isChecked);
                    oldHoder = holder;
                }
            });
        } else {
            ViewFooterHolder holder = (ViewFooterHolder) h;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iCallback.onClickAddLocation();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.cbLocation)
        CheckBox cbLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewFooterHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.cbLocation)
        TextView cbLocation;

        public ViewFooterHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CallBackAddLocation extends ICallback {
        void onClickAddLocation();
    }
}
