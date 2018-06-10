package extra4it.fahmy.com.rentei.Fragment.UserFragmenta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.progresviews.ProgressWheel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import extra4it.fahmy.com.rentei.Activity.intro.MainActivity;
import extra4it.fahmy.com.rentei.Adapter.UserPendingAdapter;
import extra4it.fahmy.com.rentei.Fragment.AgencyTabsFragment;
import extra4it.fahmy.com.rentei.Fragment.HomeFragment;
import extra4it.fahmy.com.rentei.Model.UserPendingModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import extra4it.fahmy.com.rentei.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Fehoo on 1/31/2018.
 */

public class OrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.wheelprogress)
    ProgressWheel wheelProgress;
    Unbinder unbinder;
    String MY_PREFS_NAME = "UserFile";
    String userType, userID;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    @BindView(R.id.pending_recycle)
    RecyclerView pendingRecycle;
    @BindView(R.id.pending_order_view)
    LinearLayout pendingOrderView;
    @BindView(R.id.active_order_view)
    LinearLayout activeOrderView;

    public UserPendingAdapter pendingAdapter;
    public ArrayList<UserPendingModel.DataEntity> orderPendingList = new ArrayList<>();

    public UserPendingAdapter activeAdapter;
    public ArrayList<UserPendingModel.DataEntity> orderActiveList = new ArrayList<>();

    ApiInterface apiService;

    @BindView(R.id.active_recycle)
    RecyclerView activeRecycle;
    @BindView(R.id.no_pending_view)
    LinearLayout noPendingView;
    @BindView(R.id.no_active_view)
    LinearLayout noActiveView;
    @BindView(R.id.orders_view)
    LinearLayout ordersView;
    @BindView(R.id.btn_create_account)
    Button btnCreateAccount;
    @BindView(R.id.register_view)
    LinearLayout registerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    private MainFragment.OnFragmentInteractionListener mListener;

    public OrdersFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_orders, container, false);
        apiService = ApiClient.getClient(ApiInterface.class);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        unbinder = ButterKnife.bind(this, v);
        wheelProgress.setPercentage(300);
        wheelProgress.setDefText("HR");
        wheelProgress.setStepCountText("3");

        pendingAdapter = new UserPendingAdapter(getContext(), orderPendingList);
        pendingRecycle.setHasFixedSize(true);
        pendingRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

        activeAdapter = new UserPendingAdapter(getContext(), orderActiveList);
        activeRecycle.setHasFixedSize(true);
        activeRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("id", null);

        if (userID.equals("999999999")) {
            ordersView.setVisibility(View.GONE);
            registerView.setVisibility(View.VISIBLE);
        } else {
            callPendingOrders();
            callActiveOrders();
        }
        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                callPendingOrders();
                callActiveOrders();
                // Stop refresh animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });*/
        return v;
    }

    private void callPendingOrders() {
        Call<UserPendingModel> call = apiService.getUserPending_orders(userID);
        call.enqueue(new Callback<UserPendingModel>() {
            @Override
            public void onResponse(Call<UserPendingModel> call, Response<UserPendingModel> response) {
                orderPendingList.clear();
                UserPendingModel model = response.body();
                if (model != null) {
                    orderPendingList.addAll(model.getData());
                }

                if (orderPendingList.size() >= 1) {
                    pendingOrderView.setVisibility(View.VISIBLE);
                } else {
                    noPendingView.setVisibility(View.VISIBLE);
                }
                pendingRecycle.setAdapter(pendingAdapter);
            }

            @Override
            public void onFailure(Call<UserPendingModel> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    private void callActiveOrders() {
        Call<UserPendingModel> callActiveOrders = apiService.getUserActive_orders(userID);
        callActiveOrders.enqueue(new Callback<UserPendingModel>() {
            @Override
            public void onResponse(Call<UserPendingModel> call, Response<UserPendingModel> response) {
                orderActiveList.clear();
                UserPendingModel model = response.body();
                if (model != null) {
                    orderActiveList.addAll(model.getData());
                }
                if (orderActiveList.size() >= 1) {
                    activeOrderView.setVisibility(View.VISIBLE);
                } else {
                    noActiveView.setVisibility(View.VISIBLE);
                }
                activeRecycle.setAdapter(activeAdapter);
            }

            @Override
            public void onFailure(Call<UserPendingModel> call, Throwable t) {
                // Log error here since request failed
            }
        });
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
            editor = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            userType = prefs.getString("type", null);
            Log.i("userType Shared", userType + "");
            if (userType.equals("company")) {
                AgencyTabsFragment.ChangeTabItems("orders");
            } else if (userType.equals("user")) {
                HomeFragment.ChangeTabItems("orders");
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
                        if (userType.equals("company")) {
                            AgencyTabsFragment.ChangeTabItems("home");
                        } else if (userType.equals("user")) {
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

