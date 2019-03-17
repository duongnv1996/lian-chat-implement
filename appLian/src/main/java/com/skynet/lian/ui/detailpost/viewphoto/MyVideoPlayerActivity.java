package com.skynet.lian.ui.detailpost.viewphoto;

import android.net.Uri;
import android.os.Bundle;

//import com.halilibo.bvpkotlin.BetterVideoPlayer;
import com.marcinmoskala.videoplayview.VideoPlayView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.utils.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class MyVideoPlayerActivity extends BaseActivity {
    @BindView(R2.id.player)
    JzvdStd player;

    @Override
    protected int initLayout() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void initVariables() {
        player.setUp(getIntent().getStringExtra(AppConstant.MSG)
                , "" , Jzvd.SCREEN_WINDOW_NORMAL);
//        player.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
//        player.setVideoUrl(getIntent().getStringExtra(AppConstant.MSG));
//        player.setAutoplay(true);
    }

    @Override
    protected void initViews() {

    }
    @Override
    public void onBackPressed() {
        if (player.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        player.releaseAllVideos();
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
}
