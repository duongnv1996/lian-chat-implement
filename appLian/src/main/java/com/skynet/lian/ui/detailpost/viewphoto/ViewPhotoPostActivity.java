package com.skynet.lian.ui.detailpost.viewphoto;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.Image;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.utils.AppConstant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPhotoPostActivity extends BaseActivity {

    @BindView(R2.id.viewpager)
    ViewPager viewpager;
    @BindView(R2.id.back)
    ImageView back;
    AdapterViewPhotoViewpager adapter;
    int pos;
    List<Image> list;

    @Override
    protected int initLayout() {
        return R.layout.activity_view_photo_post;
    }

    @Override
    protected void initVariables() {
        Bundle b = getIntent().getBundleExtra(AppConstant.BUNDLE);
        if (b != null) {
            pos = b.getInt("pos");
            list = b.getParcelableArrayList("list");

            adapter = new AdapterViewPhotoViewpager(getSupportFragmentManager(), list);
            viewpager.setAdapter(adapter);
            viewpager.setCurrentItem(pos);
//            if(list.get(pos).getType() == 2){
//                Intent i = new Intent(ViewPhotoPostActivity.this, MyVideoPlayerActivity.class);
//                i.putExtra(AppConstant.MSG, list.get(pos).getImg());
//                startActivity(i);
//            }
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

    @OnClick(R2.id.back)
    public void onViewClicked() {
        onBackPressed();
    }
}
