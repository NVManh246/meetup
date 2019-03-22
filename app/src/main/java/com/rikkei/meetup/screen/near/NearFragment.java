package com.rikkei.meetup.screen.near;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rikkei.meetup.R;
import com.rikkei.meetup.adapter.EventHorizontalAdapter;
import com.rikkei.meetup.common.CustomItemDecoration;
import com.rikkei.meetup.common.observer.Observer;
import com.rikkei.meetup.data.model.event.Event;
import com.rikkei.meetup.data.model.event.Venue;
import com.rikkei.meetup.screen.EventDetail.EventDetailActivity;
import com.rikkei.meetup.ultis.NetworkUtils;
import com.rikkei.meetup.ultis.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NearFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMyLocationButtonClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, NearContract.View,
        EventHorizontalAdapter.OnItemClickListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, Observer {

    private static final int PERMISSION_REQUEST_CODE = 111;
    private static final int SETTING_REQUEST_CODE = 222;
    private static final int ZOOM_PERCENT = 15;
    private static final int RADIUS = 1000;
    private static final double LATITUDE_DEFAULT = 21.017461;
    private static final double LONGITUDE_DEFAULT = 105.780308;
    private static final int MAX_SIZE_MARKER = 70;

    private AppCompatActivity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private Location mCenterLocation;
    private Location mCurentLocation;
    private Circle mCircle;

    @BindView(R.id.recycler_event)
    RecyclerView mRecyclerEvent;
    private SnapHelper mSnapHelper;
    private RecyclerView.LayoutManager mLayoutManager;
    private EventHorizontalAdapter mEventHorizontalAdapter;
    private Unbinder mUnbinder;
    private List<Event> mEvents;
    private List<Marker> mMarkers;
    private String mToken;
    private NearContract.Presenter mPresenter;

    public static NearFragment newInstance() {
        NearFragment fragment = new NearFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        setupRecycler();
        mToken = StringUtils.getToken(getContext());
        MapFragment mapFragment = (MapFragment) mActivity.getFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mCenterLocation = new Location("");
        mCurentLocation = new Location("");
        mMarkers = new ArrayList<>();
        mPresenter = new NearPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkLocationPermission()) {
            mGoogleApiClient.connect();
            mPresenter.getNearEvents(mToken, RADIUS,
                    String.valueOf(mCenterLocation.getLongitude()),
                    String.valueOf(mCenterLocation.getLatitude()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!checkLocationPermission()) {
                initPermission();
            } else {
                //đã có quyền
                mGoogleApiClient.connect();
                mGoogleMap.setMyLocationEnabled(true);
                if (mToken == null) {
                    mToken = StringUtils.getToken(getContext());
                    if (mToken != null) {
                        mPresenter.getNearEvents(mToken, RADIUS,
                                String.valueOf(mCenterLocation.getLongitude()),     //0.0
                                String.valueOf(mCenterLocation.getLatitude()));
                    }
                } else {
                    mToken = StringUtils.getToken(getContext());
                    if (mToken == null) {
                        mPresenter.getNearEvents(mToken, RADIUS,
                                String.valueOf(mCenterLocation.getLongitude()),     //0.0
                                String.valueOf(mCenterLocation.getLatitude()));
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnMyLocationButtonClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
    }

    private void setupRecycler() {
        mEvents = new ArrayList<>();
        mEventHorizontalAdapter = new EventHorizontalAdapter(mEvents, this);
        mRecyclerEvent.setAdapter(mEventHorizontalAdapter);
        mLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerEvent.setLayoutManager(mLayoutManager);
        mRecyclerEvent.addItemDecoration(new CustomItemDecoration(20));
        mSnapHelper = new LinearSnapHelper();
        mSnapHelper.attachToRecyclerView(mRecyclerEvent);
        mRecyclerEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View centerView = mSnapHelper.findSnapView(mLayoutManager);
                if (centerView != null) {
                    int position = mLayoutManager.getPosition(centerView);
                    focusEvent(position);
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("MissingPermission")
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude, longtitude;
        if (location != null) {
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
        } else {
            latitude = LATITUDE_DEFAULT;
            longtitude = LONGITUDE_DEFAULT;
        }
        mCurentLocation.setLatitude(latitude);
        mCurentLocation.setLongitude(longtitude);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mGoogleMap.getCameraPosition().target;
        Location newLocation = new Location("");
        newLocation.setLatitude(latLng.latitude);
        newLocation.setLongitude(latLng.longitude);
        float distance = newLocation.distanceTo(mCenterLocation);
        if (checkLocationPermission()) {
            if (distance >= RADIUS) {
                if (mCircle != null) {
                    mCircle.remove();
                    mCircle = null;
                }
                mCircle = drawCircle(newLocation);
                mCenterLocation.setLatitude(latLng.latitude);
                mCenterLocation.setLongitude(latLng.longitude);
                mToken = StringUtils.getToken(getContext());
                mPresenter.getNearEvents(mToken, RADIUS, String.valueOf(latLng.longitude),
                        String.valueOf(latLng.latitude));
            }
        }
    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        forcusMyLocation();

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_REQUEST_CODE) {
            if (checkLocationPermission()) {
                mGoogleApiClient.connect();
                mGoogleMap.setMyLocationEnabled(true);
                mPresenter.getNearEvents(mToken, RADIUS,
                        String.valueOf(mCenterLocation.getLongitude()),
                        String.valueOf(mCenterLocation.getLatitude()));
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //được cấp quyền
                mGoogleApiClient.connect();
                mGoogleMap.setMyLocationEnabled(true);
                mPresenter.getNearEvents(mToken, RADIUS,
                        String.valueOf(mCenterLocation.getLongitude()),     //0.0
                        String.valueOf(mCenterLocation.getLatitude()));     //0.0
            } else {
                //Không được cấp quyền
                //Tích vào ô không hiển thị lại
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showDialogOpenSetting();
                }
            }
        }
    }

    private boolean checkLocationPermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = getContext().checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            //đã có quyền
            mGoogleApiClient.connect();
            mGoogleMap.setMyLocationEnabled(true);
            mPresenter.getNearEvents(mToken, RADIUS,
                    String.valueOf(mCenterLocation.getLongitude()),
                    String.valueOf(mCenterLocation.getLatitude()));
        }
    }

    private void showDialogOpenSetting() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_open_setting);
        dialog.findViewById(R.id.button_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, SETTING_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private Circle drawCircle(Location location) {
        CircleOptions options = new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(RADIUS)
                .fillColor(0x33FF0000)
                .strokeColor(Color.RED)
                .strokeWidth(2);
        return mGoogleMap.addCircle(options);
    }

    private void forcusMyLocation() {
        @SuppressLint("MissingPermission")
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude, longtitude;
        if (location != null) {
            latitude = location.getLatitude();
            longtitude = location.getLongitude();
        } else {
            latitude = LATITUDE_DEFAULT;
            longtitude = LONGITUDE_DEFAULT;
        }
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(latitude, longtitude), ZOOM_PERCENT));
        mCenterLocation.setLatitude(latitude);
        mCenterLocation.setLongitude(longtitude);
        mCurentLocation.setLatitude(latitude);
        mCurentLocation.setLongitude(longtitude);
        if (mCircle != null) {
            mCircle.remove();
            mCircle = null;
        }
        mCircle = drawCircle(mCenterLocation);
        mPresenter.getNearEvents(mToken, RADIUS, String.valueOf(longtitude),
                String.valueOf(latitude));
    }

    @Override
    public void showEvents(List<Event> events) {
        mEventHorizontalAdapter.clearAll();
        mEventHorizontalAdapter.insertDataNotLoadMore(events);
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        addMarkerEvent(events);
    }

    @Override
    public void showError() {
    }

    @Override
    public void onItemClick(int position) {

    }

    private void addMarkerEvent(List<Event> events) {
        for (Marker marker : mMarkers) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            }
        }
        mMarkers.clear();
        for (Event event : events) {
            Venue venue = event.getVenue();
            LatLng latLng = new LatLng(Double.parseDouble(venue.getGeoLat()),
                    Double.parseDouble(venue.getGeoLong()));
            Location location = new Location("");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            int meter = (int) mCurentLocation.distanceTo(location);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng).snippet(String.valueOf(meter))
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerIcons(event.getMyStatus())));

            CustomInfoWindow customInfoWindow = new CustomInfoWindow(getContext());
            mGoogleMap.setInfoWindowAdapter(customInfoWindow);

            Marker marker = mGoogleMap.addMarker(markerOptions);
            marker.setTag(event);
            mMarkers.add(marker);
        }
        mMarkers.get(0).showInfoWindow();
        mRecyclerEvent.scrollToPosition(0);
    }

    public Bitmap getMarkerIcons(int status) {
        String name = "";
        switch (status) {
            case 0:
                name = "location_white";
                break;
            case 1:
                name = "location_red";
                break;
            case 2:
                name = "location_yellow";
                break;
        }
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(
                        name, "drawable", getContext().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap,
                MAX_SIZE_MARKER, MAX_SIZE_MARKER, false);
        return resizedBitmap;
    }

    private void focusEvent(int position) {
        Venue venue = mEvents.get(position).getVenue();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(Double.valueOf(venue.getGeoLat()),
                        Double.valueOf(venue.getGeoLong())), ZOOM_PERCENT));
        for (int i = 0; i < mMarkers.size(); i++) {
            if (i == position) {
                mMarkers.get(i).setZIndex(2);
                mMarkers.get(i).showInfoWindow();
            } else {
                mMarkers.get(i).setZIndex(1);
                mMarkers.get(i).hideInfoWindow();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < mMarkers.size(); i++) {
            if (mMarkers.get(i).equals(marker)) {
                mRecyclerEvent.scrollToPosition(i);
                break;
            }
        }
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (int i = 0; i < mMarkers.size(); i++) {
            if (mMarkers.get(i).equals(marker)) {
                Intent intent = EventDetailActivity.getEventDetailIntent(getContext(), mEvents.get(i));
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void update(int status) {
        if(status == NetworkUtils.CONNECTED) {
            if(mCenterLocation != null) {
                mPresenter.getNearEvents(mToken, RADIUS,
                        String.valueOf(mCenterLocation.getLongitude()),
                        String.valueOf(mCenterLocation.getLatitude()));
            }
        }
    }
}
