package extra4it.fahmy.com.rentei.Activity.User.Reservation;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.R;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Fehoo on 3/8/2018.
 */

public class CarReservationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;
    @BindView(R.id.data_pick_layout)
    LinearLayout dataPickLayout;
    @BindView(R.id.pick_date)
    TextView pickDate;
    @BindView(R.id.data_drop_layout)
    LinearLayout dataDropLayout;
    @BindView(R.id.drop_date)
    TextView dropDate;
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

    String carID, carName, carDetails, carOfficePrice, carHomePrice, carDrivePrice, carImage1,
            carImage2, carImage3, carRate, dropTime, pickTime ;
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
    @BindView(R.id.btn_pick_location)
    ImageView btnPickLocation;
    @BindView(R.id.btn_drop_location)
    ImageView btnDropLocation;

    String dropAddress, pickAddress, lat, lon, from;
    @BindView(R.id.tv_pick_location)
    TextView tvPickLocation;
    @BindView(R.id.tv_drop_location)
    TextView tvDropOffLocation;
    @BindView(R.id.btn_driver_check)
    CheckBox btnDriverCheck;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_rental_details);
        ButterKnife.bind(this);

        context = this;

        Intent i = getIntent();
        carID = i.getExtras().getString("carID");
        carImage1 = i.getExtras().getString("carImage1");
        carImage2 = i.getExtras().getString("carImage2");
        carImage3 = i.getExtras().getString("carImage3");
        carName = i.getExtras().getString("carName");
        carOfficePrice = i.getExtras().getString("carOfficePrice");
        carHomePrice = i.getExtras().getString("carHomePrice");
        carDrivePrice = i.getExtras().getString("carDrivePrice");
        carDetails = i.getExtras().getString("carDetails");
        carRate = i.getExtras().getString("carRate");

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
        locationView.setVisibility(View.GONE);
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
        tvDatePick.setText(formattedDate + "");
        tvDateDrop.setText(formattedDate + "");
    }

    @OnClick({R.id.data_pick_layout, R.id.data_drop_layout, R.id.toolbar_back, R.id.btn_proceed,
            R.id.btn_home_check, R.id.btn_pick_location, R.id.btn_drop_location , R.id.btn_driver_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.data_pick_layout:
                dateType = "pick";
                showDate(year, month, day, R.style.DatePickerSpinner);
                pickDate.setVisibility(View.VISIBLE);
                break;
            case R.id.data_drop_layout:
                dateType = "drop";
                showDate(year, month, day, R.style.DatePickerSpinner);
                dropDate.setVisibility(View.VISIBLE);
                break;
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.btn_home_check:
                if (!btnDriverCheck.isChecked()) {
                    if (locationView.getVisibility() == View.VISIBLE) {
                        locationView.setVisibility(View.GONE);
                        reservationType = "office";
                    } else {
                        locationView.setVisibility(View.VISIBLE);
                        reservationType = "home";
                    }
                }
                break;
            case R.id.btn_driver_check:
                if (btnDriverCheck.isChecked()){
                    reservationType="driver";
                }
                if (!btnHomeCheck.isChecked()) {
                    if (locationView.getVisibility() == View.VISIBLE) {
                        locationView.setVisibility(View.GONE);
                        reservationType = "office";
                    } else {
                        locationView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.btn_pick_location:
                Intent pickIntent = new Intent(this, CarReservationLocationActivity.class);
                pickIntent.putExtra("from", "pick");
                startActivityForResult(pickIntent, 1000);
                break;
            case R.id.btn_drop_location:
                Intent dropIntent = new Intent(this, CarReservationLocationActivity.class);
                dropIntent.putExtra("from", "drop");
                startActivityForResult(dropIntent, 1000);
                break;
            case R.id.btn_proceed:
                if (dropTime != null && pickTime != null) {
                    Intent detailsIntent = new Intent(this, ReservationActivity.class);
                    detailsIntent.putExtra("carID", carID + "");
                    detailsIntent.putExtra("carImage1", carImage1 + "");
                    detailsIntent.putExtra("carImage2", carImage2 + "");
                    detailsIntent.putExtra("carImage3", carImage3 + "");
                    detailsIntent.putExtra("carName", carName + "");
                    detailsIntent.putExtra("carOfficePrice", carOfficePrice + "");
                    detailsIntent.putExtra("carHomePrice", carHomePrice + "");
                    detailsIntent.putExtra("carDrivePrice", carDrivePrice + "");
                    detailsIntent.putExtra("carDetails", carDetails + "");
                    detailsIntent.putExtra("carRate", carRate + "");
                    detailsIntent.putExtra("pickTime", pickTime + "");
                    detailsIntent.putExtra("dropTime", dropTime + "");
                    detailsIntent.putExtra("dropLocation", dropAddress + "");
                    detailsIntent.putExtra("pickLocation", pickAddress + "");
                    detailsIntent.putExtra("reservationType", reservationType + "");
                    startActivity(detailsIntent);
                } else {
                    Toast.makeText(this, "Fill all fields first", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (dateType.equals("pick")) {
            tvDatePick.setText(simpleDateFormat.format(calendar.getTime()));
            pickTime = simpleDateFormat.format(calendar.getTime());
            showHourPicker();
        } else if (dateType.equals("drop")) {
            tvDateDrop.setText(simpleDateFormat.format(calendar.getTime()));
            dropTime = simpleDateFormat.format(calendar.getTime());
            showHourPicker();
        }
    }


    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(CarReservationActivity.this)
                .callback(CarReservationActivity.this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public void showHourPicker() {
        final Calendar myCalender = Calendar.getInstance();
        final int hour = myCalender.get(Calendar.HOUR);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    String status = "AM";
                    if (hourOfDay > 11) {
                        status = "PM";
                    }
                    int hour_of_12_hour_format;
                    if (hourOfDay > 11) {
                        hour_of_12_hour_format = hourOfDay - 12;
                    } else {
                        hour_of_12_hour_format = hourOfDay;
                    }
                    if (dateType.equals("pick")) {
                        pickTime += "  " + hour_of_12_hour_format + " : " + minute + " : " + status;
                        tvTimePick.setText(hour_of_12_hour_format + " : " + minute + " : " + status);
                    } else if (dateType.equals("drop")) {
                        dropTime += "  " + hour_of_12_hour_format + " : " + minute + " : " + status;
                        tvTimeDrop.setText(hour_of_12_hour_format + " : " + minute + " : " + status);
                    }
                    Log.i("Time", "" + myCalender.getTime().getHours() + hourOfDay);
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(CarReservationActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false);
        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
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

