package com.developer.hrg.noralsalehin.InsideChanel.toolbar;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.hrg.noralsalehin.Helps.ApiInterface;
import com.developer.hrg.noralsalehin.Helps.Apiclient;
import com.developer.hrg.noralsalehin.Helps.InternetCheck;
import com.developer.hrg.noralsalehin.Helps.SimpleResponse;
import com.developer.hrg.noralsalehin.R;
import com.developer.hrg.noralsalehin.Models.Profile;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Fragment extends Fragment implements View.OnClickListener {


    public static String CHANEL_ID = "chanel_iD";
    int chanel_id;
    TextView tv_count;
    ImageView iv_back;
    FrameLayout fl_profile;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    ArrayList<Profile> profiles = new ArrayList<>();
    ViewPager viewPager;
    Adapter_Profile adapter_profile;
    NetworkChangeReceiver networkChanereciver;
    boolean firstTime = true;
    boolean showDetails = true;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    public static Profile_Fragment getInstance(int chanel_id) {
        Profile_Fragment profile_fragment = new Profile_Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CHANEL_ID, chanel_id);
        profile_fragment.setArguments(bundle);
        return profile_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chanel_id = getArguments().getInt(CHANEL_ID);
        }

        adapter_profile = new Adapter_Profile(getActivity(), profiles);
        networkChanereciver = new NetworkChangeReceiver();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        iv_back = (ImageView) view.findViewById(R.id.iv_back_profile);
        tv_count = (TextView) view.findViewById(R.id.tv_count_profile);
        fl_profile = (FrameLayout) view.findViewById(R.id.framelayout_profile);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraint_profile);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_profile);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_profile);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPager.setAdapter(adapter_profile);
        viewPager.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        getAllProfiles();
        handeViewpagerChaneLisener(viewPager);

    }

    private void getAllProfiles() {
        ApiInterface apiInterface = Apiclient.getClient().create(ApiInterface.class);
        Call<SimpleResponse> call_profiles = apiInterface.getAllProfiles(chanel_id);
        call_profiles.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (!response.isSuccessful()) {

                } else {

                    profiles.addAll(response.body().getProfiles());
                    adapter_profile.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    tv_count.setText("1 / " + response.body().getProfiles().size());

                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkChanereciver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(networkChanereciver);
    }

    public void handeViewpagerChaneLisener(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int current = position + 1;
                tv_count.setText(current + " / " + profiles.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == iv_back) {

            getFragmentManager().popBackStack();
        } else if (view == viewPager) {


//            if (showDetails) {
//                constraintLayout.setVisibility(View.GONE);
//            } else {
//                constraintLayout.setVisibility(View.GONE);
//            }
//            showDetails = !showDetails;

        }
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (firstTime) {
                firstTime = false;
            } else {
                if (InternetCheck.isOnline(getActivity())) {
                    if (profiles.size() == 0) {
                        getAllProfiles();
                    }


                }
            }


        }
    }


}
