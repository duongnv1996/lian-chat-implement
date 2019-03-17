package com.skynet.lian.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.skynet.lian.R;
import com.skynet.lian.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by thaopt on 8/28/17.
 */

public class DialogCenterHelp extends Dialog {

    private Context mContext;
    private DialogCenterHelpClickListener mListener;

    public DialogCenterHelp(@NonNull Context context, DialogCenterHelpClickListener listener) {
        super(context);
        this.mListener = listener;
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_center_help);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
    }




    @OnClick(R2.id.tvEmail)
    public void onTvEmailClicked() {
        mListener.onEmailClick();

    }

    @OnClick(R2.id.tvPhone)
    public void onTvPhoneClicked() {
        mListener.onPhoneClick();

    }

    //callback

    public interface DialogCenterHelpClickListener {
        void onEmailClick();

        void onPhoneClick();
    }


    public void setDialogOneButtonClick(DialogCenterHelpClickListener listener) {
        this.mListener = listener;
    }
}
