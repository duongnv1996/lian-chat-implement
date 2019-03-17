package com.skynet.lian.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mindorks.paracamera.Camera;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.Profile;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.base.BaseFragment;
import com.skynet.lian.ui.makepost.MakePostActivity;
import com.skynet.lian.ui.setting.SettingActivity;
import com.skynet.lian.ui.viewphoto.ViewPhotoActivity;
import com.skynet.lian.ui.views.Glide4Engine;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.ui.views.ViewpagerNotSwipe;
import com.skynet.lian.utils.AppConstant;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.iwf.photopicker.PhotoPicker;

public class ProfileActivity extends BaseActivity implements UploadContract.View, ChoosePhotoBottomSheet.ChoosePhotoOptionCallback {

    @BindView(R2.id.imgMore)
    ImageView imgMore;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.viewpager)
    ViewpagerNotSwipe viewpager;
    @BindView(R2.id.bottomNavigationViewEx)
    BottomNavigationViewEx bnve;
    @BindView(R2.id.imgCover)
    ImageView imgCover;
    @BindView(R2.id.imgAvatar)
    CircleImageView imgAvatar;
    @BindView(R2.id.tvName)
    TextView tvName;
    @BindView(R2.id.imgBack)
    ImageView imgBack;
    @BindView(R2.id.tvTitle)
    TextView tvTitle;
    private AdapterProfileViewpager adapter;
    private UploadContract.Presenter presenter;
    private ProgressDialogCustom dialogLoading;
    private int typeUpload;
    Camera camera;
    private Profile profile;
    private ChoosePhotoBottomSheet choosePhotoBottomSheet;

    @Override
    protected int initLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initVariables() {
        dialogLoading = new ProgressDialogCustom(this);
        choosePhotoBottomSheet = new ChoosePhotoBottomSheet(this, this);
        presenter = new UploadPresenter(this);
        Profile profile = AppController.getInstance().getmProfileUser();
        if (profile != null) {
            this.profile = profile;
            tvName.setText(profile.getName());
            if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
                Picasso.with(this).load(profile.getAvatar()).fit().centerCrop().into(imgAvatar);
            }
            if (profile.getCover() != null && !profile.getCover().isEmpty()) {
                Picasso.with(this).load(profile.getCover()).fit().centerCrop().into(imgCover);
            }
        }
// Build the camera
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    @Override
    protected void initViews() {
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.enableItemShiftingMode(false);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Text-Medium.otf");
        bnve.setTypeface(custom_font);
        adapter = new AdapterProfileViewpager(getSupportFragmentManager(),null);
        viewpager.setAdapter(adapter);
        bnve.setupWithViewPager(viewpager);
        viewpager.setPagingEnabled(true);
        viewpager.setCurrentItem(0);
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


    @OnClick({R2.id.imgBack, R2.id.imgMore})
    public void onView2Clicked(View view) {
        switch (view.getId()) {
            case R2.id.imgBack:
                onBackPressed();
                break;
            case R2.id.imgMore:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    private void choosePhoto() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
//                    PhotoPicker.builder()
//                            .setPhotoCount(1)
//                            .setShowCamera(true)
//                            .setShowGif(true)
//                            .setPreviewEnabled(false)
//                            .start(ProfileActivity.this, PhotoPicker.REQUEST_CODE);
                    Matisse.from(ProfileActivity.this)
                            .choose(typeUpload == 1 || typeUpload == 2 ? MimeType.ofImage() : MimeType.ofAll())
                            .countable(true)
                            .capture(true)
                            .captureStrategy(new CaptureStrategy(true, "com.skynet.lian.provider","Pictures"))

                            .maxSelectable(9)
//                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new Glide4Engine())
                            .forResult(PhotoPicker.REQUEST_CODE);
                } else {

                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });


    }

    private boolean checkPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 111:
                if (grantResults.length > 2 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    checkPermissionGranted();
                    return;
                } else {
                    choosePhoto();
                }
                return;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            ((BaseFragment) adapter.getRegisteredFragment(viewpager.getCurrentItem())).doAction();

            return;
        }
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                List<String> mSelected;
                mSelected = Matisse.obtainPathResult(data);
