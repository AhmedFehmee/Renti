package extra4it.fahmy.com.rentei.Service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import extra4it.fahmy.com.rentei.Adapter.UserAgencyHomeAdapter;
import extra4it.fahmy.com.rentei.Adapter.UserCarsHomeAdapter;
import extra4it.fahmy.com.rentei.EndPoints;
import extra4it.fahmy.com.rentei.Model.AgencyModel;

/**
 * Created by Fehoo on 3/21/2018.
 */

public class AgencyService {
    public void callAgencyService(Context context, final FrameLayout progressFrame, final Double lat,
                                  final Double lon, final RecyclerView nearCarRecycleView,
                                  final ArrayList<AgencyModel.CarsEntity> carsList,
                                  final UserCarsHomeAdapter carsAdapter,
                                  final RecyclerView nearCompanyRecycleView,
                                  final ArrayList<AgencyModel.DataEntity> companiesList,
                                  final UserAgencyHomeAdapter companyAdapter) {
            //progressFrame.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.AGENCY_SERVICE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            AgencyModel agency = gson.fromJson(response, AgencyModel.class);
                            companiesList.addAll(agency.getData());
                            for (int i = 0 ; i < agency.getData().size() ; i++) {
                                for (int j = 0 ; j < agency.getData().get(i).getCars().size() ; j++) {
                                    carsList.add(agency.getData().get(i).getCars().get(j));
                                }
                            }
                            nearCarRecycleView.setAdapter(carsAdapter);
                            nearCompanyRecycleView.setAdapter(companyAdapter);
                            //progressFrame.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //progressFrame.setVisibility(View.GONE);
                }
            })
            {
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
            System.out.println("finall //// " + stringRequest);
        }
}
