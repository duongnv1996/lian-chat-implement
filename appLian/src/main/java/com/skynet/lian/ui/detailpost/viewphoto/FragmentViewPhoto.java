package com.skynet.lian.ui.detailpost.viewphoto;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
//import com.halilibo.bvpkotlin.BetterVideoPlayer;
import com.jsibbold.zoomage.ZoomageView;
import com.marcinmoskala.videoplayview.VideoPlayView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class FragmentViewPhoto extends BaseFragment {

    @BindView(R2.id.img)
    ZoomageView img;
    @BindView(R2.id.player)
    JzvdStd videoPlayer;

    String message;
    int type;

    public static FragmentViewPhoto newInstance(String url, int type) {

        Bundle args = new Bundle();
        args.putString(AppConstant.MSG, url);
        args.putInt("type", type);
        FragmentViewPhoto fragment = new FragmentViewPhoto();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void doAction() {

    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_view_photo;
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);

    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
//        videoPlayer.pause();
    }

    @Override
    protected void initVariables() {
        message = getArguments().getString(AppConstant.MSG);
        type = getArguments().getInt("type");
        if (message.contains("http")) {
//            Picasso.with(getContext()).load(message).into(img);
            Glide.with(getContext()).asBitmap().load(message).thumbnail(0.7f).into(img);
        } else {
            File file = new File(message);
            if (file.exists())
                Picasso.with(getContext()).load(file).into(img);
        }
        if (type == 1) {
            videoPlayer.setVisibility(View.GONE);
        } else {
//            videoPlayer.setVideoUrl(message);
            videoPlayer.setUp(message
                    , "" , Jzvd.SCREEN_WINDOW_NORMAL);
            videoPlayer.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
        }
    }

//
//    @OnClick(R2.id.img)
//    public void onViewClicked() {
//        if (type == 2) {
//            Intent i = new Intent(getActivity(), MyVideoPlayerActivity.class);
//            i.putExtra(AppConstant.MSG, message);
//            startActivity(i);
//        }
//    }
}
