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

import com.dapl.halaladvisor.Adapter.GalleryAdapter;
import com.dapl.halaladvisor.Adapter.OffersAdapter;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.Model.getgalleryimagesres.GetGalleryImagesRes;
import com.dapl.halaladvisor.Model.getgalleryimagesres.Result;
import com.dapl.halaladvisor.Model.getmenures.GetMenuRes;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.interfaces.RedeemClickInteface;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;


import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class GalleryFragment extends Fragment implements RedeemClickInteface {
    RecyclerView rvGallery;
    TextView tvNoRecord;
    TextView tvMsg;
    Context context;
    ProgressBar progressBar;
    ApiService apiService;
    RestaurantDetailObject restDetailsModel;
     ArrayList<Result> imagesList = new ArrayList<>();

    public GalleryFragment(RestaurantDetailObject restDetailsModel) {
        this.restDetailsModel = restDetailsModel;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        //init ui
        initUi(view);
//initAds(view );

         return view;
    }


    public void initUi(View view) {
        rvGallery = view.findViewById(R.id.rvGallery);
        tvNoRecord = view.findViewById(R.id.tvNoRecord);
        tvMsg = view.findViewById(R.id.tvMsg);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        progressBar = view.findViewById(R.id.progressBar);

       // initOfferRv();

        getImages();
    }

    public void initOfferRv() {
    /*    imagesList.clear();
        imagesList.addAll(restDetailsModel.getResImages());
*/

        if (imagesList.size() != 0) {
            GalleryAdapter galleryAdapter = new GalleryAdapter(context, this, imagesList);
            LinearLayoutManager linearLayoutManager = new
                    GridLayoutManager(context, 3);
            rvGallery.setLayoutManager(linearLayoutManager);
            rvGallery.setAdapter(galleryAdapter);
        } else {
            tvNoRecord.setVisibility(View.VISIBLE);
            rvGallery.setVisibility(View.GONE);
        }

    }



    public void setGalleryAdapter(){
        if (imagesList.size() != 0) {




            GalleryAdapter galleryAdapter = new GalleryAdapter(context, this, imagesList);
            LinearLayoutManager linearLayoutManager = new
                    GridLayoutManager(context, 3);
            rvGallery.setLayoutManager(linearLayoutManager);
            rvGallery.setAdapter(galleryAdapter);
        } else {
            tvNoRecord.setVisibility(View.VISIBLE);
            rvGallery.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(String cuisineId, String name) {

    }
    @Override
    public void onResume() {
         super.onResume();



    }

    private void getImages() {

        progressBar.setVisibility(View.VISIBLE);
        apiService.getGalleryImages(restDetailsModel.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetGalleryImagesRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetGalleryImagesRes res) {
                        Log.e("res", " res--" + res);
                        progressBar.setVisibility(View.GONE);
                        if (res.getStatus()) {
                            imagesList.clear();
                            imagesList.addAll(res.getResult());

                            setGalleryAdapter();

                        }else{
                            tvNoRecord.setVisibility(View.VISIBLE);
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
}