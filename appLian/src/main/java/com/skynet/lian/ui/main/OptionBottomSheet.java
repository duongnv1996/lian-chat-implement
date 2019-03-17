package com.skynet.lian.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import android.support.design.widget.BottomSheetDialog;
import com.skynet.lian.R;
import com.skynet.lian.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OptionBottomSheet extends BottomSheetDialog {
    MoreOptionCallback paymentBottomCallback;
    @BindView(R2.id.imgConfirm)
    ImageView imgConfirm;
    @BindView(R2.id.tvContent)
    TextView tvContent;
    @BindView(R2.id.b)
    View b;
    @BindView(R2.id.OptionRight)
    TextView OptionRight;
    @BindView(R2.id.OptionLeft)
    TextView OptionLeft;
    @BindView(R2.id.view4)
    View view4;
    @BindView(R2.id.confirmlayout)
    LinearLayout confirmlayout;


    public OptionBottomSheet(@NonNull final Context context, final MoreOptionCallback paymentBottomCallback) {

        super(context, R.style.CoffeeDialog);
        View contentView = View.inflate(getContext(), R.layout.bottom_confirm, null);
        this.paymentBottomCallback = paymentBottomCallback;
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        configureBottomSheetBehavior(contentView);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

    }

    public OptionBottomSheet(@NonNull final Context context, int resIcon, Spanned content, String left, String right,final MoreOptionCallback paymentBottomCallback) {

        super(context, R.style.CoffeeDialog);
        View contentView = View.inflate(getContext(), R.layout.bottom_confirm, null);
        this.paymentBottomCallback = paymentBottomCallback;
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        configureBottomSheetBehavior(contentView);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setData(resIcon,content,left,right);
    }

    private void configureBottomSheetBehavior(View contentView) {
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from((View) contentView.getParent());

        if (mBottomSheetBehavior != null) {
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    //showing the different states
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            dismiss(); //if you want the modal to be dismissed when user drags the bottomsheet down
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    @OnClick(R2.id.OptionLeft)
    public void onViewClicked() {

                dismiss();
                paymentBottomCallback.onCancleCallback();
    }

    @OnClick({R2.id.OptionRight})
    public void onViewClicked(View view) {
                dismiss();
                paymentBottomCallback.onMoreOptionCallback();

    }

    public void setData(int resIcon, Spanned content, String left, String right) {
        imgConfirm.setImageResource(resIcon);
        tvContent.setText(content);
        OptionLeft.setText(left);
        OptionRight.setText(right);
    }


    public interface MoreOptionCallback {
        void onMoreOptionCallback();
        void onCancleCallback();
    }
}
