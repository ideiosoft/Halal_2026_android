package com.dapl.halaladvisor.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dapl.halaladvisor.Activity.AutoCompleteAddressSearchActivity;
import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.LocationPickerActivity;
import com.dapl.halaladvisor.Adapter.CatsAdapter;
import com.dapl.halaladvisor.Adapter.HomeRestaurentAdapter;
import com.dapl.halaladvisor.Adapter.ViewPagerAdapter;
import com.dapl.halaladvisor.MainActivity;
import com.dapl.halaladvisor.Model.AddressModelResponse;
import com.dapl.halaladvisor.Model.AddressObject;
import com.dapl.halaladvisor.Model.LastSearchLocationObject;
import com.dapl.halaladvisor.Model.RestaurantBannerListModel;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.Model.cuisine.CuisineModelNew;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.InstanceCreate;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.MarkerInfoWindowAdapter;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.application_data.CurrentDateTime;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.extra.AppController;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;
import com.dapl.halaladvisor.interfaces.MapViewChangeListner;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
//import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements View.OnClickListener, CuisineClickInterface, MapViewChangeListner {

    private static final String TAG_REQUEST_RESTAURANT_SEARCH = "request_restaurant_search";
    HomeRestaurentAdapter restaurentAdapter;
    private MarkerOptions options = new MarkerOptions();
    Handler handler;
    ArrayList<Object> restaurentList = new ArrayList<>();
    public static ArrayList<Marker> markerArrayList = new ArrayList<>();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    ArrayList<CuisineModelNew.Datum> cuisnineList = new ArrayList<>();
    public ArrayList restaurantListModels = new ArrayList<RestaurantListModel>();
    public ArrayList restaurantBannerListModel = new ArrayList<RestaurantBannerListModel>();
    ApiService apiService;
    View toolbar;
    TextView tvAddress;
    TextView tvLoadMore;
    TextView tvSatellite;
    int totalPage = 1;
    TextView tvStreetView;
    ImageView ivlist, crossBtn;
    ImageView ivMap;
    NestedScrollView nested;
    boolean isFirstTimeLoad;
    String catType = "2,3,6";
    String cuisineId = "";
    ProgressBar progressBar;
    ProgressBar progressBarBottom;
    private int AUTOCOMPLETE_REQUEST_CODE = 1;
    private View view;
    private Context context;
    private RecyclerView rvHome;
    private EditText searchVenue;
    private RecyclerView rvCuisine;
    private static int LOCATION_REQ = 1001;
    public static GoogleMap googleMap;
    MapView mapView;
    Boolean isFirstTime = true;
    private StreetViewPanorama mStreetViewPanorama;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    //  int pastVisibleItems, visibleItemCount, totalItemCount;
    int count = 0;
    boolean isLoading = false;
    LinearLayoutManager linearLayoutManager;
    private int pageCount = 1;
    private boolean isFirstTimeOpenActivity = true;
    private Bitmap bmImg;
    //DotsIndicator dot1;
    ViewPager mViewPager;
    RelativeLayout bannerLay;
    ImageView lt1, cloase, showsoopercent, hidesooprecent, halaloption, hideoption, showboth, hideboth;
    LinearLayout popupfilter, sooclick, halaloptionclik, bothclick, apply;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    // images array
    int[] images = {R.drawable.app_logo, R.drawable.app_default_image, R.drawable.app_logo, R.drawable.app_default_image};
    private static Context mContext;
    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;

    JSONObject jsonObject;
    static String lati = "", lang = "", filtervalue = "Both";
    private boolean isScrolling = false;
    public static HomeFragment newInstance(boolean isFirstTimeLoad, String categoryType) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("first_time_load", isFirstTimeLoad);
        bundle.putString("cat_type", categoryType);

        HomeFragment myFragment = new HomeFragment();

        myFragment.setArguments(bundle);

        return myFragment;
    }

    public static void newInstance2(String lat, String lng) throws JSONException {


        lati = lat;
        lang = lng;
        Log.d("newInstance2", "newInstance2: " + lati);

    }


    public void reviewPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.review_dialog, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        TextView noThanksTxt = dialogView.findViewById(R.id.noThanksTxt);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName(); // from Context
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                PrefUtils.saveReviewStatus(getContext(), "1");
                alertDialog.dismiss();
            }
        });


        noThanksTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.saveReviewStatus(getContext(), "1");
                alertDialog.dismiss();
            }
        });

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.show();
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            isFirstTimeLoad = bundle.getBoolean("first_time_load");
            //  catType = bundle.getString("cat_type");

        }
    }

    public void setCatAdapter(View view, ArrayList<CuisineModelNew.Datum> list) {

        rvCuisine = (RecyclerView) view.findViewById(R.id.rvCuisine);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvCuisine.setLayoutManager(layoutManager);
        CatsAdapter catsAdapter = new CatsAdapter(context, list, this);
        rvCuisine.setAdapter(catsAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        readBundle(getArguments());
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initUi(view);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        showProgressBar();
        timer = new Timer();
        handler = new Handler();
        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerMain);
        bannerLay = (RelativeLayout) view.findViewById(R.id.bannerLay);
        //dot1 = view.findViewById(R.id.dot1);
        lt1 = view.findViewById(R.id.lt1);
        popupfilter = view.findViewById(R.id.popupfilter);
        cloase = view.findViewById(R.id.cloase);
        showsoopercent = view.findViewById(R.id.showsoopercent);
        hidesooprecent = view.findViewById(R.id.hidesooprecent);
        halaloption = view.findViewById(R.id.halaloption);
        hideoption = view.findViewById(R.id.hideoption);
        showboth = view.findViewById(R.id.showboth);
        hideboth = view.findViewById(R.id.hideboth);
        sooclick = view.findViewById(R.id.sooclick);
        halaloptionclik = view.findViewById(R.id.halaloptionclik);
        bothclick = view.findViewById(R.id.bothclick);
        apply = view.findViewById(R.id.apply);



     /*   if (isFirstTimeLoad) {
            startActivityForResult(new Intent(context, LocationPickerActivity.class), LOCATION_REQ);
        } else {
            //=====Load Data Using Last Searched location====================
            setDataFromLastSearchedLocation(PrefUtils.getLastSearchedLocation(context));
        }*/


        //get cuisine


        initMap(savedInstanceState);

        return view;
    }


    public void initMap(Bundle savedInstanceState) {
/*
        mapFragment = (SupportMapFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);*/

        mapView = (MapView) view.findViewById(R.id.mapFragment);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.setVisibility(View.GONE);
        // mapView.getMapAsync(this);


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                Log.d("onMapReady", "esseseonMapReady: " + lati + " " + isFirstTimeLoad);
                if (!lati.equals("")) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("lat", lati);

                        jsonObject.put("lng", lang);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    setAddressFromLocation(jsonObject.toString());
                } else {

                    if (isFirstTimeLoad) {
                        startActivityForResult(new Intent(context, LocationPickerActivity.class), LOCATION_REQ);
                    } else {
                        //=====Load Data Using Last Searched location====================
                        setDataFromLastSearchedLocation(PrefUtils.getLastSearchedLocation(context));
                    }
                }


            }
        });
        lt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lt1.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option));
                rvHome.setVisibility(View.VISIBLE);
                if (mapView != null) {
                    mapView.setVisibility(View.GONE);

                }

                popupfilter.setVisibility(View.VISIBLE);
                if (InstanceCreate.getfilterselect(getContext()).equals("12")) {
                    showsoopercent.setVisibility(View.VISIBLE);
                    hidesooprecent.setVisibility(View.GONE);
                    halaloption.setVisibility(View.GONE);
                    hideoption.setVisibility(View.VISIBLE);
                    showboth.setVisibility(View.GONE);
                    hideboth.setVisibility(View.VISIBLE);

                } else if (InstanceCreate.getfilterselect(getContext()).equals("10")) {
                    showsoopercent.setVisibility(View.GONE);
                    hidesooprecent.setVisibility(View.VISIBLE);
                    halaloption.setVisibility(View.VISIBLE);
                    hideoption.setVisibility(View.GONE);
                    showboth.setVisibility(View.GONE);
                    hideboth.setVisibility(View.VISIBLE);

                } else {
                    showsoopercent.setVisibility(View.GONE);
                    hidesooprecent.setVisibility(View.VISIBLE);
                    halaloption.setVisibility(View.GONE);
                    hideoption.setVisibility(View.VISIBLE);
                    showboth.setVisibility(View.VISIBLE);
                    hideboth.setVisibility(View.GONE);

                }

            }
        });

        sooclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showsoopercent.setVisibility(View.VISIBLE);
                hidesooprecent.setVisibility(View.GONE);
                halaloption.setVisibility(View.GONE);
                hideoption.setVisibility(View.VISIBLE);
                showboth.setVisibility(View.GONE);
                hideboth.setVisibility(View.VISIBLE);


            }
        });

        halaloptionclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showsoopercent.setVisibility(View.GONE);
                hidesooprecent.setVisibility(View.VISIBLE);
                halaloption.setVisibility(View.VISIBLE);
                hideoption.setVisibility(View.GONE);
                showboth.setVisibility(View.GONE);
                hideboth.setVisibility(View.VISIBLE);

            }
        });

        bothclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showsoopercent.setVisibility(View.GONE);
                hidesooprecent.setVisibility(View.VISIBLE);
                halaloption.setVisibility(View.GONE);
                hideoption.setVisibility(View.VISIBLE);
                showboth.setVisibility(View.VISIBLE);
                hideboth.setVisibility(View.GONE);


            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showsoopercent.getVisibility() == View.VISIBLE) {
                    InstanceCreate.setfilterselect(getContext(), "12");
                }
                if (halaloption.getVisibility() == View.VISIBLE) {
                    InstanceCreate.setfilterselect(getContext(), "10");

                }
                if (showboth.getVisibility() == View.VISIBLE) {
                    InstanceCreate.setfilterselect(getContext(), "Both");

                }
                pageCount = 1;
                popupfilter.setVisibility(View.GONE);
                restaurentList.clear();

                if(!AllConstants.UPlat.equals("0.0")){
                    reqRestaurantListVolley(AllConstants.UPlat, AllConstants.UPlng);
                    Log.d("check this ", "onClick: 1");
                }
                Log.d("onClickonClick", "onClick: " + InstanceCreate.getfilterselect(getContext()));
                Log.d("onClickonClick", "onClick: " + AllConstants.UPlat);
                Log.d("onClickonClick", "onClick: " + PrefUtils.getFirebaseToken(context));
                lt1.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option_white));
                ivlist.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option));
                ivMap.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option_white));
                rvHome.setVisibility(View.VISIBLE);
                popupfilter.setVisibility(View.GONE);
                if (mapView != null) {
                    mapView.setVisibility(View.GONE);
                }

            }
        });


        cloase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupfilter.setVisibility(View.GONE);





                lt1.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option_white));
                rvHome.setVisibility(View.VISIBLE);
                popupfilter.setVisibility(View.GONE);
                if (mapView != null) {
                    mapView.setVisibility(View.GONE);

                }


            }
        });

        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        nested.requestDisallowInterceptTouchEvent(true);
                        nested.arrowScroll(0);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        nested.requestDisallowInterceptTouchEvent(false);
                        nested.arrowScroll(0);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        nested.requestDisallowInterceptTouchEvent(true);
                        nested.arrowScroll(0);
                        return false;

                    default:
                        return true;
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        initUi(view);
        LogUtils.printf("I am state: onResume");
        getCuisineList(view);
     /*   if (!isFirstTimeOpenActivity) {
            restaurentList.clear();
            reqRestaurantListVolley("", "");
        }*/

        isFirstTimeOpenActivity = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.printf("I am state: onStop");
        AppController.getInstance().getRequestQueue().cancelAll(TAG_REQUEST_RESTAURANT_SEARCH);
    }

    private void setDataFromLastSearchedLocation(LastSearchLocationObject lastSearchedLocation) {

        if (lastSearchedLocation != null) {

            //  Toast.makeText(context, "last location", Toast.LENGTH_SHORT).show();
            tvAddress.setText(lastSearchedLocation.getAddress());

            AllConstants.UPlat = String.valueOf(lastSearchedLocation.getLattitude());
            AllConstants.UPlng = String.valueOf(lastSearchedLocation.getLongitude());
            restaurentList.clear();
            timer.cancel();
            if(!AllConstants.UPlat.equals("0.0")){
                reqRestaurantListVolley(String.valueOf(lastSearchedLocation.getLattitude()), String.valueOf(lastSearchedLocation.getLongitude()));
                Log.d("check this ", "onClick: 2");
            }

            //new ReqRestaurentList(String.valueOf(lastSearchedLocation.getLattitude()), String.valueOf(lastSearchedLocation.getLongitude())).execute();

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        isFirstTimeOpenActivity = true;

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            switch (resultCode) {
                case Activity.RESULT_OK: {
                    Log.d("sdfsfsdfsdf", "RESULT_OK: ");
                    if (data != null) {
                        pageCount = 1;

                        //   Place place = Autocomplete.getPlaceFromIntent(data);

                        AddressObject addressObject = (AddressObject) Utils.getObjectFromJson(data.getStringExtra("result"), AddressObject.class);


                        try {
                            LastSearchLocationObject locationObject = new LastSearchLocationObject();
                            locationObject.setAddress(addressObject.getFullAddress());
                           /* Geocoder gcd = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = gcd.getFromLocation(Double.parseDouble(addressObject.getLatitude())
                                    , Double.parseDouble(addressObject.getLongitude()), 1);
                            if (addresses.size() > 0) {
                                System.out.println(addresses.get(0).getLocality());
                                locationObject.setName(addresses.get(0).getLocality());
                            }
                            else {
                                // do your stuff
                            }
*/

                            locationObject.setName(addressObject.getUrban_area());
                            locationObject.setCode(addressObject.getState_code());
                            locationObject.setLattitude(Double.parseDouble(addressObject.getLatitude()));
                            locationObject.setLongitude(Double.parseDouble(addressObject.getLongitude()));
                            PrefUtils.saveLastSearchedLocation(context, locationObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        tvAddress.setText(addressObject.getFullAddress());

                        if (addressObject.getLatitude() != null && addressObject.getLongitude() != null)
//
                            AllConstants.UPlat = String.valueOf(addressObject.getLatitude());
                        AllConstants.UPlng = String.valueOf(addressObject.getLongitude());

                        restaurentList.clear();
                        timer.cancel();
                        if(!AllConstants.UPlat.equals("0.0")){
                            reqRestaurantListVolley(addressObject.getLatitude(), addressObject.getLongitude());
                            Log.d("check this ", "onClick: 3");
                        }


                        // new ReqRestaurentList(addressObject.getLatitude(), addressObject.getLongitude()).execute();

                      /*  try {
                            LastSearchLocationObject locationObject = new LastSearchLocationObject();
                            locationObject.setAddress(place.getAddress());
                            locationObject.setLattitude(place.getLatLng().latitude);
                            locationObject.setLongitude(place.getLatLng().longitude);
                            PrefUtils.saveLastSearchedLocation(context, locationObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        tvAddress.setText(place.getAddress());

                        if (place.getLatLng() != null)
                            new ReqRestaurentList(String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude)).execute();*/
                    }


                    break;
                }
                case AutocompleteActivity.RESULT_ERROR: {

                    Toast.makeText(context, Autocomplete.getStatusFromIntent(data).getStatusMessage(), Toast.LENGTH_SHORT).show();

                    break;

                }
                case Activity.RESULT_CANCELED: {

                    Log.d("sdfsfsdfsdf", "RESULT_CANCELED: ");
                    //
                    break;
                }


            }


            //==========================
        } else if (requestCode == LOCATION_REQ) {

            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                String latLngJsonObj = data.getStringExtra("response");

//yha coding kru ga
                Log.d("latLngJsonObj", "onActivityResult: " + latLngJsonObj);
                Log.d("latLngJsonObj", "onActivityResult: " + lati);
                if (lati.equals("")) {
                    setAddressFromLocation(latLngJsonObj);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("lat", lati);

                        jsonObject.put("lng", lang);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    setAddressFromLocation(jsonObject.toString());
                }


                //


                // Toast.makeText(mContext, latLngJsonObj, Toast.LENGTH_SHORT).show();

                LogUtils.printf("I am in Line:" + latLngJsonObj);
            }


        }

    }

    private void initUi(View view) {

        if (!Places.isInitialized()) {
            Places.initialize(context, getString(R.string.api_key));
        }


        progressBar = view.findViewById(R.id.progressBar);
        progressBarBottom = view.findViewById(R.id.progressBarBottom);

        hideProgressBar();
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        searchVenue = view.findViewById(R.id.searchVenue);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvSatellite = view.findViewById(R.id.tvSatellite);
        tvStreetView = view.findViewById(R.id.tvStreetView);
        nested = view.findViewById(R.id.nested);
        ivlist = view.findViewById(R.id.ivList);
        crossBtn = view.findViewById(R.id.crossBtn);
        ivMap = view.findViewById(R.id.ivMap);
        rvHome = view.findViewById(R.id.rvHome);

        tvLoadMore = view.findViewById(R.id.tvLoadMore);

        linearLayoutManager = new LinearLayoutManager(context);
        rvHome.setLayoutManager(linearLayoutManager);

        restaurentAdapter = new HomeRestaurentAdapter(context, restaurentList, "home");
        rvHome.setAdapter(restaurentAdapter);
        rvHome.setNestedScrollingEnabled(false);

        searchVenue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    pageCount = 1;
                    restaurentList.clear();
                    if(!AllConstants.UPlat.equals("0.0")){
                        reqRestaurantListVolley(AllConstants.UPlat, AllConstants.UPlng);
                        Log.d("check this ", "onClick: 4");
                    }


                    return true;
                }
                return false;
            }
        });
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchVenue.setText("");
            }
        });
        searchVenue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchVenue.getText().toString().equalsIgnoreCase("")) {
                    pageCount = 1;
                    restaurentList.clear();
                    crossBtn.setVisibility(View.GONE);
                    if(!AllConstants.UPlat.equals("0.0")){
                        reqRestaurantListVolley(AllConstants.UPlat, AllConstants.UPlng);
                        Log.d("check this ", "onClick: 5");
                    }

                } else {
                    crossBtn.setVisibility(View.VISIBLE);
                }
            }
        });


        //clicks
        ivlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (restaurentList.size() > 0) {
                    tvLoadMore.setVisibility(View.GONE);
                }else {
                    tvLoadMore.setVisibility(View.VISIBLE);
                }
