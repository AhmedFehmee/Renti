package extra4it.fahmy.com.rentei.Fragment.UserFragmenta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import extra4it.fahmy.com.rentei.Fragment.HomeFragment;
import extra4it.fahmy.com.rentei.GPSTracker;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import extra4it.fahmy.com.rentei.Service.SearchService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Fehoo on 1/31/2018.
 */

public class SearchFragment extends Fragment implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    GoogleMap mMap;
    MapView mapView;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private LatLng mainPosition = null;
    private MainFragment.OnFragmentInteractionListener mListener;
    public ArrayList<AgencyModel.DataEntity> companiesList = new ArrayList<>();
    GPSTracker gpsTracker;

    public SearchFragment() {
        // Required empty public constructor
    }

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
        v = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, v);
        mapView = (MapView) v.findViewById(R.id.map);
        try {
            mapView.onCreate(savedInstanceState);
            mapView.onResume(); // needed to get the map to display immediately
            MapsInitializer.initialize(getActivity().getApplicationContext());
            mapView.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiInterface apiService = ApiClient.getClient(ApiInterface.class);

        gpsTracker = new GPSTracker(getContext());
        callSearchService(apiService);

        return v;
    }

    private void callSearchService(ApiInterface apiService) {
        if (gpsTracker.canGetLocation()) {
            Call<AgencyModel> call = apiService.getSearchData(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            call.enqueue(new Callback<AgencyModel>() {
                @Override
                public void onResponse(Call<AgencyModel> call, Response<AgencyModel> response) {
                    AgencyModel agency = response.body();
                    if (agency != null) {
                        for (int i = 0; i < agency.getData().size(); i++) {
                            LatLng mainPosition = new LatLng(agency.getData().get(i).getLat(), agency.getData().get(i).getLang());
                            placeMarker(agency.getData().get(i).getName(), mainPosition);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AgencyModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 8));
            mMap.addMarker(new MarkerOptions()
                    .position(marker)
                    .title(title)
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(true);
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 50, 50);

            //Initialize Google Play Services
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set a listener for marker click.
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                MarkerOptions options = new MarkerOptions();
                mainPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                options.position(mainPosition);
                options.title("Current Position");
                //mCurrLocationMarker = mMap.addMarker(options);
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mainPosition));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            /*SearchService searchService = new SearchService();
            if (gpsTracker.canGetLocation()) {
                searchService.callSearchService(getContext(),progressFrame ,mMap, gpsTracker.getLatitude(),
                        gpsTracker.getLongitude());
            } else {
                gpsTracker.showSettingsAlert();
            }*/
            }

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        } catch (Exception e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            HomeFragment.ChangeTabItems("search");
            // Call GoogleApiClient connection when starting the Activity
            buildGoogleApiClient();
        } else {
            // fragment is no longer visible
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
