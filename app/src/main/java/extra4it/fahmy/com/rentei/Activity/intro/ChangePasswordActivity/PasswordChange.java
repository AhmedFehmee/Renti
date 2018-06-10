package extra4it.fahmy.com.rentei.Activity.intro.ChangePasswordActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import extra4it.fahmy.com.rentei.Activity.intro.MainActivity;
import extra4it.fahmy.com.rentei.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Fehoo on 1/2/2018.
 */

public class PasswordChange extends AppCompatActivity {

    Toolbar toolbar;
    AVLoadingIndicatorView progressBar;
    FrameLayout progressFrame;
    EditText password, confirmPass;
    Button change;
    String email;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        Intent i = getIntent();
        email = i.getExtras().getString("email");
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar = findViewById(R.id.avi_progress);
        progressFrame = findViewById(R.id.progress_frame);
        progressBar.hide();
        progressFrame.setVisibility(View.GONE);

        password = (EditText) findViewById(R.id.input_password);
        confirmPass = (EditText) findViewById(R.id.input_confirm);

        change = (Button) findViewById(R.id.btn_change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().isEmpty() && confirmPass.getText().toString().isEmpty()) {
                    password.setError(getString(R.string.required_pass));
                } else if (!password.getText().toString().matches(confirmPass.getText().toString())) {
                    confirmPass.setError("password must match!");
                } else {
                    callPasswordService();
                }
            }
        });
    }

    private void callPasswordService() {
        progressBar.show();
        progressFrame.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
        String url = "http://2.extra4it.net/tajeree/api/resetpassword";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the response string.
                            System.out.println("///// " + response);
                            String id, name, email, type_user, user_token, reset_code, phone, image;

                            JSONObject jsonObject1 = new JSONObject(response);
                            String status = jsonObject1.getString("success");
                            if (status.equals("1")) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                ((Activity) MainActivity.context).finish();
                                ((Activity) ForgetPassword.context).finish();
                                ((Activity) ForgetPasswordConfirmation.context).finish();
                            } else if (status.equals("0")) {
                                Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                            } else if (status.equals("2")) {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        getString(R.string.login_fail), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                Snackbar.make(getWindow().getDecorView().getRootView(),
                                        getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                            progressBar.smoothToHide();
                            progressFrame.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Snackbar.make(getWindow().getDecorView().getRootView(),
                                    getString(R.string.error), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            progressBar.smoothToHide();
                            progressFrame.setVisibility(View.GONE);
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
                        getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        })

        {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", password.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        System.out.println("finall //// " + stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}





