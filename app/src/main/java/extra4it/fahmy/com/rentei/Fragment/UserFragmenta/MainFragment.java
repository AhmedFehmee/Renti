package extra4it.fahmy.com.rentei.Fragment.UserFragmenta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import extra4it.fahmy.com.rentei.Adapter.UserAgencyHomeAdapter;
import extra4it.fahmy.com.rentei.Adapter.UserCarsHomeAdapter;
import extra4it.fahmy.com.rentei.Fragment.HomeFragment;
import extra4it.fahmy.com.rentei.GPSTracker;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import extra4it.fahmy.com.rentei.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainFragment extends Fragment /*implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener*/ {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.address_city)
    TextView addressCity;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    @BindView(R.id.cars_view)
    LinearLayout carsView;
    @BindView(R.id.company_view)
    LinearLayout companyView;
    @BindView(R.id.gps_alert_view)
    LinearLayout gpsAlertView;
    @BindView(R.id.mapView)
    ImageView mapView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    /*GoogleMap mMap;
    MapView mapView;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private LatLng mainPosition = null;*/
    MapFragment mapFragment;
    private OnFragmentInteractionListener mListener;

    public RecyclerView nearCarRecycleView;
    public UserCarsHomeAdapter carsAdapter;
    public ArrayList<AgencyModel.CarsEntity> carsList = new ArrayList<>();

    public RecyclerView nearCompanyRecycleView;
    public UserAgencyHomeAdapter companyAdapter;
    public ArrayList<AgencyModel.DataEntity> companiesList = new ArrayList<>();
    ApiInterface apiService;
    GPSTracker gpsTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, v);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //mapView = (MapView) v.findViewById(R.id.map);

        //destination Recycle
        nearCarRecycleView = (RecyclerView) v.findViewById(R.id.cars_recycle);
        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        nearCarRecycleView.setLayoutManager(horizontalLayoutManagaer);
        carsAdapter = new UserCarsHomeAdapter(getContext(), carsList);
        nearCarRecycleView.setHasFixedSize(true);

        //destination Recycle
        nearCompanyRecycleView = (RecyclerView) v.findViewById(R.id.company_recycle);
        companyAdapter = new UserAgencyHomeAdapter(getContext(), companiesList);
        nearCompanyRecycleView.setHasFixedSize(true);

        apiService = ApiClient.getClient(ApiInterface.class);
        gpsTracker = new GPSTracker(getContext());

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled && network_enabled) {
            bindUI();
        } else
            try {
                gpsTracker.showSettingsAlert();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgencyMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private void bindUI() {
        if (Utility.isNetworkAvailable(getContext())) {
            if (gpsAlertView.getVisibility() == View.VISIBLE) {
                gpsAlertView.setVisibility(View.GONE);
            }

            callHomeService(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    //String address = addresses.get(0).getAddressLine(0);
                    String street = addresses.get(0).getFeatureName();
                    String government = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String city = addresses.get(0).getLocality();
                    addressCity.setText(city + "," + government + "," + country);

                    System.out.println("////////" + addressCity.getText().toString()+"//"+government+" " + country);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Snackbar.make(v, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void callHomeService(double latitude, double longitude) {
        progressFrame.setVisibility(View.VISIBLE);
        Call<AgencyModel> call = apiService.getSearchData(latitude, longitude);
        call.enqueue(new Callback<AgencyModel>() {
            @Override
            public void onResponse(Call<AgencyModel> call, Response<AgencyModel> response) {
                companiesList.clear();
                carsList.clear();
                AgencyModel agency = response.body();
                if (agency != null) {
                    companiesList.addAll(agency.getData());
                    for (int i = 0; i < agency.getData().size(); i++) {
                        for (int j = 0; j < agency.getData().get(i).getCars().size(); j++) {
                            carsList.add(agency.getData().get(i).getCars().get(j));
                        }
                    }
                }

                if (carsList.size() >= 1) {
                    if (carsView.getVisibility() == View.GONE) {
                        carsView.setVisibility(View.VISIBLE);
                    }
                }
                if (companiesList.size() >= 1) {
                    if (companyView.getVisibility() == View.GONE) {
                        companyView.setVisibility(View.VISIBLE);
                    }
                }
                nearCarRecycleView.setAdapter(carsAdapter);
                nearCompanyRecycleView.setAdapter(companyAdapter);
                progressFrame.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AgencyModel> call, Throwable t) {
                // Log error here since request failed
                progressFrame.setVisibility(View.GONE);
            }
        });
    }


    @OnClick({R.id.mapView, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mapView:
                HomeFragment.ChangeTabItems("search");
                break;
            case R.id.btn_search:
                HomeFragment.ChangeTabItems("search");
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            //((AppCompatActivity)getActivity()).getSupportActionBar().show();
            HomeFragment.ChangeTabItems("home");
        } else {
            // fragment is no longer visible
        }
    }

    /* protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                if (gpsAlertView.getVisibility() == View.VISIBLE)
                    gpsAlertView.setVisibility(View.GONE);
                MarkerOptions options = new MarkerOptions();
                mainPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                options.position(mainPosition);
                options.title("Current Position");
                mCurrLocationMarker = mMap.addMarker(options);
                try {
                    callHomeService(mainPosition.latitude, mainPosition.longitude);
                } catch (Exception e) {
                    Log.d(TAG, "onConnected: " + e.getMessage());
                }

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mainPosition));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getContext(), Locale.getDefault());

                addresses = geocoder.getFromLocation(mainPosition.latitude, mainPosition.longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    //String address = addresses.get(0).getAddressLine(0);
                    String street = addresses.get(0).getFeatureName();
                    String government = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String city = addresses.get(0).getLocality();
                    addressCity.setText(city + "," + government + "," + country);
                }
            }
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            *//*if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }*//*
        } catch (IOException e) {
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
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            //Initialize Google Play Services
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

                // Set a listener for marker click.
                mMap.setOnMarkerClickListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
