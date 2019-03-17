package com.skynet.lian.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.skynet.lian.R;

public class RoundLinerLayoutNormal extends LinearLayout {
    public RoundLinerLayoutNormal(Context context) {
        super(context);
        initBackground();
    }

    public RoundLinerLayoutNormal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public RoundLinerLayoutNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    private void initBackground() {
        setBackground(ViewUtils.generateBackgroundWithShadow(this,R.color.white,
                R.dimen.radius_corner,R.color.dusty_orange_30,R.dimen.elevation, Gravity.BOTTOM));
    }
}