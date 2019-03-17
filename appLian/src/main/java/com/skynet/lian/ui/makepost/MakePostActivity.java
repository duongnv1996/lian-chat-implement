package com.skynet.lian.ui.makepost;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mindorks.paracamera.Camera;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.manager.CameraOutputModel;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.application.AppController;
import com.skynet.lian.interfaces.ICallback;
import com.skynet.lian.interfaces.SnackBarCallBack;
import com.skynet.lian.models.Image;
import com.skynet.lian.models.MyPlace;
import com.skynet.lian.models.Place;
import com.skynet.lian.models.Post;
import com.skynet.lian.ui.base.BaseActivity;
import com.skynet.lian.ui.detailpost.DetailPostActivity;
import com.skynet.lian.ui.main.OptionBottomSheet;
import com.skynet.lian.ui.profile.ChoosePhotoBottomSheet;
import com.skynet.lian.ui.views.Glide4Engine;
import com.skynet.lian.ui.views.ProgressDialogCustom;
import com.skynet.lian.utils.AppConstant;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.iwf.photopicker.PhotoPicker;

public class MakePostActivity extends BaseActivity implements ICallback, ChoosePhotoBottomSheet.ChoosePhotoOptionCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, MakePostContract.View {
    @BindView(R2.id.btnBack)
    ImageView btnBack;
    @BindView(R2.id.textView12)
    TextView textView12;
    @BindView(R2.id.edtContent)
    EditText edtContent;
    @BindView(R2.id.textView13)
    TextView textView13;
    @BindView(R2.id.rcvAddress)
    RecyclerView rcvAddress;
    @BindView(R2.id.rcvPhoto)
    RecyclerView rcvPhoto;
    @BindView(R2.id.textView15)
    TextView textView15;
    @BindView(R2.id.radPublic)
    RadioButton radPublic;
    @BindView(R2.id.radOnlyFriend)
    RadioButton radOnlyFriend;
    @BindView(R2.id.radOnlyMe)
    RadioButton radOnlyMe;
    @BindView(R2.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R2.id.imageView16)
    ImageView imageView16;
    @BindView(R2.id.addPhoto)
    FrameLayout addPhoto;
    @BindView(R2.id.button3)
    Button button3;
    private ChoosePhotoBottomSheet choosePhotoBottomSheet;
    Post postToEdit;
    List<Image> listImage;
    AdapterPhotoMakePost adapterPhotoMakePost;
    Camera camera;
    private boolean isCallRequest;
    private Location location;
    MyPlace myPlace;
    List<MyPlace> list = new ArrayList<>();
    AdapterLocation adapterLocation;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 6000, FASTEST_INTERVAL = 6000; // = 5 seconds
    int PLACE_PICKER_REQUEST = 122222;

