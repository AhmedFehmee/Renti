package extra4it.fahmy.com.rentei.Service;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import extra4it.fahmy.com.rentei.EndPoints;
import extra4it.fahmy.com.rentei.R;

/**
 * Created by Fehoo on 3/8/2018.
 */

public class CarDetailsService {
    public static void callRegisterService(final Context context, final FrameLayout progressFrame,
                                           final String carId,
                                           final TextView carName, final ImageView img1,
                                           final ImageView img2, final ImageView img3) {
        progressFrame.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SHOW_CAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the response string.
                            System.out.println("///// " + response);

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray dataObject = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataObject.length(); i++) {
                                JSONObject carObject = dataObject.getJSONObject(i);
                                carName.setText(carObject.getString("brand") + " " + carObject.getString("model"));
                                Glide.with(context).load(carObject.getString("main_photo")).into(img1);
                                Glide.with(context).load(carObject.getString("second_photo")).into(img2);
                                Glide.with(context).load(carObject.getString("first_photo")).into(img3);
                            }
                            progressFrame.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                            progressFrame.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressFrame.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("car_id", carId);
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

}
