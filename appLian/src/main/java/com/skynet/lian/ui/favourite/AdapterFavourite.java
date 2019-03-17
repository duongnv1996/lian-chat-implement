package com.skynet.lian.ui.favourite;

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

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallbackTwoM;
import com.skynet.lian.models.Excercise;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterFavourite extends RecyclerView.Adapter<AdapterFavourite.ViewHolder> {
    List<Excercise> list;
    Context context;
    ICallbackTwoM iCallback;
    SparseBooleanArray cachedBoolen;

    public AdapterFavourite(List<Excercise> list, Context context, ICallbackTwoM iCallback) {
        this.list = list;
        this.context = context;
        this.iCallback = iCallback;
        cachedBoolen = new SparseBooleanArray();
        for (int i = 0; i < this.list.size(); i++) {
            list.get(i).setIs_fav(1);
            cachedBoolen.put(i, true);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourite_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(list.get(i).getName());

        Picasso.with(context).load(list.get(i).getImg()).fit().centerCrop().into(viewHolder.img);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCallback.onCallBack(i);
            }
        });
        viewHolder.checkBox2.setChecked(cachedBoolen.get(i));
        viewHolder.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                iCallback.onCallBackToggle(i, b);
                cachedBoolen.put(i, b);
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
        @BindView(R2.id.checkBox2)
        CheckBox checkBox2;
        @BindView(R2.id.cardView)
        CardView cardView;
        @BindView(R2.id.tvName)
        TextView tvName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
