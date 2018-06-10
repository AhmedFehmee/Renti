package extra4it.fahmy.com.rentei.Activity.User.Payment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.R;

/**
 * Created by Fehoo on 2/24/2018.
 */

public class PaymentFinalActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_home)
    ImageView toolbarHome;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.carName)
    TextView tvCcarName;
    @BindView(R.id.tv_car_location)
    TextView tvCarLocation;
    @BindView(R.id.reservation_car_img_1)
    ImageView reservationCarImg1;
    @BindView(R.id.reservation_car_img_2)
    ImageView reservationCarImg2;
    @BindView(R.id.reservation_car_img_3)
    ImageView reservationCarImg3;
    @BindView(R.id.tv_date_pick)
    TextView tvDatePick;
    @BindView(R.id.tv_location_pick)
    TextView tvLocationPick;
    @BindView(R.id.tv_date_drop)
    TextView tvDateDrop;
    @BindView(R.id.tv_location_drop)
    TextView tvLocationDrop;
    @BindView(R.id.btn_change_reservation)
    Button btnChangeReservation;

    String carID, carName, carDetails, carOfficePrice, carHomePrice, carDrivePrice, carImage1,
            carImage2, carImage3, carRate, dropTime, dropLocation, pickTime, pickLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_final);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }
    }

    @OnClick({R.id.toolbar_home, R.id.toolbar_search , R.id.btn_change_reservation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_home:
                break;
            case R.id.toolbar_search:
                break;
            case R.id.btn_change_reservation:
                break;
        }
    }

}

