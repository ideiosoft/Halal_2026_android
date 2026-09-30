package com.dapl.halaladvisor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Activity.AddReviewActivity;
import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.ReviewNewActivity;
import com.dapl.halaladvisor.Adapter.GalleryAdapter;
import com.dapl.halaladvisor.Adapter.GoogleReviewsAdapter;
import com.dapl.halaladvisor.Adapter.RestGoogleReviewsAdapter;
import com.dapl.halaladvisor.Adapter.ReviewAdapterDetails;
import com.dapl.halaladvisor.Adapter.ReviewDetailsAdapter;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.Model.getgooglereviews.GetGoogleReviewsRes;
import com.dapl.halaladvisor.Model.getplaceidres.GetPlaceIdRes;
import com.dapl.halaladvisor.Model.resdetailsres.Review;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
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

public class ReviewFragment extends Fragment implements RedeemClickInteface {
    TextView tv_allReview;
    RecyclerView rvReviews, rvGoogleReviews;
    Context context;
    TextView tvMsg;
    RestGoogleReviewsAdapter googleReviewsAdapter;
    RelativeLayout mAddReviewButton;
    ArrayList<com.dapl.halaladvisor.Model.getgooglereviews.Review> googleReviewList = new ArrayList<>();
    RestaurantDetailObject restDetailsModel;
    private TextView tv_review_value, tv_review_rate;
    private RatingBar mRatingBar, mRating_review;
    ApiService apiService;
  //  AdView adView;

    public ReviewFragment(RestaurantDetailObject restDetailsModel) {
        this.restDetailsModel = restDetailsModel;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);


        //init ui
        initUi(view);
      //  initAds(view);
        return view;
    }


/*
    public void initAds(View view) {
     //   adView = view.findViewById(R.id.ad_view);

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(context);

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



        AdRequest adRequest = new AdRequest.Builder().build();
        // Create an ad request.


        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }
*/

    @Override
    public void onResume() {

        super.onResume();



    }
    public void initUi(View view) {
        rvGoogleReviews = view.findViewById(R.id.rvGoogleReviews);
        tv_allReview = view.findViewById(R.id.tv_readAllReview);
        rvReviews = view.findViewById(R.id.rvReviews);
        tvMsg = view.findViewById(R.id.tvMsg);
        mRating_review = view.findViewById(R.id.review_user_ratingbar);
        mAddReviewButton = view.findViewById(R.id.iv_details_addReviewButton); // add by Moitri
        tv_review_value = view.findViewById(R.id.tv_review_value); // added by Moitri
        tv_review_rate = view.findViewById(R.id.tv_review_rate); // added by Moitri
        Log.e("reviewlost", "---" + restDetailsModel.getReviewsList());

        getGooglePlaceId(restDetailsModel.getName());
        if (restDetailsModel.getReviewsList().size() > 0) {
            setReviewAdapter(restDetailsModel.getReviewsList());
        }

        tv_allReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentViewReview = new Intent(context, ReviewNewActivity.class);
                intentViewReview.putExtra("rid", restDetailsModel.getId());
                intentViewReview.putExtra("name", restDetailsModel.getName());
                intentViewReview.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intentViewReview);
            }
        });

        mAddReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (PrefUtils.getProfileObject(context) != null) {
                    startActivity(new Intent(context, AddReviewActivity.class));

                } else {
                    Toast.makeText(context, "Please LogIn to review this restaurant", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //============Rating and review==============
        try {
            String rating = restDetailsModel.getRest_avg_rating();//.get(i).getRating();
            LogUtils.printf("I am Rating: " + rating);

            Float count = Float.parseFloat(rating);
            mRating_review.setRating(count);
            tv_review_rate.setText(rating);
            //set all review count
            LogUtils.printf("I am count: " + rating);

            tv_review_value.setText(String.format("[%s]", restDetailsModel.getTotal_rating()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setReviewAdapter(ArrayList<Review> list) {
        ReviewDetailsAdapter reviewAdapterDetails = new ReviewDetailsAdapter(context, list, "https://admin.halaladvisor.com.au//");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvReviews.setLayoutManager(linearLayoutManager);
        rvReviews.setAdapter(reviewAdapterDetails);


    }

    @Override
    public void onClick(String cuisineId, String name) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    public void getGooglePlaceId(String placename) {
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);

        apiService.getGooglePlaceId(
                "https://maps.googleapis.com/maps/api/geocode/json?address=" + placename + "&key=AIzaSyAwDJEMBWF47UbCpPlGh-86vBpN7ggsfDo").
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetPlaceIdRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetPlaceIdRes res) {
                        if (res.getStatus().equals("OK")) {
                            getPlaceReviews(res.getResults().get(0).getPlaceId());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err", " in profile--" + e.toString());

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    public void getPlaceReviews(String placeid) {

        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        apiService.getPlaceReviews(


                "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeid + "&key=AIzaSyAwDJEMBWF47UbCpPlGh-86vBpN7ggsfDo").
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetGoogleReviewsRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetGoogleReviewsRes res) {
                        if (res.getStatus().equals("OK")) {


                            if (res.getResult().getReviews() != null) {
                                googleReviewList.clear();
                                googleReviewList.addAll(res.getResult().getReviews());
                                if(restDetailsModel.getReviewsList().size()==0){
                                    setGoogleReviewAdapter();
                                }


                                if (googleReviewList.size() > 0) {
                                    //  tv_review_value.setText("[" + googleReviewList.size()+"]");


                                    //============Rating and review==============
                                    try {
                                        String rating = res.getResult().getRating();//.get(i).getRating();
                                        LogUtils.printf("I am Rating: " + rating);

                                        Float count = Float.parseFloat(rating);
                                        mRating_review.setRating(count);
                                        tv_review_rate.setText(rating);
                                        //set all review count
                                        LogUtils.printf("I am count: " + rating);

                                        tv_review_value.setText(String.format("[%s]",restDetailsModel.getReviewsList().size()+ googleReviewList.size()));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    tv_review_value.setText("[0]");
                                }

                            }


                        } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err", " in profile--" + e.toString());

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    public void setGoogleReviewAdapter() {
        rvGoogleReviews.setLayoutManager(new LinearLayoutManager(context));
        googleReviewsAdapter = new RestGoogleReviewsAdapter(context, googleReviewList);
        rvGoogleReviews.setAdapter(googleReviewsAdapter);
    }


}