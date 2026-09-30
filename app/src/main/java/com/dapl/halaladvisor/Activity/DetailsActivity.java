package com.dapl.halaladvisor.Activity;

import static com.dapl.halaladvisor.application_data.AppData.BASE_URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Adapter.EnhancedWrapContentViewPager;
import com.dapl.halaladvisor.Adapter.HomeRestaurentAdapter;
import com.dapl.halaladvisor.Adapter.MyPagerAdapter;
import com.dapl.halaladvisor.Adapter.NearByRestAdapter;
import com.dapl.halaladvisor.Adapter.NearByRestaurentAdapter;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.MainActivity;
import com.dapl.halaladvisor.Model.NearByResturent;
import com.dapl.halaladvisor.Model.RestaurantCusineObject;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.Model.RestaurantOpenHourObject;
import com.dapl.halaladvisor.Model.RestaurentMoreInfoObject;
import com.dapl.halaladvisor.Model.addreviewres.AddReviewRes;
import com.dapl.halaladvisor.Model.getgooglereviews.GetGoogleReviewsRes;
import com.dapl.halaladvisor.Model.getplaceidres.GetPlaceIdRes;
import com.dapl.halaladvisor.Model.offerredeemres.OfferRedeemRes;
import com.dapl.halaladvisor.Model.resdetailsres.Review;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.HeightWrappingViewPager;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.application_data.CurrentDateTime;
import com.dapl.halaladvisor.extra.AlertDialogManager;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.extra.AppController;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
//import com.jude.rollviewpager.RollPagerView;
//import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Struct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    Context context;
    double lat;
    double lng;
    GoogleMap googleMap;
    RestaurantDetailObject restDetailsModel = null;

    TextView tv_cuisine_type, tv_establishment_tag, tv_establishment_type, tv_avgCost_tag, tv_avgCost_type, tv_cashCardAccepted_type, tv_moreInfo_option6, tv_moreInfo_option5, tv_moreInfo_option4, tv_moreInfo_option3, tv_moreInfo_option2, tv_moreInfo_option1, tv_halal_rate, tv_moreInfo, tv_allReview, tv_allPhotos;

    TextView tvTitle, tvSubTitle;
    ImageView ivBack, ivShare, ivLike;
    LinearLayout clDirection;
    LinearLayout clCall;
    LinearLayout clWeb;
    LinearLayout clShare;
    LinearLayout clFav;
    LinearLayout clReview;
    CardView ordernow_;

    ArrayList<Review> reviewArrayList = new ArrayList<>();
    String address = "";
    private TextView tv_address, tv_website;
    private TextView tv_review_value, tv_review_rate;
    private TextView tv_mon_timing, tv_tues_timing, tvDistance, tv_wed_timing, tv_thur_timing, tv_fri_timing, tv_sat_timing, tv_sun_timing, tv_order_now, tvRestaurantName;
    private RelativeLayout mAddReviewButton, navigateAddress; // add "mAddReviewButton" by Moitri
    private ImageView mArrow_left, mArrow_right, mCall_details, mAddPhoto, ivTopBanner; /*mAddReviewButton*/
    private TextView tv_nowOpen, tv_openTime, tv_deliveryAvailable;
    private RatingBar mRatingBar, mRating_review;
    private String str_restaurantName = "";
    private String str_restaurant = "";
    private ImageView ivRestaurant;
    private String str_cityName = "";
    private String str_stateName = "";
    private String str_website_address = "", orderNowLink = "";
    private String str_contact_no = "";
    private MapView map_restaurant;
    private CardView cDistance;
    AdView adView;
    LinearLayout llnearBy;
    ConstraintLayout adViewSection;
    private RelativeLayout rr_book_table;
    MyPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;
    private HeightWrappingViewPager viewPagerRes;
    ProgressBar progressBar;
    NearByRestAdapter nearByRestaurentAdapter;
    RecyclerView rvNearBy;
    private LinearLayout ll_book_table;
    //================
    private String book_table_link = "";
    //private RollPagerView viewPager;
    private FirebaseAnalytics mFirebaseAnalytics;
    ApiService apiService;
    ArrayList<NearByResturent> nearbyRest = new ArrayList();
    double distance = 0.0;
    String rid = "";
    String typeCount = "";
    public static int tabposition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_details);
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        initMap(savedInstanceState);


        initUi();
//        if (getIntent().hasExtra("rid")) {
//
//            rid = getIntent().getStringExtra("rid");
//            new ReqRestaurantDetails().execute();
//
//        } else if (getIntent().getStringExtra("rid") != null) {
//            rid = getIntent().getStringExtra("restaurant_id");
//        } else {
//            Log.e("111111", "aaa-" + new Gson().toJson(AppController.getSelectedRestaurent()));
//            if (AppController.getSelectedRestaurent() == null) {
////                Log.e("111111", "bbb-" + new Gson().toJson(AppController.getSelectedRestaurent().getId()));
//
//                if (getIntent().getStringExtra("restaurant_id") != null) {
//                    rid = getIntent().getStringExtra("restaurant_id");
//                } else if (AppController.getSelectedRestaurent().getId() == null) {
//                    rid = PrefUtils.getResId(DetailsActivity.this);
//                } else {
//                    rid = PrefUtils.getResId(DetailsActivity.this);
//                }
//
//
//            } else {
//
//                rid = AppController.getSelectedRestaurent().getId();
//
//                //yha aana hai
//            }
//
//            new ReqRestaurantDetails().execute();
//
//        }
        if (getIntent().hasExtra("rid")) {
            rid = getIntent().getStringExtra("rid");
        } else if (getIntent().hasExtra("restaurant_id")) {
            rid = getIntent().getStringExtra("restaurant_id");
        } else if (AppController.getSelectedRestaurent() != null) {
            rid = AppController.getSelectedRestaurent().getId();
        } else {
            rid = PrefUtils.getResId(DetailsActivity.this);
        }
        new ReqRestaurantDetails().execute();
        typeCount = "1";

      //  new ReqCountRestaurentList().execute();

