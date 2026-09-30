package com.dapl.halaladvisor.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.Credential;
import com.dapl.halaladvisor.Model.ThirdPartyModel;
import com.dapl.halaladvisor.Model.UserDataModel;
import com.dapl.halaladvisor.Model.socialloginres.SocialLoginRes;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.extra.mAsyncTaskMultipart;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * Created by DAT-Asset-128 on 29-08-2016.
 * Developed by- Rana Saha Ray
 */

public class LogInActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final String IS_LOGOUT = "bundle_is_logout";
    private static final int GOOGLE_SING_IN = 502;
    private static final int RC_SIGN_IN = 007;
    ProgressDialog pDialogSignIn;
    Context context;
    ProgressDialog progressDialog;
    ApiService apiService;
    String mFullName, mUrl,
            mGender,
            mId, mEmail, mRegistrationID;
    private TextView tv_cancel, tv_forgot_password, tv_login;
    private EditText et_email, et_password;
    // facebook
    private TextView tvFacebookLogin, tvGoogleLogin;
    private CallbackManager mFacebookCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog dialog;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
        //googleInit();
        initGoogleLogin();
        // googleInit();
        initProgress();

    }

    public void initProgress() {
        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Please Wait...");

        progressDialog.setCancelable(false);


    }

    @Override
    protected void onResume() {
        super.onResume();
        initFcm();
        initGoogleLogin();
        try {
           /* if (mFacebookCallbackManager != null) {
                //setUpFacebookLogin_New();
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initFcm() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                // device_token = task.getResult();
                try {
                    String token = task.getResult();
                    LogUtils.printf("I am Token success getInstanceId failed" + token);
                    PrefUtils.saveFirebaseToken(context, token);
                } catch (Exception e) {

                }


            }
        });
    }

  /*  //=========================
    private void setUpFacebookLogin_New() {

        LogUtils.printf("d" + "Facebook login 1 !!");

        final Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                // TODO Auto-generated method stub
                LogUtils.printf("FB" + "SUCCESS");

                boolean tokenActive = loginResult.getAccessToken() != null;
                LogUtils.printf("d" + "Facebook login 2 !!");
                mRegistrationID = loginResult.getAccessToken().getApplicationId();

                if (tokenActive) {

                    AccessToken.setCurrentAccessToken(loginResult.getAccessToken());

                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                            // TODO Auto-generated method stub
                            LogUtils.printf("USER" + response.toString());
                            if (jsonObject != null) {
                                try {
                                    mFullName = jsonObject.optString("name");
                                    mEmail = jsonObject.optString("email");
                                    mId = jsonObject.optString("id");
                                    mGender = jsonObject.optString("gender");
                                    mUrl = "https://graph.facebook.com/" + mId + "/picture?type=large";
                                    //SOCIAL_LOGIN_TYPE = LOGIN_TYPE_FACEBOOK;

                                    LogUtils.printf("FB_PROFILE mFullName mFullName " + mFullName);
                                    LogUtils.printf("FB_PROFILE mEmail mEmail " + mEmail);
                                    LogUtils.printf("FB_PROFILE mId mId " + mId);
                                    LogUtils.printf("FB_PROFILE mGender mGender " + mGender);
                                    LogUtils.printf("FB_PROFILE mUrl mUrl " + mUrl);
                                    // LogUtils.printf("FB_PROFILE SOCIAL_LOGIN_TYPE  SOCIAL_LOGIN_TYPE " + SOCIAL_LOGIN_TYPE);
                                    // new mCallSocialUserCheck(mId, LOGIN_TYPE_FACEBOOK).execute();


                                } catch (Exception e) {
                                    e.printStackTrace();

                                    if (loginResult.getAccessToken() != null) {
                                        Set<String> deniedPermissions = loginResult.getRecentlyDeniedPermissions();

                                        if (deniedPermissions.contains("email")) {

                                            LoginManager.getInstance().logInWithReadPermissions(LogInActivity.this, Arrays.asList("email"));

                                        } else {
                                            Toast.makeText(LogInActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                            } else {
                                Toast msg = Toast.makeText(LogInActivity.this, " " + response.getError().getErrorMessage(), Toast.LENGTH_LONG);

                                msg.show();
                            }
                        }
                    });

                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                LogUtils.printf("FB CANCEL");
                Toast.makeText(LogInActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                // TODO Auto-generated method stub
                LogUtils.printf("FB ERROR");
                LogUtils.printf("I am FB Error" + error.getMessage());
                // Toast.makeText(LogInActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }

        });
    }
    //=========================
*/

    @SuppressWarnings("deprecation")
    private void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }


    private void handleGoogleSignInResult(final GoogleSignInResult result) {
        if (result.isSuccess()) {


            final GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("email: ", "--" + acct.getEmail());
            if (acct != null) {
                ThirdPartyModel thirdPartyModel = new ThirdPartyModel();

                thirdPartyModel.setId(acct.getId());
                thirdPartyModel.setUserFullName(acct.getDisplayName());
                thirdPartyModel.setUserEmail(acct.getEmail());
                thirdPartyModel.setId(acct.getId());
                thirdPartyModel.setUserProfileImage(acct.getPhotoUrl() == null ? null : acct.getPhotoUrl().toString());


                LogUtils.printf("I am Acc: " + acct.getEmail());

                // SignUpByThirdParty(thirdPartyModel, bitmapToFile());
                socialLogin(thirdPartyModel);
     /*           Plus.PeopleApi.load(mGoogleApiClient, acct.getId()).setResultCallback(
                        new ResultCallback<People.LoadPeopleResult>() {
                            @Override
                            public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

                                ThirdPartyModel thirdPartyModel = new ThirdPartyModel();

                                thirdPartyModel.setId(acct.getId());
                                thirdPartyModel.setUserFullName(acct.getDisplayName());
                                thirdPartyModel.setUserEmail(acct.getEmail());
                                thirdPartyModel.setId(acct.getId());
                                thirdPartyModel.setUserProfileImage(acct.getPhotoUrl() == null ? null : acct.getPhotoUrl().toString());


                                LogUtils.printf("I am Acc: " + acct.getEmail());

                                // SignUpByThirdParty(thirdPartyModel, bitmapToFile());
                                socialLogin(thirdPartyModel);
                                //SignUpByThirdParty(thirdPartyModel,bitmapToFile(),"Google");
                                //doThirdPartySignUp(thirdPartyModel, "Google");

                            }
                        });*/
            } else {
                LogUtils.printf("I am Acc: null" + result);

                Log.d("TANAY", "account null");
            }
        } else {
            LogUtils.printf("I am Acc: logged out" + result);
            Log.d("TANAY", "logged out" + result);
        }
    }


    public void googleInit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //   updateUI(false);
                    }
                });
    }

    private void googlePlusSignIn() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SING_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);

        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        LogUtils.printf("I am google Signin response: " + data);
       /* switch (requestCode) {


            case GOOGLE_SING_IN:


                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);

                LogUtils.printf("I am google Signin response: " + result.toString());

                break;


        }*/
    }

    private void initFacebook() {

        try {


            // clearFacebookCredentials();
            if (LoginManager.getInstance() != null) {

                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(LogInActivity.this, Arrays.asList("public_profile", "email"));

                LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code

                                try {
                                    facebookGraphRequest(loginResult);


                                } catch (Exception e) {

                                    LogUtils.printf("I am login graph exp " + e);

                                    e.printStackTrace();
                                }
                                LogUtils.printf("I am login callback fb onSuccess" + loginResult.toString());
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                LogUtils.printf("I am login callback fb onCancel");

                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                LogUtils.printf("I am login callback fb onError" + exception.getMessage());

                            }
                        });

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    private void facebookGraphRequest(LoginResult loginResult) {


        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
                LogUtils.printf("I am fb login graph response: " + object.toString());


                try {

                    ThirdPartyModel thirdPartyModel = new ThirdPartyModel();
                    thirdPartyModel.setId(object.optString("id"));
                    thirdPartyModel.setUserFullName(object.optString("first_name") + " " + object.optString("last_name"));
                    thirdPartyModel.setUserEmail(object.optString("email"));
                    thirdPartyModel.setUserProfileImage("http://graph.facebook.com/" + object.optString("id") + "/picture?type=large");


                    Log.e("image model", "--" + thirdPartyModel.getUserProfileImage());

                    // SignUpByThirdParty(thirdPartyModel, null, AppData.THIRD_PARTY_LOGIN);

                    //   SignUpByThirdParty(thirdPartyModel, bitmapToFile());
                    socialLogin(thirdPartyModel);
                    // doThirdPartySignUp(thirdPartyModel, "Facebook");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void initView() {

        mFacebookCallbackManager = CallbackManager.Factory.create();
        mappingViews();
        onClick();
    }


    private void mappingViews() {

        tvFacebookLogin = findViewById(R.id.tv_fb_login);
        tvGoogleLogin = findViewById(R.id.tv_google_login);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);

        //  tv_login_email = (TextView) findViewById(R.id.tv_registration_email);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_cancel = findViewById(R.id.tv_reg_cancel);
        tv_login = findViewById(R.id.tv_btnLogin);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);


      /*  changeEdittextBackground(et_email);
        changeEdittextBackground(et_password);*/

        //applyFonts();
    }


    private void onClick() {
        tv_login.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        tvFacebookLogin.setOnClickListener(this);
        //tvGoogleLogin.setOnClickListener(this);


        tvGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // googlePlusSignIn();
                //initGoogleLogin();
                // signOut();
               /* if (!mGoogleApiClient.isConnected()) {
                   initGoogleLogin();
                }*/


                signIn();
            }
        });

      /*  tvFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                LoginManager.getInstance()
                        .logInWithReadPermissions(this, listOf("public_profile", "email"));

                callFacebookLogin();
            }
        });*/

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_btnLogin:

                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (!is_field_validate(email, password)) {
                    // getLoginResponse(email, password);

                    new ReqLogIn().execute();

                }

                break;

            case R.id.tv_reg_cancel:
                Intent intent = new Intent(LogInActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.tv_forgot_password:


                startActivity(new Intent(context, ForgotPasswordActivity.class));

                break;

            case R.id.tv_fb_login:
                initFacebook();

                //mLoginManager.setLoginBehavior(LoginBehavior.WEB_ONLY);
                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
                break;

            case R.id.tv_google_login:
                googlePlusSignIn();
                break;
        }
    }

    private boolean is_field_validate(String email, String password) {

        boolean inValid = false;

        if (!email.trim().equalsIgnoreCase("") && !password.trim().equalsIgnoreCase("")) {
            if (!AppData.isValidEmail(email)) {
                et_email.setError("Email format Error");
                inValid = true;
            } else {
                et_email.setError(null);
            }
            if (password.length() < 6/*!AppData.isValidPassword(password)*/) {
//                et_password.setError("Should contain atleast an alphabet\na number,\na special character,\nmin 8 characters");
                et_password.setError("Should contain atleast 6 characters");
                inValid = true;
            } else {
                et_password.setError(null);
            }
        } else {
            if (email.trim().equalsIgnoreCase("")) {
                et_email.setError("Email is Empty");
                inValid = true;
            } else {
                et_email.setError(null);
            }
            if (password.trim().equalsIgnoreCase("")) {
                et_password.setError("Password is Empty");
                inValid = true;
            } else {
                et_password.setError(null);
            }
        }
        return inValid;
    }

    private void openHomePage() {
        startActivity(new Intent(context, SwipeHomeActivity.class));
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        Intent intent = new Intent(LogInActivity.this, BeforeLoginActivity.class);
        startActivity(intent);
        finish();*/
    }

    /*
     * Third Party Login Requestor
     * */
    @SuppressLint("StaticFieldLeak")
    private void doThirdPartySignUp(final ThirdPartyModel thirdPartyModel, final String loginType) {

        bitmap = null;

        if (!TextUtils.isEmpty(thirdPartyModel.getUserProfileImage())) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog = new SpotsDialog(LogInActivity.this, R.style.CustomDialog);
                    dialog.setCancelable(true);
                    dialog.show();
                }

                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        URL url = new URL(thirdPartyModel.getUserProfileImage());
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    LogUtils.printf("I am in bitmap: post exe 1 ");
                    try {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        //sendThirdPartSignUpData(thirdPartyModel, loginType);
                        LogUtils.printf("I am in bitmap: post exe 2 ");
                        //new ReqLogInByTrdParty(thirdPartyModel).execute();
                        //SignUpByThirdParty(thirdPartyModel, bitmapToFile());
                        LogUtils.printf("I am in bitmap: post exe 33 ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            try {
                thirdPartyModel.setUserProfileImage("");
                //sendThirdPartSignUpData(thirdPartyModel, loginType);
                //  SignUpByThirdParty(thirdPartyModel, bitmapToFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File bitmapToFile() {

        File file = null;
        try {
            file = new File(LogInActivity.this.getCacheDir(), "temp_picture.jpg");
            file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos);
            byte[] bitmapData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void socialLogin(ThirdPartyModel thirdPartyModel) {

        progressDialog.show();

        String tempLat = "0.0";
        String tempLng = "0.0";
        if (PrefUtils.getUserLatLong(context) != null) {
            tempLat = String.valueOf(PrefUtils.getUserLatLong(context).getLattitude());
            tempLng = String.valueOf(PrefUtils.getUserLatLong(context).getLongitude());

        }

/*
        third_party_id, profileimage, username, email, phone, device_type, device_token, lat,long*/
        apiService.socialLogin(thirdPartyModel.getId(), thirdPartyModel.getUserProfileImage(),
                        thirdPartyModel.getUserFullName(), thirdPartyModel.getUserEmail(), "", "A",
                        PrefUtils.getFirebaseToken(context), tempLat,
                        tempLng).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<SocialLoginRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SocialLoginRes res) {
                        progressDialog.dismiss();
                        Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();

                        if (res.getStatus().equals("1")) {


                            UserDataModel model = new UserDataModel();
                            model.setU_id(res.getUserdetails().getuId());
                            model.setUsername(res.getUserdetails().getUsername());
                            model.setUseremail(res.getUserdetails().getUseremail());
                            model.setProfile_image_url(res.getUserdetails().getProfileImageUrl());


                            String profile_obj = PrefUtils.getJsonFromObject(model, UserDataModel.class).trim();

                            PrefUtils.saveProfile(context, profile_obj);

                            startActivity(new Intent(context, SwipeHomeActivity.class));
                            finish();

                        } else if (res.getStatus().equals("0")) {
                            Toast.makeText(LogInActivity.this, "You are not registered yet", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LogInActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err", " in profile--" + e.toString());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                        progressDialog.dismiss();
                    }
                });

    }


    private void SignUpByThirdParty(ThirdPartyModel thirdPartyModel) {


        String url = AppData.THIRD_PARTY_LOGIN;


/*  social login params:
        third_party_id,profileimage,username,email,phone,device_type,device_token,lat,long*/


        LogUtils.printf("I am SignUpByThirdParty ====  1");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        LogUtils.printf("I am SignUpByThirdParty ====  2");
        builder.setType(MultipartBody.FORM);
        LogUtils.printf("I am SignUpByThirdParty ====  3");

   /*     try {
            if (file != null) {
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                builder.addFormDataPart("profileimage", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

            }else {

                builder.addFormDataPart("third_party_id","");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        try {
            LogUtils.printf("I am SignUpByThirdParty ====  4");
            builder.addFormDataPart("third_party_id", thirdPartyModel.getId());
            builder.addFormDataPart("profileimage", thirdPartyModel.getUserProfileImage());
            LogUtils.printf("I am SignUpByThirdParty ====  5");
            builder.addFormDataPart("username", thirdPartyModel.getUserFullName());
            LogUtils.printf("I am SignUpByThirdParty ====  6");
            builder.addFormDataPart("email", thirdPartyModel.getUserEmail());
            LogUtils.printf("I am SignUpByThirdParty ====  7");
            builder.addFormDataPart("phone", "");
            LogUtils.printf("I am SignUpByThirdParty ====  8");
            builder.addFormDataPart("device_type", "A");
            LogUtils.printf("I am SignUpByThirdParty ====  9");
            builder.addFormDataPart("device_token", PrefUtils.getFirebaseToken(context));
            LogUtils.printf("I am SignUpByThirdParty ====  10");
            Log.e("builder ====  10", "--" + builder);
            Log.e("thirdPar de", "--" + thirdPartyModel);

            try {
                if (PrefUtils.getUserLatLong(context) != null) {

                    builder.addFormDataPart("lat", String.valueOf(PrefUtils.getUserLatLong(context).getLattitude()));
                    builder.addFormDataPart("long", String.valueOf(PrefUtils.getUserLatLong(context).getLongitude()));

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            pDialogSignIn = new ProgressDialog(context);
            pDialogSignIn.setMessage("Signing In...");
            pDialogSignIn.setCancelable(false);
            pDialogSignIn.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // LogUtils.printf("I am Builder ====  " + builder.toString());
        new mAsyncTaskMultipart(LogInActivity.this, url, builder, new mAsyncTaskMultipart.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                LogUtils.printf("I am MultiPaart OutPut " + output);

                try {
                    pDialogSignIn.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject object = new JSONObject(output);

                    if (object.optInt("status") == 1) {

                        JSONObject jsonObject = object.optJSONObject("userdetails");

                        UserDataModel model = new UserDataModel();
                        model.setU_id(jsonObject.optString("u_id"));
                        model.setUsername(jsonObject.optString("username"));
                        model.setUseremail(jsonObject.optString("useremail"));
                        model.setProfile_image_url(jsonObject.optString("profile_image_url"));

                        String profile_obj = PrefUtils.getJsonFromObject(model, UserDataModel.class).trim();

                        PrefUtils.saveProfile(context, profile_obj);

                        startActivity(new Intent(context, SwipeHomeActivity.class));
                        finish();

                    } else if (object.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(LogInActivity.this, "You are not registered yet", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LogInActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).execute();
    }

    private class ReqLogIn extends AsyncTask<Void, Void, String> {

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


            HashMap<String, String> map = new HashMap<>();
            map.put("email", et_email.getText().toString().trim());
            map.put("password", et_password.getText().toString().trim());
            map.put("device_type", "A");
            map.put("device_token", PrefUtils.getFirebaseToken(context));
            try {
                if (PrefUtils.getUserLatLong(context) != null) {

                    map.put("lat", String.valueOf(PrefUtils.getUserLatLong(context).getLattitude()));
                    map.put("long", String.valueOf(PrefUtils.getUserLatLong(context).getLongitude()));

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONParser parser = new JSONParser();


            String mURL = AppData.LOGIN_URL;

            LogUtils.printf("I am url: " + mURL);
            LogUtils.printf("I am param: " + map);


            response = parser.makeHttpRequest(mURL, "POST", map);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am LogIn response: " + response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status") == 1) {

                    Credential credential = new Credential();
                    credential.setUserName(et_email.getText().toString().trim());
                    credential.setPassWord(et_password.getText().toString().trim());

                    String object = PrefUtils.getJsonFromObject(credential, Credential.class).trim();

                    PrefUtils.saveCredential(context, object);
                    PrefUtils.saveProfile(context, jsonObject.optJSONObject("userdetails").toString().trim());

                    openHomePage();

                }


                Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


}