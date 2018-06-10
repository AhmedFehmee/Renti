package extra4it.fahmy.com.rentei.Service;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import extra4it.fahmy.com.rentei.EndPoints;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.R;

/**
 * Created by Fehoo on 3/21/2018.
 */

public class SearchService {
    public void callSearchService(Context context, final FrameLayout progressFrame, final GoogleMap mapView, final Double lat,
                                  final Double lon) {
        progressFrame.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.AGENCY_SERVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        AgencyModel agency = gson.fromJson(response, AgencyModel.class);
                        for (int i = 0; i < agency.getData().size(); i++) {
                            LatLng mainPosition = new LatLng(agency.getData().get(i).getLat(), agency.getData().get(i).getLang());
                            placeMarker(mapView, agency.getData().get(i).getName(), mainPosition);
                        }
                        progressFrame.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressFrame.setVisibility(View.GONE);
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", lat + "");
                params.put("lang", lon + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //Added public method to be called from the Activity
    public void placeMarker(GoogleMap mMap, String title, LatLng marker) {
        if (mMap != null) {
            //LatLng marker = new LatLng(lat, lon);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));
            mMap.addMarker(new MarkerOptions()
                    .position(marker)
                    .title(title)
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));
        }
    }
}
