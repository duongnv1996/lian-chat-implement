package com.skynet.lian.ui.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.models.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContactItem extends RecyclerView.Adapter<AdapterContactItem.SimpleViewHolder> implements Filterable {

    private final Context mContext;
    SparseBooleanArray cacheOnline;

    private List<Profile> contactListFiltered;
    private int type;
    private List<Profile> mData;
    SparseBooleanArray cacheChecked;
    SparseBooleanArray cacheInvite;
    SparseBooleanArray cacheCall;
    CallBackContact iCallback;

    public void add(Profile s, int position) {
        position = position == -1 ? getItemCount() : position;
        contactListFiltered.add(position, s);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            contactListFiltered.remove(position);
            notifyItemRemoved(position);
        }
    }

    public List<Profile> getContactListFiltered() {
        return contactListFiltered;
    }

    public void refreshCache() {
        if(contactListFiltered == null) return;
        for (int i = 0; i < contactListFiltered.size(); i++) {
            cacheChecked.put(i, contactListFiltered.get(i).isChecked());
            if (contactListFiltered.get(i).getId().isEmpty()) {
                cacheInvite.put(i, true);
            } else {
                cacheInvite.put(i, false);

            }
            cacheOnline.put(i, contactListFiltered.get(i).getOnline() == 1);
            cacheCall.put(i, contactListFiltered.get(i).getId() != null && !contactListFiltered.get(i).getId().isEmpty() && contactListFiltered.get(i).getOnline() == 1);
        }
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.circleImageView)
        CircleImageView circleImageView;
        @BindView(R2.id.tvName)
        TextView tvName;
        @BindView(R2.id.tvInvite)
        TextView tvInvite;
        @BindView(R2.id.imgStatus)
        ImageView imgStatus;
        @BindView(R2.id.imgChecked)
        ImageView imgChecked;
        @BindView(R2.id.call)
        ImageView call;
        @BindView(R2.id.video)
        ImageView video;

        public SimpleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AdapterContactItem(Context context, List<Profile> data, CallBackContact iCallback, int type) {
        this.iCallback = iCallback;
        cacheOnline = new SparseBooleanArray();
        cacheInvite = new SparseBooleanArray();
        cacheCall = new SparseBooleanArray();
        this.type = type;
        cacheChecked = new SparseBooleanArray();
        mContext = context;
        this.mData = data;
        this.contactListFiltered = data;
        refreshCache();
    }

    public void setType(int type) {
        this.type = type;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.tab_item_contact, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        try {


            holder.tvName.setText(contactListFiltered.get(position).getName());
            if (contactListFiltered.get(position).getAvatar() != null && !contactListFiltered.get(position).getAvatar().isEmpty()) {
                Picasso.with(mContext).load(contactListFiltered.get(position).getAvatar()).fit().into(holder.circleImageView);
            } else {
                Picasso.with(mContext).load(R.drawable.avt_defaut).fit().into(holder.circleImageView);

            }
            holder.imgChecked.setVisibility(cacheChecked.get(position) ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iCallback.onCallBack(position);
                    if (type == ContactActivity.GROUP_MSG_CODE) {
                        contactListFiltered.get(position).setChecked(!contactListFiltered.get(position).isChecked());
                        cacheChecked.put(position, contactListFiltered.get(position).isChecked());
                        holder.imgChecked.setVisibility(cacheChecked.get(position) ? View.VISIBLE : View.INVISIBLE);
                    }
                }
            });
            holder.imgStatus.setVisibility(View.VISIBLE);
            if (cacheOnline.get(position)) {
                holder.imgStatus.setImageResource(R.drawable.dot_green_stock);
            } else {
                holder.imgStatus.setImageResource(R.drawable.dot_gray_stock);
            }
            if (cacheCall.get(position)) {
                holder.call.setVisibility(View.VISIBLE);
                holder.video.setVisibility(View.VISIBLE);
            } else {
                holder.call.setVisibility(View.GONE);
                holder.video.setVisibility(View.GONE);
            }

            holder.tvInvite.setVisibility(cacheInvite.get(position) ? View.VISIBLE : View.GONE);
            holder.tvInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iCallback.onClickInvite(position, contactListFiltered.get(position));
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = mData;
                } else {
                    List<Profile> filteredList = new ArrayList<>();
                    for (Profile row : mData) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                if ((ArrayList<Profile>) filterResults.values == null) return;
                contactListFiltered = (ArrayList<Profile>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public interface CallBackContact extends ICallback {
        void onClickInvite(int pos, Profile profile);

        void onClickAddfriend(int pos, Profile profile);

        void onClickCall(int pos, Profile profile);

        void onClickVideo(int pos, Profile profile);
    }
}