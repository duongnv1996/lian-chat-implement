package com.skynet.lian.ui.privacy;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Term;
import com.skynet.lian.network.api.ApiResponse;
import com.skynet.lian.network.api.ApiUtil;
import com.skynet.lian.network.api.CallBackBase;
import com.skynet.lian.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PrivacyActivity extends BaseActivity {
    @BindView(R2.id.imgBtn_back_toolbar)
    ImageView imgBtnBackToolbar;
    @BindView(R2.id.tvTitle_toolbar)
    TextView tvTitleToolbar;
    @BindView(R2.id.tv)
    TextView tv;

    @Override
    protected int initLayout() {
        return R.layout.activity_privacy;
    }

    @Override
    protected void initVariables() {
        ApiUtil.createNotTokenApi().getPrivacy().enqueue(new CallBackBase<ApiResponse<Term>>() {
            @Override
            public void onRequestSuccess(Call<ApiResponse<Term>> call, Response<ApiResponse<Term>> response) {
                if (response.isSuccessful() && response.body().getData() != null) {
                    tv.setText(response.body().getData().getContent());
                }
            }

            @Override
            public void onRequestFailure(Call<ApiResponse<Term>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        tvTitleToolbar.setText("Chính sách");
    }

    @Override
    protected int initViewSBAnchor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

    }

    @OnClick(R2.id.imgBtn_back_toolbar)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
