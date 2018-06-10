package extra4it.fahmy.com.rentei.Activity.User.search;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.BrandsAdapter;
import extra4it.fahmy.com.rentei.Activity.Agency.addCar.adapter.ModelAdapter;
import extra4it.fahmy.com.rentei.Adapter.CitiesAdapter;
import extra4it.fahmy.com.rentei.Adapter.SearchAdapter;
import extra4it.fahmy.com.rentei.Adapter.YearAdapter;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.Model.BrandModel;
import extra4it.fahmy.com.rentei.Model.CityModel;
import extra4it.fahmy.com.rentei.Model.Model;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements CitiesAdapter.OnItemClick
        , BrandsAdapter.OnItemClick, ModelAdapter.OnItemClick , YearAdapter.OnItemClick{

    @BindView(R.id.search_toolbar)
    LinearLayout searchToolbar;
    @BindView(R.id.search_city)
    TextView searchCity;
    @BindView(R.id.search_brand)
    TextView searchBrand;
    @BindView(R.id.search_model)
    TextView searchModel;
    @BindView(R.id.search_year)
    TextView searchYear;
    @BindView(R.id.search_recycle)
    RecyclerView searchRecycle;
    @BindView(R.id.brands_recycle)
    RecyclerView brandsRecycle;
    @BindView(R.id.brands_view)
    LinearLayout brandsView;
    @BindView(R.id.models_recycle)
    RecyclerView modelsRecycle;
    @BindView(R.id.model_view)
    LinearLayout modelView;
    @BindView(R.id.cities_recycle)
    RecyclerView citiesRecycle;
    @BindView(R.id.cities_view)
    LinearLayout citiesView;
    @BindView(R.id.years_recycle)
    RecyclerView yearsRecycle;
    @BindView(R.id.years_view)
    LinearLayout yearsView;
    @BindView(R.id.search_view)
    LinearLayout searchView;
    @BindView(R.id.cars_view)
    ScrollView carsView;
    @BindView(R.id.btn_search)
    AppCompatButton btnSearch;
    @BindView(R.id.layout_nothing)
    LinearLayout layoutNothing;
    @BindView(R.id.progress_frame)
    FrameLayout progressFrame;

    private BottomSheetBehavior brandsSheetBehavior;
    public BrandsAdapter brandAdapter;
    public ArrayList<BrandModel.BrandsEntity> brandArray = new ArrayList<>();
    String brandId = null;

    private BottomSheetBehavior modelSheetBehavior;
    public ModelAdapter modelAdapter;
    public ArrayList<Model.ModelsEntity> modelArray = new ArrayList<>();
    String modelId = null;

    private BottomSheetBehavior citiesSheetBehavior;
    public CitiesAdapter citiesAdapter;
    public ArrayList<CityModel.CitiesEntity> citiesArray = new ArrayList<>();
    String cityId = null;

    private BottomSheetBehavior yearsSheetBehavior;
    public YearAdapter yearsAdapter;
    public ArrayList<Model.ModelsEntity> yearsArray = new ArrayList<>();
    String yearId = null;

    public SearchAdapter searchAdapter;
    public ArrayList<AgencyModel.DataEntity> searchArray = new ArrayList<>();

    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#6ad465"));
        }

        carsView.setVisibility(View.GONE);
        layoutNothing.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);

        apiService = ApiClient.getClient(ApiInterface.class);

        citiesAdapter = new CitiesAdapter(this, citiesArray, this);
        citiesRecycle.setHasFixedSize(true);
        citiesRecycle.setLayoutManager(new LinearLayoutManager(this));
        citiesRecycle.setAdapter(citiesAdapter);
        citiesSheetBehavior = BottomSheetBehavior.from(citiesView);
        citiesSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        citiesSheetBehavior.setPeekHeight(0);

        brandAdapter = new BrandsAdapter(this, brandArray, this);
        brandsRecycle.setHasFixedSize(true);
        brandsRecycle.setLayoutManager(new LinearLayoutManager(this));
        brandsRecycle.setAdapter(brandAdapter);
        brandsSheetBehavior = BottomSheetBehavior.from(brandsView);
        brandsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        brandsSheetBehavior.setPeekHeight(0);

        modelAdapter = new ModelAdapter(this, modelArray, this);
        modelsRecycle.setHasFixedSize(true);
        modelsRecycle.setLayoutManager(new LinearLayoutManager(this));
        modelsRecycle.setAdapter(modelAdapter);
        modelSheetBehavior = BottomSheetBehavior.from(modelView);
        modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        modelSheetBehavior.setPeekHeight(0);

        yearsAdapter = new YearAdapter(this, yearsArray, this);
        yearsRecycle.setHasFixedSize(true);
        yearsRecycle.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0 ; i <= 20 ; i++ ){
            Model.ModelsEntity year = new Model.ModelsEntity();
            year.setName((2018 - i) + "");
            yearsArray.add(year);
        }
        yearsRecycle.setAdapter(yearsAdapter);
        yearsSheetBehavior = BottomSheetBehavior.from(yearsView);
        yearsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        yearsSheetBehavior.setPeekHeight(0);

        searchAdapter = new SearchAdapter(this, searchArray);
        searchRecycle.setHasFixedSize(true);
        searchRecycle.setLayoutManager(new LinearLayoutManager(this));
        searchRecycle.setAdapter(searchAdapter);

        callCitiesService();
        callBrandsService();

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

    private void callCitiesService() {
        Call<CityModel> call = apiService.getCities();
        call.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if (response.body().getCities().size() >= 1) {
                    citiesArray.addAll(response.body().getCities());
                    citiesRecycle.setAdapter(citiesAdapter);
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        brandId = String.valueOf(brandArray.get(position).getId());
        searchBrand.setText(brandArray.get(position).getName());
        if (brandsSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            brandsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onModelClick(View view, int position) {
        modelId = String.valueOf(modelArray.get(position).getId());
        searchModel.setText(modelArray.get(position).getName());
        if (modelSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onCityClick(View view, int position) {
        cityId = String.valueOf(citiesArray.get(position).getId());
        searchCity.setText(citiesArray.get(position).getName());
        if (citiesSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            citiesSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onYearClick(View view, int position) {
        yearId = String.valueOf(yearsArray.get(position).getName());
        searchYear.setText(yearsArray.get(position).getName());
        if (yearsSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            yearsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
    @OnClick({R.id.search_city, R.id.search_brand, R.id.search_model, R.id.search_year, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_city:
                if (citiesSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    citiesSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    citiesSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.search_brand:
                if (brandsSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    brandsSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    brandsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.search_model:
                if (brandId != null) {
                    callModelsService(brandId);
                    if (modelSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        modelSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
                break;
            case R.id.search_year:
                if (yearsSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    yearsSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    yearsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.btn_search:
                callSearchService();

                carsView.setVisibility(View.GONE);
                layoutNothing.setVisibility(View.VISIBLE);

                searchView.setVisibility(View.GONE);
                break;
        }
    }

    private void callSearchService() {
        progressFrame.setVisibility(View.VISIBLE);
        Call<AgencyModel> call = apiService.searchService(brandId, modelId, "2018", cityId);
        call.enqueue(new Callback<AgencyModel>() {
            @Override
            public void onResponse(Call<AgencyModel> call, Response<AgencyModel> response) {
                if(!response.message().contains("error")) {
                    if (response.body().getData().size() >= 1) {
                        searchArray.addAll(response.body().getData());
                        searchRecycle.setAdapter(searchAdapter);
                        carsView.setVisibility(View.VISIBLE);
                        layoutNothing.setVisibility(View.GONE);
                    }
                }
                progressFrame.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AgencyModel> call, Throwable t) {
                progressFrame.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (brandsSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            brandsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (modelSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            modelSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (citiesSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            citiesSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (yearsSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            yearsSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (carsView.getVisibility() == View.VISIBLE) {
            carsView.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }
}
