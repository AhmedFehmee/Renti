package extra4it.fahmy.com.rentei.Fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import extra4it.fahmy.com.rentei.Activity.HomeActivity;
import extra4it.fahmy.com.rentei.Adapter.ViewPagerAdapter;
import extra4it.fahmy.com.rentei.Fragment.AgencyFragment.AddCarFragment;
import extra4it.fahmy.com.rentei.Fragment.AgencyFragment.AgencyMainFragment;
import extra4it.fahmy.com.rentei.Fragment.AgencyFragment.AgencyProfileFragment;
import extra4it.fahmy.com.rentei.Fragment.UserFragmenta.MainFragment;
import extra4it.fahmy.com.rentei.Fragment.UserFragmenta.NotificationFragment;
import extra4it.fahmy.com.rentei.Fragment.UserFragmenta.OrdersFragment;
import extra4it.fahmy.com.rentei.Fragment.UserFragmenta.ProfileFragment;
import extra4it.fahmy.com.rentei.R;

/**
 * Created by Fehoo on 2/25/2018.
 */

public class AgencyTabsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static TabLayout tabLayout;
    int color;
    static Window window;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgencyTabsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewPage_home);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout_home);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.lower_tab_Indicator));
        tabLayout.getTabAt(0).setIcon(R.drawable.home_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.order_tab);
        tabLayout.getTabAt(2).setIcon(R.drawable.notification_tab);
        tabLayout.getTabAt(3).setIcon(R.drawable.account_tab);
        window = getActivity().getWindow();

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new AgencyMainFragment());
        adapter.addFrag(new AddCarFragment() );
        adapter.addFrag(new NotificationFragment());
        adapter.addFrag(new AgencyProfileFragment());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void ChangeTabItems(String string) {
        if (string.equals("home")) {
            if (HomeActivity.toolbar.getVisibility() == View.GONE) {
                HomeActivity.toolbar.setVisibility(View.VISIBLE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#6ad465"));
            }
            HomeActivity.homeToolbar.setBackgroundColor(Color.parseColor("#84F77E"));
            HomeActivity.searchToolbar.setVisibility(View.GONE);
            HomeActivity.homeToolbar.setVisibility(View.VISIBLE);
            tabLayout.getTabAt(0).setIcon(R.drawable.home_tab_active);
            tabLayout.getTabAt(1).setIcon(R.drawable.add_car);
            tabLayout.getTabAt(2).setIcon(R.drawable.notification_tab);
            tabLayout.getTabAt(3).setIcon(R.drawable.account_tab);
            tabLayout.getTabAt(0).select();
        } else if (string.equals("orders")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#6ad465"));
            }
            HomeActivity.toolbar.setVisibility(View.GONE);
            HomeActivity.homeToolbar.setBackgroundColor(Color.parseColor("#84F77E"));
            HomeActivity.searchToolbar.setVisibility(View.GONE);
            HomeActivity.homeToolbar.setVisibility(View.GONE);
            tabLayout.getTabAt(1).setIcon(R.drawable.add_car_active);
            tabLayout.getTabAt(0).setIcon(R.drawable.home_tab);
            tabLayout.getTabAt(2).setIcon(R.drawable.notification_tab);
            tabLayout.getTabAt(3).setIcon(R.drawable.account_tab);
        } else if (string.equals("notification")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#79d9d9d9"));
            }
            HomeActivity.toolbar.setVisibility(View.GONE);
            HomeActivity.homeToolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            HomeActivity.searchToolbar.setVisibility(View.GONE);
            HomeActivity.homeToolbar.setVisibility(View.GONE);
            tabLayout.getTabAt(2).setIcon(R.drawable.notification_tab_active);
            tabLayout.getTabAt(0).setIcon(R.drawable.home_tab);
            tabLayout.getTabAt(1).setIcon(R.drawable.add_car);
            tabLayout.getTabAt(3).setIcon(R.drawable.account_tab);
        } else if (string.equals("profile")) {
            if (HomeActivity.toolbar.getVisibility() == View.GONE) {
                HomeActivity.toolbar.setVisibility(View.VISIBLE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#6ad465"));
            }
            HomeActivity.homeToolbar.setBackgroundColor(Color.parseColor("#84F77E"));
            HomeActivity.searchToolbar.setVisibility(View.GONE);
            HomeActivity.homeToolbar.setVisibility(View.VISIBLE);
            tabLayout.getTabAt(3).setIcon(R.drawable.account_tab_active);
            tabLayout.getTabAt(0).setIcon(R.drawable.home_tab);
            tabLayout.getTabAt(1).setIcon(R.drawable.add_car);
            tabLayout.getTabAt(2).setIcon(R.drawable.notification_tab);
            tabLayout.getTabAt(3).select();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

