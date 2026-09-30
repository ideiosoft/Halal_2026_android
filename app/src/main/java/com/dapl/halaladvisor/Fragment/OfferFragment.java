package com.dapl.halaladvisor.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Adapter.OffersAdapter;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.Model.getofferres.Datum;
import com.dapl.halaladvisor.Model.getofferres.GetOfferListingRes;
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

public class OfferFragment extends Fragment implements RedeemClickInteface {
    RecyclerView rvOffers;
    TextView tvNoRecord;
    Context context;
    RestaurantDetailObject restDetailsModel;
    ProgressBar progressBar;
    ApiService apiService;
//AdView adView;
    TextView tvMsg;
    ArrayList<Datum> offerList = new ArrayList();

    public OfferFragment(RestaurantDetailObject restDetailsModel) {
        this.restDetailsModel = restDetailsModel;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);


        //init ui
        initUi(view);

        //get offer listing
        getOfferList();
//initAds(view);

        return view;
    }


  /*  public  void initAds(View view){
        adView = view.findViewById(R.id.ad_view);

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(context) ;

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
      *//*  MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
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
    @Override
    public void onResume() {
     //   adView.pause();
        super.onResume();



    }
    public void initUi(View view) {
        rvOffers = view.findViewById(R.id.rvOffers);
        tvNoRecord = view.findViewById(R.id.tvNoRecord);
        tvMsg = view.findViewById(R.id.tvMsg);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        progressBar = view.findViewById(R.id.progressBar);


        getOfferList();

    }

    public void initOfferRv(ArrayList<Datum> offerList) {
        OffersAdapter offersAdapter = new OffersAdapter(context, offerList);
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(context);
        rvOffers.setLayoutManager(linearLayoutManager);
        rvOffers.setAdapter(offersAdapter);
    }

    @Override
    public void onClick(String cuisineId, String name) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void getOfferList() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getOfferListing(restDetailsModel.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetOfferListingRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetOfferListingRes res) {
                        progressBar.setVisibility(View.GONE);
                        if (res.isStatus()) {
                            offerList.clear();
                            offerList.addAll(res.getData());


                            if (offerList.size() == 0) {
                                tvNoRecord.setVisibility(View.VISIBLE);
                                rvOffers.setVisibility(View.GONE);
                            } else {
                                tvNoRecord.setVisibility(View.GONE);
                                rvOffers.setVisibility(View.VISIBLE);
                            }
                            initOfferRv(offerList);
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

}