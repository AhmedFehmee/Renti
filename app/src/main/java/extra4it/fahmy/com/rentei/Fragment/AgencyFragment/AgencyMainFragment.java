package extra4it.fahmy.com.rentei.Fragment.AgencyFragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import extra4it.fahmy.com.rentei.Activity.HomeActivity;
import extra4it.fahmy.com.rentei.Adapter.AgencyPendingOrdersAdapter;
import extra4it.fahmy.com.rentei.Fragment.AgencyTabsFragment;
import extra4it.fahmy.com.rentei.Fragment.UserFragmenta.MainFragment;
import extra4it.fahmy.com.rentei.Model.AgencyPendingOrder;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Fehoo on 3/1/2018.
 */

public class AgencyMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.pending_recycle)
    RecyclerView pendingRecycle;
    @BindView(R.id.pending_view)
    LinearLayout pendingView;
    @BindView(R.id.active_recycle)
    RecyclerView activeRecycle;
    @BindView(R.id.active_view)
    LinearLayout activeView;
    Unbinder unbinder;
    @BindView(R.id.no_pending_view)
    LinearLayout noPendingView;
    @BindView(R.id.no_active_view)
    LinearLayout noActiveView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;

    public AgencyPendingOrdersAdapter pendingAdapter;
    public ArrayList<AgencyPendingOrder.DataEntity> orderPendingList = new ArrayList<>();

    public AgencyPendingOrdersAdapter activeAdapter;
    public ArrayList<AgencyPendingOrder.DataEntity> orderActiveList = new ArrayList<>();

    ApiInterface apiService;

    String MY_PREFS_NAME = "UserFile";
    String userType, userID;
    SharedPreferences prefs;

    public AgencyMainFragment() {
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
        v = inflater.inflate(R.layout.fragment_agency_main, container, false);
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity.getMyData() != null) {
            AgencyTabsFragment.ChangeTabItems("profile");
        }
        unbinder = ButterKnife.bind(this, v);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        apiService = ApiClient.getClient(ApiInterface.class);
        pendingAdapter = new AgencyPendingOrdersAdapter(getContext(), orderPendingList);
        pendingRecycle.setHasFixedSize(true);
        pendingRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

        activeAdapter = new AgencyPendingOrdersAdapter(getContext(), orderActiveList);
        activeRecycle.setHasFixedSize(true);
        activeRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getString("id", null);

        getOfficePendingOrder();

        getOfficeActiveOrders();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOfficePendingOrder();

        getOfficeActiveOrders();
    }

    private void getOfficePendingOrder() {
        Call<AgencyPendingOrder> call = apiService.getOfficePending_orders(userID);
        call.enqueue(new Callback<AgencyPendingOrder>() {
            @Override
            public void onResponse(Call<AgencyPendingOrder> call, Response<AgencyPendingOrder> response) {
                AgencyPendingOrder model = response.body();
                orderPendingList.clear();
                if (model != null && model.getData() != null) {
                    orderPendingList.addAll(model.getData());
                }

                if (orderPendingList.size() >= 1) {
                    pendingView.setVisibility(View.VISIBLE);
                } else {
                    noPendingView.setVisibility(View.VISIBLE);
                }
                pendingRecycle.setAdapter(pendingAdapter);
            }

            @Override
            public void onFailure(Call<AgencyPendingOrder> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    private void getOfficeActiveOrders() {
        Call<AgencyPendingOrder> callActiveOrders = apiService.getOfficeActive_orders(userID);
        callActiveOrders.enqueue(new Callback<AgencyPendingOrder>() {
            @Override
            public void onResponse(Call<AgencyPendingOrder> call, Response<AgencyPendingOrder> response) {
                AgencyPendingOrder model = response.body();
                orderActiveList.clear();
                if (model != null && model.getData() != null) {
                    orderActiveList.addAll(model.getData());
                }

                if (orderActiveList.size() >= 1) {
                    activeView.setVisibility(View.VISIBLE);
                }else {
                    noActiveView.setVisibility(View.VISIBLE);
                }
                activeRecycle.setAdapter(activeAdapter);
            }

            @Override
            public void onFailure(Call<AgencyPendingOrder> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                    AgencyTabsFragment.ChangeTabItems("home");
                }
            } else {
                AgencyTabsFragment.ChangeTabItems("home");
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
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}

