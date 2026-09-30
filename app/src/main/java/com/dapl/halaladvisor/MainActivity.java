package com.dapl.halaladvisor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dapl.halaladvisor.Activity.AboutWebScreen;
import com.dapl.halaladvisor.Activity.AutoCompleteAddressSearchActivity;
import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.LogInActivity;
import com.dapl.halaladvisor.Activity.ProfileActivity;
import com.dapl.halaladvisor.Activity.SendFeedbackActivity;
import com.dapl.halaladvisor.Activity.VoucharActivity;
import com.dapl.halaladvisor.Adapter.NavigationListAdapter;
import com.dapl.halaladvisor.Fragment.FavouriteFragment;
import com.dapl.halaladvisor.Fragment.HomeFragment;
import com.dapl.halaladvisor.Fragment.ProductScannerFragment;
import com.dapl.halaladvisor.Fragment.PromosFragment;
import com.dapl.halaladvisor.Model.AddressObject;
import com.dapl.halaladvisor.Model.LastSearchLocationObject;
import com.dapl.halaladvisor.Model.addreviewres.AddReviewRes;
import com.dapl.halaladvisor.Model.featuredimages.FeaturedImagesRes;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.AlertDialogManager;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.interfaces.OnClickOkDelegate;
import com.facebook.BuildConfig;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener,
        DrawerLayout.DrawerListener
        , View.OnClickListener, AdapterView.OnItemClickListener
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {

    public String[] nav_nameId_logout;
    public String[] nav_nameId_login;
    Context context;
    TabLayout tabLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    View navHeader;
    int AUTOCOMPLETE_REQUEST_CODE = 1256;


    ListView nav_listView;

    ImageView navUserImage;

    TextView tv_navUsername;


    int DRAWER_ACTION = -1;
    int DRAWER_ACTION_HOME = 0;
    int DRAWER_ACTION_SCAN_GROCERIES = 1;
    int DRAWER_ACTION_MY_FAVOURITES = 2;
    int DRAWER_ACTION_PROFILE = 3;
    int DRAWER_ACTION_OFFER = 4;
    int DRAWER_ACTION_SEND_FEEDBACK = 5;
    int DRAWER_ACTION_RATE_US = 6;
    int DRAWER_ACTION_ADVERTISE_ON_HALAL_APP = 7;
    int DRAWER_ACTION_ABOUT_HALAL_APP = 8;
    int DRAWER_ACTION_LOG_OUT = 9;
    //int DRAWER_ACTION_PICK_UP = 1;
    // int DRAWER_ACTION_DELIVERY = 2;

    //  int DRAWER_ACTION_SHARE = 4;


    ApiService apiService;
    String selectedCateroryType = "2";

    GoogleApiClient mGoogleApiClient;

    FirebaseAnalytics mFirebaseAnalytics;
    Intent intent;
    String lat = "", lng = "", notification_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        Intent intent1 = getIntent();

        View rootView = findViewById(android.R.id.content);


        ViewCompat.setOnApplyWindowInsetsListener(rootView, (view, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            view.setPadding(
                    systemBars.left,
                   0,
                    systemBars.right,
                    0
            );

            return insets;
        });
        notification_type = intent1.getStringExtra("notification_type");
        Log.d("notification_type", "onCreate: " + intent1.getStringExtra("notification_type"));

        try {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initUi();
        PrefUtils.saveActivityOpenFromNotification(context, false);

        // Warm scan-usage & subscription caches on home so the scanner renders without flicker
        com.dapl.halaladvisor.productscan.ScanUsageRepository.INSTANCE.prefetch(context);
        com.dapl.halaladvisor.productscan.SubscriptionRepository.INSTANCE.prefetch(context);

        if (getIntent().getStringExtra("lat") != null) {
            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");

        }
        if (getIntent().getStringExtra("restaurant_id") != null) {
            startActivity(new Intent(context, DetailsActivity.class).putExtra("restaurant_id", getIntent().getStringExtra("restaurant_id")));
        }

        if (notification_type != null) {
            if (notification_type.equals("scanner")) {
                tabLayout.getTabAt(1).select();
            } else if (notification_type.equals("menu")) {
                tabLayout.getTabAt(3).select();
            } else {
                tabLayout.getTabAt(2).select();
            }
        } else {
            setView(HomeFragment.newInstance(true, selectedCateroryType), KEYS.HOME);
        }


        try {

            HomeFragment.newInstance2(lat, lng);
            Log.d("mainact", "HomeFragment" + lat + " " + lng);
        } catch (JSONException e) {

            e.printStackTrace();


            Log.d("sssddddd", "onCreate: " + e);
        }

        LogUtils.printf("I am fcm token: " + PrefUtils.getFirebaseToken(context));


        try {


            Bundle params_restaurantList = new Bundle();
            params_restaurantList.putString("UserEmail", PrefUtils.getProfileObject(context).getUseremail());
            params_restaurantList.putString("UserType", "Registered");
            mFirebaseAnalytics.logEvent("Restaurant_Listing", params_restaurantList);


        } catch (Exception e) {
            e.printStackTrace();
        }
        initFcm();
        setupNavigation();
    }

    private void initFcm() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                // device_token = task.getResult();
                String token = task.getResult();
                LogUtils.printf("I am Token success getInstanceId failed" + token);
                PrefUtils.saveFirebaseToken(context, token);

            }
        });
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
        Log.d("Month", dateFormat.format(date));
        return dateFormat.format(date);
    }

    private void trackActivity(String activityType) {
        apiService.trackActivity(activityType,
                        PrefUtils.getProfileObject(MainActivity.this).getU_id(), getCurrentTimestamp(), getCurrentMonth(), "").
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<AddReviewRes>() {
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


    private void getFeaturedImages() {
        apiService.getFeaturedImages().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<FeaturedImagesRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FeaturedImagesRes res) {

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
    protected void onResume() {
        super.onResume();
        // setupNavigation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            switch (resultCode) {
                case Activity.RESULT_OK: {

                    if (data != null) {

                        AddressObject addressObject = (AddressObject) Utils.getObjectFromJson(data.getStringExtra("result"), AddressObject.class);


                        try {
                            LastSearchLocationObject locationObject = new LastSearchLocationObject();
                            locationObject.setAddress(addressObject.getFullAddress());
                            locationObject.setName(addressObject.getUrban_area());
                            locationObject.setCode(addressObject.getState_code());
                            locationObject.setLattitude(Double.parseDouble(addressObject.getLatitude()));
                            locationObject.setLongitude(Double.parseDouble(addressObject.getLongitude()));
                            PrefUtils.saveLastSearchedLocation(context, locationObject);
                            if (tabLayout.getTabAt(0) != null) {
                                Objects.requireNonNull(tabLayout.getTabAt(0)).select();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                    break;
                }
           /* case AutocompleteActivity
                    .RESULT_ERROR: {

                Toast.makeText(context, Autocomplete.getStatusFromIntent(data).getStatusMessage(), Toast.LENGTH_SHORT).show();

                break;

            }*/
                case Activity.RESULT_CANCELED: {


                    //
                    break;
                }


            }


            //=======
        }

    }


    private void initUi() {

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(this);

        navigationView = findViewById(R.id.nvView);
        nav_listView = findViewById(R.id.nav_listview);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        navHeader = navigationView.getHeaderView(0);
    }

    // Opens the product scanner via its bottom-nav tab (position 1) so the tab highlight updates too.
    public void openScanner() {
        if (tabLayout != null && tabLayout.getTabAt(1) != null) {
            tabLayout.getTabAt(1).select();
        }
    }

    // Opens the right-side navigation drawer (used by the Home hamburger icon).
    public void openNavigationDrawer() {
        if (drawerLayout != null && !drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void setView(Fragment fragment, int keyFragment) {

        FragmentManager transaction = getSupportFragmentManager();

        transaction.beginTransaction()
                //.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment, "" + keyFragment).commit();
       /* transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.add(R.id.fragment_container, fragment, keyFragment);*/


    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0: {

                setView(HomeFragment.newInstance(false, selectedCateroryType), KEYS.HOME);
                try {
                    HomeFragment.newInstance2(lat, lng);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case 1: {

                // openAutoCompletePlaceActivityResult();
//                setView(new InstaExplorePage(), KEYS.FAVOURITE);
                setView(new ProductScannerFragment(), KEYS.FAVOURITE);

                break;
            }

            case 2: {


                setView(PromosFragment.newInstance(), KEYS.FAVOURITE);


                break;
            }
            case 3: {


                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }


                break;
            }


        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {


        if (tab.getPosition() == 3) {

            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }


        }


    }

    private void openAutoCompletePlaceActivityResult() {
/*
asdasdasd
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
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/

        Intent intent = new Intent(context, AutoCompleteAddressSearchActivity.class);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }

    private void setupNavigation() {


        tv_navUsername = findViewById(R.id.tv_navigation_username);

        navUserImage = findViewById(R.id.iv_navigation_userImage);

        try {

            tv_navUsername.setText(PrefUtils.getProfileObject(context).getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (PrefUtils.getProfileObject(context).getProfile_image_url() != null) {

                Picasso.get()
                        .load(PrefUtils.getProfileObject(context).getProfile_image_url())
                        .noFade()

                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(navUserImage);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }





        /*Passing List into adpater
         * Setting adapter in listview*/
        nav_nameId_logout = getResources().getStringArray(R.array.left_menu_logout);
        nav_nameId_login = getResources().getStringArray(R.array.left_menu_login);
        nav_listView.setAdapter(new NavigationListAdapter(this, nav_nameId_logout));

        nav_listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));
        nav_listView.setOnItemClickListener(this);
        //nav_listView.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        try {

            drawerLayout.closeDrawer(Gravity.RIGHT);

            DRAWER_ACTION = position;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

        DRAWER_ACTION = -1;

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

        try {

            if (DRAWER_ACTION == DRAWER_ACTION_LOG_OUT) {

                AlertDialogManager alertDialogManager = new AlertDialogManager();
                alertDialogManager.setLogoutDelegate(new OnClickOkDelegate() {
                    @Override
                    public void onClickOkDelegate(Boolean isLogoutOk) {
                        if (isLogoutOk) {

                            new ReqLogOut().execute();
                            //logOutFunction();
                        }
                    }
                });
                AlertDialogManager.showOkCancelAlertDialog(context, "Logout", "Do you really want to logout?");


            } else if (DRAWER_ACTION == DRAWER_ACTION_SCAN_GROCERIES) {
                openScanner();
            } else if (DRAWER_ACTION == DRAWER_ACTION_OFFER) {
                startActivity(new Intent(context, VoucharActivity.class));
            } else if (DRAWER_ACTION == DRAWER_ACTION_RATE_US) {
                openPlayStore();
            } else if (DRAWER_ACTION == DRAWER_ACTION_SEND_FEEDBACK) {
                startActivity(new Intent(context, SendFeedbackActivity.class));
            } else if (DRAWER_ACTION == DRAWER_ACTION_HOME) {
                setView(HomeFragment.newInstance(false, selectedCateroryType), KEYS.HOME);
                try {
                    HomeFragment.newInstance2(lat, lng);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (DRAWER_ACTION == DRAWER_ACTION_MY_FAVOURITES) {
                setView(FavouriteFragment.newInstance(), KEYS.FAVOURITE);
            } else if (DRAWER_ACTION == DRAWER_ACTION_ADVERTISE_ON_HALAL_APP) {
                startActivity(new Intent(context, AboutWebScreen.class).
                        putExtra("about", "https://halaladvisor.com.au/restaurant-discovery/")
                        .putExtra("type", "ADVERTISE WITH HALAL ADVISOR")
                );
            } else if (DRAWER_ACTION == DRAWER_ACTION_ABOUT_HALAL_APP) {
                startActivity(new Intent(context, AboutWebScreen.class).
                        putExtra("about", "https://halaladvisor.com.au/about-halal-advisor")
                        .putExtra("type", "About Halal Advisor")
                );
            } else if (DRAWER_ACTION == DRAWER_ACTION_PROFILE) {
                startActivity(new Intent(context, ProfileActivity.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onDrawerStateChanged(int newState) {

    }


    private void shareIt() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, AllConstants.ShareName);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download the Halal Advisor App -" + " https://play.google.com/store/apps/details?id=com.dapl.halaladvisor");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }


    public void openBrowser(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    public void openPlayStore() {

        final String appPackageName = BuildConfig.APPLICATION_ID;// from Context or Activity object
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            startActivity(intent);

        } catch (android.content.ActivityNotFoundException anfe) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpGoogleApiClientInstance() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        mGoogleApiClient.connect();
    }

    private void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Log.e("TANAY", "signed out from google");
                    }
                });
    }

    private void signOutFromGoogle() {

        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }


       /* Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.disconnect();*/
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e("TANAY", "revoked access ot from google");
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(People.LoadPeopleResult loadPeopleResult) {

                    Log.e("TANAY", "onResult");
                    // signOutFromGoogle();
                    //  signOutGoogle();
                    revokeAccess();

                }
            });
            signOutFromGoogle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class ReqLogOut extends AsyncTask<Void, Void, String> {

        String response;

        public void deleteCache(Context context) {
            try {
                File dir = context.getCacheDir();
                deleteDir(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
                return dir.delete();
            } else if (dir != null && dir.isFile()) {
                return dir.delete();
            } else {
                return false;
            }
        }

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

            params.put("u_id", PrefUtils.getProfileObject(context).getU_id());
            params.put("device_type", "A");
            if (PrefUtils.getFirebaseToken(context) != null && !PrefUtils.getFirebaseToken(context).isEmpty()) {
                params.put("device_token", PrefUtils.getFirebaseToken(context));
            } else {
                params.put("device_token", "test");
            }
            LogUtils.printf("I am Token: " + PrefUtils.getFirebaseToken(getApplicationContext()));

            JSONParser parser = new JSONParser();


            String mURL = AppData.LOGOUT_URL;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am LogOut response: " + response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status") == 1) {


                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();

                    if (FacebookSdk.isInitialized()) {
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    } else {
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                    setUpGoogleApiClientInstance();

                    PrefUtils.removeAllSavedData(context);
                    deleteCache(context);
                    startActivity(new Intent(context, LogInActivity.class));
                    finish();


                } else {


                    AlertDialogManager.showAlertDialog(context, "Alert", jsonObject.optString("message"), false, false);

                }


                //   Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}
