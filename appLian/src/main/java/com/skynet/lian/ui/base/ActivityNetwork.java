package com.skynet.lian.ui.base;



import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityNetwork extends BaseActivity {
    @Override
    protected int initLayout() {
        return R.layout.dialog_lost_connect;
    }

    @Override
    protected void initVariables() {
        AppController.getInstance().setToogleInternet(true);
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.imgBack)
    public void onclickBack() {
        onBackPressed();
    }

    @Override
    public void onNetworkTurnOn() {
        super.onNetworkTurnOn();
        onBackPressed();
    }

    @Override
    public String getTag(){
        super.getTag();
        return "ActivityNetwork";
    }
    @Override
    protected int initViewSBAnchor() {
        return 0;
    }
}
