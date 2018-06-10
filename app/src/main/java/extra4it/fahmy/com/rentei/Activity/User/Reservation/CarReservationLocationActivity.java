package extra4it.fahmy.com.rentei.Activity.User.Reservation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

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
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CarReservationLocationActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    Toolbar toolbar;
    LocationRequest mLocationRequest;
    Button btn_agree;
    private LatLng mainPosition = null;

    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    TextView mLocationMarkerText;
    private LatLng mCenterLatLong;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    String address;
    String lat, lon;
    AVLoadingIndicatorView progressBar;
    FrameLayout progressFrame;
    FrameLayout screen;
    Context context;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_location);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.trailer_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        context = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressBar = findViewById(R.id.avi_trailer_details);
        progressFrame = findViewById(R.id.progress_frame);
        progressBar.hide();
        progressFrame.setVisibility(View.GONE);
        screen = findViewById(R.id.location_map_frame);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        mLocationMarkerText.setText(R.string.location_set);

        btn_agree = (Button) findViewById(R.id.call_trailer_agree);
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (mCenterLatLong != null) {
                        progressBar.show();
                        progressFrame.setVisibility(View.VISIBLE);

                        double newLat = mCenterLatLong.latitude;
                        double newLong = mCenterLatLong.longitude;

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(newLat, newLong, 1);
                            if (addresses != null && addresses.size() > 0) {
                                address = addresses.get(0).getAddressLine(0);
                                lat = String.valueOf(newLat);
                                lon = String.valueOf(newLong);
                                String address11 = addresses.get(0).getAddressLine(1);
                                String city = addresses.get(0).getLocality();
                                Log.i("Address", "street // " + address + " /// " + address11 + "//" + city);
                            }
                        } catch (IOException e) {
                        }
                        Intent i = getIntent();
                        if (i.getExtras().getString("from").equals("drop")) {
                            Intent intent = new Intent(CarReservationLocationActivity.this, CarReservationActivity.class);
                            intent.putExtra("lat", lat);
                            intent.putExtra("lon", lon);
                            intent.putExtra("from", "drop");
                            intent.putExtra("address", address);
                            setResult(1000, intent);
                            finish();
                        }else {
                            Intent intent = new Intent(CarReservationLocationActivity.this, CarReservationActivity.class);
                            intent.putExtra("lat", lat);
                            intent.putExtra("lon", lon);
                            intent.putExtra("from", "pick");
                            intent.putExtra("address", address);
                            setResult(1000, intent);
                            finish();
                        }
                    }
                } else

                {
                    Utility.showGPSDisabledAlertToUser(context);
                    Snackbar.make(view, getString(R.string.enable_gps), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Called when the user clicks a marker.
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    //Added public method to be called from the Activity
    public void placeMarker(String title, LatLng marker) {
        if (mMap != null) {
            //LatLng marker = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));
            mMap.addMarker(new MarkerOptions()
                    .position(marker)
                    .title(title)
                    .draggable(true)
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                // mMap.clear();
                try {
                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    mLocationMarkerText.setText("Set Location");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //move map camera
        mainPosition = new LatLng(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude(),
                LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mainPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}

