package extra4it.fahmy.com.rentei.Activity.User.CompanyDetails;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.User.CarDetails.CarsDetailsActivity;
import extra4it.fahmy.com.rentei.Adapter.CarsOfCompanyAdapter;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import extra4it.fahmy.com.rentei.Service.CallRatingService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Fehoo on 2/1/2018.
 */

public class CompanyDetails extends AppCompatActivity implements RatingDialogListener {

    Toolbar toolbar;
    @BindView(R.id.cars_card)
    CardView carsCard;

    String MY_PREFS_NAME = "UserFile";
    SharedPreferences prefs;

    ApiInterface apiService;

    @BindView(R.id.company_img)
    ImageView companyImg;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.company_address)
    TextView companyAddress;
    @BindView(R.id.company_work_time)
    TextView companyWorkTime;
    @BindView(R.id.company_phone)
    TextView companyPhone;

    public CarsOfCompanyAdapter carsAdapter;
    public ArrayList<AgencyModel.CarsEntity> carsList = new ArrayList<>();

    @BindView(R.id.cars_recycle)
    RecyclerView carsRecycle;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;
    @BindView(R.id.no_cars_view)
    LinearLayout noCarsView;
    @BindView(R.id.add_rate)
    Button addRate;

    String companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_rentals);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient(ApiInterface.class);

        /*prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String userId = prefs.getString("id", null);
*/
        Intent i = getIntent();
        String userId = i.getStringExtra("companyId");

        Log.i("user ", "" + userId);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }

        callCompanyService(userId);

        //destination Recycle
        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        carsRecycle.setLayoutManager(horizontalLayoutManagaer);
        carsAdapter = new CarsOfCompanyAdapter(this, carsList);
        carsRecycle.setHasFixedSize(true);

    }

    private void callCompanyService(String userId) {
        progressFrame.setVisibility(View.VISIBLE);
        Call<AgencyModel> call = apiService.getCompany(userId);
        call.enqueue(new Callback<AgencyModel>() {
            @Override
            public void onResponse(Call<AgencyModel> call, Response<AgencyModel> response) {
                AgencyModel company = response.body();
                if (company.getData().size() >= 1) {
                    companyId = company.getData().get(0).getId() + "";
                    companyName.setText(company.getData().get(0).getName());
                    companyPhone.setText(company.getData().get(0).getPhone());
                    companyAddress.setText(company.getData().get(0).getAddress());
                    companyWorkTime.setText(company.getData().get(0).getStart() + " to " +
                            company.getData().get(0).getEnd());
                    if (!company.getData().get(0).getPhoto().equals("")) {
                        Glide.with(getApplicationContext())
                                .load("http://2.extra4it.net/tajeree/" + company.getData().get(0).getPhoto()).into(companyImg);
                    }
                    for (int i = 0; i < company.getData().get(0).getCars().size(); i++) {
                        carsList.add(company.getData().get(0).getCars().get(i));
                    }

                    if (carsList.size() >= 1) {
                        if (carsCard.getVisibility() == View.GONE) {
                            carsCard.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (carsCard.getVisibility() == View.VISIBLE) {
                            carsCard.setVisibility(View.GONE);
                        }
                        if (noCarsView.getVisibility() == View.GONE) {
                            noCarsView.setVisibility(View.VISIBLE);
                        }
                    }
                    progressFrame.setVisibility(View.GONE);
                    carsRecycle.setAdapter(carsAdapter);
                }
            }


            @Override
            public void onFailure(Call<AgencyModel> call, Throwable t) {
                progressFrame.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.add_rate , R.id.toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.add_rate:
                showRateDialog();
                break;
        }
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
                .create(CompanyDetails.this)
                .show();
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String userID = prefs.getString("id", null);

        Log.i("rate", i + s + "");
        CallRatingService callRatingService = new CallRatingService();
        callRatingService.callRatingService(CompanyDetails.this, progressFrame,
                userID, companyId, i + "");
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