    private AdapterLocation.CallBackAddLocation iCallbackLocation = new AdapterLocation.CallBackAddLocation() {
        @Override
        public void onClickAddLocation() {
            RxPermissions rxPermissions = new RxPermissions(MakePostActivity.this);
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(new Observer<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (aBoolean) {
                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                        try {
                            startActivityForResult(builder.build(MakePostActivity.this), PLACE_PICKER_REQUEST);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
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

//            startActivityForResult(new Intent(MakePostActivity.this, SearchMapAdressActivity.class), 1001);
        }

        @Override
        public void onCallBack(int pos) {
            myPlace = list.get(pos);
        }
    };
    private ProgressDialogCustom dialogLoading;
    private MakePostContract.Presenter presenter;

    @Override
    protected int initLayout() {
        return R.layout.activity_make_post;
    }

    @Override
    protected void initVariables() {
        dialogLoading = new ProgressDialogCustom(this);
        presenter = new MakePostPresenter(this);
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)

                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        choosePhotoBottomSheet = new ChoosePhotoBottomSheet(this, this);
        Bundle b = getIntent().getBundleExtra(AppConstant.BUNDLE);
        if (b != null) {
            listImage = b.getParcelableArrayList(AppConstant.MSG);
        }
        if (listImage == null) {
            listImage = new ArrayList<>();
        }
        list = new ArrayList<>();
        adapterLocation = new AdapterLocation(list, iCallbackLocation);
        rcvAddress.setAdapter(adapterLocation);
        adapterPhotoMakePost = new AdapterPhotoMakePost(listImage, this, this);
        rcvPhoto.setAdapter(adapterPhotoMakePost);
        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();
        requestLocation();
    }

    @Override
    protected void initViews() {
        rcvPhoto.setHasFixedSize(true);
        rcvPhoto.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rcvAddress.setHasFixedSize(true);
        rcvAddress.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }

    }

    private void requestLocation() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
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
    protected void onResume() {
        super.onResume();
        if (!checkPlayServices()) {
            showToast("Không thể truy cập vị trí trên thiết bị", AppConstant.NEGATIVE);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            // todo location here
            AppController.getInstance().getmSetting().put(AppConstant.LAT, (float) location.getLatitude());
            AppController.getInstance().getmSetting().put(AppConstant.LNG, (float) location.getLongitude());
//            if (list == null || list.isEmpty())
            if (!isCallRequest) {
                LogUtils.e((float) location.getLatitude() + "," + (float) location.getLongitude());

                presenter.getPlaceNearby((float) location.getLatitude(), (float) location.getLongitude());
                isCallRequest = true;
            }
        }
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // todo location here
            AppController.getInstance().getmSetting().put(AppConstant.LAT, (float) location.getLatitude());
            //   LogUtils.e((float) location.getLatitude() +","+ (float) location.getLongitude());
            AppController.getInstance().getmSetting().put(AppConstant.LNG, (float) location.getLongitude());
            if (!isCallRequest) {
                LogUtils.e((float) location.getLatitude() + "," + (float) location.getLongitude());
                presenter.getPlaceNearby((float) location.getLatitude(), (float) location.getLongitude());
                isCallRequest = true;
            }
        }
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

    @OnClick({R2.id.btnBack, R2.id.button3, R2.id.addPhoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R2.id.btnBack:
                onBackPressed();
                break;
            case R2.id.addPhoto:
                choosePhotoBottomSheet.show();
                break;
            case R2.id.button3:
//                if ( listImage.isEmpty()) {
//                    showToast("Bạn cần ít nhất 1 ảnh để đăng bài viết mới", AppConstant.NEGATIVE);
//                    return;
//                }
                int type = 1;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R2.id.radPublic: {
                        type = 1;
                        break;
                    }
                    case R2.id.radOnlyFriend: {
                        type = 2;
                        break;
                    }
                    case R2.id.radOnlyMe: {
                        type = 3;
                        break;
                    }
                }
                String address = "";
                if (myPlace != null) {
                    address = myPlace.getName();
                }
                presenter.submitPost(edtContent.getText().toString(), type, address, listImage);
                break;
        }
    }

    @Override
    public void onCallBack(final int pos) {
        if (pos != -1 && postToEdit != null && postToEdit.getList_image() != null && pos < postToEdit.getList_image().size()) {
            new OptionBottomSheet(this, R.drawable.ic_delete,
                    Html.fromHtml(getString(R.string.confirm_delete_photo)),
                    "Quay lại", "Xoá ",
                    new OptionBottomSheet.MoreOptionCallback() {
                        @Override
                        public void onMoreOptionCallback() {
                            //presenter.deletePost(post.getId());
                            listImage.remove(pos);
                            adapterPhotoMakePost.notifyItemRemoved(pos);
                            adapterPhotoMakePost.notifyItemRangeChanged(pos, adapterPhotoMakePost.getItemCount());
                        }

                        @Override
                        public void onCancleCallback() {

                        }
                    });
        } else {
            listImage.remove(pos);
            adapterPhotoMakePost.notifyItemRemoved(pos);
            adapterPhotoMakePost.notifyItemRangeChanged(pos, adapterPhotoMakePost.getItemCount());
        }
    }

    @Override
    public void onClickCapturePhoto() {
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
//                        launchCamera();
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
        choosePhoto();
    }

    private static final int CAPTURE_MEDIA = 368;

