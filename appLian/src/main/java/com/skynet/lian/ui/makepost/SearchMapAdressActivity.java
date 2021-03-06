package com.skynet.lian.ui.makepost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skynet.lian.R;
import com.skynet.lian.R2;
import com.skynet.lian.models.MyPlace;
import com.skynet.lian.models.Place;
import com.skynet.lian.utils.AppConstant;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchMapAdressActivity extends AppCompatActivity implements OnMapReadyCallback,LocationContract.View {

    private static final int PLACE_PICKER_REQUEST = 10;
    @BindView(R2.id.loading)
    ProgressBar loading;
    @BindView(R2.id.address)
    TextView tvaddress;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng myLatlng;
    Place place;
    private LocationContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map_adress);
        ButterKnife.bind(this);
        presenter = new LocationPresenter(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            }
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            myLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                });


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                myLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapLoaded() {
                if (myLatlng != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 17));
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
//                new GetPlace(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude).execute();
                presenter.getMyAddress(new LatLng(mMap.getCameraPosition().target.latitude,mMap.getCameraPosition().target.longitude));
            }
        });
    }

    @OnClick(R2.id.search)
    public void onViewClicked() {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    // Toast.makeText(this, R.string.alert_missing_google_play, Toast.LENGTH_SHORT).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    //Toast.makeText(this, R.string.alert_missing_google_play, Toast.LENGTH_SHORT).show();
                }

    }
    @OnClick( R2.id.mylocation)
    public void onViewmylocationClicked() {

                if (mMap != null && myLatlng != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatlng, 17));
                }

    }
    @OnClick( R2.id.lay)
    public void onViewlayClicked() {

                if (place == null) return;
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putParcelable(AppConstant.MSG, this.place);
                i.putExtra(AppConstant.BUNDLE, b);
                setResult(RESULT_OK, i);
                finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                passPlaceBack(place);
                return;
            }
        }
    }

    private void passPlaceBack(com.google.android.gms.location.places.Place place) {
        this.place = new Place();
        this.place.setLatLng(place.getLatLng());
        this.place.setName(place.getName().toString());
        this.place.setAddress(place.getAddress().toString());
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putParcelable(AppConstant.MSG, this.place);
        i.putExtra(AppConstant.BUNDLE, b);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onSuccessGetMyAddress(MyPlace response) {
        if (response == null) return;
         place = new Place();
        place.setLatLng(new LatLng(response.getLat(),response.getLng()));
        place.setName(response.getAddress());
        String[] splitAddress = response.getAddress().split(",");
        if (splitAddress.length > 2)
            place.setAddress(splitAddress[0] + ", " + splitAddress[1]);
        else
            place.setAddress(response.getAddress());
        tvaddress.setText(place.getAddress());
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        loading.setVisibility(View.VISIBLE);

    }

    @Override
    public void hiddenProgress() {
        loading.setVisibility(View.GONE);

    }

    @Override
    public void onErrorApi(String message) {
        LogUtils.e(message);
    }

    @Override
    public void onError(String message) {
        LogUtils.e(message);

    }

    @Override
    public void onErrorAuthorization() {

    }

    class GetPlace extends AsyncTask<Void, Void, Place> {
        double lat, lng;

        public GetPlace( double lat, double lng) {
            this.lat = lat;
            this.lng = lng;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);

        }

        @Override
        protected Place doInBackground(Void... voids) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(SearchMapAdressActivity.this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses.size() == 0) {
                    return null;
                }
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                Place place = new Place();
                place.setLatLng(new LatLng(lat,lng));
                place.setName(knownName);
                String[] splitAddress = address.split(",");
                if (splitAddress.length > 2)
                    place.setAddress(splitAddress[0] + ", " + splitAddress[1]);
                else
                    place.setAddress(address);
                return place;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Place place1) {
            super.onPostExecute(place1);
            loading.setVisibility(View.INVISIBLE);
            if (place1 == null) return;
            place = place1;
            tvaddress.setText(place.getAddress());
        }
    }

}