package com.dapl.halaladvisor.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.Model.RestaurantOpenHourObject;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import org.json.JSONArray;

import java.util.Arrays;


public class HoursFragment extends Fragment {
    TextView tv_mon_timing, tv_tues_timing, tv_wed_timing,
            tv_thur_timing, tv_fri_timing, tv_sat_timing,
            tv_sun_timing;
    Context context;
   // AdView adView;
    TextView tvMsg;
    RestaurantDetailObject restDetailsModel;

    public HoursFragment(RestaurantDetailObject restDetailsModel) {
        this.restDetailsModel = restDetailsModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hours, container, false);
        initUi(view);
      //  initAds(view);
        return view;
    }
/*

    public  void initAds(View view){
        adView = view.findViewById(R.id.ad_view);

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(context) ;

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
      */
/*  MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("8158E5D3B1EB35D6983BD8FAF1A58DDC"))
                .build()
        );
*//*



        AdRequest adRequest= new AdRequest.Builder().build();
        // Create an ad request.


        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }
*/


    public void setTimings() {
        try {
            JSONArray arrayOpeningHour = new JSONArray(restDetailsModel.getOpen_hour_jsn_arr());

            String openingHourJsnObj = arrayOpeningHour.getJSONObject(0).toString().trim();
            RestaurantOpenHourObject openHourObject = (RestaurantOpenHourObject) PrefUtils.getObjectFromJson(openingHourJsnObj, RestaurantOpenHourObject.class);


            //Monday=======

            if (!openHourObject.getMdonday_opening_time().equalsIgnoreCase("") && !openHourObject.getMdonday_closeing_time().equalsIgnoreCase("")) {
                tv_mon_timing.setText(String.format("%s to %s", openHourObject.getMdonday_opening_time(), openHourObject.getMdonday_closeing_time()));
            } else {
                tv_mon_timing.setText("NA");
            }

            //Tuesday=======
            if (!openHourObject.getTuesday_opening_time().equalsIgnoreCase("") && !openHourObject.getTuesday_closeing_time().equalsIgnoreCase("")) {
                tv_tues_timing.setText(String.format("%s to %s", openHourObject.getTuesday_opening_time(), openHourObject.getTuesday_closeing_time()));
            } else {
                tv_tues_timing.setText("NA");
            }


            //Wedday=======
            if (!openHourObject.getWednesday_opening_time().equalsIgnoreCase("") && !openHourObject.getWednesday_closeing_time().equalsIgnoreCase("")) {
                tv_wed_timing.setText(String.format("%s to %s", openHourObject.getWednesday_opening_time(), openHourObject.getWednesday_closeing_time()));
            } else {
                tv_wed_timing.setText("NA");
            }

            //ThusDay=======
            if (!openHourObject.getThursday_opening_time().equalsIgnoreCase("") && !openHourObject.getThursday_closeing_time().equalsIgnoreCase("")) {
                tv_thur_timing.setText(String.format("%s to %s", openHourObject.getThursday_opening_time(), openHourObject.getThursday_closeing_time()));
            } else {
                tv_thur_timing.setText("NA");
            }


            //Friday=======
            if (!openHourObject.getFriday_opening_time().equalsIgnoreCase("") && !openHourObject.getFriday_closeing_time().equalsIgnoreCase("")) {
                tv_fri_timing.setText(String.format("%s to %s", openHourObject.getFriday_opening_time(), openHourObject.getFriday_closeing_time()));
            } else {
                tv_fri_timing.setText("NA");
            }

            //SatDay=======
            if (!openHourObject.getSaturday_opening_time().equalsIgnoreCase("") && !openHourObject.getSaturday_closeing_time().equalsIgnoreCase("")) {
                tv_sat_timing.setText(String.format("%s to %s", openHourObject.getSaturday_opening_time(), openHourObject.getSaturday_closeing_time()));
            } else {
                tv_sat_timing.setText("NA");
            }

            //Sunday=======
            if (!openHourObject.getSun_opening_time().equalsIgnoreCase("") && !openHourObject.getSun_closeing_time().equalsIgnoreCase("")) {
                tv_sun_timing.setText(String.format("%s to %s", openHourObject.getSun_opening_time(), openHourObject.getSun_closeing_time()));
            } else {
                tv_sun_timing.setText("NA");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
//        adView.pause();
        super.onResume();



    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public void initUi(View view) {
        tvMsg = view.findViewById(R.id.tvMsg);
        tv_mon_timing = view.findViewById(R.id.tv_mon_timing);
        tv_tues_timing = view.findViewById(R.id.tv_tues_timing);
        tv_wed_timing = view.findViewById(R.id.tv_wed_timing);
        tv_thur_timing = view.findViewById(R.id.tv_thur_timing);
        tv_fri_timing = view.findViewById(R.id.tv_fri_timing);
        tv_sat_timing = view.findViewById(R.id.tv_sat_timing);
        tv_sun_timing = view.findViewById(R.id.tv_sun_timing);
        setTimings();
    }
}