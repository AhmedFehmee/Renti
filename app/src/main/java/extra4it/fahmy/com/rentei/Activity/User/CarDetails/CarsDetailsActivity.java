package extra4it.fahmy.com.rentei.Activity.User.CarDetails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.User.Reservation.CarReservationActivity;
import extra4it.fahmy.com.rentei.Model.CarModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import extra4it.fahmy.com.rentei.Service.CallRatingService;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fehoo on 2/22/2018.
 */

public class CarsDetailsActivity extends AppCompatActivity implements RatingDialogListener {

    Toolbar toolbar;

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.btn_proceed)
    Button btnProceed;
    @BindView(R.id.car_img_1)
    ImageView carImg1;
    @BindView(R.id.car_img_2)
    ImageView carImg2;
    @BindView(R.id.car_img_3)
    ImageView carImg3;
    @BindView(R.id.rate_stars)
    MaterialRatingBar carRateStars;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    @BindView(R.id.car_name)
    TextView carName;

    String carID;
    ApiInterface apiService;
    @BindView(R.id.car_price)
    TextView carPrice;
    @BindView(R.id.home_price)
    TextView homePrice;
    @BindView(R.id.driver_price)
    TextView driverPrice;
    @BindView(R.id.details)
    TextView details;
    @BindView(R.id.office_price)
    TextView officePrice;
    @BindView(R.id.car_rate_text)
    TextView carRateText;

    CarModel car;
    public static Context context;
    @BindView(R.id.add_rate)
    Button addRate;

    String MY_PREFS_NAME = "UserFile";
    String userType, userID;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_rental);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        Intent i = getIntent();
        if (i.getExtras().getString("carID") != null) {
            carID = i.getStringExtra("carID");
        }

        Intent ii = getIntent();
        carID = ii.getExtras().getString("carID");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }
        apiService = ApiClient.getClient(ApiInterface.class);

        progressFrame.setVisibility(View.VISIBLE);
        Call<CarModel> call = apiService.showCar(carID);
        call.enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                car = response.body();
                if (car.getData().size() >= 1) {
                    carName.setText(car.getData().get(0).getBrand_name() + " " +
                            car.getData().get(0).getModel_name());
                    carPrice.setText(car.getData().get(0).getOffice_price());
                    officePrice.setText(car.getData().get(0).getOffice_price());
                    homePrice.setText(car.getData().get(0).getHome_price());
                    driverPrice.setText(car.getData().get(0).getDriver_price());
                    details.setText(car.getData().get(0).getDetails());
                    carRateText.setText(car.getData().get(0).getRate() + "");
                    carRateStars.setNumStars(5);
                    carRateStars.setRating(car.getData().get(0).getRate());
                    carRateStars.setEnabled(false);
                    if (!car.getData().get(0).getMain_photo().equals("")) {
                        Glide.with(getApplicationContext())
                                .load("http://2.extra4it.net/tajeree/" + car.getData().get(0).getMain_photo())
                                .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                                .into(carImg1);
                        Glide.with(getApplicationContext())
                                .load("http://2.extra4it.net/tajeree/" + car.getData().get(0).getFirst_photo())
                                .apply(new RequestOptions().placeholder(R.drawable.car_img2).error(R.drawable.car_img2))
                                .into(carImg2);
                        Glide.with(getApplicationContext())
                                .load("http://2.extra4it.net/tajeree/" + car.getData().get(0).getSecond_photo())
                                .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                                .into(carImg3);
                    }
                    progressFrame.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                // Log error here since request failed
                progressFrame.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.toolbar_back, R.id.btn_proceed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.btn_proceed:
                Intent detailsIntent = new Intent(this, CarReservationActivity.class);
                detailsIntent.putExtra("carID", car.getData().get(0).getId() + "");
                detailsIntent.putExtra("carImage1", "http://2.extra4it.net/tajeree/" + car.getData().get(0).getFirst_photo() + "");
                detailsIntent.putExtra("carImage2", "http://2.extra4it.net/tajeree/" + car.getData().get(0).getMain_photo() + "");
                detailsIntent.putExtra("carImage3", "http://2.extra4it.net/tajeree/" + car.getData().get(0).getSecond_photo() + "");
                detailsIntent.putExtra("carName", car.getData().get(0).getBrand_name() + " " +
                        car.getData().get(0).getModel_name());
                detailsIntent.putExtra("carOfficePrice", car.getData().get(0).getOffice_price() + "");
                detailsIntent.putExtra("carHomePrice", car.getData().get(0).getHome_price() + "");
                detailsIntent.putExtra("carDrivePrice", car.getData().get(0).getDriver_price() + "");
                detailsIntent.putExtra("carDetails", car.getData().get(0).getDetails() + "");
                detailsIntent.putExtra("carRate", car.getData().get(0).getRate() + "");
                startActivity(detailsIntent);
                break;
        }
    }

    @OnClick(R.id.add_rate)
    public void onViewClicked() {
        showRateDialog();
    }
    private void showRateDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText(getString(R.string.submit))
                .setNegativeButtonText(getString(R.string.cancel))
                .setNoteDescriptions(Arrays.asList(getString(R.string.very_bad),
                        getString(R.string.not_good), getString(R.string.ouite_ok),
                        getString(R.string.very_good), getString(R.string.excellent)))
                .setDefaultRating(3)
                .setTitle(getString(R.string.rate_trailer))
                .setDescription(getString(R.string.rate_description))
                .setStarColor(R.color.starColor)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor)
                .setTitleTextColor(R.color.titleTextColor)
                .setDescriptionTextColor(R.color.contentTextColor)
                .setHint(getString(R.string.comment_hint))
                .setHintTextColor(R.color.hintTextColor)
                .setCommentTextColor(R.color.commentTextColor)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(CarsDetailsActivity.this)
                .show();
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("id", null);

        Log.i("rate", i + s + "");
        CallRatingService callRatingService = new CallRatingService();
        callRatingService.callRatingService(CarsDetailsActivity.this, progressFrame,
                userID, car.getData().get(0).getId()+"", i + "");
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