//                ArrayList<String> photos =
//                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (String urlPath : mSelected) {
                    File file = new File(urlPath);
                    if (!file.exists()) {
                        Toast.makeText(this, "File không tồn tại.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (typeUpload != 1 && typeUpload != 2) {
                        LogUtils.e(file.getAbsoluteFile());
                        List<Image> list = new ArrayList<>();
                        Image image = new Image();
                        image.setFile(file);
                        image.setId(-1);
                        list.add(image);

                        Intent i = new Intent(ProfileActivity.this, MakePostActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelableArrayList(AppConstant.MSG, (ArrayList<? extends Parcelable>) list);
                        i.putExtra(AppConstant.BUNDLE, b);
                        startActivityForResult(i, 1000);

                    } else {
                        CropImage.activity(Uri.fromFile(file))
                                .setAspectRatio(4, 3)
//                            .setRequestedSize(800, 800, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                                .start(this);
                    }

                    break;
                }

//                listImage.addAll(listPickup);
//                adapterPhoto.notifyItemInserted(adapterPhoto.getItemCount() - 1);
//                recyclerView2.smoothScrollToPosition(adapterPhoto.getItemCount() - 1);
//                if (postToEdit != null && postToEdit.getPost() != null) {
//                    presenter.addPhoto(listPickup, postToEdit.getPost().getId());
//                }

            }

//            File fileImage = CommomUtils.scanFileMetisse(this, Matisse.obtainResult(data).get(0));
            // File fileImage = new File(Matisse.obtainPathResult(data).get(0));

            //  LogUtils.d("Matisse", "mSelected: " + (fileImage.exists() ? fileImage.getAbsolutePath() : "null"));

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File file = new File(resultUri.getPath());
                if (typeUpload == 1) {
                    Picasso.with(this).load(file).fit().centerCrop().into(imgAvatar);
                    ((BaseFragment) adapter.getRegisteredFragment(viewpager.getCurrentItem())).doAction();
                    presenter.upload(file, typeUpload);

                } else if (typeUpload == 2) {
                    Picasso.with(this).load(file).fit().centerCrop().into(imgCover);
                    presenter.upload(file, typeUpload);

                } else {
                    LogUtils.e(file.getAbsoluteFile());
                    List<Image> list = new ArrayList<>();
                    Image image = new Image();
                    image.setFile(file);
                    image.setId(-1);
                    list.add(image);

                    Intent i = new Intent(ProfileActivity.this, MakePostActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelableArrayList(AppConstant.MSG, (ArrayList<? extends Parcelable>) list);
                    i.putExtra(AppConstant.BUNDLE, b);
                    startActivityForResult(i, 1000);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            String path = camera.getCameraBitmapPath();
            File file = new File(path);
            LogUtils.e(path);

            if (file.exists()) {
                List<Image> list = new ArrayList<>();
                Image image = new Image();
                image.setFile(file);
                image.setId(-1);
                list.add(image);

                Intent i = new Intent(ProfileActivity.this, MakePostActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList(AppConstant.MSG, (ArrayList<? extends Parcelable>) list);
                i.putExtra(AppConstant.BUNDLE, b);
                startActivityForResult(i, 1000);
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R2.id.imgUploadCover, R2.id.imgAvatar, R2.id.imgAddNewMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R2.id.imgAddNewMessage:
//                choosePhotoBottomSheet.show();
                startActivityForResult(new Intent(ProfileActivity.this,MakePostActivity.class),1000);
                break;
            case R2.id.imgUploadCover:
                typeUpload = 2;
                choosePhoto();
                break;
            case R2.id.imgAvatar:
                typeUpload = 1;
                choosePhoto();

                break;
        }
    }

    @OnClick(R2.id.imgCover)
    public void onClickImgCover() {
        if (profile!=null && profile.getCover() != null && !profile.getCover().isEmpty()) {
            Intent intent = new Intent(ProfileActivity.this, ViewPhotoActivity.class);
            intent.putExtra(AppConstant.MSG,profile.getCover() );
            startActivity(intent);
        }
    }

    @Override
    public void onSucessUploadAvat() {
        setResult(RESULT_OK);
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    public void showProgress() {
        dialogLoading.showDialog();
    }

    @Override
    public void hiddenProgress() {
        dialogLoading.hideDialog();
    }

    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);
        showToast(message, AppConstant.NEGATIVE);
    }

    @Override
    public void onErrorAuthorization() {
        showDialogExpired();
    }

    @Override
    public void onClickCapturePhoto() {
        typeUpload = 3;
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    try {
                        camera.takePicture();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    @Override
    public void onClickGalleryPhoto() {
        typeUpload = 3;
        choosePhoto();

    }
}
