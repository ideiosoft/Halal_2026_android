package com.dapl.halaladvisor;


import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.RegistrationActivity;
import com.dapl.halaladvisor.Activity.SwipeHomeActivity;
import com.dapl.halaladvisor.Activity.WelcomeActivity;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.Model.UserLatLong;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.AppController;
import com.dapl.halaladvisor.extra.SessionManagement;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 21;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final String TAG_REQUEST_VERSION_CHECK = "req_version_check";

    Context context;


    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;


    SessionManagement sessionManagement;
    LottieAnimationView animation_view;
    private Uri uri;
    Intent intent;
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        context = this;
        animation_view = findViewById(R.id.animation_view);

        intent = getIntent();
        String action = intent.getAction();
        Log.d("actionaction", "onCreate: " + action);
        WebView home = (WebView) this.findViewById(R.id.splashweb);
        String url = "file:///android_asset/splash.html";
        home.setVisibility(View.VISIBLE);
        home.getSettings().setJavaScriptEnabled(true);
        home.loadUrl(url);


        animation_view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("Animation:", "start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animation:", "end");
                //Your code for remove the fragment
                home.destroy();
                try {



                  /*  if(NotificationManagerCompat.from(SplashActivity.this).areNotificationsEnabled())
                    {*/
                        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

                        initFcm();


                        initSession();

                        initLocation();
                        startLocationFetching();
                        //Do your Work
                   /* }
                    else
                    {
//            Snackbar.make(findViewById(R.id.drawer_layout), "Notification blocked", Snackbar.LENGTH_LONG).setAction("Settings") {
//                // Responds to click on the action
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                val uri: Uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }.show()

                        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("Turn Notifications On")
                                .setCancelable(false)
                                .setMessage("Turn push notifications on for Halal Advisor to receive new venue alerts and special deals.")
                                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Code to handle when the "Settings" button is clicked.
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Code to handle when the "Cancel" button is clicked.
                                        dialog.dismiss();
                                    }
                                })
                                .create();

                        alertDialog.show();
                        //Ask for permission
                    }*/



                } catch (Exception ex) {
                    ex.toString();
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:", "cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:", "repeat");
            }
        });


        try {

            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                LogUtils.printf("I am keyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("Key:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }
        //  openNextScreen();
        // Toast.makeText(SplashActivity.this,""+ com.dapl.halaladvisor.BuildConfig.VERSION_CODE,Toast.LENGTH_SHORT).show();
    }


    public void initSession() {
        sessionManagement = new SessionManagement(SplashActivity.this);

    }

    private void initFcm() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                // device_token = task.getResult();
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    LogUtils.printf("I am Token success getInstanceId failed" + token);
                    PrefUtils.saveFirebaseToken(context, token);
                }
                // openNextScreen();
            }
        });
 /*       FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            LogUtils.printf("I am Token Error getInstanceId failed" + task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        LogUtils.printf("I am Token success getInstanceId failed" + token);
                        PrefUtils.saveFirebaseToken(context, token);
                        // Log and toast

                    }
                });*/
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();


                UserLatLong userLatLong = new UserLatLong();
                userLatLong.setLattitude(mCurrentLocation.getLatitude());
                userLatLong.setLongitude(mCurrentLocation.getLongitude());


                String str = PrefUtils.getJsonFromObject(userLatLong, UserLatLong.class).trim();
                PrefUtils.saveUserLatLong(context, str);

            }
        };


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    public void startLocationFetching() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        // LogUtils.printf( "All location settings are satisfied.");

                        // Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                // device_token = task.getResult();
                                if (task.isSuccessful()) {
                                    String token = task.getResult();
                                    LogUtils.printf("I am Token success getInstanceId failed" + token);
                                    PrefUtils.saveFirebaseToken(context, token);
                                }
                                openNextScreen();
                            }
                        });
                        //  countDownTimer.start();
         /*               FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            LogUtils.printf("getInstanceId failed" + task.getException());

                                        } else {
                                            String token = task.getResult().getToken();
                                            LogUtils.printf("I am FCM Token: " + token);
                                            PrefUtils.saveFirebaseToken(context, "" + token);
                                            LogUtils.printf("I am FCM Token from PrefUtil: " + PrefUtils.getFirebaseToken(context));

                                        }

                                        // Get new Instance ID token

                                        openNextScreen();

                                        // Log and toast
                                    }
                                });*/


                        //updateLocationUI();
                    }


                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                LogUtils.printf("Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    LogUtils.printf("PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                //LogUtils.printf( errorMessage);

                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        //updateLocationUI();
                    }
                });
    }

    private void openNextScreen() {

        checkUserAuthenticationWithVersionControl();
    }

    private void openRestaurantDetailPage(String notificationData) {
        try {
            JSONArray array = new JSONArray(notificationData);
            JSONObject jsonObject = array.optJSONObject(0);

            RestaurantListModel model = new RestaurantListModel();
            model.setId(jsonObject.optString("id"));
            model.setName(jsonObject.optString("name"));
            model.setStatus(jsonObject.optString("ostatus"));
            model.setImage(jsonObject.optString("image"));
            AppController.setSelectedRestaurent(model);

            PrefUtils.saveActivityOpenFromNotification(context, true);
            startActivity(new Intent(context, DetailsActivity.class));
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkUserAuthenticationWithVersionControl() {

        //hideLayoutMessage();
        AppController.getInstance().getRequestQueue().cancelAll(TAG_REQUEST_VERSION_CHECK);


        String baseUrl = AppData.VERSION_CHECK_URL;
        Log.e("ff", "" + baseUrl);
        //showProgressBar();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                baseUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                LogUtils.printf("I am Restaurant List response: " + response);
                Log.e("res", response + "");
                try {
                    JSONObject object = new JSONObject(response.trim());

                    JSONObject version = object.optJSONObject("version");

                    int versionCode = version.optInt("version_code");

                    if (version.optString("system_active").equalsIgnoreCase("1")) {
                        if (version.optString("force_update").equalsIgnoreCase("1")) {

                            if (versionCode == com.dapl.halaladvisor.BuildConfig.VERSION_CODE) {
                                checkUserAuthentication();
                            } else {
                                showForceUpdateDialog();
                                //TODO Show Force Update Dialog.
                            }


                        } else {
                            if (versionCode == com.dapl.halaladvisor.BuildConfig.VERSION_CODE) {
                                checkUserAuthentication();
                            } else {
                                showFlexibleUpdateDialog();
                                //TODO Show Flexible Update Dialog.
                            }


                        }
                    } else {
                        //TODO Call NextExecute Code.
                        checkUserAuthentication();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("I am error: ", "" + error.getMessage());
                checkUserAuthentication();
                //hideProgressBar();

                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();


                params.put("check", "0");

                Log.e("Params", params + "");
                LogUtils.printf("I am Restaurant List param: " + params.toString());

                return params;
            }

        };

        Utils.setRetryPolicy(strReq);
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQUEST_VERSION_CHECK);
    }

    private void showForceUpdateDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Important Update Required")
                .setCancelable(false)
                .setMessage("To keep using the Halal Advisor app, please download the latest version.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        openAppInPlayStore();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("", null)
                //.setIcon(R.drawable.splash_icon_trans)
                .show();
    }

    private void showFlexibleUpdateDialog() {
        new AlertDialog.Builder(context)
                .setTitle("New Update")
                .setCancelable(false)
                .setMessage("A new update available in play store. Please Update now")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        openAppInPlayStore();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        checkUserAuthentication();
                    }
                })
                //.setIcon(R.drawable.splash_icon_trans)
                .show();
    }

    private void openAppInPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + com.dapl.halaladvisor.BuildConfig.APPLICATION_ID)));
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(context, "Play Store not installed", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkUserAuthentication() {

        if (PrefUtils.getProfileObject(context) != null) {
            Uri data = intent.getData();
            Intent intent = getIntent();
            Log.d("sdfsfsdfsdfsdfsf", "onCreate: " + intent.getExtras());
            String notificationType = intent.getStringExtra("notification_type");
            String lattitude = intent.getStringExtra("lattitude");
            String longitude = intent.getStringExtra("longitude");
            Log.d("notificationType", "onCreate: " + notificationType);
            Log.d("notificationType", "onCreate: " + data);
            if (data != null) {
                String restaurant_id = data.getQueryParameter("id");
                if (restaurant_id != null) {
                    PrefUtils.saveActivityOpenFromNotification(context, false);
                    startActivity(new Intent(context, MainActivity.class).putExtra("restaurant_id", restaurant_id));
                } else {
                    String lata = data.getQueryParameter("lati");
                    String lnga = data.getQueryParameter("longi");
                    Log.d("uriuri", "checkUserAuthentication: " + lata + " " + lnga);
                    startActivity(new Intent(context, MainActivity.class).putExtra("lat", lata).putExtra("lng", lnga).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
//
//
            } else {
                if (notificationType!=null) {
                    if(!notificationType.equals("Offer")){
                        startActivity(new Intent(context, SwipeHomeActivity.class));
                    }else{
                        startActivity(new Intent(context, MainActivity.class).putExtra("notification_type", notificationType).putExtra("lat", lattitude).putExtra("lng", longitude).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    }

                } else {

                    startActivity(new Intent(context, SwipeHomeActivity.class));
                }

            }


        } else {


            if (sessionManagement.getInfStatus()) {
                startActivity(new Intent(context, RegistrationActivity.class));
            } else {
                startActivity(new Intent(context, WelcomeActivity.class));
            }


        }

        finish();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        LogUtils.printf("User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        LogUtils.printf("User chose not to make required location settings changes.");

                        break;
                }
                break;
                case NOTIFICATION_PERMISSION_REQUEST_CODE:
                    if(NotificationManagerCompat.from(SplashActivity.this).areNotificationsEnabled())
                    {
                        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

                        initFcm();


                        initSession();

                        initLocation();
                        startLocationFetching();
                        //Do your Work
                    }
                    else
                    {
//            Snackbar.make(findViewById(R.id.drawer_layout), "Notification blocked", Snackbar.LENGTH_LONG).setAction("Settings") {
//                // Responds to click on the action
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                val uri: Uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }.show()

                        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("Turn Notifications On")
                                .setCancelable(false)
                                .setMessage("Turn push notifications on for Halal Advisor to receive new venue alerts and special deals.")
                                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Code to handle when the "Settings" button is clicked.
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Code to handle when the "Cancel" button is clicked.
                                        dialog.dismiss();
                                    }
                                })
                                .create();

                        alertDialog.show();
                        //Ask for permission
                    }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                com.dapl.halaladvisor.BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}