/*
        initAds();
        if (getIntent().hasExtra("rid")) {

            rid = getIntent().getStringExtra("rid");
            new ReqRestaurantDetails().execute();

        } else {
            rid = AppController.getSelectedRestaurent().getId();
            new ReqRestaurantDetails().execute();

        }


        trackActivity("restaurant_view");*/

    }

    private class ReqCountRestaurentList extends AsyncTask<Void, Void, String> {

        String response;


        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Please Wait...");

            progressDialog.setCancelable(false);

            // progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            HashMap<String, String> params = new HashMap<>();

            params.put("r_id", /*AppController.getSelectedRestaurent().getId()*/ rid);


            params.put("user_id", PrefUtils.getProfileObject(context).getU_id());
            params.put("type", typeCount);

            LogUtils.printf("I am rest count param: " + params);

            JSONParser parser = new JSONParser();


            String mURL = AppData.RESTAURANT_DETAILS_COUNT;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", params);


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Restaurant count response: " + response);


            //  setData(response);


        }


    }

    public void setNearByRestaurants() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvNearBy.setLayoutManager(linearLayoutManager);

        nearByRestaurentAdapter = new NearByRestAdapter(context, nearbyRest);
        rvNearBy.setAdapter(nearByRestaurentAdapter);
    }

    public void initAds() {
        adView = findViewById(R.id.ad_view);

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(context);

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
                // .setTestDeviceIds(listOf("8158E5D3B1EB35D6983BD8FAF1A58DDC"))
                .build());


        AdRequest adRequest = new AdRequest.Builder().build();
        // Create an ad request.


        // Start loading the ad in the background.
        adView.loadAd(adRequest);


        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                adViewSection.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.

                if (adError.getCode() == 3) {

                    adViewSection.setVisibility(View.VISIBLE);
                }
                Log.e("error ", "--" + adError);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    private void initMap(Bundle savedInstanceState) {


        map_restaurant = findViewById(R.id.map_restaurant);
        map_restaurant.onCreate(savedInstanceState);
        map_restaurant.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map_restaurant.onResume();


        //oncreate code

        initAds();

        if (tabposition == 1) {
            if (getIntent().hasExtra("rid")) {

                rid = getIntent().getStringExtra("rid");
                new ReqRestaurantDetails().execute();

            } else {
                rid = AppController.getSelectedRestaurent().getId();
                new ReqRestaurantDetails().execute();

            }

        }


        trackActivity("restaurant_view");
    }


    private void initUi() {

        ivTopBanner = findViewById(R.id.ivTopBanner);
        adViewSection = findViewById(R.id.adViewSection);
        llnearBy = findViewById(R.id.llnearBy);
        rvNearBy = findViewById(R.id.rvNearBy);
        tvDistance = findViewById(R.id.tvDistance);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        ivBack = findViewById(R.id.ivBack);
        ivRestaurant = findViewById(R.id.ivRestaurant);
        clDirection = findViewById(R.id.clDirection);
        clCall = findViewById(R.id.clCall);
        clWeb = findViewById(R.id.clWeb);
        clFav = findViewById(R.id.clFav);
        ivLike = findViewById(R.id.ivLike);
        ivRestaurant = findViewById(R.id.ivRestaurant);
        clShare = findViewById(R.id.clShare);
        clReview = findViewById(R.id.clReview);
        ordernow_ = findViewById(R.id.ordernow_);
        ivBack.setOnClickListener(this);
        ivShare = findViewById(R.id.ivShare);
        ivShare.setOnClickListener(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvTitle.setText("");
        tvRestaurantName.setText("");
        tvSubTitle = findViewById(R.id.tvSubTitle);
        tvSubTitle.setText("");
/*
        viewPager = findViewById(R.id.viewPager);
        try {
            viewPager.setHintView(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        mArrow_left = findViewById(R.id.iv_slide_arrow_left);
        mArrow_right = findViewById(R.id.iv_slide_arrow_right);
        mRatingBar = findViewById(R.id.ratingbar_details);
        mCall_details = findViewById(R.id.iv_call_details);
        tv_order_now = findViewById(R.id.tv_order_now);
        tv_nowOpen = findViewById(R.id.tv_nowOpen);
        tv_openTime = findViewById(R.id.tv_restaurant_openTime);
        tv_deliveryAvailable = findViewById(R.id.tv_deliveryAvailable);
        rr_book_table = findViewById(R.id.rr_book_table);
        tv_cuisine_type = findViewById(R.id.tv_cuisine_type);
        tv_establishment_tag = findViewById(R.id.tv_establishment_tag);
        tv_establishment_type = findViewById(R.id.tv_establishment_type);
        tv_avgCost_tag = findViewById(R.id.tv_avgCost_tag);
        tv_avgCost_type = findViewById(R.id.tv_avgCost_type);
        tv_cashCardAccepted_type = findViewById(R.id.tv_cashCardAccepted_type);
        tv_website = findViewById(R.id.tv_website);
        tv_moreInfo_option6 = findViewById(R.id.tv_moreInfo_option6);
        tv_moreInfo_option5 = findViewById(R.id.tv_moreInfo_option5);
        tv_moreInfo_option4 = findViewById(R.id.tv_moreInfo_option4);
        tv_moreInfo_option3 = findViewById(R.id.tv_moreInfo_option3);
        tv_moreInfo_option2 = findViewById(R.id.tv_moreInfo_option2);
        tv_moreInfo_option1 = findViewById(R.id.tv_moreInfo_option1);
        tv_halal_rate = findViewById(R.id.tv_halal_rate);
        tv_moreInfo = findViewById(R.id.tv_moreInfo);
        tv_allReview = findViewById(R.id.tv_readAllReview);
        tv_allPhotos = findViewById(R.id.tv_seeAllPhotos);
        tv_address = findViewById(R.id.tv_address);
        tv_review_value = findViewById(R.id.tv_review_value); // added by Moitri
        tv_review_rate = findViewById(R.id.tv_review_rate); // added by Moitri
        mRating_review = findViewById(R.id.review_user_ratingbar);
        tv_mon_timing = findViewById(R.id.tv_mon_timing);
        tv_tues_timing = findViewById(R.id.tv_tues_timing);
        tv_wed_timing = findViewById(R.id.tv_wed_timing);
        tv_thur_timing = findViewById(R.id.tv_thur_timing);
        tv_fri_timing = findViewById(R.id.tv_fri_timing);
        tv_sat_timing = findViewById(R.id.tv_sat_timing);
        tv_sun_timing = findViewById(R.id.tv_sun_timing);
        mAddPhoto = findViewById(R.id.iv_addPhoto_details);
        mAddReviewButton = findViewById(R.id.iv_details_addReviewButton); // add by Moitri
        navigateAddress = findViewById(R.id.navigateAddress);
        cDistance = findViewById(R.id.cDistance);
        ll_book_table = findViewById(R.id.ll_book_table);
        viewPagerRes = findViewById(R.id.vp);
        tabLayout = findViewById(R.id.tbLayout);
        // tabLayout.addTab(tabLayout.newTab().setText("Address"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.addTab(tabLayout.newTab().setText("Menu"));
        tabLayout.addTab(tabLayout.newTab().setText("Offers"));
        tabLayout.addTab(tabLayout.newTab().setText("Gallery"));
        tabLayout.addTab(tabLayout.newTab().setText("Hours"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        onClickView();

        setNearByRestaurants();

    }


    public void initViewPager(RestaurantDetailObject restDetailsModel) {
        myPagerAdapter = new MyPagerAdapter(DetailsActivity.this, getSupportFragmentManager(), address, restDetailsModel);
        viewPagerRes.setOffscreenPageLimit(1);
        viewPagerRes.setAdapter(myPagerAdapter);


        viewPagerRes.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabposition = tab.getPosition();

                viewPagerRes.setCurrentItem(tab.getPosition());
                myPagerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        clShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareIt();
            }
        });

        clCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trackActivity("call_button");

                LogUtils.printf("I am number: " + restDetailsModel.getContact());
            /*    orderNowLink = restDetailsModel.getOrder_now_link();
                if (!TextUtils.isEmpty(orderNowLink.trim()) && Patterns.WEB_URL.matcher(orderNowLink).matches()) {
                    *//*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(orderNowLink));
                    startActivity(Intent.createChooser(myIntent, "Open using..."));*//*
                    AllConstants.webUrl = orderNowLink;
                    Intent next = new Intent(DetailsActivity.this, DroidWebViewActivity.class);
                    next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(next);
                } else {*/
                /*Toast.makeText(DetailsActivity.this, "Website Not Available", Toast.LENGTH_LONG).show();*/
                Bundle params_call = new Bundle();
                params_call.putString("RestaurantName", restDetailsModel.getName());
                mFirebaseAnalytics.logEvent("Call_To_Restaurant", params_call);
                str_contact_no = restDetailsModel.getContact();
                if (isValidMobile(str_contact_no)) {

                    typeCount = "3";
                 //   new ReqCountRestaurentList().execute();
                    dialNumber(str_contact_no);
                } else {
                    Toast.makeText(DetailsActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
            // }
        });
        clDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackActivity("get_direction");
                AllConstants.lat = String.valueOf(lat);
                AllConstants.lng = String.valueOf(lng);
                //startActivityForResult(new Intent(context,LocationPickerActivity.class),    REQ_LOCATION_FETCH );
                if (AllConstants.UPlat != null && AllConstants.UPlng != null && AllConstants.lat != null && AllConstants.lng != null) {

                    typeCount = "2";
                //    new ReqCountRestaurentList().execute();
                    startActivity(new Intent(DetailsActivity.this, DirectionActivity.class));
                } else {
                    AlertDialogManager.showAlertDialog(DetailsActivity.this, "Alert", "An error occurred, please try again later.", false, false);
                }


            }
            // }
        });

        clWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser();
            }
        });

        clReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PrefUtils.getProfileObject(context) != null) {
                    startActivity(new Intent(DetailsActivity.this, AddReviewActivity.class));
                } else {
                    Toast.makeText(context, "Please LogIn to review this restaurant", Toast.LENGTH_SHORT).show();
                }
            }
        });


        clFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
                ivLike.setBackground(context.getResources().getDrawable(R.drawable.icons_background));

                new ReqAddToFavList(restDetailsModel.getId()).execute();
            }
        });


        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
                ivLike.setBackground(context.getResources().getDrawable(R.drawable.icons_background));

                new ReqAddToFavList(restDetailsModel.getId()).execute();
            }
        });
    }

    private void onClickView() {
        tv_deliveryAvailable.setOnClickListener(this);
        mArrow_left.setOnClickListener(this);
        mArrow_right.setOnClickListener(this);
        tv_allPhotos.setOnClickListener(this);
        tv_allReview.setOnClickListener(this);
        //tv_direction.setOnClickListener(this); // commented by Moitri; design changed, so need changes
        mAddReviewButton.setOnClickListener(this);
        mAddPhoto.setOnClickListener(this);
        tv_website.setOnClickListener(this);
        mCall_details.setOnClickListener(this);
        ordernow_.setOnClickListener(this);
        navigateAddress.setOnClickListener(this);
        tvDistance.setOnClickListener(this);
        cDistance.setOnClickListener(this);

        rr_book_table.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_website:
                openBrowser();
                break;


            case R.id.rr_book_table: {
                try {
                    if ((book_table_link != null)) {
                        AllConstants.webUrl = book_table_link;
                        startActivity(new Intent(DetailsActivity.this, DroidWebViewActivity.class));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;
            }

            //=================
            case R.id.tv_readAllReview:
                Intent intentViewReview = new Intent(DetailsActivity.this, ReviewNewActivity.class).putExtra("rid", rid);
                intentViewReview.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intentViewReview);
                break;

            case R.id.iv_slide_arrow_right:

                try {

                   /* if (restDetailsModel != null) {
                        JSONArray jsonArray = new JSONArray(restDetailsModel.getPhotos_jsn_arr());
                        if (viewPager.getViewPager().getCurrentItem() < jsonArray.length()) {
                            viewPager.getViewPager().setCurrentItem(viewPager.getViewPager().getCurrentItem() + 1);
                        }
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


            case R.id.iv_slide_arrow_left:

               /* if (viewPager.getViewPager().getCurrentItem() > 0) {
                    viewPager.getViewPager().setCurrentItem(viewPager.getViewPager().getCurrentItem() - 1);
                }*/

                break;

            case R.id.iv_details_addReviewButton:

                if (PrefUtils.getProfileObject(context) != null) {
                    startActivity(new Intent(DetailsActivity.this, AddReviewActivity.class));

                } else {
                    Toast.makeText(context, "Please LogIn to review this restaurant", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.iv_call_details:

            case R.id.ordernow_:
                LogUtils.printf("I am number: " + restDetailsModel.getContact());

                orderNowLink = restDetailsModel.getOrder_now_link();
//                orderNowLink="https://halaladvisor-demo.halaladvisor.online/";

                if (!TextUtils.isEmpty(orderNowLink.trim()) && Patterns.WEB_URL.matcher(orderNowLink).matches()) {
                    /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(orderNowLink));
                    startActivity(Intent.createChooser(myIntent, "Open using..."));*/
                    AllConstants.webUrl = orderNowLink;
                    Intent next = new Intent(DetailsActivity.this, DroidWebViewActivity.class);
                    next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(next);
                } else {
                    /*Toast.makeText(DetailsActivity.this, "Website Not Available", Toast.LENGTH_LONG).show();*/
                    Bundle params_call = new Bundle();
                    params_call.putString("RestaurantName", restDetailsModel.getName());
                    mFirebaseAnalytics.logEvent("Call_To_Restaurant", params_call);


                    str_contact_no = restDetailsModel.getContact();
                    if (isValidMobile(str_contact_no)) {
                        dialNumber(str_contact_no);
                        trackActivity("call_button");
                    } else {
                        Toast.makeText(DetailsActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                    }
                }
                ///////////////////
               /* Bundle params_call = new Bundle();
                params_call.putString("RestaurantName", str_restaurantName);
                mFirebaseAnalytics.logEvent("Call_To_Restaurant", params_call);

                if (isValidMobile(str_contact_no)) {
                    dialNumber(str_contact_no);
                } else {
                    Toast.makeText(DetailsActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.navigateAddress:
                trackActivity("get_direction");
                AllConstants.lat = String.valueOf(lat);
                AllConstants.lng = String.valueOf(lng);
                //startActivityForResult(new Intent(context,LocationPickerActivity.class),    REQ_LOCATION_FETCH );
                if (AllConstants.UPlat != null && AllConstants.UPlng != null && AllConstants.lat != null && AllConstants.lng != null) {

                    typeCount = "2";
                   // new ReqCountRestaurentList().execute();
                    startActivity(new Intent(this, DirectionActivity.class));
                } else {
                    AlertDialogManager.showAlertDialog(this, "Alert", "An error occurred, please try again later.", false, false);
                }

                break;
            case R.id.cDistance:
                trackActivity("get_direction");
                AllConstants.lat = String.valueOf(lat);
                AllConstants.lng = String.valueOf(lng);
                //startActivityForResult(new Intent(context,LocationPickerActivity.class),    REQ_LOCATION_FETCH );
                if (AllConstants.UPlat != null && AllConstants.UPlng != null && AllConstants.lat != null && AllConstants.lng != null) {
                    typeCount = "2";
                 //   new ReqCountRestaurentList().execute();
                    startActivity(new Intent(this, DirectionActivity.class));
                } else {
                    AlertDialogManager.showAlertDialog(this, "Alert", "An error occurred, please try again later.", false, false);
                }

                break;

            case R.id.tvDistance:
                trackActivity("get_direction");
                AllConstants.lat = String.valueOf(lat);
                AllConstants.lng = String.valueOf(lng);
                //startActivityForResult(new Intent(context,LocationPickerActivity.class),    REQ_LOCATION_FETCH );
                if (AllConstants.UPlat != null && AllConstants.UPlng != null && AllConstants.lat != null && AllConstants.lng != null) {
                    typeCount = "2";
                  //  new ReqCountRestaurentList().execute();

                    startActivity(new Intent(this, DirectionActivity.class));
                } else {
                    AlertDialogManager.showAlertDialog(this, "Alert", "An error occurred, please try again later.", false, false);
                }

                break;
            case R.id.ivShare:

                shareIt();
                break;
            case R.id.ivBack:

                backFunction();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backFunction();
    }


    public String getCurrentTimestamp() {
        String ts = "";
        try {
            Long tsLong = System.currentTimeMillis() / 1000;
            ts = tsLong.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ts;
    }


    public String getCurrentMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        Date date = new Date();

        return dateFormat.format(date);
    }

    private void trackActivity(String activityType) {
        apiService.trackActivity(activityType, PrefUtils.getProfileObject(DetailsActivity.this).getU_id(), getCurrentTimestamp(), getCurrentMonth(), rid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<AddReviewRes>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AddReviewRes res) {

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


    private class ReqAddToFavList extends AsyncTask<Void, Void, String> {

        String response;
        String restId;
        ProgressDialog progressDialog;


        public ReqAddToFavList(String restId) {
            this.restId = restId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Please Wait...");

            progressDialog.setCancelable(false);

            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            HashMap<String, String> map = new HashMap<>();

            JSONParser parser = new JSONParser();


            String mURL = "https://admin.halaladvisor.com.au/halal_api/res_fav.php?req=fav_res&key=1702NKDRDA&user_id=" + PrefUtils.getProfileObject(context).getU_id() + "&r_id=" + restId;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", map);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Restaurant List response: " + response);


            try {

                JSONObject jsonObject = new JSONObject(response);

                String message = jsonObject.getString("message");

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            } catch (Exception eX) {

                eX.printStackTrace();

            }


        }


    }

    private void shareIt() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, AllConstants.ShareName);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, restDetailsModel.getName() + "\n" + restDetailsModel.getCity_name() + ", " + restDetailsModel.getState_name() + "\n" + "Please download Halal Advisor App from the link given below:" + "\n" + "https://play.google.com/store/apps/details?id=com.dapl.halaladvisor");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    private void backFunction() {

        if (PrefUtils.isActivityOpenFromNotification(context)) {
            startActivity(new Intent(context, MainActivity.class));
            finish();
        } else {

            if (getIntent().hasExtra("new_type")) {
                startActivity(new Intent(context, MainActivity.class));
                finishAffinity();
            } else {
                finish();
            }
        }


    }

    private void dialNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);

    }

    private void parseRestaurantDetailsResponse(JSONObject result) {

        try {
            reviewArrayList.clear();
            nearbyRest.clear();

            System.out.println("DetailsActivity: response success- " + result.toString());

            restDetailsModel = (RestaurantDetailObject) PrefUtils.getObjectFromJson(result.toString().trim(), RestaurantDetailObject.class);

            restDetailsModel.setMore_info_jsn_arr(result.optJSONArray("more_info").toString());

            restDetailsModel.setReviews_jsn_arr(result.optJSONArray("reviews").toString());
            PrefUtils.saveReviewArrayFromDetailScreen(context, result.optJSONArray("reviews").toString());

            restDetailsModel.setOpen_hour_jsn_arr(result.optJSONArray("open_hours").toString());

            restDetailsModel.setCusine_jsn_arr(result.optJSONArray("cuisines_name").toString());
            restDetailsModel.setPhotos_jsn_arr(result.optJSONArray("photos").toString());

            restDetailsModel.setLatitude(result.optString("latitude"));
            restDetailsModel.setLongitude(result.optString("longitude"));
            restDetailsModel.setRes_fav(result.optInt("res_fav"));
            restDetailsModel.setStatus_resturent(result.optString("status_resturent"));
            //set r name
            str_restaurantName = restDetailsModel.getName().trim().toUpperCase();
            str_restaurant = restDetailsModel.getName().trim();
            tvTitle.setText(str_restaurantName);
            tvRestaurantName.setText(str_restaurant);
            Log.d("estaurantDetail", "parseRsResponse: "+restDetailsModel.getWebsite());
            if (!restDetailsModel.getOrder_now_link().equals("")) {
                ordernow_.setVisibility(View.VISIBLE);
            } else {
                ordernow_.setVisibility(View.GONE);
            }
            if (restDetailsModel.getRes_fav() == 1) {
                ivLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like));
                ivLike.setBackground(context.getResources().getDrawable(R.drawable.icons_background));

            } else {
                ivLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_d));
                ivLike.setBackground(context.getResources().getDrawable(R.drawable.icons_background));


            }
            try {


                if (restDetailsModel.getImage() != null && !restDetailsModel.getImage().toString().isEmpty()) {
                    Picasso.get().load(restDetailsModel.getImage()).error(R.drawable.list_placeholder).placeholder(R.drawable.list_placeholder).noFade().resize(800, 400).into(ivRestaurant);

                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }


            reviewArrayList.addAll(new Gson().fromJson(result.getJSONArray("reviews").toString(), new TypeToken<List<Review>>() {
            }.getType()));
            restDetailsModel.setReviewArrayList(reviewArrayList);


            ArrayList<String> imagesArray = new ArrayList<>();


            if (result.getJSONArray("photos") != null) {
                for (int i = 0; i < result.getJSONArray("photos").length(); i++) {
                    JSONObject object = result.getJSONArray("photos").getJSONObject(i);
                    imagesArray.add(object.getString("image"));
                }
            }
            Log.e("json img", "--" + imagesArray);
            restDetailsModel.setImagesArray(imagesArray);
            initViewPager(restDetailsModel);


            if (getIntent().hasExtra("from")) {
                int pos = Integer.parseInt(getIntent().getStringExtra("from"));
                TabLayout.Tab tab = tabLayout.getTabAt(pos);
                tab.select();
                //  View targetView = findViewById(R.id.tabLayout);
                //   targetView.getParent().requestChildFocus(targetView, targetView);
            } else {
                /*else {
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                tab.select();
            }*/
                viewPagerRes.setCurrentItem(tabposition);
                TabLayout.Tab tab = tabLayout.getTabAt(tabposition);
                tab.select();


            }

            //=====Photos==============
            try {

                JSONArray jsonArray = new JSONArray();


                try {
                    jsonArray = new JSONArray(restDetailsModel.getPhotos_jsn_arr());
                } catch (Exception e) {
                    e.printStackTrace();
                }

/*
                if (jsonArray.length() > 1) {
                    mArrow_left.setVisibility(View.VISIBLE);
                    mArrow_right.setVisibility(View.VISIBLE);
                } else {
                    mArrow_left.setVisibility(View.GONE);
                    mArrow_right.setVisibility(View.GONE);
                }*/

                ArrayList<String> photos = new ArrayList<>();

                try {

/*
                    Picasso.get()
                            .load(AppController.getSelectedRestaurent().getImage())
                            .placeholder(R.drawable.list_placeholder)
                            .error(R.drawable.list_placeholder)
                            .into(ivTopBanner);
*/

                    photos.add(AppController.getSelectedRestaurent().getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                nearbyRest.addAll(new Gson().fromJson(result.getJSONArray("near_by_resturent").toString(), new TypeToken<List<NearByResturent>>() {
                }.getType()));


                if (nearbyRest.size() == 0) {
                    llnearBy.setVisibility(View.GONE);
                } else {
                    llnearBy.setVisibility(View.VISIBLE);
                }
                nearByRestaurentAdapter.notifyDataSetChanged();

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        LogUtils.printf("I am photo: " + jsonArray.getJSONObject(i).getString("image"));
                        photos.add(jsonArray.getJSONObject(i).getString("image"));

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                }

              /*  if (photos.size() <= 1) {

                    viewPager.pause();

                }


                TestLoopAdapter loopAdapter = new TestLoopAdapter(viewPager, context, photos);

                viewPager.setAdapter(loopAdapter);


                if (photos.size() == 0) {
                    try {
                        viewPager.pause();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                AppController.setSelectedRestaurantDetailObject(restDetailsModel);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (map_restaurant != null) {
                    lat = Double.parseDouble(restDetailsModel.getLatitude());
                    lng = Double.parseDouble(restDetailsModel.getLongitude());
                    //googleMap = map_restaurant.getMap();
                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).anchor(0.0f, 1.0f).position(new LatLng(lat, lng)));
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMap.getUiSettings().setZoomControlsEnabled(false);
                    MapsInitializer.initialize(this);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(lat, lng));
                    LatLngBounds bounds = builder.build();
                    int padding = 0;
                    // Updates the location and zoom of the MapView
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    //googleMap.moveCamera(cameraUpdate);
                    googleMap.setMaxZoomPreference(14.0f);
                    googleMap.animateCamera(cameraUpdate);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                //ToolBar Title=========
                str_restaurantName = restDetailsModel.getName().trim().toUpperCase();
                str_restaurant = restDetailsModel.getName().trim();
                tvTitle.setText(str_restaurantName);
                tvRestaurantName.setText(str_restaurant);

                /*Setting Toolbar City and State Name*/
                str_cityName = restDetailsModel.getCity_name().trim().toUpperCase();
                str_stateName = restDetailsModel.getState_name().trim().toUpperCase();
                tvSubTitle.setText(String.format("%s, %s", str_cityName, str_stateName));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                /*
                 * Setting Restaurant Status
                 * if restaurant is currently closed or open
                 *
                 *
                 * */

                if (restDetailsModel.getStatus_resturent().trim().toLowerCase().equalsIgnoreCase("closed")) {
                    tv_openTime.setVisibility(View.GONE);
                    tv_nowOpen.setText("Currently Closed");
                    tv_nowOpen.setTextColor(getResources().getColor(R.color.text_color_red));
                } else {
                    tv_openTime.setVisibility(View.VISIBLE);
                    tv_nowOpen.setText("Now Open ");
                    tv_nowOpen.setTextColor(getResources().getColor(R.color.loginCancel_green));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                /*
                 * Setting restaurant Open Time
                 * */
                tv_openTime.setText(String.format("- %s", restDetailsModel.getTime().replace("-", "to ")));
                System.out.println("details getTime: " + restDetailsModel.getTime());


            } catch (Exception e) {
                e.printStackTrace();
            }


            /*
             * Setting avg rating
             * */
            try {
                String rating = restDetailsModel.getRest_avg_rating();
                Float count = Float.parseFloat(rating);
                // setting rating
                mRatingBar.setRating(count);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * Setting Delivery type
             * */
            try {
                if (restDetailsModel.getDelivary_type().equalsIgnoreCase("no")) {
                    tv_deliveryAvailable.setText("Delivery Not Available");
                } else {
                    tv_deliveryAvailable.setText("Delivery Available");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
             * Setting cuisine type
             * */
            try {

                JSONArray cusArray = new JSONArray(restDetailsModel.getCusine_jsn_arr());

                RestaurantCusineObject cusineObject = (RestaurantCusineObject) PrefUtils.getObjectFromJson(cusArray.getJSONObject(0).toString().trim(), RestaurantCusineObject.class);

                if (!cusineObject.getCuisines_name().trim().equalsIgnoreCase("")) {
                    tv_cuisine_type.setText(cusineObject.getCuisines_name());
                } else {
                    tv_cuisine_type.setText("NA");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            /*
             * Setting Establishment Type
             * */
            try {
                if (restDetailsModel.getEstb_type() != null) {
                    if (!restDetailsModel.getEstb_type().trim().equalsIgnoreCase("")) {
                        tv_establishment_type.setText(restDetailsModel.getEstb_type());
                    } else {
                        tv_establishment_type.setText("NA");
                    }
                } else {
                    tv_establishment_type.setText("NA");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            /*
             * Setting Cash and Card Availability
             * */
            try {
                if (!restDetailsModel.getCash_and_card().trim().equalsIgnoreCase("")) {
                    if (restDetailsModel.getCash_and_card().equalsIgnoreCase("Not Accepted")) {
                        tv_cashCardAccepted_type.setText("Only Cash accepted");
                    } else {
                        tv_cashCardAccepted_type.setText(getString(R.string.cash_amp_cards));
                    }
                } else {
                    tv_cashCardAccepted_type.setText("NA");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            /*
             * Setting Halal type
             * */
            String search = "Halal";
            String search_halalType = "100%";

            JSONArray moreInfoArray = new JSONArray(restDetailsModel.getMore_info_jsn_arr());

            int searchListLength = moreInfoArray.length();
            for (int i = 0; i < searchListLength; i++) {

                RestaurentMoreInfoObject moreInfoObject = (RestaurentMoreInfoObject) PrefUtils.getObjectFromJson(moreInfoArray.getJSONObject(i).toString().trim(), RestaurentMoreInfoObject.class);

                if (moreInfoObject.getMore_info().contains(search)) {
                    /*For 100% halal*/
                    if (moreInfoObject.getMore_info().contains(search_halalType)) {
                        tv_halal_rate.setText(moreInfoObject.getMore_info());
                        tv_halal_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.resturent_profile_logo, 0);
                    } else {
                        /*For partially halal*/
                        tv_halal_rate.setText(moreInfoObject.getMore_info());
                    }
                    System.out.println("List halal type- " + moreInfoObject.getMore_info());
                    // System.out.println("List halal type index- " + i + " Size- " + moreInfoObject.size());
                    //restDetailsModel.getMoreInfo_list().remove(i);
                    JSONArray tempArray = new JSONArray();

                    for (int j = 0; j < moreInfoArray.length(); j++) {
                        if (i != j) {
                            tempArray.put(moreInfoArray.get(j));
                        }
                    }
                    restDetailsModel.setMore_info_jsn_arr(tempArray.toString());

                    // System.out.println("List halal type size- " + restDetailsModel.getMoreInfo_list().size());
                    break;
                }

            }


            try {


                if (restDetailsModel.getImage() != null && !restDetailsModel.getImage().toString().isEmpty()) {
                    Picasso.get().load(restDetailsModel.getImage()).error(R.drawable.list_placeholder).placeholder(R.drawable.list_placeholder).noFade().resize(800, 400).into(ivRestaurant);

                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }




            /*
             * Setting More Info data
             * */
            try {
                int arraySize;

                JSONArray infoArray = new JSONArray(restDetailsModel.getMore_info_jsn_arr());

                if (infoArray.length() > 7) {
                    arraySize = 7;
                } else {
                    arraySize = infoArray.length();
                }

                for (int i = 0; i < arraySize; i++) {

                    RestaurentMoreInfoObject moreInfoObject = (RestaurentMoreInfoObject) PrefUtils.getObjectFromJson(infoArray.getJSONObject(i).toString().trim(), RestaurentMoreInfoObject.class);


                    switch (i) {

                        case 0:
                            tv_moreInfo_option1.setText(moreInfoObject.getMore_info());
                            break;

                        case 1:
                            tv_moreInfo_option2.setText(moreInfoObject.getMore_info());
                            break;

                        case 2:
                            tv_moreInfo_option3.setText(moreInfoObject.getMore_info());
                            break;

                        case 3:
                            tv_moreInfo_option4.setText(moreInfoObject.getMore_info());
                            break;

                        case 4:
                            tv_moreInfo_option5.setText(moreInfoObject.getMore_info());
                            break;

                        case 5:
                            tv_moreInfo_option6.setText(moreInfoObject.getMore_info());
                            break;
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }




            /*
             * Setting Address
             * */
            try {

                address = String.format("%s, %s, %s", restDetailsModel.getAddress(), restDetailsModel.getCity_name(), restDetailsModel.getState_name());
                tv_address.setText(String.format("%s, %s, %s", restDetailsModel.getAddress(), restDetailsModel.getCity_name(), restDetailsModel.getState_name()));
            } catch (Exception exe) {
                exe.printStackTrace();
            }



            /*Setting Website address
             * and storing it in a string
             * */
            try {
                str_website_address = restDetailsModel.getWebsite();
                //        orderNowLink = "https://www.google.co.in";
                orderNowLink = restDetailsModel.getOrder_now_link();
                if (!TextUtils.isEmpty(orderNowLink.trim()) && Patterns.WEB_URL.matcher(orderNowLink).matches())
                    mCall_details.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.order_button));
                else
                    mCall_details.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.call_button));

                System.out.println("DetailsActivity: str_website- " + str_website_address);

            } catch (Exception e) {
                e.printStackTrace();
            }


            /*Setting Contact info
             * and storing it in a string
             * */
            try {
                str_contact_no = restDetailsModel.getContact();
                System.out.println("DetailsActivity: str_website- " + str_contact_no);

            } catch (Exception e) {
                e.printStackTrace();
            }


            /*
             * Setting Average Cost per meal Type
             * */
            try {
                if (restDetailsModel.getAvaragecost_range() != null) {

                    if (!restDetailsModel.getAvaragecost_range().trim().equalsIgnoreCase("") && !restDetailsModel.getAvaragecost_range().trim().equalsIgnoreCase("-")) {

                        tv_avgCost_type.setText(restDetailsModel.getAvaragecost_range());

                    } else {

                        tv_avgCost_type.setText("NA");
                    }

                } else {
                    tv_avgCost_type.setText("NA");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

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



            /*
             * Setting Opening Hours
             *
             *


             *

             */


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

            /*Book Table*/

            try {
                if (restDetailsModel.getR_url().trim().length() > 0) {
                    if (restDetailsModel.getR_url() != null) {
                        book_table_link = restDetailsModel.getR_url();
                        ll_book_table.setVisibility(View.VISIBLE);
                    } else {
                        book_table_link = "";
                        ll_book_table.setVisibility(View.GONE);
                    }
                } else {
                    book_table_link = "";
                    ll_book_table.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //////==============

//user lat lng
            String tempLat = "0.0";
            String tempLng = "0.0";
            if (PrefUtils.getUserLatLong(context) != null) {
                tempLat = String.valueOf(PrefUtils.getUserLatLong(context).getLattitude());
                tempLng = String.valueOf(PrefUtils.getUserLatLong(context).getLongitude());

            }


            Log.e("lat ", "rest--latlng---" + AllConstants.UPlat + "," + AllConstants.UPlng);
            Log.e("current ", "--latlng---" + AllConstants.lat + "," + AllConstants.lng);
            try {

                if (tempLat != null && tempLng != null && restDetailsModel.getLatitude() != null && restDetailsModel.getLongitude() != null) {

                    distance = distance(Double.parseDouble(tempLat), Double.parseDouble(tempLng), Double.parseDouble(restDetailsModel.getLatitude()), Double.parseDouble(restDetailsModel.getLongitude()));
                    //  tvDistance.setText(String.format("%.2f", distance) + " km\nNavigate");
                    tvDistance.setText(String.format("%.2f", Double.parseDouble(restDetailsModel.getTotal_distance())) + " km\nNavigate");
                }

            } catch (Exception e) {

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onMapLoaded() {

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMapLoadedCallback(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        map_restaurant.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map_restaurant.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_restaurant.onLowMemory();
    }

    private boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    private void openBrowser() {
        if (Patterns.WEB_URL.matcher(str_website_address).matches()) {
            AllConstants.webUrl = str_website_address;


            startActivity(new Intent(DetailsActivity.this, DroidWebViewActivity.class));

            /*/Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_website_address));
            startActivity(Intent.createChooser(myIntent, "Open using..."));*/
        } else {
            Toast.makeText(DetailsActivity.this, "Website Not Available", Toast.LENGTH_LONG).show();
        }
    }

    private class ReqRestaurantDetails extends AsyncTask<Void, Void, String> {

        String response;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Please Wait...");

            progressDialog.setCancelable(false);

            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            HashMap<String, String> params = new HashMap<>();

            params.put("r_id", /*AppController.getSelectedRestaurent().getId()*/ rid);

            params.put("lattitude", AllConstants.UPlat); // ""
            params.put("longitude", AllConstants.UPlng);  //
            params.put("presentdate", CurrentDateTime.getCurrntDate());
            params.put("time", CurrentDateTime.getCurrentTime());
            params.put("u_id", PrefUtils.getProfileObject(context).getU_id());

            LogUtils.printf("I am rest detail param: " + params);

            JSONParser parser = new JSONParser();


            String mURL = AppData.RESTAURANT_DETAILS_URL;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Details response: " + response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status") == 1) {

                    parseRestaurantDetailsResponse(jsonObject.optJSONObject("result"));

                }


                //   Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

   /* private class TestLoopAdapter extends LoopPagerAdapter {

        Context context;
        ArrayList<String> arrayList;


        public TestLoopAdapter(RollPagerView viewPager, Context context, ArrayList<String> arrayList) {
            super(viewPager);
            this.arrayList = arrayList;
            this.context = context;
        }


        @Override
        public View getView(ViewGroup container, final int position) {

            View mParentView = getLayoutInflater().inflate(R.layout.inflater_viewpager_detail, null);
            ImageView mImageview = mParentView.findViewById(R.id.imageview);


            try {

                Picasso.get().load(arrayList.get(position)).error(R.drawable.list_placeholder).placeholder(R.drawable.list_placeholder).noFade().resize(800, 400).into(mImageview);


            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                mImageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mParentView;
        }


        @Override
        public int getRealCount() {
            return arrayList.size();
        }
    }*/


}
