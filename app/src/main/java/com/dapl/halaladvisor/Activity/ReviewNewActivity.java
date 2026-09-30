package com.dapl.halaladvisor.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapl.halaladvisor.Adapter.GoogleReviewsAdapter;
import com.dapl.halaladvisor.Adapter.ReviewsAdapter;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.ReviewObject;
import com.dapl.halaladvisor.Model.addreviewres.AddReviewRes;
import com.dapl.halaladvisor.Model.fetchallreviews.Datum;
import com.dapl.halaladvisor.Model.fetchallreviews.FetchAllReviewsRes;
import com.dapl.halaladvisor.Model.getgooglereviews.GetGoogleReviewsRes;
import com.dapl.halaladvisor.Model.getgooglereviews.Review;
import com.dapl.halaladvisor.Model.getplaceidres.GetPlaceIdRes;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.AlertDialogManager;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.extra.AppController;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReviewNewActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    Context context;
    RecyclerView rvReviews;
    RecyclerView rvGoogleReviews;
    ReviewsAdapter reviewAdapter;
    GoogleReviewsAdapter googleReviewsAdapter;
    ArrayList<Datum> reviewList = new ArrayList<>();
    ArrayList<Review> googleReviewList = new ArrayList<>();
    ArrayList<String> reviewImagesList = new ArrayList<>();
    TextView tvTitle, tvSubTitle, tvAddReview;
    ImageView ivBack;
    ApiService apiService;
    String baseUrl = "";
    String rid = "";
    String name = "";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        context = this;
        initUi();


    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAllReviews();


    }


    private void initUi() {
        if (getIntent().hasExtra("rid")) {
            rid = getIntent().getStringExtra("rid");
            name = getIntent().getStringExtra("name");
            getGooglePlaceId(name);

        }
        try {
            tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setText(AppController.getSelectedRestaurantDetailObject().getName());

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            tvSubTitle = findViewById(R.id.tvSubTitle);

            String mCity_name = AppController.getSelectedRestaurantDetailObject().getCity_name();
            String mState_name = AppController.getSelectedRestaurantDetailObject().getState_name();

            tvSubTitle.setText(String.format("%s, %s", mCity_name, mState_name));
        } catch (Exception e) {
            e.printStackTrace();
        }


        tvAddReview = findViewById(R.id.tvAddReview);
        tvAddReview.setOnClickListener(this);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.RED, Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(this);
        rvReviews = findViewById(R.id.rvReviews);
        rvGoogleReviews = findViewById(R.id.rvGoogleReviews);
      /*  swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        // new ReqRestaurantReviews().execute();
                                        fetchAllReviews();
                                    }
                                }
        );*/


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchAllReviews();
                swipeRefreshLayout.setRefreshing(false);
            }


        });

    }


    public void setReviewAdapter() {
        rvReviews.setLayoutManager(new LinearLayoutManager(context));
        reviewAdapter = new ReviewsAdapter(context, reviewList, baseUrl);
        rvReviews.setAdapter(reviewAdapter);
    }


    public void setGoogleReviewAdapter() {
        rvGoogleReviews.setLayoutManager(new LinearLayoutManager(context));
        googleReviewsAdapter = new GoogleReviewsAdapter(context, googleReviewList);
        rvGoogleReviews.setAdapter(googleReviewsAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvAddReview: {

                Intent intent_addReview = new Intent(ReviewNewActivity.this, AddReviewActivity.class);
                startActivity(intent_addReview);
                break;
            }


            case R.id.ivBack: {
                onBackPressed();
                break;
            }
        }


    }

    @Override
    public void onRefresh() {

    }


    private void stopRefreshing() {
        /*Checks if swipe refresh layout is still refreshing the content*/
        if (swipeRefreshLayout.isRefreshing()) {
            /*Stops refreshing on getting response*/
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void parseData(String response) {
        try {


            reviewList.clear();
            reviewImagesList.clear();
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optInt("status") == 1) {
                JSONArray array = jsonObject.optJSONArray("data");
                PrefUtils.saveReviewArrayFromReviewListScreen(context, array.toString());
                for (int i = 0; i < array.length(); i++) {

                    //  reviewList.add(array.get(i));

                    /*String jsnObj = array.getJSONObject(i).toString().trim();

                    ReviewObject object = (ReviewObject) PrefUtils.getObjectFromJson(jsnObj, ReviewObject.class);

                    reviewList.add(object);*/


                }


                baseUrl = jsonObject.optJSONObject("data").optString("base_url");

                JSONArray jArray = jsonObject.optJSONObject("data").getJSONArray("review_images");

                for (int i = 0; i < jArray.length(); i++) {


                    reviewImagesList.add(jArray.getString(i));


                }
                Log.e("reviewImagesList", "--" + reviewImagesList);
                setReviewAdapter();
                reviewAdapter.notifyDataSetChanged();

            } else {
                AlertDialogManager.showAlertDialog(ReviewNewActivity.this, "", "No Reviews Found", false, false);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ReqRestaurantReviews extends AsyncTask<Void, Void, String> {

        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, String> params = new HashMap<>();
            params.put("r_id", AppController.getSelectedRestaurent().getId());
            JSONParser parser = new JSONParser();
            String mURL = AppData.GET_ALL_REVIEWS_URL;
            LogUtils.printf("I am url: " + mURL);
            response = parser.makeHttpRequest(mURL, "POST", params);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            LogUtils.printf("I am review response: " + response);

            // parseData(response);
            fetchAllReviews();

            stopRefreshing();

            swipeRefreshLayout.setRefreshing(false);


        }


    }


    public void fetchAllReviews() {
        apiService.fetchAllReviews(
                /*   AppController.getSelectedRestaurent().getId()*/  rid).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<FetchAllReviewsRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FetchAllReviewsRes res) {
                        baseUrl = res.getBaseUrl();
                        swipeRefreshLayout.setRefreshing(false);
                        reviewList.clear();
                        reviewList.addAll(res.getData());
                        setReviewAdapter();
                        Log.e("reviewList", "-- from api --" + reviewList);
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
                            googleReviewList.clear();
                            googleReviewList.addAll(res.getResult().getReviews());
                            setGoogleReviewAdapter();
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
}
