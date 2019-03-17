package com.skynet.lian.ui.detailpost;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.skynet.lian.R;
import com.skynet.lian.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditShareBottomSheet extends BottomSheetDialog {
    private final Context context;
    MoreOptionCallback bottomCallback;
    @BindView(R2.id.imgConfirm)
    EditText edtName;
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
    @BindView(R2.id.imgHide)
    ImageView imgHide;
    @BindView(R2.id.confirmlayout)
    LinearLayout confirmlayout;
    @BindView(R2.id.radioGroup)
    RadioGroup radioGroup;


    public EditShareBottomSheet(@NonNull final Context context, final MoreOptionCallback paymentBottomCallback) {

        super(context, R.style.CoffeeDialog);
        this.context = context;
        final View contentView = View.inflate(getContext(), R.layout.bottom_share, null);
        this.bottomCallback = paymentBottomCallback;
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        configureBottomSheetBehavior(contentView);
        setCanceledOnTouchOutside(false);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d("", "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    imgHide.setVisibility(View.VISIBLE);
                }
                else {
                    // keyboard is closed
                    imgHide.setVisibility(View.GONE);

                }
            }
        });
        imgHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(context,edtName);

            }
        });
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
                    KeyboardUtils.hideSoftInput(context,edtName);
                }
            });
        }
    }

    @OnClick({R2.id.OptionRight, R2.id.OptionLeft})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R2.id.OptionRight:
                dismiss();
                int type = 1;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R2.id.radPublic: {
                        type = 1;
                        break;
                    }
                    case R2.id.radOnlyFriend: {
                        type = 2;
                        break;
                    }
                    case R2.id.radOnlyMe: {
                        type = 3;
                        break;
                    }
                }
                bottomCallback.onMoreOptionCallback(edtName.getText().toString(),type);
                break;
            case R2.id.OptionLeft:
                dismiss();
                break;
        }
    }


    public void setText(String edt) {
        edtName.setText(edt);
    }


    public interface MoreOptionCallback {
        void onMoreOptionCallback(String msg, int type);
    }
}