    // showImagePicker is boolean value: Default is true
    // setAutoRecord() to start recording the video automatically if media action is set to video.
    private void launchCamera() {
        SandriosCamera
                .with(this)
                .setShowPicker(true)
                .setShowPickerType(CameraConfiguration.VIDEO)
                .setVideoFileSize(20)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                .enableImageCropping(true)
                .launchCamera(new SandriosCamera.CameraCallback() {
                    @Override
                    public void onComplete(CameraOutputModel model) {
                        Log.e("File", "" + model.getPath());
                        Log.e("Type", "" + model.getType()); //Check SandriosCamera.MediaType
//                        Toast.makeText(getApplicationContext(), "Media captured.", Toast.LENGTH_SHORT).show();
                        File fileImage = new File(model.getPath());
                        LogUtils.e(model.getPath());
                        if (fileImage.exists()) {
                            Image image = new Image();
                            image.setId(-1);
                            image.setFile(fileImage);
                            listImage.add(0, image);
//                adapterPhotoMakePost.notifyItemInserted(adapterPhotoMakePost.getItemCount() - 1);
                            adapterPhotoMakePost.notifyDataSetChanged();
                            rcvPhoto.smoothScrollToPosition(0);
                        }
                    }
                });
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
                    Matisse.from(MakePostActivity.this)
                            .choose(MimeType.ofAll())
                            .countable(true)
                            .maxSelectable(9)
                            .captureStrategy(new CaptureStrategy(true, "com.skynet.lian.provider","Pictures"))
                            .capture(true)
//                            .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new Glide4Engine())
                            .forResult(PhotoPicker.REQUEST_CODE);
//                    PhotoPicker.builder()
//                            .setPhotoCount(5)
//                            .setShowCamera(true)
//                            .setShowGif(true)
//                            .setPreviewEnabled(false)
//                            .start(MakePostActivity.this, PhotoPicker.REQUEST_CODE);
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
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                MyPlace myPlace = new MyPlace();
                myPlace.setName(place.getName().toString());
                myPlace.setDescription(place.getName().toString());
                myPlace.setLat(place.getLatLng().latitude);
                myPlace.setLng(place.getLatLng().longitude);
                list.add(0, myPlace);
                this.myPlace = myPlace;
                adapterLocation.refreshAt(0);
                rcvAddress.smoothScrollToPosition(0);
            }
            return;
        }
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            Bundle b = data.getBundleExtra(AppConstant.BUNDLE);
            if (b != null) {
                Place place = b.getParcelable(AppConstant.MSG);
                if (place != null) {
                    MyPlace myPlace = new MyPlace();
                    myPlace.setName(place.getAddress());
                    myPlace.setDescription(place.getAddress());
                    myPlace.setLat(place.getLatLng().latitude);
                    myPlace.setLng(place.getLatLng().longitude);

                    list.add(0, myPlace);
                    this.myPlace = myPlace;
                    adapterLocation.refreshAt(0);
                    rcvAddress.smoothScrollToPosition(0);
                }
            }
        }
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                List<String> mSelected;
                mSelected = Matisse.obtainPathResult(data);
                Log.d("Matisse", "mSelected: " + mSelected);
//                ArrayList<String> photos =
//                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                List<Image> listPickup = new ArrayList<>();

                for (String urlPath : mSelected) {
                    File fileImage = new File(urlPath);
                    if (!fileImage.exists()) {
                        Toast.makeText(this, "File không tồn tại.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Image image = new Image();
                    image.setId(-1);
                    image.setFile(fileImage);
                    listPickup.add(image);
                }
                listImage.addAll(0, listPickup);
//                adapterPhotoMakePost.notifyItemInserted(adapterPhotoMakePost.getItemCount() - 1);
                adapterPhotoMakePost.notifyDataSetChanged();
                rcvPhoto.smoothScrollToPosition(0);
//                if (postToEdit != null && postToEdit.getPost() != null) {
//                    presenter.addPhoto(listPickup, postToEdit.getPost().getId());
//                }


            }

//            File fileImage = CommomUtils.scanFileMetisse(this, Matisse.obtainResult(data).get(0));
            // File fileImage = new File(Matisse.obtainPathResult(data).get(0));

            //  LogUtils.d("Matisse", "mSelected: " + (fileImage.exists() ? fileImage.getAbsolutePath() : "null"));

        }

//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                File file = new File(resultUri.getPath());
//              listImage.add()
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//
//        }

        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String path = camera.getCameraBitmapPath();
            File fileImage = new File(path);
            LogUtils.e(path);
            if (fileImage.exists()) {
                Image image = new Image();
                image.setId(-1);
                image.setFile(fileImage);
                listImage.add(0, image);
//                adapterPhotoMakePost.notifyItemInserted(adapterPhotoMakePost.getItemCount() - 1);
                adapterPhotoMakePost.notifyDataSetChanged();
                rcvPhoto.smoothScrollToPosition(0);
            }
        }
    }

    @Override
    public void onSuccessSubmit(final int idPost) {
        setResult(RESULT_OK);
        AppController.getInstance().getmProfileUser().setLast_status(edtContent.getText().toString());
        showToast("Đăng bài viết thành công", AppConstant.POSITIVE, new SnackBarCallBack() {
            @Override
            public void onClosedSnackBar() {
                Intent i = new Intent(MakePostActivity.this, DetailPostActivity.class);
                i.putExtra(AppConstant.MSG, idPost);
                startActivityForResult(i, 1000);
                finish();
            }
        });
    }

    @Override
    public void onSucessGetPlaces(List<MyPlace> myPlaces) {
        list.addAll(myPlaces);
        adapterLocation.notifyDataSetChanged();
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
        showToast("Đã có lỗi xảy ra. Vui lòng thử lại sau!",AppConstant.NEGATIVE);
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
}
