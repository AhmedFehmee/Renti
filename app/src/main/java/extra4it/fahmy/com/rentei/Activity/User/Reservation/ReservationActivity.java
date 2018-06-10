package extra4it.fahmy.com.rentei.Activity.User.Reservation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.User.CarDetails.CarsDetailsActivity;
import extra4it.fahmy.com.rentei.ImageFilePath;
import extra4it.fahmy.com.rentei.Model.OrderModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fehoo on 2/23/2018.
 */

public class ReservationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.reservation_car_img_1)
    ImageView reservationCarImg1;
    @BindView(R.id.reservation_car_img_2)
    ImageView reservationCarImg2;
    @BindView(R.id.reservation_car_img_3)
    ImageView reservationCarImg3;
    @BindView(R.id.btn_proceed_reservation)
    Button btnProceedReservation;

    @BindView(R.id.et_id_pic)
    EditText etIdPic;
    @BindView(R.id.add_id_photo)
    ImageView addIdPhoto;
    @BindView(R.id.et_drive_pic)
    EditText etDrivePic;
    @BindView(R.id.add_drive_photo)
    ImageView addDrivePhoto;
    @BindView(R.id.gallery)
    ImageButton gallery;
    @BindView(R.id.camera)
    ImageButton camera;
    @BindView(R.id.pics_frame)
    FrameLayout picsFrame;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    Bitmap bm;
    String encodedImage;
    String pic_chooser;
    @BindView(R.id.iv_id_pic)
    ImageView ivIdPic;
    @BindView(R.id.iv_driver_pic)
    ImageView ivDriverPic;

    String carID, carName, carDetails, carOfficePrice, carHomePrice, carDrivePrice, carImage1,
            carImage2, carImage3, carRate, dropTime, dropLocation, pickTime, pickLocation, reservationType;
    @BindView(R.id.carName)
    TextView tvCarName;
    @BindView(R.id.tv_car_location)
    TextView tvCarLocation;
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
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    @BindView(R.id.thanks_View)
    CardView thanksView;

    ApiInterface apiService;
    String userId;
    String MY_PREFS_NAME = "UserFile";
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }
        apiService = ApiClient.getClient(ApiInterface.class);

        getData();

        bindUi();
    }

    private void bindUi() {
        Glide.with(getApplicationContext())
                .load(carImage1)
                .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                .into(reservationCarImg1);
        Glide.with(getApplicationContext())
                .load(carImage2)
                .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                .into(reservationCarImg2);
        Glide.with(getApplicationContext())
                .load(carImage3)
                .apply(new RequestOptions().placeholder(R.drawable.car_img).error(R.drawable.car_img))
                .into(reservationCarImg3);
        tvCarName.setText(carName + "");
        tvDatePick.setText(pickTime + "");
        tvDateDrop.setText(dropTime + "");

        Log.i("ssssss", dropLocation + " " + pickLocation);
        if (!pickLocation.equals("") && !pickLocation.equals("null")) {
            tvLocationPick.setText(pickLocation + "");
        } else {
            pickLocation = getString(R.string.from_office);
            tvLocationPick.setText(getString(R.string.from_office) + "");
        }
        if (!dropLocation.equals("") && !dropLocation.equals("null")) {
            tvLocationDrop.setText(dropLocation + "");
        } else {
            dropLocation = getString(R.string.from_office);
            tvLocationDrop.setText(getString(R.string.from_office) + "");
        }
        progressFrame.setVisibility(View.GONE);
    }

    private void getData() {
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getString("id", null);

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

        dropLocation = i.getExtras().getString("dropLocation");
        dropTime = i.getExtras().getString("dropTime");
        pickLocation = i.getExtras().getString("pickLocation");
        pickTime = i.getExtras().getString("pickTime");
        reservationType = i.getExtras().getString("reservationType");
    }

    @OnClick({R.id.toolbar_back, R.id.reservation_car_img_1, R.id.reservation_car_img_2,
            R.id.reservation_car_img_3, R.id.btn_proceed_reservation, R.id.et_id_pic,
            R.id.add_id_photo, R.id.et_drive_pic, R.id.add_drive_photo, R.id.gallery, R.id.camera,
            R.id.btn_change_reservation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.btn_change_reservation:
                finish();
                break;
            case R.id.reservation_car_img_1:
               /* bitmapMain = ((BitmapDrawable) reservationCarImg1.getDrawable()).getBitmap();
                bitmapSecond = ((BitmapDrawable) reservationCarImg3.getDrawable()).getBitmap();
                reservationCarImg3.setImageBitmap(bitmapMain);
                reservationCarImg1.setImageBitmap(bitmapSecond);*/
                break;
            case R.id.reservation_car_img_2:
                /*bitmapMain = ((BitmapDrawable) reservationCarImg2.getDrawable()).getBitmap();
                bitmapSecond = ((BitmapDrawable) reservationCarImg1.getDrawable()).getBitmap();
                reservationCarImg3.setImageBitmap(bitmapMain);
                reservationCarImg2.setImageBitmap(bitmapSecond);
                */
                break;
            case R.id.reservation_car_img_3:
                break;
            case R.id.add_id_photo:
                pic_chooser = "id";
                picsFrame.setVisibility(View.VISIBLE);
                break;
            case R.id.add_drive_photo:
                pic_chooser = "driver";
                picsFrame.setVisibility(View.VISIBLE);
                break;
            case R.id.gallery:
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select File"),
                        SELECT_FILE);
                break;
            case R.id.camera:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
                break;
            case R.id.btn_proceed_reservation:
                progressFrame.setVisibility(View.VISIBLE);
                Call<OrderModel> call = apiService.setOrder(userId, carID, pickTime, dropTime,
                        pickLocation, dropLocation, reservationType);
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {

                        thanksView.setVisibility(View.VISIBLE);
                        btnChangeReservation.setText(R.string.back_to_home);
                        toolbarBack.setImageDrawable(getResources().getDrawable(R.drawable.toolbar_home));
                        btnProceedReservation.setVisibility(View.GONE);
                        ((Activity) CarsDetailsActivity.context).finish();
                        ((Activity) CarReservationActivity.context).finish();
                        progressFrame.setVisibility(View.GONE);
                    }


                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
                        progressFrame.setVisibility(View.GONE);
                    }
                });

                /*Intent paymentIntent = new Intent(this, PaymentFinalActivity.class);
                paymentIntent.putExtra("carID", carID + "");
                paymentIntent.putExtra("carImage1", carImage1 + "");
                paymentIntent.putExtra("carImage2", carImage2 + "");
                paymentIntent.putExtra("carImage3", carImage3 + "");
                paymentIntent.putExtra("carName", carName + "");
                paymentIntent.putExtra("carOfficePrice", carOfficePrice + "");
                paymentIntent.putExtra("carHomePrice", carHomePrice + "");
                paymentIntent.putExtra("carDrivePrice", carDrivePrice + "");
                paymentIntent.putExtra("carDetails", carDetails + "");
                paymentIntent.putExtra("carRate", carRate + "");
                paymentIntent.putExtra("pickTime", pickTime + "");
                paymentIntent.putExtra("dropTime", dropTime + "");
                paymentIntent.putExtra("dropLocation", dropLocation + "");
                paymentIntent.putExtra("pickLocation", pickLocation + "");
                startActivity(paymentIntent);*/
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bm, 300, 300, false);
        bm = resizedBitmap;
        bitmaps.add(bm);
        picsFrame.setVisibility(View.GONE);
        String backgroundImageName;
        if (pic_chooser.equals("id")) {
            ivIdPic.setVisibility(View.VISIBLE);
            ivIdPic.setImageBitmap(bm);
            Uri path;
            path = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "rentei_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            String filepath = path.getPath();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            etIdPic.setText(filename);
        } else {
            ivDriverPic.setVisibility(View.VISIBLE);
            ivDriverPic.setImageBitmap(bm);
            Uri path;
            path = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "rentei_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            String filepath = path.getPath();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            etDrivePic.setText(filename);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            bm = bitmap;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 90, bytes);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    bm, 300, 300, false);
            bm = resizedBitmap;
            bitmaps.add(bm);
            picsFrame.setVisibility(View.GONE);
            String backgroundImageName;
            if (pic_chooser.equals("id")) {
                ivIdPic.setVisibility(View.VISIBLE);
                ivIdPic.setImageBitmap(bm);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    backgroundImageName = ImageFilePath.getPath(getApplicationContext(), filePath);
                    String[] picName = (backgroundImageName.split("/"));
                    etIdPic.setText(picName[picName.length - 1]);
                }
            } else {
                ivDriverPic.setVisibility(View.VISIBLE);
                ivDriverPic.setImageBitmap(bm);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    backgroundImageName = ImageFilePath.getPath(getApplicationContext(), filePath);
                    String[] picName = (backgroundImageName.split("/"));
                    etDrivePic.setText(picName[picName.length - 1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
