package com.skynet.lian.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skynet.lian.R;

import java.util.ArrayList;

public class AdapterMenu extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> list;
    LayoutInflater layoutInflater;

    public AdapterMenu(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.popup_menu_chat, parent, false);
        TextView tv = convertView.findViewById(R.id.TextView);
        tv.setText(list.get(position));
        switch (position) {
            case 0: {
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_comment_dots), null, null, null);
                break;
            }
            case 1: {
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic__ionicons_svg_ios_people), null, null, null);
                break;
            }
            case 2: {
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic__ionicons_svg_md_contact), null, null, null);
                break;
            }
            case 3: {
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic_cog), null, null, null);
                break;
            }
            case 4: {
                tv.setCompoundDrawablesRelativeWithIntrinsicBounds(context.getDrawable(R.drawable.ic__ionicons_svg_md_home), null, null, null);
                break;
            }
        }
        return convertView;
    }
}
