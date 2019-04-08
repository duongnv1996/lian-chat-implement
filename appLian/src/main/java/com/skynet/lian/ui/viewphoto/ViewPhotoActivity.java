package com.skynet.lian.ui.viewphoto;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.jsibbold.zoomage.ZoomageView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.ui.DownloadService;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPhotoActivity extends BaseActivity {
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.img)
    ZoomageView img;

    @Override
    protected int initLayout() {
        return R.layout.activity_view_photo;
    }

    @Override
    protected void initVariables() {
        String message = getIntent().getStringExtra(AppConstant.MSG);
        if(message.contains("http")) {
            Picasso.with(this).load(message).into(img);
        }else{
            File file = new File(message);
            if(file.exists())
                Picasso.with(this).load(file).into(img);
        }
    }

    @Override
    protected void initViews() {

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
    @OnClick(R2.id.imgDownload)
    public void onViewDownloadClicked() {
        String message = getIntent().getStringExtra(AppConstant.MSG);
        LogUtils.e(message);
        showToast("Downloading....",AppConstant.POSITIVE);
        startService(DownloadService.getDownloadService(this, message, Environment.DIRECTORY_DOWNLOADS+ "/lian/" ));
    }
    @OnClick(R2.id.back)
    public void onViewClicked() {
        onBackPressed();
    }
}
