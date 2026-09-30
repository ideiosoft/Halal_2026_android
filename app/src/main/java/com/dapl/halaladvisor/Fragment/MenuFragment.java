package com.dapl.halaladvisor.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Adapter.MenuAdapter;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.Model.getmenures.GetMenuRes;
import com.dapl.halaladvisor.Model.getmenures.Menu;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.interfaces.RedeemClickInteface;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MenuFragment extends Fragment implements RedeemClickInteface {
    RecyclerView rvMenu;
    Context context;
    TextView tvNoRecord;
    TextView tvMsg;
    RestaurantDetailObject restDetailsModel;
    ProgressBar progressBar;
    ApiService apiService;
    AdView adView;
    MenuAdapter menuAdapter;
    ArrayList<Menu> menuList = new ArrayList<>();

    public MenuFragment(RestaurantDetailObject restDetailsModel) {
        this.restDetailsModel = restDetailsModel;

    }

  /*  public void initAds(View view) {
        adView = view.findViewById(R.id.ad_view);

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(context);

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
      *//*  MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("8158E5D3B1EB35D6983BD8FAF1A58DDC"))
                .build()
        );
*//*


        AdRequest adRequest = new AdRequest.Builder().build();
        // Create an ad request.


        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);


        //init ui
        initUi(view);
       // initAds(view);
        return view;
    }
    @Override
    public void onResume() {
       // adView.pause();
        super.onResume();



    }

    public void initUi(View view) {
        rvMenu = view.findViewById(R.id.rvMenu);
        tvMsg = view.findViewById(R.id.tvMsg);


        tvNoRecord = view.findViewById(R.id.tvNoRecord);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        progressBar = view.findViewById(R.id.progressBar);
        setImagesAdapter();


        getMenuList();
    }


    @Override
    public void onClick(String cuisineId, String name) {

    }


    private void getMenuList() {

        progressBar.setVisibility(View.VISIBLE);
        apiService.getRestaurantMenuApi(restDetailsModel.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetMenuRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetMenuRes res) {
                        Log.e("res", " res--" + res);
                        progressBar.setVisibility(View.GONE);
                        if (res.getStatus().equals("success")) {
                            menuList.clear();
                            menuList.addAll(res.getMenus());


                            if (menuList.size() == 0) {
                                tvNoRecord.setVisibility(View.VISIBLE);
                                rvMenu.setVisibility(View.GONE);
                            } else {
                                tvNoRecord.setVisibility(View.GONE);
                                rvMenu.setVisibility(View.VISIBLE);
                            }

                            setImagesAdapter();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err", " in profile--" + e.toString());
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public void setImagesAdapter() {
        menuAdapter = new MenuAdapter(context, menuList);
        LinearLayoutManager linearLayoutManager =
                new GridLayoutManager(context, 2);
        rvMenu.setLayoutManager(linearLayoutManager);
        rvMenu.setAdapter(menuAdapter);


    }

}