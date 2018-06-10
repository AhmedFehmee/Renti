package extra4it.fahmy.com.rentei.Activity.Agency.addCar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
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
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.AddCarPicsAdapter;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.BrandsAdapter;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.ModelAdapter;
import extra4it.fahmy.com.rentei.EndPoints;
import extra4it.fahmy.com.rentei.Model.BrandModel;
import extra4it.fahmy.com.rentei.Model.Model;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fehoo on 3/4/2018.
 */

public class AddCarActivity extends AppCompatActivity implements BrandsAdapter.OnItemClick, ModelAdapter.OnItemClick {

    @BindView(R.id.et_car_name)
    EditText etCarName;
    @BindView(R.id.et_car_price)
    EditText etCarPrice;
    @BindView(R.id.et_car_price_extra)
    EditText etCarPriceExtra;
    @BindView(R.id.et_car_pics)
    TextView etCarPics;
    @BindView(R.id.et_car_details)
    EditText etCarDetails;
    @BindView(R.id.btn_add_car)
    Button btnAddCar;
    @BindView(R.id.top_card_view)
    OptRoundCardView topCardView;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    Bitmap bm;
    String encodedImage;
    String pic_chooser;

    @BindView(R.id.gallery)
    ImageButton gallery;
    @BindView(R.id.camera)
    ImageButton camera;
    @BindView(R.id.pics_frame)
    FrameLayout picsFrame;
    @BindView(R.id.avi_progress)
    AVLoadingIndicatorView aviProgress;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    @BindView(R.id.cars_pics_recycle)
    RecyclerView carsPicsRecycle;

    public AddCarPicsAdapter picsAdapter;
    @BindView(R.id.brands_view)
    LinearLayout brandsView;
    @BindView(R.id.brands_recycle)
    RecyclerView brandsRecycle;
    @BindView(R.id.models_recycle)
    RecyclerView modelsRecycle;
    @BindView(R.id.model_view)
    LinearLayout modelView;
    @BindView(R.id.et_car_brand)
    TextView etCarBrand;
    @BindView(R.id.et_car_model)
    TextView etCarModel;

    String MY_PREFS_NAME = "UserFile";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    @BindView(R.id.add_car_pics)
    ImageView addCarPics;

    private BottomSheetBehavior sheetBehavior;
    public BrandsAdapter brandAdapter;
    public ArrayList<BrandModel.BrandsEntity> brandArray = new ArrayList<>();
    String brandId = null;

    private BottomSheetBehavior modelSheetBehavior;
    public ModelAdapter modelAdapter;
    public ArrayList<Model.ModelsEntity> modelArray = new ArrayList<>();
    String modelId = null;
    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agency_add_car_activity);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }
        apiService = ApiClient.getClient(ApiInterface.class);

        picsAdapter = new AddCarPicsAdapter(this, bitmaps);
        carsPicsRecycle.setAdapter(picsAdapter);

        brandAdapter = new BrandsAdapter(this, brandArray, this);
        brandsRecycle.setHasFixedSize(true);
        brandsRecycle.setLayoutManager(new LinearLayoutManager(this));
        brandsRecycle.setAdapter(brandAdapter);

        sheetBehavior = BottomSheetBehavior.from(brandsView);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setPeekHeight(0);

        modelAdapter = new ModelAdapter(this, modelArray, this);
        modelsRecycle.setHasFixedSize(true);
        modelsRecycle.setLayoutManager(new LinearLayoutManager(this));
        modelsRecycle.setAdapter(modelAdapter);

        modelSheetBehavior = BottomSheetBehavior.from(modelView);
        modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        modelSheetBehavior.setPeekHeight(0);

    }

    private void callBrandsService() {
        Call<BrandModel> call = apiService.getBrands();
        call.enqueue(new Callback<BrandModel>() {
            @Override
            public void onResponse(Call<BrandModel> call, Response<BrandModel> response) {
                brandArray.clear();
                if (response.body().getBrands().size() >= 1) {
                    brandArray.addAll(response.body().getBrands());
                    brandsRecycle.setAdapter(brandAdapter);
                }
            }

            @Override
            public void onFailure(Call<BrandModel> call, Throwable t) {

            }
        });
    }

    private void callModelsService(String brandId) {
        Call<Model> call = apiService.getModels(brandId);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                modelArray.clear();
                if (response.body().getModels().size() >= 1) {
                    modelArray.addAll(response.body().getModels());
                    modelsRecycle.setAdapter(modelAdapter);
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.et_car_brand, R.id.et_car_model, R.id.et_car_price,R.id.btn_back,
            R.id.et_car_price_extra, R.id.et_car_pics, R.id.et_car_details, R.id.btn_add_car,
            R.id.gallery, R.id.camera, R.id.progress_frame , R.id.add_car_pics})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_car_brand:
                callBrandsService();
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.et_car_model:
                if (brandId != null) {
                    callModelsService(brandId);
                    if (modelSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        modelSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
                break;
            case R.id.et_car_pics:
                if (picsFrame.getVisibility() == View.GONE) {
                    picsFrame.setVisibility(View.VISIBLE);
                }                break;
            case R.id.add_car_pics:
                if (picsFrame.getVisibility() == View.GONE) {
                    picsFrame.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_add_car:
                if (brandId != null && modelId != null && !etCarPrice.getText().toString().isEmpty() &&
                        !etCarPriceExtra.getText().toString().isEmpty() && bitmaps.size() == 3) {
                    prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String userId = prefs.getString("id", null);

                    callAddCarService(userId, brandId, modelId, "2018", "red",
                            etCarPrice.getText().toString(), "0", "0", getStringImage(bitmaps.get(0))
                            , getStringImage(bitmaps.get(1)), getStringImage(bitmaps.get(2))
                            , etCarDetails.getText().toString());
                } else {
                    Snackbar.make(view, "all fields is required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        carsPicsRecycle.setVisibility(View.VISIBLE);
        picsFrame.setVisibility(View.GONE);
        carsPicsRecycle.setAdapter(picsAdapter);

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
            carsPicsRecycle.setVisibility(View.VISIBLE);
            picsFrame.setVisibility(View.GONE);
            carsPicsRecycle.setAdapter(picsAdapter);
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

    @Override
    public void onBackPressed() {
        if (picsFrame.getVisibility() == View.VISIBLE) {
            picsFrame.setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        etCarBrand.setText(brandArray.get(position).getName());
        brandId = String.valueOf(brandArray.get(position).getId());
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onModelClick(View view, int position) {
        etCarModel.setText(modelArray.get(position).getName());
        modelId = String.valueOf(modelArray.get(position).getId());
        modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void callAddCarService(final String user_id, final String brand_id, final String model_id,
                                  final String year, final String color,
                                  final String office_price, final String home_price, final String driver_price,
                                  final String main_photo, final String first_photo, final String second_photo,
                                  final String details) {
        progressFrame.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.ADD_CAR,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the response string.
                            System.out.println("///// " + response);
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                finish();
                            }
                            progressFrame.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                            progressFrame.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressFrame.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id + "");
                params.put("brand_id", brand_id + "");
                params.put("model_id", model_id + "");
                params.put("year", year);
                params.put("color", color + "");
                params.put("office_price", office_price + "");
                params.put("home_price", home_price + "");
                params.put("driver_price", driver_price + "");
                params.put("main_photo", main_photo + "");
                params.put("first_photo", first_photo + "");
                params.put("second_photo", second_photo + "");
                params.put("details", details + "");

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
