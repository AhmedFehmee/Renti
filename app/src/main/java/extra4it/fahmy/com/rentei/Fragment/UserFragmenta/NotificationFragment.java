package extra4it.fahmy.com.rentei.Fragment.UserFragmenta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import extra4it.fahmy.com.rentei.Activity.intro.MainActivity;
import extra4it.fahmy.com.rentei.Adapter.NotificationAapter;
import extra4it.fahmy.com.rentei.Fragment.AgencyTabsFragment;
import extra4it.fahmy.com.rentei.Fragment.HomeFragment;
import extra4it.fahmy.com.rentei.Model.NotificationModel;
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
 * Created by Fehoo on 2/1/2018.
 */

public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.notifications_recycle)
    RecyclerView notificationsRecycle;
    Unbinder unbinder;
    @BindView(R.id.no_notification_view)
    LinearLayout noNotificationView;
    @BindView(R.id.notification_view)
    LinearLayout notificationView;
    @BindView(R.id.btn_create_account)
    Button btnCreateAccount;
    @BindView(R.id.register_view)
    LinearLayout registerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    private MainFragment.OnFragmentInteractionListener mListener;
    String MY_PREFS_NAME = "UserFile";
    String userType;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public NotificationAapter notificationAdapter;
    public ArrayList<NotificationModel.NotificationsEntity> notificationList = new ArrayList<>();

    ApiInterface apiService;

    public NotificationFragment() {
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
        v = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, v);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String userId = prefs.getString("id", null);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        if (userId.equals("999999999")) {
            noNotificationView.setVisibility(View.GONE);
            registerView.setVisibility(View.VISIBLE);
        } else {
            apiService = ApiClient.getClient(ApiInterface.class);

            notificationAdapter = new NotificationAapter(getContext(), notificationList);
            notificationsRecycle.setHasFixedSize(true);
            notificationsRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

            callNotificationService(userId);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Refresh items
                    callNotificationService(userId);
                    // Stop refresh animation
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        return v;
    }

    private void callNotificationService(String userId) {
        Call<NotificationModel> callActiveOrders = apiService.getUserNotifications(userId);
        callActiveOrders.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                notificationList.clear();
                NotificationModel model = response.body();
                if (model != null) {
                    notificationList.addAll(model.getNotifications());
                }
                if (notificationList.size() == 0) {
                    noNotificationView.setVisibility(View.VISIBLE);
                }
                notificationsRecycle.setAdapter(notificationAdapter);
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
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
                AgencyTabsFragment.ChangeTabItems("notification");
            } else if (userType.equals("user")) {
                HomeFragment.ChangeTabItems("notification");
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