*/


                ivlist.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option));
                ivMap.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option_white));
                if(!AllConstants.UPlat.equals("0.0")){
                    reqRestaurantListVolley(AllConstants.UPlat, AllConstants.UPlng);
                    Log.d("check this ", "onClick: 6");
                }

                rvHome.setVisibility(View.VISIBLE);
                popupfilter.setVisibility(View.GONE);
                if (mapView != null) {
                    mapView.setVisibility(View.GONE);

                }
//                restaurentList.clear();


            }
        });


       /* tvSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                tvSatellite.setVisibility(View.GONE);
                tvStreetView.setVisibility(View.VISIBLE);
            }
        });


        tvStreetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


                tvStreetView.setVisibility(View.GONE);
                tvSatellite.setVisibility(View.VISIBLE);
            }
        });*/


        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  tvLoadMore.setVisibility(View.GONE);


                //   tvStreetView.setVisibility(View.VISIBLE);
                // tvSatellite.setVisibility(View.GONE);


                ivlist.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option_white));
                ivMap.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_option));


                rvHome.setVisibility(View.GONE);
                popupfilter.setVisibility(View.GONE);
                if (mapView != null) {


                    mapView.setVisibility(View.VISIBLE);
                }


            }
        });


        nested.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nested.getChildAt(nested.getChildCount() - 1);

                int diff = (view.getBottom() - (nested.getHeight() + nested.getScrollY()));
                Log.e("diff", diff + "");
                Log.e("pageCount", pageCount + "");
                Log.e("pseCountRaafaf", totalPage + "");
                isScrolling = true;
                // Check if the user has reached the end of the list and more data can be loaded
                if (diff <= 0 && !isLoading && pageCount <=totalPage) {
                    isLoading = true; // Prevent multiple simultaneous loading

                    progressBarBottom.setVisibility(View.VISIBLE);

                    if (pageCount == 2 && PrefUtils.getReviewStatus(context).equals("")) {
                        PrefUtils.saveReviewStatus(getContext(), "1");
                        reviewPopup();
                    }

                    // Load the next page of data
                    if(!AllConstants.UPlat.equals("0.0")){
                        reqRestaurantListVolley(AllConstants.UPlat, AllConstants.UPlng);
                        Log.d("check this ", "onClick: 7");
                    }

                }
            }
        });
    }


    private void hideProgressBar() {

        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {

        progressBar.setVisibility(View.VISIBLE);
    }

    private void setData(String response) {

        try {


            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
/*
            if (status.equals("0")) {
               restaurentList.clear();
               restaurantListModels.clear();
                googleMap.clear();
                latlngs.clear();
                restaurentAdapter.notifyDataSetChanged();


            }*/

            Log.e("status", "---" + status);
            if (jsonObject.optInt("status") == 1) {
                progressBarBottom.setVisibility(View.GONE);
                JSONArray data = jsonObject.optJSONArray("homePageBanners");

                if (data.length() > 0) {

                    bannerLay.setVisibility(View.VISIBLE);
                    restaurantBannerListModel = new Gson().fromJson(data.toString(), new TypeToken<List<RestaurantBannerListModel>>() {
                    }.getType());


                    // Initializing the ViewPagerAdapter
                    mViewPagerAdapter = new ViewPagerAdapter(getContext(), restaurantBannerListModel);

                    // Adding the Adapter to the ViewPager
                    mViewPager.setAdapter(mViewPagerAdapter);
                    //dot1.setViewPager(mViewPager);


                    /*After setting the adapter use the timer */
                    // final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {

                            if (currentPage == restaurantBannerListModel.size()) {
                                currentPage = 0;
                            }
                            mViewPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    handler.removeCallbacks(Update);
                    timer = new Timer(); // This will create a new Thread
                    timer.schedule(new TimerTask() { // task to be scheduled
                        @Override
                        public void run() {

                            handler.post(Update);

                        }
                    }, DELAY_MS, PERIOD_MS);


                } else {
                    bannerLay.setVisibility(View.GONE);
                }

                JSONArray array = jsonObject.optJSONArray("result");
                totalPage = jsonObject.optInt("total_pages");
                Log.d("arrayarray", "setData: "+array.length());
                Log.d("arrayarray", "setData: "+jsonObject.optInt("total_pages"));
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsn = array.getJSONObject(i);

                    RestaurantListModel model = (RestaurantListModel) PrefUtils.getObjectFromJson(jsn.toString(), RestaurantListModel.class);

                    restaurantListModels = new Gson().fromJson(array.toString(), new TypeToken<List<RestaurantListModel>>() {
                    }.getType());


                    Log.e("rest list ", "--" + restaurantListModels);
                    JSONArray cuisineArray = jsn.getJSONArray("cuisines_name");

                    model.setCusineJsnArray(jsn.getJSONArray("cuisines_name").toString().trim());

                    String cuisine_name = "";
                    String latitude = "";
                    String longitude = "";


                    latitude = jsn.getString("latitude");
                    longitude = jsn.getString("longitude");


                    if (!latitude.isEmpty() && !longitude.isEmpty()) {


                        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                        //  latlngs.add(latLng);


                    }


                    Log.e("latlngs--", "--" + latlngs);


                    if (cuisineArray.length() >= 0) {

                        for (int j = 0; j < cuisineArray.length(); j++) {

                            if (!TextUtils.isEmpty(cuisineArray.getJSONObject(j).optString("cuisines_name")))

                                cuisine_name = cuisine_name + " " + getString(R.string.bullet_str) + " " + cuisineArray.getJSONObject(j).optString("cuisines_name");
                        }
                    } else {
                        cuisine_name = "NA";
                    }

                    model.setCuisine_type(cuisine_name);

                    LogUtils.printf("I am model before:" + model.toString());
                    restaurentList.add(model);
                    Log.d("restaurentListSize", "setData: "+restaurentList.size());

                }


                //====Sorting Based on Open/Closed======

/*
                Collections.sort(restaurentList, new Comparator<Object>() {
                    @Override
                    public int compare(Object object1, Object object2) {

                        RestaurantListModel obj1 = (RestaurantListModel) object1;
                        RestaurantListModel obj2 = (RestaurantListModel) object2;

                        if (obj1.getStatus().trim().toLowerCase().equalsIgnoreCase("open")) {
                            return -1;
                        }
                        return 0;
                    }
                });
*/

                hideKeyboard(getActivity());
                //===================================
                restaurentAdapter.notifyDataSetChanged();
                loading = false;


                setDataOnMap();
            }
            else {

                JSONArray data = jsonObject.optJSONArray("homePageBanners");

                if (data.length() > 0) {

                    bannerLay.setVisibility(View.VISIBLE);
                    restaurantBannerListModel = new Gson().fromJson(data.toString(), new TypeToken<List<RestaurantBannerListModel>>() {
                    }.getType());


                    // Initializing the ViewPagerAdapter
                    mViewPagerAdapter = new ViewPagerAdapter(getContext(), restaurantBannerListModel);

                    // Adding the Adapter to the ViewPager
                    mViewPager.setAdapter(mViewPagerAdapter);
                    //dot1.setViewPager(mViewPager);


                    /*After setting the adapter use the timer */

                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == restaurantBannerListModel.size()) {
                                currentPage = 0;
                            }
                            mViewPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    handler.removeCallbacks(Update);
                    timer = new Timer(); // This will create a new Thread
                    timer.schedule(new TimerTask() { // task to be scheduled
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, DELAY_MS, PERIOD_MS);

                } else {
                    bannerLay.setVisibility(View.GONE);

                }

                progressBarBottom.setVisibility(View.GONE);
                restaurentList.clear();
                restaurentAdapter.notifyDataSetChanged();
                Toast.makeText(context, "No venues found in your area,", Toast.LENGTH_SHORT).show();


            }

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setAddressFromLocation(String latLng) {

        try {
            JSONObject jsonObject = new JSONObject(latLng);


            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            //dONT rEOMVE
            AllConstants.UPlat = String.valueOf(jsonObject.optDouble("lat"));
            AllConstants.UPlng = String.valueOf(jsonObject.optDouble("lng"));

            getPlaceReviews(jsonObject.optDouble("lat"), jsonObject.optDouble("lng"));


            //  addresses = geocoder.getFromLocation(jsonObject.optDouble("lat"), jsonObject.optDouble("lng"), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            AllConstants.UPlat = String.valueOf(jsonObject.optDouble("lat"));
            AllConstants.UPlng = String.valueOf(jsonObject.optDouble("lng"));

            //restaurentList.clear();
            if(!AllConstants.UPlat.equals("0.0")){
                Log.d("check this ", "onClick: 8");
                // reqRestaurantListVolley(String.valueOf(jsonObject.optDouble("lat")), String.valueOf(jsonObject.optDouble("lng")));

            }

            //new ReqRestaurentList(String.valueOf(jsonObject.optDouble("lat")), String.valueOf(jsonObject.optDouble("lng"))).execute();

          /*  if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
              *//*  String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();*//*

                tvAddress.setText(address);


                try {
                    LastSearchLocationObject locationObject = new LastSearchLocationObject();
                    locationObject.setAddress(address);
             *//*       Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addressess = gcd.getFromLocation(jsonObject.optDouble("lat")
                            , jsonObject.optDouble("lng"), 1);
                    if (addressess.size() > 0) {
                        System.out.println(addressess.get(0).getLocality());
                        locationObject.setName(addressess.get(0).getLocality());
                    }
                    else {
                        // do your stuff
                    }*//*
                   locationObject.setName("Sydney");
                    locationObject.setCode(address);
                    locationObject.setLattitude(jsonObject.optDouble("lat"));
                    locationObject.setLongitude(jsonObject.optDouble("lng"));
                    PrefUtils.saveLastSearchedLocation(context, locationObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, "Fail To Fetch Your Address", Toast.LENGTH_SHORT).show();
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getPlaceReviews(Double lat, Double lng) {

        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        apiService.getPlaceAddress("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + getString(R.string.api_key)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<AddressModelResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AddressModelResponse res) {
                if (res.getStatus().equals("OK")) {
                    Log.e("resssss", new Gson().toJson(res.getResults()));
                    Log.e("address", "" + res.getResults().get(0).getFormatted_address());

                    tvAddress.setText(res.getResults().get(0).getFormatted_address());


                    LastSearchLocationObject locationObject = new LastSearchLocationObject();
                    locationObject.setAddress(res.getResults().get(0).getFormatted_address());
             /*       Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addressess = gcd.getFromLocation(jsonObject.optDouble("lat")
                            , jsonObject.optDouble("lng"), 1);
                    if (addressess.size() > 0) {
                        System.out.println(addressess.get(0).getLocality());
                        locationObject.setName(addressess.get(0).getLocality());
                    }
                    else {
                        // do your stuff
                    }*/
                    locationObject.setName(getCityName(lat,
                            lng));
                    locationObject.setCode(res.getResults().get(0).getFormatted_address());
                    locationObject.setLattitude(lat);
                    locationObject.setLongitude(lng);
                    PrefUtils.saveLastSearchedLocation(context, locationObject);
                    Log.d("resres", "onNext: "+res);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.toolbar: {

                openAutoCompletePlaceActivityResult();
                break;
            }


        }
    }

    /*
        private void openAutoCompletePlaceActivityResult() {


    // Set the fields to specify which types of place data to
    // return after the user has made a selection.
            List<Place.Field>
                    fields = Arrays.asList(
                    Place.Field.ID
                    , Place.Field.NAME
                    , Place.Field.ADDRESS
                    , Place.Field.LAT_LNG);

    // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setCountry("AU")
                    .build(context);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

        }
    */
    private void openAutoCompletePlaceActivityResult() {
        Intent intent = new Intent(context, AutoCompleteAddressSearchActivity.class);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }


    private void reqRestaurantListVolley(final String lat, final String lng) {
        showProgressBar();
        //hideLayoutMessage();

        AppController.getInstance().getRequestQueue().cancelAll(TAG_REQUEST_RESTAURANT_SEARCH);

        String baseUrl = AppData.RESTAURANT_LIST_URL;

        LogUtils.printf("I am Restaurant List baseUrl: " + baseUrl);
        /*if (restaurentList.size() > 0) {
            hideProgressBar();
        } else {
            showProgressBar();
        }*/


        //showProgressBar();

        StringRequest strReq = new StringRequest(Request.Method.POST, baseUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideProgressBar();

//                if (isScrolling) {
//                    // Increment pageCount and reset the flag only if scrolling occurred
//                    pageCount++;
//                    isScrolling = false;
//                }
                LogUtils.printf("pageCountpageCount " + pageCount);
                try {
                    LogUtils.printf("I am Restaurant List response: " + response);

                    // restaurentList.clear();
                    //   latlngs.clear();

                    pageCount++;

                    progressBarBottom.setVisibility(View.GONE);
                    isLoading = false; // Reset loading state
                    setData(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.printf("I am error: " + error.getMessage());
                hideProgressBar();
                //hideProgressBar();

                try {

                    /*showLayoutMessage();
                    ivMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_net_conn_fail));
                    String msg1 = "Server not respond. <u><font color='#202c5d'>Click to Retry</font></u>";
                    tvMessage.setText(Html.fromHtml(msg1));*/
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cat_id", catType);
                params.put("cuisine_id", cuisineId);
                //params.put("lattitude", "-33.7246958");
                //params.put("longitude", "150.8408682");
                params.put("search", searchVenue.getText().toString());
                params.put("lattitude", AllConstants.UPlat);
                params.put("longitude", AllConstants.UPlng);

//                params.put("lattitude", "-33.73088937");
//                params.put("longitude", "150.86388023");

//                params.put("lattitude", "-33.73088908");
//                params.put("longitude", "150.86388798");
                params.put("presentdate", CurrentDateTime.getCurrntDate());
                params.put("time", CurrentDateTime.getCurrentTime());
                params.put("page_no", String.valueOf(pageCount));
                params.put("distance", "15");
                params.put("u_id", "");
                params.put("halal_type", InstanceCreate.getfilterselect(getContext()));


                LogUtils.printf("I am Restaurant List param: " + params.toString());

                return params;
            }

        };

        Utils.setRetryPolicy(strReq);
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQUEST_RESTAURANT_SEARCH);
        //  Toast.makeText(getActivity(), AllConstants.UPlat+"=="+AllConstants.UPlng, Toast.LENGTH_SHORT).show();
    }

    public void getCuisineList(final View view) {
        String getCuisineListUrl = AppData.GET_CUISINE_LIST;

        final List<String> jsonResponses = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getCuisineListUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Data");
                    Log.e("json arrray--", "----" + jsonArray);

                    hideProgressBar();
                    cuisnineList.clear();

                    cuisnineList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CuisineModelNew.Datum>>() {
                    }.getType());
                    setCatAdapter(view, cuisnineList);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onClick(String cuisineId, String name) {

       /* restaurentList.clear();
        pageCount = 1;
        googleMap.clear();*/
        restaurentList.clear();
        pageCount = 1;
        if (name.equals("All")) {
            this.cuisineId = "";
        } else {
            this.cuisineId = cuisineId;
        }
        timer.cancel();
        if(!AllConstants.UPlat.equals("0.0")){
            Log.d("check this ", "onClick: 9");
            reqRestaurantListVolley(AllConstants.UPlat, AllConstants.UPlng);
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDataOnMap() {
        this.googleMap.clear();
        Log.e("rest list  on data", "--" + restaurantListModels);
        Log.e("restaurentList  on data", "--" + restaurentList);
        Log.e("restaurentList  on data", "--" + restaurentList.size());
        if (restaurentList.size() > 0) {

          /*  for (LatLng point : latlngs)
            {*/
            for (int i = 0; i < restaurentList.size(); i++) {


                final RestaurantListModel model = (RestaurantListModel) restaurentList.get(i);

                options.position(new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude())));


                // Log.e("bitmapp",getBitmapFromURL(model.getImage())+"");


             /*   final Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));
*/


                new GetImageFromUrl(model, this).execute(model.getImage());


            }

            //  }

        }

    }
  /*  public void setDataOnMap() {
        this.googleMap.clear();
        Log.e("rest list  on data", "--" + restaurantListModels);
        Log.e("restaurentList  on data", "--" + restaurentList);
        if (restaurentList.size() > 0) {

          *//*  for (LatLng point : latlngs)
            {*//*
            for (int i = 0; i < restaurentList.size(); i++) {


                final RestaurantListModel model = (RestaurantListModel) restaurentList.get(i);

                options.position(new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude())));
                LatLngBounds.Builder b = new LatLngBounds.Builder();


               // Log.e("bitmapp",getBitmapFromURL(model.getImage())+"");


               *//* final Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));
*//*
                final Marker marker = googleMap.addMarker(new MarkerOptions()
                         .position(new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude())))
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromURL(model.getImage())))
                        // Specifies the anchor to be at a particular point in the marker image.
                        .anchor(0.5f, 1));
                marker.setTag(model);


                markerArrayList.add(marker);


                for (Marker m : markerArrayList) {
                    b.include(m.getPosition());

                }
                LatLngBounds bounds = b.build();
//Change the padding as per needed
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 230, 230, 20);
                googleMap.animateCamera(cu);


                MarkerInfoWindowAdapter markerInfoWindowAdapter =
                        new MarkerInfoWindowAdapter(context, (RestaurantListModel) restaurentList.get(i), this);
                googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);


                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        final RestaurantListModel user = (RestaurantListModel) marker.getTag();


                        Log.e("List- ", "Restaurant_id: " + user.getId());

                        AppController.setSelectedRestaurent(user);
                        PrefUtils.saveActivityOpenFromNotification(context, false);
                        context.startActivity(new Intent(context, DetailsActivity.class));

                    }
                });


            }

            //  }

        }

    }*/

    @Override
    public void onClick() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    /*

        private class ReqRestaurentList extends AsyncTask<Void, Void, String> {

            String response;

            String lat, lng;

            ProgressDialog progressDialog;

            public ReqRestaurentList(String lat, String lng) {
                this.lat = lat;
                this.lng = lng;
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
                map.put("cat_id", catType);
                map.put("lattitude", lat);
                map.put("longitude", lng);
                map.put("presentdate", CurrentDateTime.getCurrntDate());
                map.put("time", CurrentDateTime.getCurrentTime());
                map.put("page_no", String.valueOf(pageCount));
                map.put("distance", "18");

                JSONParser parser = new JSONParser();


                String mURL = AppData.RESTAURANT_LIST_URL;

                LogUtils.printf("I am url: " + mURL);


                LogUtils.printf("I am param: " + map);


                response = parser.makeHttpRequest(mURL, "POST", map);

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                progressDialog.dismiss();

                LogUtils.printf("I am Restaurant List response: " + response);


                //TODO Remove it.
                Toast.makeText(context, "Date: " + CurrentDateTime.getCurrntDate()
                        + "\nTime: " + CurrentDateTime.getCurrentTime(), Toast.LENGTH_SHORT).show();
                setData(response);


            }


        }

    */
    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        RestaurantListModel model;
        private Bitmap bitmap;
        MapViewChangeListner listner;

        public GetImageFromUrl(RestaurantListModel img, MapViewChangeListner listner) {
            this.model = img;
            this.listner = listner;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                // bitmap=Bitmap.createScaledBitmap(bitmap,50,50,false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            LatLngBounds.Builder b = new LatLngBounds.Builder();

       /* final Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude())))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));*/

            final Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.new_pinn)));


            marker.setTag(model);


            markerArrayList.add(marker);


            for (Marker m : markerArrayList) {
                b.include(m.getPosition());

            }
            LatLngBounds bounds = b.build();
//Change the padding as per needed
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 230, 230, 20);
            googleMap.animateCamera(cu);

            model.setBitmap(bitmap);
            MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(context, model, listner);
            googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    final RestaurantListModel user = (RestaurantListModel) marker.getTag();
                    Log.e("List- ", "Restaurant_id: " + user.getId());
                    AppController.setSelectedRestaurent(user);
                    PrefUtils.saveActivityOpenFromNotification(context, false);
                    context.startActivity(new Intent(context, DetailsActivity.class));
                }
            });
            //  imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        initUi(view);

    }
    private String getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
