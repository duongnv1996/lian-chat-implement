package com.skynet.lian.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

;


public class ViewpagerNotSwipe extends android.support.v4.view.ViewPager {
    private boolean enabled;
    public ViewpagerNotSwipe(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public ViewpagerNotSwipe(@NonNull Context context) {
        super(context);
        this.enabled = false;

    }
}
