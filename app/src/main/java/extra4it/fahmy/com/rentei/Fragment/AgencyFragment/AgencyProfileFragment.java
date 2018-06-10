package extra4it.fahmy.com.rentei.Fragment.AgencyFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.AddCarActivity;
import extra4it.fahmy.com.rentei.Activity.HomeActivity;
import extra4it.fahmy.com.rentei.Activity.intro.RegisterActivity.CompanyLocation;
import extra4it.fahmy.com.rentei.FilePath;
import extra4it.fahmy.com.rentei.Fragment.AgencyTabsFragment;
import extra4it.fahmy.com.rentei.Fragment.UserFragmenta.MainFragment;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.Model.UpdateModel;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Fehoo on 3/1/2018.
 */

public class AgencyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.btn_logout)
    Button btnLogout;
    Unbinder unbinder;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.avi_progress)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;

    String MY_PREFS_NAME = "UserFile";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    String userName, userEmail, userImage;
    @BindView(R.id.et_office_name)
    EditText etOfficeName;
    @BindView(R.id.et_office_email)
    EditText etOfficeEmail;
    @BindView(R.id.et_office_password)
    EditText etOfficePassword;
    @BindView(R.id.et_office_phone)
    EditText etOfficePhone;
    @BindView(R.id.btn_get_location)
    Button btnGetLocation;
    @BindView(R.id.et_office_address)
    EditText etOfficeAddress;
    @BindView(R.id.et_office_time_start)
    EditText etOfficeTimeStart;
    @BindView(R.id.et_office_time_end)
    EditText etOfficeTimeEnd;
    ImageButton gallery;
    @BindView(R.id.camera)
    ImageButton camera;
    @BindView(R.id.pics_frame)
    FrameLayout picsFrame;

    private final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    Bitmap bm;
    String encodedImage;
    String pic_chooser;
    String userType, address, lat, lon, userId;
    @BindView(R.id.btn_add_car)
    Button btnAddCar;
    @BindView(R.id.profile_user_name)
    TextView profileUserName;
    @BindView(R.id.profile_user_email)
    TextView profileUserEmail;

    ApiInterface apiService;
    String selectedFirstPath;
    Uri selectedFirstFileUri;

    private ImageView image;
    private String pictureFilePath;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    private MainFragment.OnFragmentInteractionListener mListener;

    public AgencyProfileFragment() {
        // Required empty public constructor
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
        v = inflater.inflate(R.layout.fragment_agency_profile, container, false);
        unbinder = ButterKnife.bind(this, v);

        apiService = ApiClient.getClient(ApiInterface.class);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        editor = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userId = prefs.getString("id", null);
        userType = prefs.getString("type", null);
        userName = prefs.getString("username", null);
        userEmail = prefs.getString("email", null);
        userImage = prefs.getString("profilePic", null);
        Log.i("userType Shared", userType + "");

        HomeActivity activity = (HomeActivity) getActivity();
        String myDataFromActivity = activity.getMyData();
        if (myDataFromActivity != null) {
            String[] addressDetail = (myDataFromActivity.split("/"));
            address = addressDetail[0];
            lat = addressDetail[1];
            lon = addressDetail[2];
            etOfficeAddress.setText(address);
        }
        Log.i("addressLocation ", address + " / " + lat + " / " + lon + " / " + userImage + " / ");

        profileUserName.setText(userName + "");
        profileUserEmail.setText(userEmail + "");
        if (!userImage.isEmpty()) {
            Glide.with(getContext())
                    .load("http://2.extra4it.net/tajeree/" + userImage)
                    .apply(new RequestOptions().placeholder(R.drawable.tagere_logo_pic).error(R.drawable.tagere_logo_pic))
                    .thumbnail(0.1f)
                    .into(profileImage);
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            AgencyTabsFragment.ChangeTabItems("profile");
            HomeActivity activity = (HomeActivity) getActivity();
            String myDataFromActivity = activity.getMyData();
            if (myDataFromActivity != null) {
                String[] addressDetail = (myDataFromActivity.split("/"));
                address = addressDetail[0];
                lat = addressDetail[1];
                lon = addressDetail[2];
                etOfficeAddress.setText(address);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_get_location, R.id.et_office_address, R.id.btn_update, R.id.btn_logout,
            R.id.profile_image, R.id.gallery, R.id.camera, R.id.btn_add_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                checkOfficeValidation();
                break;
            case R.id.btn_add_car:
                AgencyTabsFragment.ChangeTabItems("home");
                break;
            case R.id.profile_image:
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
                /*File pictureFile = null;
                try {
                    pictureFile = getPictureFile();
                } catch (IOException ex) {
                    Toast.makeText(getContext(),
                            "Photo file can't be created, please try again",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pictureFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "extra4it.fahmy.com.rentei.fileprovider", pictureFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }*/
                break;
            case R.id.btn_get_location:
                Intent locationIntent = new Intent(getContext(), CompanyLocation.class);
                locationIntent.putExtra("location", "profile");
                startActivityForResult(locationIntent, 1000);
                break;
            case R.id.et_office_address:
                Intent intent = new Intent(getContext(), CompanyLocation.class);
                intent.putExtra("profile", "profile");
                startActivityForResult(intent, 1000);
                break;
            case R.id.btn_logout:
                SharedPref.saveSharedPref(getContext(), "id", null);
                SharedPref.saveSharedPref(getContext(), "username", null);
                SharedPref.saveSharedPref(getContext(), "email", null);
                SharedPref.saveSharedPref(getContext(), "phone", null);
                SharedPref.saveSharedPref(getContext(), "profilePic", null);
                SharedPref.saveSharedPref(getContext(), "lat", null);
                SharedPref.saveSharedPref(getContext(), "lang", null);
                SharedPref.saveSharedPref(getContext(), "driving_license", null);
                SharedPref.saveSharedPref(getContext(), "national_identity", null);
                SharedPref.saveSharedPref(getContext(), "type", null);
                SharedPref.saveSharedPref(getContext(), "address", null);
                SharedPref.saveSharedPref(getContext(), "start", null);
                SharedPref.saveSharedPref(getContext(), "end", null);
                SharedPref.saveSharedPref(getContext(), "isLogIn", "false");
                getActivity().finish();
                break;
        }
    }

    private void callUpdateService() {
        progressFrame.setVisibility(View.VISIBLE);

        try {
            File file = new File(selectedFirstPath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photo", file.getName(), mFile);
            Log.i("values ", " " + userId + " " + lat + " " + lon + " " + fileToUpload);
            Call<UpdateModel> call = apiService.updateService(userId, etOfficeName.getText().toString() + "",
                    etOfficeEmail.getText().toString() + "", etOfficePhone.getText().toString() + "",
                    "company", etOfficePassword.getText().toString() + "",
                    address + "", fileToUpload, lat + "", lon + "",
                    etOfficeTimeStart.getText().toString() + "",
                    etOfficeTimeEnd.getText().toString() + "");
            call.enqueue(new Callback<UpdateModel>() {
                @Override
                public void onResponse(Call<UpdateModel> call, retrofit2.Response<UpdateModel> response) {
                    progressFrame.setVisibility(View.GONE);
                    Log.i("response", response + "");
                    if (response.body() != null) {
                        if (response.body().getUser() != null) {
                            SharedPref.saveSharedPref(getContext(), "profilePic", response.body().getUser().getPhoto());
                            etOfficeTimeStart.setText("");
                            etOfficeTimeEnd.setText("");
                            etOfficePassword.setText("");
                            etOfficeAddress.setText("");
                            etOfficeName.setText("");
                            etOfficePhone.setText("");
                            etOfficeEmail.setText("");
                            AgencyTabsFragment.ChangeTabItems("home");
                        }
                    }
                    Snackbar.make(getView(),
                            R.string.update_done, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                @Override
                public void onFailure(Call<UpdateModel> call, Throwable t) {
                    // Log error here since request failed
                    progressFrame.setVisibility(View.GONE);
                    Toast.makeText(getContext()," error : try Again" , Toast.LENGTH_LONG).show();
                    Log.e(TAG, t.toString());
                }
            });
        } catch (Exception e) {
           // Toast.makeText(getContext(),"Error : " + e.printStackTrace()  , Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
        } else if (bitmaps.size() == 0) {
            Toast.makeText(getContext(), R.string.required_profile_pic, Toast.LENGTH_LONG).show();
        } else {
            callUpdateService();
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "ZOFTINO_" + timeStamp;
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);
                    selectedFirstFileUri = data.getData();
                    selectedFirstPath = RealPathUtil.getRealPathFromURI_API19(getContext(), selectedFirstFileUri);
                } else if (requestCode == REQUEST_CAMERA) {
                    onCaptureImageResult(data);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    selectedFirstPath = getRealPathFromURI(tempUri);
                    System.out.println(" iiiiiiii " + getRealPathFromURI(tempUri) + " //// " + finalFile  );

                    /*final Uri contentUri = data.getData();
                    final String[] proj = {MediaStore.Images.Media.DATA};
                    final Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
                    final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToLast();
                    selectedFirstPath = cursor.getString(column_index);*/
                    Log.i("iiiiiiiiiiii", " " + " " + selectedFirstPath);
                    Toast.makeText(getContext(),"image saved"  , Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1000) {
            address = data.getStringExtra("newLocation");
            lat = data.getStringExtra("lat");
            lon = data.getStringExtra("lon");
            userType = data.getStringExtra("typeUser");
            etOfficeAddress.setText(address);
            userType = "company";
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
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bm, 300, 300, false);
        bm = resizedBitmap;
        bitmaps.add(bm);
        profileImage.setImageBitmap(bm);
        picsFrame.setVisibility(View.GONE);
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
            //profileImage.setImageResource(android.R.color.transparent);
            profileImage.setImageBitmap(bm);
            picsFrame.setVisibility(View.GONE);
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


