package extra4it.fahmy.com.rentei.Activity.intro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.HomeActivity;
import extra4it.fahmy.com.rentei.Activity.intro.ChangePasswordActivity.ForgetPassword;
import extra4it.fahmy.com.rentei.Activity.intro.LoginActivity.LoginActivity;
import extra4it.fahmy.com.rentei.Activity.intro.RegisterActivity.RegisterActivity;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.SharedPref;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forget)
    TextView tvForget;

    public static Context context;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Boolean check = Boolean.valueOf(SharedPref.readSharedPref(MainActivity.this,
                "isLogIn", "false"));
        Intent introIntent = new Intent(MainActivity.this, HomeActivity.class);
        introIntent.putExtra("newLocation" , "newLogin");
        introIntent.putExtra("isLogIn", check);
        if (check) {
            startActivity(introIntent);
            finish();
        } else {
            requestLocationPermission();
            context = this;
        }
    }

    @OnClick({R.id.btn_register, R.id.btn_login, R.id.tv_forget , R.id.btn_skip_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (requestLocationPermission()) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    intent.putExtra("newLocation", "main");
                    startActivity(intent);
                }
                break;
            case R.id.btn_login:
                if (requestLocationPermission()) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.tv_forget:
                if (requestLocationPermission()) {
                    startActivity(new Intent(MainActivity.this, ForgetPassword.class));
                }
                break;
            case R.id.btn_skip_register:
                if (requestLocationPermission()) {
                    SharedPref.saveSharedPref(getApplicationContext(), "id", "999999999");
                    SharedPref.saveSharedPref(getApplicationContext(), "username", "Guest user");
                    SharedPref.saveSharedPref(getApplicationContext(), "email", "guest@Tagere.com");
                    SharedPref.saveSharedPref(getApplicationContext(), "phone", "00000000000");
                    SharedPref.saveSharedPref(getApplicationContext(), "profilePic", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "lat", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "lang", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "driving_license", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "national_identity", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "type", "user");
                    SharedPref.saveSharedPref(getApplicationContext(), "address", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "start", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "end", "");
                    SharedPref.saveSharedPref(getApplicationContext(), "isLogIn", "true");
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    i.putExtra("newLocation" , "newLogin");
                    startActivity(i);
                    finish();
                }
                break;
        }
    }

    Boolean isGranted = false;

    private Boolean requestLocationPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            isGranted = true;
                            System.out.println("All permissions are granted!");
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //showSettingsDialog();
                            isGranted = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        isGranted = false;
                        //showSettingsDialog();
                        Toast.makeText(getApplicationContext(), "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
        return isGranted;
    }
}
