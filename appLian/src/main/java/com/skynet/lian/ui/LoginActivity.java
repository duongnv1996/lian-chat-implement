package com.skynet.lian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Profile;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.splash.SplashActivity;
import com.skynet.lian.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R2.id.edt)
    EditText edt;
    @BindView(R2.id.btn)
    Button btn;

    @Override
    protected int initLayout() {
        return R.layout.activity_input;
    }

    @Override
    protected void initVariables() {

        //AppController.getInstance().setmProfileUser(null);
        if (AppController.getInstance().getmSetting().getString("phone") != null && !AppController.getInstance().getmSetting().getString("phone").isEmpty()) {
            edt.setText(AppController.getInstance().getmSetting().getString("phone"));
            onViewClicked();
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        // getmSocket().leave();
    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.btn)
    public void onViewClicked() {
        if (edt.getText().toString().isEmpty()) return;
        ApiUtil.createNotTokenApi().login(edt.getText().toString()).enqueue(new CallBackBase<ApiResponse<Profile>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Profile>> call, Response<ApiResponse<Profile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == AppConstant.CODE_API_SUCCESS) {
                        AppController.getInstance().getmSetting().put("phone", edt.getText().toString());
                        AppController.getInstance().setmProfileUser(response.body().getData());
                        startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                        finish();
                    } else {
                        showToast(response.body().getMessage(), AppConstant.NEGATIVE);
                    }
                } else {
                    showToast(response.message(), AppConstant.NEGATIVE);
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Profile>> call, Throwable t) {
                showToast(t.getMessage(), AppConstant.NEGATIVE);
            }
        });
    }
}
