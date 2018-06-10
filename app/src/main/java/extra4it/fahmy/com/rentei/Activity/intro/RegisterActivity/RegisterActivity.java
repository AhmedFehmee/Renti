package extra4it.fahmy.com.rentei.Activity.intro.RegisterActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.HomeActivity;
import extra4it.fahmy.com.rentei.Activity.intro.MainActivity;
import extra4it.fahmy.com.rentei.Adapter.CitiesAdapter;
import extra4it.fahmy.com.rentei.EndPoints;
import extra4it.fahmy.com.rentei.ImageUtility;
import extra4it.fahmy.com.rentei.Model.CityModel;
import extra4it.fahmy.com.rentei.Model.UserModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.RealPathUtil;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import extra4it.fahmy.com.rentei.SharedPref;
import extra4it.fahmy.com.rentei.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Fehoo on 1/29/2018.
 */

public class RegisterActivity extends AppCompatActivity implements CitiesAdapter.OnItemClick {
    @BindView(R.id.car_ok)
    FrameLayout carOk;
    @BindView(R.id.car_choice)
    CardView carChoice;
    @BindView(R.id.user_ok)
    FrameLayout userOk;
    @BindView(R.id.user_choice)
    CardView userChoice;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.layout_user_sign_up)
    LinearLayout layoutUserSignUp;
    @BindView(R.id.et_office_name)
    EditText etOfficeName;
    @BindView(R.id.et_office_email)
    EditText etOfficeEmail;
    @BindView(R.id.et_office_password)
    EditText etOfficePassword;
    @BindView(R.id.et_office_phone)
    EditText etOfficePhone;
    @BindView(R.id.btn_office_sign_up)
    Button btnOfficeSignUp;
    @BindView(R.id.layout_office_sign_up)
    LinearLayout layoutOfficeSignUp;
    @BindView(R.id.avi_progress)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    @BindView(R.id.et_id_pic)
    EditText etIdPic;
    @BindView(R.id.add_id_photo)
    ImageView addIdPhoto;
    @BindView(R.id.et_drive_pic)
    EditText etDrivePic;
    @BindView(R.id.add_drive_photo)
    ImageView addDrivePhoto;
    @BindView(R.id.pics_frame)
    FrameLayout picsFrame;
    @BindView(R.id.gallery)
    ImageButton gallery;
    @BindView(R.id.camera)
    ImageButton camera;
    @BindView(R.id.iv_id_pic)
    ImageView ivIdPic;
    @BindView(R.id.iv_driver_pic)
    ImageView ivDriverPic;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap bm;
    String encodedImage;
    String pic_chooser;
    private final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @BindView(R.id.btn_get_location)
    Button btnGetLocation;
    @BindView(R.id.et_office_address)
    EditText etOfficeAddress;
    @BindView(R.id.et_office_time_start)
    EditText etOfficeTimeStart;
    @BindView(R.id.et_office_time_end)
    EditText etOfficeTimeEnd;

    String userType, address, lat, lon;
    ApiInterface apiService;
    @BindView(R.id.cities_recycle)
    RecyclerView citiesRecycle;
    @BindView(R.id.cities_view)
    LinearLayout citiesView;
    @BindView(R.id.city_view)
    LinearLayout cityView;
    @BindView(R.id.et_cities)
    TextView etCities;

    private BottomSheetBehavior sheetBehavior;
    public CitiesAdapter citiesAdapter;
    public ArrayList<CityModel.CitiesEntity> citiesArray = new ArrayList<>();
    String cityId = null;

    String selectedFirstPath;
    Uri selectedFirstFileUri;
    String selectedSecondPath;
    Uri selectedSecondFileUri;
    String imageType;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient(ApiInterface.class);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Intent i = getIntent();
        if (!i.getExtras().getString("newLocation").equals("main")) {

        } else {
            userType = "user";
            carOk.setVisibility(View.GONE);
            layoutOfficeSignUp.setVisibility(View.GONE);
        }
        Utility.hideSoftKeyboard(getApplicationContext(), this);

        citiesAdapter = new CitiesAdapter(this, citiesArray, this);
        citiesRecycle.setAdapter(citiesAdapter);
        if (Utility.isNetworkAvailable(this)) {
            callCitiesService();
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        citiesRecycle.setHasFixedSize(true);
        citiesRecycle.setLayoutManager(new LinearLayoutManager(this));

        sheetBehavior = BottomSheetBehavior.from(citiesView);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setPeekHeight(0);
        etCities.setText("City");
    }

    private void callCitiesService() {
        Call<CityModel> call = apiService.getCities();
        call.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if (response.body() != null) {
                    if (response.body().getCities().size() >= 1) {
                        citiesArray.addAll(response.body().getCities());
                        citiesRecycle.setAdapter(citiesAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            address = data.getStringExtra("newLocation");
            lat = data.getStringExtra("lat");
            lon = data.getStringExtra("lon");
            userType = data.getStringExtra("typeUser");
            etOfficeAddress.setText(address);
            userType = "company";
            if (carOk.getVisibility() == View.GONE || userOk.getVisibility() == View.VISIBLE) {
                carOk.setVisibility(View.VISIBLE);
                userOk.setVisibility(View.GONE);
                layoutOfficeSignUp.setVisibility(View.VISIBLE);
                layoutUserSignUp.setVisibility(View.GONE);
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == SELECT_FILE) {
                    if (pic_chooser.equals("id")) {
                        selectedFirstFileUri = data.getData();
                        selectedFirstPath = RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), selectedFirstFileUri);
                    } else {
                        selectedSecondFileUri = data.getData();
                        selectedSecondPath = RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), selectedSecondFileUri);
                    }
                    ImageUtility.onSelectFromGalleryResult(data, getApplicationContext(), picsFrame,
                            pic_chooser, ivIdPic, ivDriverPic, etIdPic, etDrivePic);
                } else if (requestCode == REQUEST_CAMERA) {
                    ImageUtility.onCaptureImageResult(data, picsFrame, pic_chooser, ivIdPic, ivDriverPic,
                            etIdPic, etDrivePic);

                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));

                    if (pic_chooser.equals("id")) {
                        selectedFirstPath = getRealPathFromURI(tempUri);
                    } else {
                        selectedSecondPath = getRealPathFromURI(tempUri);
                    }
                    System.out.println(" iiiiiiii " + getRealPathFromURI(tempUri) + " //// " + finalFile  );

                    /*final Uri contentUri = data.getData();
                    final String[] proj = {MediaStore.Images.Media.DATA};
                    final Cursor cursor = managedQuery(contentUri, proj, null, null, null);
                    final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToLast();
                    if (pic_chooser.equals("id")) {
                        selectedFirstPath = cursor.getString(column_index);
                    } else {
                        selectedSecondPath = cursor.getString(column_index);
                    }*/
                    Log.i("iiiiiiiiiiii", " " + " " + selectedFirstPath + "  " + selectedSecondPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"error",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private String uriToFilename(Uri uri) {
        String path = null;
        path = RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), uri);

        return path;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (picsFrame.getVisibility() == View.VISIBLE) {
            picsFrame.setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    @OnClick({R.id.car_ok, R.id.user_ok, R.id.btn_sign_up, R.id.btn_office_sign_up, R.id.add_id_photo, R.id.add_drive_photo
            , R.id.gallery, R.id.camera, R.id.btn_get_location, R.id.et_office_address,
            R.id.cities_view, R.id.et_cities})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.car_choice:
                userType = "company";
                if (carOk.getVisibility() == View.VISIBLE) {
                    carOk.setVisibility(View.GONE);
                } else {
                    carOk.setVisibility(View.VISIBLE);
                    userOk.setVisibility(View.GONE);
                    layoutOfficeSignUp.setVisibility(View.VISIBLE);
                    layoutUserSignUp.setVisibility(View.GONE);
                }
                break;
            case R.id.user_choice:
                userType = "user";
                if (userOk.getVisibility() == View.VISIBLE) {
                    userOk.setVisibility(View.GONE);
                } else {
                    userOk.setVisibility(View.VISIBLE);
                    carOk.setVisibility(View.GONE);
                    layoutOfficeSignUp.setVisibility(View.GONE);
                    layoutUserSignUp.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.city_view:
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    etCities.setText("city");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //etCities.setText("الرياض");
                }
                break;
            case R.id.et_cities:
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    etCities.setText(cityId + "");
                }
                break;
            case R.id.btn_sign_up:
                checkUserValidation();
                break;
            case R.id.btn_office_sign_up:
                checkOfficeValidation();
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
            case R.id.btn_get_location:
                Intent intent = new Intent(this, CompanyLocation.class);
                intent.putExtra("location", "register");
                startActivityForResult(intent, 1000);
                break;
            case R.id.et_office_address:
                if (address == null) {
                    startActivity(new Intent(RegisterActivity.this, CompanyLocation.class));
                    finish();
                }
                break;
        }
    }

    private void checkOfficeValidation() {
        if (etOfficeName.getText().toString().isEmpty()) {
            etOfficeName.setError(getString(R.string.required_user_name));
        } else if (etOfficeEmail.getText().toString().isEmpty()) {
            etOfficeEmail.setError(getString(R.string.required_mail));
        } else if (!etOfficeEmail.getText().toString().matches(EMAIL_PATTERN)) {
            etOfficeEmail.setError(getString(R.string.invalid_email));
        } else if (etOfficePassword.getText().toString().isEmpty()) {
            etOfficePassword.setError(getString(R.string.required_pass));
        } else if (etOfficePassword.getText().length() < 8) {
            etOfficePassword.setError(getString(R.string.required_pass));
        } else if (etOfficePhone.getText().toString().isEmpty()) {
            etOfficePhone.setError(getString(R.string.required_phone));
        } else if (etOfficeAddress.getText().toString().isEmpty()) {
            etOfficeAddress.setError(getString(R.string.required_location));
        } else if (etOfficeTimeStart.getText().toString().isEmpty()) {
            etOfficeTimeStart.setError(getString(R.string.required_time));
        } else if (etOfficeTimeEnd.getText().toString().isEmpty()) {
            etOfficeTimeEnd.setError(getString(R.string.required_time));
        } else {
            callRegisterService(userType, RegisterActivity.this, progressFrame,
                    etOfficeName.getText().toString(), etOfficeEmail.getText().toString(),
                    etOfficePassword.getText().toString(), etOfficePhone.getText().toString(),
                    etOfficeAddress.getText().toString(), lat, lon, etOfficeTimeStart.getText().toString(),
                    etOfficeTimeEnd.getText().toString(), "", "", " ");
        }
    }

    private void checkUserValidation() {
        if (etUserName.getText().toString().isEmpty()) {
            etUserName.setError(getString(R.string.required_user_name));
        } else if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.required_mail));
        } else if (!etEmail.getText().toString().matches(EMAIL_PATTERN)) {
            etEmail.setError(getString(R.string.invalid_email));
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.required_pass));
        } else if (etPassword.getText().length() < 8) {
            etPassword.setError(getString(R.string.required_pass));
        } else if (etPhone.getText().toString().isEmpty()) {
            etPhone.setError(getString(R.string.required_phone));
        } else if (etDrivePic.getText().toString().isEmpty()) {
            etDrivePic.setError(getString(R.string.required_photo));
        } else if (etIdPic.getText().toString().isEmpty()) {
            etIdPic.setError(getString(R.string.required_photo));
        } else {
            progressFrame.setVisibility(View.VISIBLE);
            try {
                File file = new File(selectedFirstPath);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("driving_license", file.getName(), mFile);

                File secondfile = new File(selectedSecondPath);
                RequestBody secondmFile = RequestBody.create(MediaType.parse("image/*"), secondfile);
                MultipartBody.Part secondfileToUpload = MultipartBody.Part.createFormData("national_identity", secondfile.getName(), secondmFile);
                Call<UserModel> call = apiService.callUserRegister(etUserName.getText().toString(), etEmail.getText().toString(),
                        etPassword.getText().toString(), etPhone.getText().toString(), userType,
                        "0", "0", "0", "0", "0", fileToUpload, secondfileToUpload,
                        cityId + "");
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        progressFrame.setVisibility(View.GONE);
                        UserModel model = response.body();
                        if (model != null) {

                            if (model.getSuccess() == 1) {

                                SharedPref.saveSharedPref(RegisterActivity.this, "id", model.getData().getId() + "");
                                SharedPref.saveSharedPref(RegisterActivity.this, "username", model.getData().getName() + "");
                                SharedPref.saveSharedPref(RegisterActivity.this, "email", model.getData().getEmail() + "");
                                SharedPref.saveSharedPref(RegisterActivity.this, "phone", model.getData().getPhone());
                                SharedPref.saveSharedPref(RegisterActivity.this, "profilePic", model.getData().getPhoto() + "");
                                SharedPref.saveSharedPref(RegisterActivity.this, "driving_license", model.getData().getDriving_license() + "");
                                SharedPref.saveSharedPref(RegisterActivity.this, "national_identity", model.getData().getNational_identity() + "");
                                SharedPref.saveSharedPref(RegisterActivity.this, "type", model.getData().getType());
                                SharedPref.saveSharedPref(RegisterActivity.this, "isLogIn", "true");

                                Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("newLocation", "newLogin");
                                startActivity(i);
                                finish();
                                ((Activity) MainActivity.context).finish();
                            }
                            if (model.getSuccess() == 0) {
                                Toast.makeText(getApplicationContext(), getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext()," error : try Again" , Toast.LENGTH_LONG).show();
                        Log.i("ssss", t.getMessage() + " " + t.getStackTrace());
                        progressFrame.setVisibility(View.GONE);
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCityClick(View view, int position) {
        etCities.setText(citiesArray.get(position).getName());
        cityId = String.valueOf(citiesArray.get(position).getId());
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void callRegisterService(final String userType, final Context context, final FrameLayout progressFrame,
                                    final String name, final String mail, final String pass, final String phone,
                                    final String address, final String lat, final String lon,
                                    final String timeStart, final String timeEnd, final String cityId,
                                    final String ivDriverPic, final String ivIdPic) {
        //progressFrame.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.REGISTER,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the response string.
                            System.out.println("///// response " + response);

                            JSONObject jsonObject1 = new JSONObject(response);
                            String success = jsonObject1.getString("success");
                            if (success.equals("1")) {
                                JSONObject userObject = jsonObject1.getJSONObject("data");
                                if (userType.equals("user")) {
                                    SharedPref.saveSharedPref(context, "id", userObject.getString("id"));
                                    SharedPref.saveSharedPref(context, "username", userObject.getString("name"));
                                    SharedPref.saveSharedPref(context, "email", userObject.getString("email"));
                                    SharedPref.saveSharedPref(context, "phone", userObject.getString("phone"));
                                    SharedPref.saveSharedPref(context, "profilePic", userObject.getString("photo"));
                                    SharedPref.saveSharedPref(context, "driving_license", userObject.getString("driving_license"));
                                    SharedPref.saveSharedPref(context, "national_identity", userObject.getString("national_identity"));
                                    SharedPref.saveSharedPref(context, "type", userObject.getString("type"));
                                    SharedPref.saveSharedPref(context, "isLogIn", "true");
                                } else if (userType.equals("company")) {
                                    SharedPref.saveSharedPref(context, "id", userObject.getString("id"));
                                    SharedPref.saveSharedPref(context, "username", userObject.getString("name"));
                                    SharedPref.saveSharedPref(context, "email", userObject.getString("email"));
                                    SharedPref.saveSharedPref(context, "phone", userObject.getString("phone"));
                                    SharedPref.saveSharedPref(context, "profilePic", userObject.getString("photo"));
                                    SharedPref.saveSharedPref(context, "driving_license", userObject.getString("driving_license"));
                                    SharedPref.saveSharedPref(context, "national_identity", userObject.getString("national_identity"));
                                    SharedPref.saveSharedPref(context, "type", userObject.getString("type"));
                                    SharedPref.saveSharedPref(context, "lat", userObject.getString("lat"));
                                    SharedPref.saveSharedPref(context, "lon", userObject.getString("lang"));
                                    SharedPref.saveSharedPref(context, "start", userObject.getString("start"));
                                    SharedPref.saveSharedPref(context, "end", userObject.getString("end"));
                                    SharedPref.saveSharedPref(context, "isLogIn", "true");
                                }
                                Intent i = new Intent(context, HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("newLocation", "newLogin");
                                startActivity(i);
                                finish();
                                ((Activity) MainActivity.context).finish();
                            } else if (success.equals("0")) {
                                Toast.makeText(context, context.getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                            }
                            progressFrame.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                            progressFrame.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error ", " " + error.getMessage());
                progressFrame.setVisibility(View.GONE);
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (userType.equals("user")) {
                    params.put("name", name + "");
                    params.put("email", mail + "");
                    params.put("phone", phone + "");
                    params.put("type", "user");
                    params.put("password", pass + "");
                    params.put("driving_license", ivDriverPic + "");
                    params.put("national_identity", ivIdPic + "");
                    params.put("country_id", cityId + "");
                    params.put("address", "0");
                    params.put("lat", "0");
                    params.put("lang", "0");
                    params.put("start", "0");
                    params.put("end", "0");
                } else if (userType.equals("company")) {
                    params.put("name", name + "");
                    params.put("email", mail + "");
                    params.put("phone", phone + "");
                    params.put("type", "company");
                    params.put("password", pass + "");
                    params.put("address", address + "");
                    params.put("lat", lat + "");
                    params.put("lang", lon + "");
                    params.put("start", timeStart + "");
                    params.put("end", timeEnd + "");
                }
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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}

