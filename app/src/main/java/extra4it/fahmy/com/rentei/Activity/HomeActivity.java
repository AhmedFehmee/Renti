package extra4it.fahmy.com.rentei.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import extra4it.fahmy.com.rentei.Activity.User.search.SearchActivity;
import extra4it.fahmy.com.rentei.Fragment.AgencyTabsFragment;
import extra4it.fahmy.com.rentei.Fragment.HomeFragment;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.MyFirebaseMessagingService;
import extra4it.fahmy.com.rentei.RegistrationServices;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {
    public static Toolbar toolbar;
    FragmentManager mFragmentManager;
    public static LinearLayout homeToolbar;
    public static CardView searchToolbar;
    SharedPreferences prefs;
    String userType, address, lat, lon;
    String result;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchToolbar = findViewById(R.id.search_toolbar);
        homeToolbar = findViewById(R.id.home_toolbar);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }

        //Start service
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent);
        Intent intent2 = new Intent(this, RegistrationServices.class);
        startService(intent2);

        Intent i = getIntent();
        if(getIntent() != null) {
            if (!i.getExtras().getString("newLocation").equals("newLogin")) {
                address = i.getExtras().getString("newLocation");
                lat = i.getExtras().getString("lat");
                lon = i.getExtras().getString("lon");
                result = address + "/" + lat + "/" + lon;
            }
        }
        bindHomeUi();

        searchToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });
    }

    private void bindHomeUi() {
        prefs = getSharedPreferences("UserFile", MODE_PRIVATE);
        String userType = prefs.getString("type", null);
        String userid = prefs.getString("id", null);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the HomeTabs as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (userType.equals("user")) {
            Log.i("UserType", userType + " // " + userid);
            fragmentTransaction.replace(R.id.containerView, new HomeFragment()).commit();
        } else if (userType.equals("company")) {
            Log.i("UserType", userType + " // " + userid);
            fragmentTransaction.replace(R.id.containerView, new AgencyTabsFragment()).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Start service
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent);
        Intent intent2 = new Intent(this, RegistrationServices.class);
        startService(intent2);
    }

    public String getMyData() {
        return result;
    }
}
