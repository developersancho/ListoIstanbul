package sf.listoistanbul.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.arsy.maps_library.MapRadar;
import com.arsy.maps_library.MapRipple;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unstoppable.submitbuttonview.SubmitButton;

import sf.cuboidcirclebutton.CuboidButton;
import sf.listoistanbul.R;
import sf.listoistanbul.util.MessageUtils;
import sf.slideupmenu.SlideUp;
import sf.slideupmenu.SlideUpBuilder;

/**
 * Created by mesutgenc on 9.09.2017.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener,
        View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Toast toast;
    private SlideUp slideUp;
    private View dim;
    private View sliderView;
    private CuboidButton cbtnSearch;
    private SubmitButton btnSearchByVoice, btnSearchByCity;
    private SupportMapFragment mapFragment;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private GoogleMap mMap;
    private LatLng myLocation;
    private String lati;
    private String longi;
    private MapRipple mapRipple;
    private MapRadar mapRadar;
    private ImageView imgIspark, imgHotelpark, imgAvmpark, imgOzelpark;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        sliderView = rootView.findViewById(R.id.slideView);
        dim = rootView.findViewById(R.id.dim);
        cbtnSearch = (CuboidButton) rootView.findViewById(R.id.cbtnSearch);
        cbtnSearch.setOnClickListener(this);
        btnSearchByVoice = (SubmitButton) rootView.findViewById(R.id.btnSearchByVoice);
        btnSearchByVoice.setOnClickListener(this);
        btnSearchByCity = (SubmitButton) rootView.findViewById(R.id.btnSearchByCity);
        btnSearchByCity.setOnClickListener(this);
        imgIspark = (ImageView) rootView.findViewById(R.id.imgIspark);
        imgIspark.setOnClickListener(this);
        imgHotelpark = (ImageView) rootView.findViewById(R.id.imgHotelpark);
        imgHotelpark.setOnClickListener(this);
        imgAvmpark = (ImageView) rootView.findViewById(R.id.imgAvmpark);
        imgAvmpark.setOnClickListener(this);
        imgOzelpark = (ImageView) rootView.findViewById(R.id.imgOzelpark);
        imgOzelpark.setOnClickListener(this);


        slideUp = new SlideUpBuilder(sliderView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            cbtnSearch.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_home);
        mapFragment.getMapAsync(this);


        return rootView;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cbtnSearch) {
            slideUp.show();
            cbtnSearch.setVisibility(View.INVISIBLE);
        } else if (view.getId() == R.id.btnSearchByVoice) {
            MessageUtils.showAlerterNotification(getActivity(), "BİLGİ", "SESLİ ARAMA YAPILIYOR...", R.color.colorAccent);
            slideUp.hide();
            btnSearchByVoice.reset();
        } else if (view.getId() == R.id.btnSearchByCity) {
            MessageUtils.showToast(getContext(), "İLÇE SEÇİLEREK ARAMA YAPILIYOR");
            slideUp.hide();
            btnSearchByCity.reset();
        } else if (view.getId() == R.id.imgIspark) {
            MessageUtils.showAlerterNotification(getActivity(), "UYARI", "İSPARK...", R.color.color_FF5722);
        } else if (view.getId() == R.id.imgHotelpark) {
            MessageUtils.showToast(getContext(), "HOTEL PARK");
        } else if (view.getId() == R.id.imgAvmpark) {
            MessageUtils.showToast(getContext(), "AVMPARK");
        } else if (view.getId() == R.id.imgOzelpark) {
            MessageUtils.showToast(getContext(), "ÖZELPARK");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            // my location xCoor and yCoor
            lati = String.valueOf(mLastLocation.getLatitude());
            longi = String.valueOf(mLastLocation.getLongitude());
            myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            mMap.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title("KONUMUM")
                    .snippet("Şuan ki Konumum")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_my_location_64))
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .alpha(0.7f));

            CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 14));

/*            mapRipple = new MapRipple(mMap, myLocation, getContext());
            mapRipple.withNumberOfRipples(3);
            mapRipple.withFillColor(Color.BLUE);
            mapRipple.withStrokeColor(Color.BLACK);
            mapRipple.withStrokewidth(10);      // 10dp
            mapRipple.withDistance(2000);      // 2000 metres radius
            mapRipple.withRippleDuration(12000);    //12000ms
            mapRipple.withTransparency(0.5f);
            mapRipple.startRippleMapAnimation();*/
            /*mapRadar = new MapRadar(mMap, myLocation, getContext());
            mapRadar.withDistance(2000);
            mapRadar.withOuterCircleStrokeColor(Color.parseColor("#fccd29"));
            mapRadar.withOuterCircleStrokewidth(7);
            mapRadar.withOuterCircleTransparency(0.4f);
            mapRadar.withClockWiseAnticlockwise(true);        //enable both side rotation
            mapRadar.withClockwiseAnticlockwiseDuration(2);
            //withClockwiseAnticlockwiseDuration(duration), here duration denotes how much cycles should animation makes in
            //one direction
            mapRadar.withOuterCircleFillColor(Color.parseColor("#12000000"));
            mapRadar.withRadarColors(Color.parseColor("#e31722"), Color.parseColor("#fffccd29"));
            //withRadarColors() have two parameters, startColor and tailColor respectively
            //startColor should start with transparency, here 00 in front of fccd29 indicates fully transparent
            //tailColor should end with opaqueness, here f in front of fccd29 indicates fully opaque
            mapRadar.withRadarSpeed(5);    //controls radar speed
            mapRadar.withRadarTransparency(0.4f);
            mapRadar.startRadarAnimation();*/

        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // my location xCoor and yCoor
        lati = String.valueOf(mLastLocation.getLatitude());
        longi = String.valueOf(mLastLocation.getLongitude());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }


        mMap.setOnMarkerClickListener(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
}
