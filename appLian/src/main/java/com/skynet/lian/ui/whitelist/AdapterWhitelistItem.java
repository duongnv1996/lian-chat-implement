package com.skynet.lian.ui.whitelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterWhitelistItem extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> implements Filterable {

    List<Profile> chatItems;
    Context context;
    CallBack callBack;
    private List<Profile> contactListFiltered;
    public AdapterWhitelistItem(List<Profile> chatItems, Context context, CallBack callBack) {
        this.chatItems = chatItems;
        this.context = context;
        this.callBack = callBack;
        this.contactListFiltered = chatItems;
    }

    public void notiDateChange() {

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_white_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder oldholder, final int position) {
        if (oldholder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) oldholder;
            mItemManger.bindView(holder.itemView, position);
            holder.tvName.setText(contactListFiltered.get(position).getName());
            if (contactListFiltered.get(position).getAvatar() != null && !contactListFiltered.get(position).getAvatar().isEmpty()) {
                Picasso.with(context).load(contactListFiltered.get(position).getAvatar()).fit().centerCrop().into(holder.circleImageView);
            }
            holder.tvOptionLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemManger.closeItem(position);
                    callBack.onClickDelete(position, contactListFiltered.get(position));
                }
            });
        }
    }

    public void remove(int position) {
        contactListFiltered.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, chatItems.size());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = chatItems;
                } else {
                    List<Profile> filteredList = new ArrayList<>();
                    for (Profile row : chatItems) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if( (ArrayList<Profile>) filterResults.values == null) return;
                contactListFiltered = (ArrayList<Profile>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tvOptionLeft)
        FrameLayout tvOptionLeft;
        @BindView(R2.id.circleImageView)
        CircleImageView circleImageView;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.imgStatus)
        ImageView imgStatus;
        @BindView(R2.id.layoutContent)
        LinearLayout layoutContent;
        @BindView(R2.id.swipe)
        SwipeLayout swipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface CallBack {
        void onClickDelete(int pos, Profile item);


    }
}
