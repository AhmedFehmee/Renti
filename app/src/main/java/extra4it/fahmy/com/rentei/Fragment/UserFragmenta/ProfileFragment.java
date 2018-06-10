package extra4it.fahmy.com.rentei.Fragment.UserFragmenta;

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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import extra4it.fahmy.com.rentei.Activity.intro.MainActivity;
import extra4it.fahmy.com.rentei.FilePath;
import extra4it.fahmy.com.rentei.Fragment.HomeFragment;
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
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Fehoo on 2/1/2018.
 */

public class ProfileFragment extends Fragment {
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

    @BindView(R.id.et_profile_name)
    EditText etProfileName;
    @BindView(R.id.et_profile_email)
    EditText etProfileEmail;
    @BindView(R.id.et_profile_password)
    EditText etProfilePassword;
    @BindView(R.id.et_profile_phone)
    EditText etProfilePhone;
    @BindView(R.id.et_id_pic)
    EditText etIdPic;
    @BindView(R.id.add_id_photo)
    ImageView addIdPhoto;
    @BindView(R.id.iv_id_pic)
    ImageView ivIdPic;
    @BindView(R.id.et_drive_pic)
    EditText etDrivePic;
    @BindView(R.id.add_drive_photo)
    ImageView addDrivePhoto;
    @BindView(R.id.iv_driver_pic)
    ImageView ivDriverPic;
    @BindView(R.id.gallery)
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
    String userType, userId;
    String userName, userEmail, userImage;
    @BindView(R.id.profile_user_name)
    TextView profileUserName;
    @BindView(R.id.profile_user_email)
    TextView profileUserEmail;

    ApiInterface apiService;
    @BindView(R.id.btn_create_account)
    Button btnCreateAccount;
    @BindView(R.id.register_view)
    FrameLayout registerView;

    String selectedFirstPath;
    Uri selectedFirstFileUri;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    private MainFragment.OnFragmentInteractionListener mListener;

    public ProfileFragment() {
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
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, v);
        apiService = ApiClient.getClient(ApiInterface.class);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        editor = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userType = prefs.getString("type", null);
        userId = prefs.getString("id", null);
        userName = prefs.getString("username", null);
        userEmail = prefs.getString("email", null);
        userImage = prefs.getString("profilePic", null);

        profileUserName.setText(userName + "");
        profileUserEmail.setText(userEmail + "");

        if (userId.equals("999999999")) {
            registerView.setVisibility(View.VISIBLE);
        } else {
            registerView.setVisibility(View.GONE);
        }
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
            HomeFragment.ChangeTabItems("profile");
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
                            HomeFragment.ChangeTabItems("home");

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

    @OnClick({R.id.btn_update, R.id.btn_logout, R.id.profile_image,
            R.id.add_id_photo, R.id.add_drive_photo, R.id.gallery, R.id.camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                checkUserValidation();
                break;
            case R.id.profile_image:
                pic_chooser = "profile";
                picsFrame.setVisibility(View.VISIBLE);
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

        prefs = getContext().getSharedPreferences("UserFile", MODE_PRIVATE);
        String userId = prefs.getString("id", null);

        try {
            File file = new File(selectedFirstPath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photo", file.getName(), mFile);
            Log.i("values ", " " + userId + " " + fileToUpload);

            Call<UpdateModel> call = apiService.updateService(userId, etProfileName.getText().toString() + "",
                    etProfileEmail.getText().toString() + "", etProfilePhone.getText().toString() + "",
                    userType, etProfilePassword.getText().toString() + "", "0",
                    fileToUpload, "0", "0", "0", "0");
            call.enqueue(new Callback<UpdateModel>() {
                @Override
                public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                    progressFrame.setVisibility(View.GONE);
                    if (response.body() != null) {
                        if (response.body().getUser() != null) {
                            SharedPref.saveSharedPref(getContext(), "profilePic", response.body().getUser().getPhoto());
                            etProfileName.setText("");
                            etProfileEmail.setText("");
                            etProfilePassword.setText("");
                            etProfilePhone.setText("");
                            HomeFragment.ChangeTabItems("home");
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
            e.printStackTrace();
        }
    }

    private void checkUserValidation() {
        if (etProfileName.getText().toString().isEmpty()) {
            etProfileName.setError(getString(R.string.required_user_name));
        } else if (etProfileEmail.getText().toString().isEmpty()) {
            etProfileEmail.setError(getString(R.string.required_mail));
        } else if (!etProfileEmail.getText().toString().matches(EMAIL_PATTERN)) {
            etProfileEmail.setError(getString(R.string.invalid_email));
        } else if (etProfilePassword.getText().toString().isEmpty()) {
            etProfilePassword.setError(getString(R.string.required_pass));
        } else if (etProfilePassword.getText().length() < 8) {
            etProfilePassword.setError(getString(R.string.required_pass));
        } else if (etProfilePhone.getText().toString().isEmpty()) {
            etProfilePhone.setError(getString(R.string.required_phone));
        } /*else if (etDrivePic.getText().toString().isEmpty()) {
            etDrivePic.setError(getString(R.string.required_photo));
        } else if (etIdPic.getText().toString().isEmpty()) {
            etIdPic.setError(getString(R.string.required_photo));
        }*/ else if (bitmaps.size() == 0) {
            Toast.makeText(getContext(), R.string.required_profile_pic, Toast.LENGTH_LONG).show();
        } else {
            callUpdateService();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == SELECT_FILE) {
                    selectedFirstFileUri = data.getData();
                    selectedFirstPath = RealPathUtil.getRealPathFromURI_API19(getContext(), selectedFirstFileUri);
                    onSelectFromGalleryResult(data);
                } else if (requestCode == REQUEST_CAMERA) {
                    onCaptureImageResult(data);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getContext(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    selectedFirstPath = getRealPathFromURI(tempUri);
                    /*final Uri contentUri = data.getData();
                    final String[] proj = {MediaStore.Images.Media.DATA};
                    final Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
                    final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToLast();
                    selectedFirstPath = cursor.getString(column_index);*/
                    Log.i("iiiiiiiiiiii", " " + " " + selectedFirstPath);
                    onCaptureImageResult(data);
                    Toast.makeText(getContext(),"image saved"  , Toast.LENGTH_LONG).show();
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
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bm, 300, 300, false);
        bm = resizedBitmap;
        bitmaps.add(bm);
        picsFrame.setVisibility(View.GONE);
        if (pic_chooser.equals("id")) {
            ivIdPic.setVisibility(View.VISIBLE);
            ivIdPic.setImageBitmap(bm);
            Uri path;
            path = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "rentei_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            String filepath = path.getPath();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            etIdPic.setText(filename);
            profileImage.setImageBitmap(bm);
        } else if (pic_chooser.equals("driver")) {
            ivDriverPic.setVisibility(View.VISIBLE);
            ivDriverPic.setImageBitmap(bm);
            Uri path;
            path = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "rentei_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            String filepath = path.getPath();
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            etDrivePic.setText(filename);
            profileImage.setImageBitmap(bm);
        } else {
            profileImage.setImageBitmap(bm);
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
            picsFrame.setVisibility(View.GONE);
            profileImage.setImageBitmap(bm);
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

    @OnClick(R.id.btn_create_account)
    public void onViewClicked() {
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
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}


