package com.skynet.lian.ui.detailpost.viewphoto;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
//import com.halilibo.bvpkotlin.BetterVideoPlayer;
import com.jsibbold.zoomage.ZoomageView;
import com.marcinmoskala.videoplayview.VideoPlayView;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.ui.DownloadService;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @OnClick(R2.id.imgDownload)
    public void onViewClicked() {
        LogUtils.e(message);
//            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
//            Uri Download_Uri = Uri.parse(message);
//            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
////            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
////            request.setAllowedOverRoaming(false);
////            request.setTitle("Downloading");
////            request.setVisibleInDownloadsUi(true);
////            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/lian/" + "/" + message.substring(message.lastIndexOf('/') + 1, message.length()));
//            downloadManager.enqueue(request);

        showToast("Downloading....",AppConstant.POSITIVE);
        getActivity().startService(DownloadService.getDownloadService(getContext(), message, Environment.DIRECTORY_DOWNLOADS+ "/lian/" ));
    }
}
