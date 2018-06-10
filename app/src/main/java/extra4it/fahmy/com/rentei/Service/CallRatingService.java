package extra4it.fahmy.com.rentei.Service;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import extra4it.fahmy.com.rentei.R;

/**
 * Created by Fehoo on 3/11/2018.
 */

public class CallRatingService {
    public void callRatingService(final Context context , final FrameLayout progressFrame ,
                                  final String userId , final String carId , final String rate) {
            progressFrame.setVisibility(View.VISIBLE);
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);
            //this is the url where you want to send the request
            //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
            String url = "http://2.extra4it.net/tajeree/api/office_rate";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                // Display the response string.
                                System.out.println("///// " + response);

                                JSONObject jsonObject1 = new JSONObject(response);
                                String success = jsonObject1.getString("success");
                                if (success.equals("1")) {

                                }else {
                                    Toast.makeText(context,"Bad connection , Try again!",Toast.LENGTH_SHORT).show();
                                }
                                progressFrame.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                progressFrame.setVisibility(View.GONE);
                                Toast.makeText(context,context.getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressFrame.setVisibility(View.GONE);
                    Toast.makeText(context,context.getString(R.string.error),Toast.LENGTH_SHORT).show();
                }
            })
            {
                //adding parameters to the request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    System.out.println("///// " + userId + " // " + carId + " // " + rate);

                    params.put("user_id", userId + "");
                    params.put("office_id", carId + "");
                    params.put("rate", rate + "");
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
