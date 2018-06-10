package extra4it.fahmy.com.rentei.Activity.intro.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.HomeActivity;
import extra4it.fahmy.com.rentei.Activity.intro.MainActivity;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.SharedPref;
import extra4it.fahmy.com.rentei.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_toolbar)
    Toolbar loginToolbar;
    @BindView(R.id.car_choice)
    CardView carChoice;
    @BindView(R.id.user_choice)
    CardView user_choice;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.avi_progress)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    @BindView(R.id.car_ok)
    FrameLayout carOk;
    @BindView(R.id.user_ok)
    FrameLayout userOk;
    private final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        carOk.setVisibility(View.GONE);
        Utility.hideSoftKeyboard(getApplicationContext(), this);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R.id.car_choice, R.id.user_choice, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.car_choice:
                if (carOk.getVisibility() == View.VISIBLE) {
                    carOk.setVisibility(View.GONE);
                } else {
                    carOk.setVisibility(View.VISIBLE);
                    userOk.setVisibility(View.GONE);
                }
                break;
            case R.id.user_choice:
                if (userOk.getVisibility() == View.VISIBLE) {
                    userOk.setVisibility(View.GONE);
                } else {
                    userOk.setVisibility(View.VISIBLE);
                    carOk.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_login:
                progressFrame.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                checkValidation();
                break;
        }
    }

    private void checkValidation() {
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.required_mail));
        } else if (!etEmail.getText().toString().matches(EMAIL_PATTERN)) {
            etEmail.setError(getString(R.string.invalid_email));
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.required_pass));
        } else if (etPassword.getText().length() < 8) {
            etPassword.setError(getString(R.string.required_pass));
        } else {
            callLoginService();
        }
        progressFrame.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void callLoginService() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
        String url = "http://2.extra4it.net/tajeree/api/login";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the response string.
                            System.out.println("///// " + response);

                            JSONObject jsonObject1 = new JSONObject(response);
                            String success = jsonObject1.getString("status");
                            if (success.equals("1")) {
                                JSONObject userObject = jsonObject1.getJSONObject("user");
                                SharedPref.saveSharedPref(getApplicationContext(), "id", userObject.getString("id"));
                                SharedPref.saveSharedPref(getApplicationContext(), "username", userObject.getString("name"));
                                SharedPref.saveSharedPref(getApplicationContext(), "email", userObject.getString("email"));
                                SharedPref.saveSharedPref(getApplicationContext(), "phone", userObject.getString("phone"));
                                SharedPref.saveSharedPref(getApplicationContext(), "profilePic", userObject.getString("photo"));
                                SharedPref.saveSharedPref(getApplicationContext(), "lat", userObject.getString("lat"));
                                SharedPref.saveSharedPref(getApplicationContext(), "lang", userObject.getString("lang"));
                                SharedPref.saveSharedPref(getApplicationContext(), "driving_license", userObject.getString("driving_license"));
                                SharedPref.saveSharedPref(getApplicationContext(), "national_identity", userObject.getString("national_identity"));
                                SharedPref.saveSharedPref(getApplicationContext(), "type", userObject.getString("type"));
                                SharedPref.saveSharedPref(getApplicationContext(), "address", userObject.getString("address"));
                                SharedPref.saveSharedPref(getApplicationContext(), "start", userObject.getString("start"));
                                SharedPref.saveSharedPref(getApplicationContext(), "end", userObject.getString("end"));
                                SharedPref.saveSharedPref(getApplicationContext(), "isLogIn", "true");
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                i.putExtra("newLocation", "newLogin");
                                startActivity(i);
                                finish();
                                ((Activity) MainActivity.context).finish();
                            } else if (success.equals("0")) {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        getString(R.string.invalid_login), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                            progressBar.smoothToHide();
                            progressFrame.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            progressBar.smoothToHide();
                            progressFrame.setVisibility(View.GONE);
                            Snackbar.make(getWindow().getDecorView().getRootView(),
                                    getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.smoothToHide();
                progressFrame.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        getString(R.string.error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", etEmail.getText().toString() + "");
                params.put("password", etPassword.getText().toString() + "");
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
