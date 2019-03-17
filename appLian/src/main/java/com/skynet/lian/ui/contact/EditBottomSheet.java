package com.skynet.lian.ui.contact;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.lian.R;
import com.skynet.lian.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBottomSheet extends BottomSheetDialog {
    private final Context context
            ;
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
    @BindView(R2.id.confirmlayout)
    LinearLayout confirmlayout;


    public EditBottomSheet(@NonNull final Context context, final MoreOptionCallback paymentBottomCallback) {

        super(context, R.style.CoffeeDialog);
        this.context = context;
        View contentView = View.inflate(getContext(), R.layout.bottom_edit, null);
        this.bottomCallback = paymentBottomCallback;
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        configureBottomSheetBehavior(contentView);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        edtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                    onViewClicked(OptionRight);
                }
                return false;
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
                }
            });
        }
    }

    @OnClick({R2.id.OptionRight})
    public void onViewClicked(View view) {

                if(edtName.getText().toString().isEmpty()){
                    Toast.makeText(context,"Vui lòng nhập tên nhóm",Toast.LENGTH_SHORT).show();
                    return;
                }
                dismiss();

                bottomCallback.onMoreOptionCallback(edtName.getText().toString());

    }

    @OnClick({R2.id.OptionLeft})
    public void onViewLeftClicked(View view) {

                dismiss();

    }


    public void setText(String edt){
        edtName.setText(edt);
    }


    public interface MoreOptionCallback {
        void onMoreOptionCallback(String msg);
    }
}
