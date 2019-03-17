package com.skynet.lian.ui.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import com.skynet.lian.R;
import com.skynet.lian.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePhotoBottomSheet extends BottomSheetDialog {


    @BindView(R2.id.btnCapture)
    Button btnCapture;
    @BindView(R2.id.btnGallery)
    Button btnGallery;
    @BindView(R2.id.btnback)
    Button btnback;
    ChoosePhotoOptionCallback paymentBottomCallback;

    public ChoosePhotoBottomSheet(@NonNull final Context context, final ChoosePhotoOptionCallback paymentBottomCallback) {

        super(context, R.style.CoffeeDialog);
        View contentView = View.inflate(getContext(), R.layout.bottom_choose_photo, null);
        this.paymentBottomCallback = paymentBottomCallback;
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        configureBottomSheetBehavior(contentView);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

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


    @OnClick({R2.id.btnCapture, R2.id.btnGallery, R2.id.btnback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R2.id.btnCapture:
                paymentBottomCallback.onClickCapturePhoto();
                dismiss();
                break;
            case R2.id.btnGallery:
                paymentBottomCallback.onClickGalleryPhoto();
                dismiss();

                break;
            case R2.id.btnback:
                dismiss();

                break;
        }
    }


    public interface ChoosePhotoOptionCallback {
        void onClickCapturePhoto();

        void onClickGalleryPhoto();
    }
}
