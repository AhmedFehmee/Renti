package extra4it.fahmy.com.rentei.Activity.User.OrderDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Model.AcceptModel;
import extra4it.fahmy.com.rentei.Model.CarModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.data_pick_layout)
    LinearLayout dataPickLayout;
    @BindView(R.id.pick_date)
    TextView tvPickDate;
    @BindView(R.id.data_drop_layout)
    LinearLayout dataDropLayout;
    @BindView(R.id.drop_date)
    TextView tvDropDate;
    @BindView(R.id.tv_date_pick)
    TextView tvDatePick;
    @BindView(R.id.tv_date_drop)
    TextView tvDateDrop;

    SimpleDateFormat simpleDateFormat;
    String dateType;
    @BindView(R.id.tv_time_pick)
    TextView tvTimePick;
    @BindView(R.id.tv_time_drop)
    TextView tvTimeDrop;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.btn_proceed)
    Button btnProceed;

    String orderId, carName, carDetails, carOfficePrice, carHomePrice, carDrivePrice, carImage1,
            carImage2, carImage3, carRate, dropTime, dropDate, dropPlace, pickDate, pickPlace, pickTime, userName, userPic;
    String reservationType = "office";
    @BindView(R.id.car_name)
    TextView tvCarName;
    @BindView(R.id.car_rate_text)
    TextView carRateText;
    @BindView(R.id.rate_stars)
    MaterialRatingBar rateStars;
    @BindView(R.id.car_price)
    TextView carPrice;
    @BindView(R.id.car_image)
    ImageView carMainImage;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;

    Date date;
    SimpleDateFormat df;
    int year, month, day;
    @BindView(R.id.btn_home_check)
    CheckBox btnHomeCheck;
    @BindView(R.id.location_view)
    LinearLayout locationView;
    @BindView(R.id.avi_progress)
    AVLoadingIndicatorView aviProgress;

    String dropAddress, pickAddress, lat, lon, from;
    @BindView(R.id.tv_pick_location)
    TextView tvPickLocation;
    @BindView(R.id.tv_drop_location)
    TextView tvDropOffLocation;
    @BindView(R.id.btn_driver_check)
    CheckBox btnDriverCheck;

    static Context context;
    @BindView(R.id.user_img)
    ImageView userImg;
    @BindView(R.id.user_name)
    TextView tvuserName;

    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);

        context = this;
        apiService = ApiClient.getClient(ApiInterface.class);

        Intent i = getIntent();
        orderId = i.getExtras().getString("orderID");
        carImage1 = i.getExtras().getString("carImage1");
        carImage2 = i.getExtras().getString("carImage2");
        carImage3 = i.getExtras().getString("carImage3");
        carName = i.getExtras().getString("carName");
        carOfficePrice = i.getExtras().getString("carOfficePrice");
        carHomePrice = i.getExtras().getString("carHomePrice");
        carDrivePrice = i.getExtras().getString("carDrivePrice");
        carDetails = i.getExtras().getString("carDetails");
        carRate = i.getExtras().getString("carRate");
        userName = i.getExtras().getString("userName");
        userPic = i.getExtras().getString("userPic");

        pickDate = i.getExtras().getString("pickDate");
        pickTime = i.getExtras().getString("pickTime");
        pickPlace = i.getExtras().getString("pickPlace");

        dropDate = i.getExtras().getString("dropDate");
        dropTime = i.getExtras().getString("dropTime");
        dropPlace = i.getExtras().getString("dropPlace");
        reservationType = i.getExtras().getString("reservationType");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

        date = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("dd-MMM-yyyy");

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        bindUI();
    }

    private void bindUI() {
        locationView.setVisibility(View.VISIBLE);
        tvCarName.setText(carName + " ");
        carRateText.setText(carRate + "");
        carPrice.setText(carOfficePrice + "");
        rateStars.setNumStars(5);
        rateStars.setRating(Float.parseFloat(carRate));
        rateStars.setEnabled(false);

        Glide.with(getApplicationContext())
                .load(carImage1)
                .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                .into(carMainImage);
        progressFrame.setVisibility(View.GONE);
        String formattedDate = df.format(date);

        tvDatePick.setText(pickDate + "");
        tvTimePick.setText(pickTime + "");
        tvPickLocation.setText(pickPlace + "");

        tvDateDrop.setText(dropDate + "");
        tvTimeDrop.setText(dropTime + "");
        tvDropOffLocation.setText(dropPlace + "");


        tvuserName.setText(userName + "");
        Glide.with(getApplicationContext())
                .load(carImage2)
                .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                .into(userImg);

        btnHomeCheck.setEnabled(false);
        btnDriverCheck.setEnabled(false);
        if (reservationType.equals("home")){
            btnHomeCheck.setChecked(true);
        }
        if (reservationType.equals("driver")){
            btnDriverCheck.setChecked(true);
        }
    }

    @OnClick({R.id.toolbar_back, R.id.btn_proceed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.btn_proceed:
                progressFrame.setVisibility(View.VISIBLE);
                Call<AcceptModel> call = apiService.acceptOrder(orderId);
                call.enqueue(new Callback<AcceptModel>() {
                    @Override
                    public void onResponse(Call<AcceptModel> call, Response<AcceptModel> response) {
                        finish();
                        progressFrame.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<AcceptModel> call, Throwable t) {
                        // Log error here since request failed
                        progressFrame.setVisibility(View.GONE);
                    }
                });
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1000) {
            from = data.getStringExtra("from");
            if (from.equals("drop")) {
                lat = data.getStringExtra("lat");
                lon = data.getStringExtra("lon");
                dropAddress = data.getStringExtra("address");
                tvDropOffLocation.setVisibility(View.VISIBLE);
                tvDropOffLocation.setText(dropAddress + "");
            } else {
                lat = data.getStringExtra("lat");
                lon = data.getStringExtra("lon");
                pickAddress = data.getStringExtra("address");
                tvPickLocation.setVisibility(View.VISIBLE);
                tvPickLocation.setText(pickAddress + "");
            }
        }
    }
}
