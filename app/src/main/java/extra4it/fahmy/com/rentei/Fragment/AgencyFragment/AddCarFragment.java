package extra4it.fahmy.com.rentei.Fragment.AgencyFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.AddCarPicsAdapter;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.BrandsAdapter;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.ModelAdapter;
import extra4it.fahmy.com.rentei.Activity.HomeActivity;
import extra4it.fahmy.com.rentei.Activity.intro.MainActivity;
import extra4it.fahmy.com.rentei.Activity.intro.RegisterActivity.RegisterActivity;
import extra4it.fahmy.com.rentei.EndPoints;
import extra4it.fahmy.com.rentei.FilePath;
import extra4it.fahmy.com.rentei.Fragment.AgencyTabsFragment;
import extra4it.fahmy.com.rentei.Fragment.HomeFragment;
import extra4it.fahmy.com.rentei.Fragment.UserFragmenta.MainFragment;
import extra4it.fahmy.com.rentei.Model.AddCarModel;
import extra4it.fahmy.com.rentei.Model.BrandModel;
import extra4it.fahmy.com.rentei.Model.Model;
import extra4it.fahmy.com.rentei.Model.UserModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.RealPathUtil;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import extra4it.fahmy.com.rentei.SharedPref;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

public class AddCarFragment extends Fragment implements BrandsAdapter.OnItemClick, ModelAdapter.OnItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.et_car_name)
    EditText etCarName;
    @BindView(R.id.et_car_brand)
    TextView etCarBrand;
    @BindView(R.id.et_car_model)
    TextView etCarModel;
    @BindView(R.id.et_car_price)
    EditText etCarPrice;
    @BindView(R.id.et_car_price_extra)
    EditText etCarPriceExtra;
    @BindView(R.id.et_car_pics)
    TextView etCarPics;
    @BindView(R.id.add_car_pics)
    ImageView addCarPics;
    @BindView(R.id.cars_pics_recycle)
    RecyclerView carsPicsRecycle;
    @BindView(R.id.et_car_details)
    EditText etCarDetails;
    @BindView(R.id.btn_add_car)
    Button btnAddCar;
    @BindView(R.id.top_card_view)
    OptRoundCardView topCardView;
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
    @BindView(R.id.brands_recycle)
    RecyclerView brandsRecycle;
    @BindView(R.id.brands_view)
    LinearLayout brandsView;
    @BindView(R.id.models_recycle)
    RecyclerView modelsRecycle;
    @BindView(R.id.model_view)
    LinearLayout modelView;
    Unbinder unbinder;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    Bitmap bm;
    String encodedImage;
    String pic_chooser;

    private BottomSheetBehavior sheetBehavior;
    public BrandsAdapter brandAdapter;
    public ArrayList<BrandModel.BrandsEntity> brandArray = new ArrayList<>();
    String brandId = null;

    private BottomSheetBehavior modelSheetBehavior;
    public ModelAdapter modelAdapter;
    public ArrayList<Model.ModelsEntity> modelArray = new ArrayList<>();
    String modelId = null;

    public AddCarPicsAdapter picsAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;

    ApiInterface apiService;

    String MY_PREFS_NAME = "UserFile";
    String userType, userID;
    SharedPreferences prefs;
    String selectedFirstPath;
    Uri selectedFirstFileUri;
    String selectedSecondPath;
    Uri selectedSecondFileUri;
    String selectedthirdPath;
    Uri selectedThirdFileUri;

    Uri imageURI = null;

    public AddCarFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.agency_add_car_activity, container, false);
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity.getMyData() != null) {
            AgencyTabsFragment.ChangeTabItems("profile");
        }

        unbinder = ButterKnife.bind(this, v);
        apiService = ApiClient.getClient(ApiInterface.class);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("id", null);

        picsAdapter = new AddCarPicsAdapter(getContext(), bitmaps);
        carsPicsRecycle.setAdapter(picsAdapter);

        brandAdapter = new BrandsAdapter(getContext(), brandArray, this);
        brandsRecycle.setHasFixedSize(true);
        brandsRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        brandsRecycle.setAdapter(brandAdapter);

        sheetBehavior = BottomSheetBehavior.from(brandsView);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setPeekHeight(0);

        modelAdapter = new ModelAdapter(getContext(), modelArray, this);
        modelsRecycle.setHasFixedSize(true);
        modelsRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        modelsRecycle.setAdapter(modelAdapter);

        modelSheetBehavior = BottomSheetBehavior.from(modelView);
        modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        modelSheetBehavior.setPeekHeight(0);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    @OnClick({R.id.et_car_brand, R.id.et_car_model, R.id.et_car_price, R.id.btn_back,
            R.id.et_car_price_extra, R.id.et_car_pics, R.id.et_car_details, R.id.btn_add_car,
            R.id.gallery, R.id.camera, R.id.progress_frame, R.id.add_car_pics})
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
                }
                break;
            case R.id.add_car_pics:
                if (picsFrame.getVisibility() == View.GONE) {
                    picsFrame.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_add_car:
                if (brandId != null && modelId != null && !etCarPrice.getText().toString().isEmpty() &&
                        !etCarPriceExtra.getText().toString().isEmpty() && bitmaps.size() == 3 &&
                        !etCarDetails.getText().toString().isEmpty()) {
                    progressFrame.setVisibility(View.VISIBLE);

                    try {
                        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        String userId = prefs.getString("id", null);

                        File file = new File(selectedFirstPath);
                        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("main_photo", file.getName(), mFile);

                        File secondfile = new File(selectedSecondPath);
                        RequestBody secondmFile = RequestBody.create(MediaType.parse("image/*"), secondfile);
                        MultipartBody.Part secondfileToUpload = MultipartBody.Part.createFormData("first_photo", secondfile.getName(), secondmFile);

                        File thirdFile1 = new File(selectedthirdPath);
                        RequestBody thirdmFile = RequestBody.create(MediaType.parse("image/*"), thirdFile1);
                        MultipartBody.Part thirdfileToUpload = MultipartBody.Part.createFormData("second_photo", thirdFile1.getName(), thirdmFile);

                        // must create model class for add car
                        Call<AddCarModel> call = apiService.addCar(userId, brandId, modelId, "2018",
                                "red", etCarPrice.getText().toString(), "0", "0",
                                etCarDetails.getText().toString(), fileToUpload, secondfileToUpload, thirdfileToUpload);
                        call.enqueue(new Callback<AddCarModel>() {
                            @Override
                            public void onResponse(Call<AddCarModel> call, Response<AddCarModel> response) {
                                AddCarModel model = response.body();
                                if (model != null) {
                                    if (model.getSuccess() == 1) {
                                        Snackbar.make(getView(),
                                                etCarName.getText().toString() + " is added ", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        etCarDetails.setText("");
                                        etCarPrice.setText("");
                                        etCarModel.setText("");
                                        etCarBrand.setText("");
                                        etCarName.setText("");
                                        etCarPriceExtra.setText("");
                                        bitmaps.clear();
                                        picsAdapter.notifyDataSetChanged();
                                        carsPicsRecycle.setVisibility(View.GONE);
                                        AgencyTabsFragment.ChangeTabItems("home");
                                    }
                                    progressFrame.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<AddCarModel> call, Throwable t) {
                                Log.i("ssss", t.getMessage() + "  " + t.getStackTrace());
                                Snackbar.make(getView(),
                                        R.string.error, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                progressFrame.setVisibility(View.GONE);

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressFrame.setVisibility(View.GONE);
                        //Toast.makeText(getContext()," error : try Again " + e.toString() , Toast.LENGTH_LONG).show();
                        Snackbar.make(getView(),
                                etCarName.getText().toString() + " is added ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        etCarDetails.setText("");
                        etCarPrice.setText("");
                        etCarModel.setText("");
                        etCarBrand.setText("");
                        etCarName.setText("");
                        etCarPriceExtra.setText("");
                        bitmaps.clear();
                        picsAdapter.notifyDataSetChanged();
                        carsPicsRecycle.setVisibility(View.GONE);
                        AgencyTabsFragment.ChangeTabItems("home");
                    }
                } else {
                    Snackbar.make(view, R.string.all_fields_required, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.gallery:
                pic_chooser = "gallery";
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select File"),
                        SELECT_FILE);
                break;
            case R.id.camera:
                pic_chooser = "camera";
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
                break;
            case R.id.btn_back:
                if (picsFrame.getVisibility() == View.VISIBLE) {
                    picsFrame.setVisibility(View.GONE);
                } else if (modelSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    AgencyTabsFragment.ChangeTabItems("home");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
               /* final Uri contentUri = data.getData();
                final String[] proj = {MediaStore.Images.Media.DATA};
                final Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
                final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToLast();*/

                if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);
                    if (bitmaps.size() == 0) {
                        selectedFirstFileUri = data.getData();
                        selectedFirstPath = RealPathUtil.getRealPathFromURI_API19(getContext(), selectedFirstFileUri);
                    } else if (bitmaps.size() == 1) {
                        selectedSecondFileUri = data.getData();
                        selectedSecondPath = RealPathUtil.getRealPathFromURI_API19(getContext(), selectedSecondFileUri);
                    } else if (bitmaps.size() == 2) {
                        selectedThirdFileUri = data.getData();
                        selectedthirdPath = RealPathUtil.getRealPathFromURI_API19(getContext(), selectedThirdFileUri);
                    }
                } else if (requestCode == REQUEST_CAMERA) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    //File finalFile = new File(getRealPathFromURI(tempUri));

                    if (bitmaps.size() == 0) {
                        selectedFirstPath = getRealPathFromURI(tempUri);
                        //Toast.makeText(getContext() , " path 1 : " + selectedFirstPath,Toast.LENGTH_LONG).show();
                    }else if (bitmaps.size() == 1) {
                        selectedSecondPath = getRealPathFromURI(tempUri);
                        //Toast.makeText(getContext() , " path 2 : " + selectedSecondPath,Toast.LENGTH_LONG).show();
                    }else if (bitmaps.size() == 2) {
                        selectedthirdPath = getRealPathFromURI(tempUri);
                       // Toast.makeText(getContext() , " path 3 : " + selectedthirdPath,Toast.LENGTH_LONG).show();
                    }
                    onCaptureImageResult(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void onCaptureImageResult(Intent data) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            HomeActivity activity = (HomeActivity) getActivity();
            if (activity != null) {
                if (activity.getMyData() != null) {
                    AgencyTabsFragment.ChangeTabItems("profile");
                } else {
                    if (picsFrame.getVisibility() == View.VISIBLE) {
                        picsFrame.setVisibility(View.GONE);
                    } else {
                        AgencyTabsFragment.ChangeTabItems("orders");
                    }
                }
            } else {
                if (picsFrame.getVisibility() == View.VISIBLE) {
                    picsFrame.setVisibility(View.GONE);
                } else {
                    AgencyTabsFragment.ChangeTabItems("orders");
                }
            }
        } else {
            // fragment is no longer visible
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (picsFrame.getVisibility() == View.VISIBLE) {
                            picsFrame.setVisibility(View.GONE);
                        } else if (modelSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        } else {
                            AgencyTabsFragment.ChangeTabItems("home");
                        }
                        return true;
                    }
                }
                return false;
            }
        });
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
        RequestQueue queue = Volley.newRequestQueue(getContext());

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
                                getActivity().finish();
                            }
                            progressFrame.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            Toast.makeText(getContext().getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                            progressFrame.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressFrame.setVisibility(View.GONE);
                Toast.makeText(getContext().getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
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

