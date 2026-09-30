package com.dapl.halaladvisor.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.MainActivity;
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
import com.facebook.login.LoginBehavior;
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
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    ProgressDialog pDialogSignIn;
    private static final int GOOGLE_SING_IN = 503;
    Context context;
    RelativeLayout rl_main;
    Bitmap bitmap;
    SpotsDialog dialog;
    ProgressDialog progressDialog;
    private TextView tv_already_member, tv_cancel, tv_fb_registration, tv_google_registration, tv_registration,tvTerms,tvPrivacy;
    private EditText et_name, et_email, et_password;

    private GoogleApiClient mGoogleApiClient;

    ApiService apiService;
    // facebook
    private LoginManager mLoginManager;

    private CallbackManager mFacebookCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        context = this;


        initUI();

        initGoogleLogin();

      //  initFacebook();
        initProgress();

    }

    public void initProgress() {
        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Please Wait...");

        progressDialog.setCancelable(false);


    }

    private void initUI() {

        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        mFacebookCallbackManager = CallbackManager.Factory.create();
        tv_already_member = findViewById(R.id.tv_already_member);
        tv_cancel = findViewById(R.id.tv_reg_cancel);
        tvTerms = findViewById(R.id.tvTerms);
        tvPrivacy = findViewById(R.id.tvPrivacy);
        tv_cancel.setVisibility(View.GONE);
        tv_fb_registration = findViewById(R.id.iv_fb_registration);
        tv_google_registration = findViewById(R.id.iv_google_registration);
        tv_registration = findViewById(R.id.iv_btnRegistration);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        rl_main = findViewById(R.id.rl_registration_layout);

        tv_cancel.setOnClickListener(this);
        tv_registration.setOnClickListener(this);
        tv_already_member.setOnClickListener(this);
        tv_fb_registration.setOnClickListener(this);
     //   tv_google_registration.setOnClickListener(this);


      //  tvTerms.setPaintFlags(tvTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    //    tvTerms.setText(Html.fromHtml("W : "+"<u><FONT COLOR=\"#05B900\" >"+getString(R.string.terms_of_use)+"</Font></u>"));


        tv_google_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlePlusSignIn();
            }
        });


        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(context, AboutWebScreen.class).
                        putExtra("about","https://halaladvisor.com.au/terms-of-use/")
                        . putExtra("type","Terms of use")
                );
            }
        });

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(context, AboutWebScreen.class).
                        putExtra("about","https://halaladvisor.com.au/privacy-policy-2/")
                        . putExtra("type","Privacy Policy")
                );
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_btnRegistration:


                if (isEveryThingOk()) {

                    new ReqRegistration().execute();

                }


                break;

            case R.id.tv_reg_cancel:
                finish();
                break;

            case R.id.tv_already_member:

                startActivity(new Intent(RegistrationActivity.this, LogInActivity.class));
                finish();

                break;
            case R.id.iv_fb_registration:
                // LoginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
            //    mLoginManager.setLoginBehavior(LoginBehavior.WEB_ONLY);
                initFacebook();
                break;
        /*    case R.id.iv_google_registration:

                googlePlusSignIn();

                break;*/
        }
    }

    private boolean isEveryThingOk() {

        boolean b;

        ArrayList<String> errorList = new ArrayList<>();

        if (et_name.getText().toString().trim().length() == 0) {
            errorList.add("Enter your full name");

        }

        if (et_email.getText().toString().trim().length() == 0) {
            errorList.add("Enter your email id");

        }

        if (!AppData.isValidEmail(et_email.getText().toString().trim())) {
            errorList.add("Enter a valid email id");

        }


        if (et_password.getText().toString().trim().length() == 0) {
            errorList.add("Enter your password");

        }

        if (et_password.getText().toString().trim().length() < 6) {
            errorList.add("Should contain at-least 6 characters");
        }


        if (errorList.size() > 0) {

            b = false;
            Toast.makeText(context, errorList.get(0), Toast.LENGTH_SHORT).show();

        } else {
            b = true;
        }


        return b;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GOOGLE_SING_IN:


                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleGoogleSignInResult(result);

                LogUtils.printf("I am google Signin response: " + result.toString());

                break;
        }
    }

    private void initFacebook() {

        try {


           // mFacebookCallbackManager = CallbackManager.Factory.create();
            // clearFacebookCredentials();
            if (LoginManager.getInstance() != null) {

                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(RegistrationActivity.this,Arrays.asList("public_profile", "email"));

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
/*
    private void facebookGraphRequest(LoginResult loginResult) throws Exception {

        Log.e("TANAY", "facebookGraphRequest");

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
                LogUtils.printf("I am fb login graph response: " + object.toString());

                try {
                    ThirdPartyModel thirdPartyModel = new ThirdPartyModel();
                    thirdPartyModel.setId(object.optString("id"));
                    thirdPartyModel.setUserFullName(object.optString("first_name") + " " + object.optString("last_name"));
                    thirdPartyModel.setUserEmail(object.optString("email"));
                    thirdPartyModel.setUserProfileImage("https://graph.facebook.com/" + object.optString("id") + "/picture?type=large");

                    Log.e("getUserProfileImage ","---"+thirdPartyModel.getUserProfileImage());

                    // SignUpByThirdParty(thirdPartyModel, bitmapToFile());
                    doThirdPartySignUp(thirdPartyModel, "Facebook");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
      //  parameters.putString("fields", "id, first_name, last_name, email, gender");
        parameters.putString("fields", "name,email,id,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }*/












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


                    // SignUpByThirdParty(thirdPartyModel, null, AppData.THIRD_PARTY_LOGIN);
                   // doThirdPartySignUp(thirdPartyModel, "Facebook");
                 //   SignUpByThirdParty(thirdPartyModel /*bitmapToFile()*/,"Facebook");
                    socialLogin(thirdPartyModel);
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

            if (acct != null) {

                ThirdPartyModel thirdPartyModel = new ThirdPartyModel();

                thirdPartyModel.setId(acct.getId());
                thirdPartyModel.setUserFullName(acct.getDisplayName());
                thirdPartyModel.setUserEmail(acct.getEmail());
                thirdPartyModel.setId(acct.getId());
                thirdPartyModel.setUserProfileImage(acct.getPhotoUrl() == null ? null : acct.getPhotoUrl().toString());


                LogUtils.printf("I am Acc: " + acct.getEmail());
                //    SignUpByThirdParty(thirdPartyModel,/*bitmapToFile()*/"Google");
                socialLogin(thirdPartyModel);



            } else {
                LogUtils.printf("I am Acc: null");

                Log.d("TANAY", "account null");
            }
        } else {
            LogUtils.printf("I am Acc: logged out");
            Log.d("TANAY", "logged out");
        }
    }


    private void googlePlusSignIn() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
        }


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SING_IN);
    }


    //======================================================================


    /*
     * Third Party Login Requestor
     * */
  /*  @SuppressLint("StaticFieldLeak")
    private void doThirdPartySignUp(final ThirdPartyModel thirdPartyModel, final String loginType) {

        bitmap = null;

        if (!TextUtils.isEmpty(thirdPartyModel.getUserProfileImage())) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog = new SpotsDialog(RegistrationActivity.this, R.style.CustomDialog);
                    dialog.setCancelable(true);
                    dialog.show();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    InputStream inputStream;
                    try {
                        URL url = new URL(thirdPartyModel.getUserProfileImage());
                        Log.e("thirdPartyModel ","---"+thirdPartyModel.getUserProfileImage());
                       // bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        inputStream = new java.net.URL(url.getPath()).openStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);

                        Log.e("bitmap: post exe 1 ","---"+bitmap);
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
                        SignUpByThirdParty(thirdPartyModel, bitmapToFile());
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
                SignUpByThirdParty(thirdPartyModel, bitmapToFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }*/
    @SuppressLint("StaticFieldLeak")
    private void doThirdPartySignUp(final ThirdPartyModel thirdPartyModel, final String loginType) {

        bitmap = null;

        if (!TextUtils.isEmpty(thirdPartyModel.getUserProfileImage())) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog = new SpotsDialog(RegistrationActivity.this, R.style.CustomDialog);
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
                      //  SignUpByThirdParty(thirdPartyModel, bitmapToFile());
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
            file = new File(RegistrationActivity.this.getCacheDir(), "temp_picture.jpg");
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
            // Log exception
            return null;
        }
    }





    public void socialLogin(ThirdPartyModel thirdPartyModel) {

        progressDialog.show();
        String tempLat="0.0";
        String tempLng="0.0";
        if(PrefUtils.getUserLatLong(context)!=null)
        {
            tempLat=String.valueOf(PrefUtils.getUserLatLong(context).getLattitude());
            tempLng=String.valueOf(PrefUtils.getUserLatLong(context).getLongitude());

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
                            Toast.makeText(RegistrationActivity.this, "You are not registered yet", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
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


    private void SignUpByThirdParty(ThirdPartyModel thirdPartyModel, /*File file,*/final String loginType) {


        String url= AppData.THIRD_PARTY_LOGIN;
        LogUtils.printf("I am SignUpByThirdParty ====  1");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        LogUtils.printf("I am SignUpByThirdParty ====  2");
        builder.setType(MultipartBody.FORM);
        LogUtils.printf("I am SignUpByThirdParty ====  3");
/*
        try {
            if (file != null) {
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                builder.addFormDataPart("profileimage", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

            }else {

                builder.addFormDataPart("third_party_id","");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        try {
            LogUtils.printf("I am SignUpByThirdParty ====  4");
            builder.addFormDataPart("profileimage", thirdPartyModel.getUserProfileImage());
            builder.addFormDataPart("third_party_id", thirdPartyModel.getId());
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

            try {
                if (PrefUtils.getUserLatLong(context)!=null){

                    builder.addFormDataPart("lat", String.valueOf(PrefUtils.getUserLatLong(context).getLattitude()));
                    builder.addFormDataPart("long", String.valueOf(PrefUtils.getUserLatLong(context).getLongitude()));

                }



            }catch (Exception e){e.printStackTrace();}

        }catch (Exception e){e.printStackTrace();}



        try {
            pDialogSignIn = new ProgressDialog(context);
            pDialogSignIn.setMessage("Signing In...");
            pDialogSignIn.setCancelable(false);
            pDialogSignIn.show();

        }catch (Exception e){e.printStackTrace();}

        // LogUtils.printf("I am Builder ====  " + builder.toString());
        new mAsyncTaskMultipart(RegistrationActivity.this, url, builder, new mAsyncTaskMultipart.AsyncResponse() {
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
                        Toast.makeText(RegistrationActivity.this, "You are not registered yet", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).execute();
    }
 /*
    private void SignUpByThirdParty(ThirdPartyModel thirdPartyModel, File file) {


        String url= AppData.THIRD_PARTY_LOGIN;
        LogUtils.printf("I am SignUpByThirdParty ====  1");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        LogUtils.printf("I am SignUpByThirdParty ====  2");

        LogUtils.printf("I am SignUpByThirdParty ====  3");

        try {
            if (file != null) {
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                builder.addFormDataPart("profileimage", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));

            }else {

                builder.addFormDataPart("third_party_id","");
            }

            try {
                if (PrefUtils.getUserLatLong(context)!=null){

                    builder.addFormDataPart("lat", String.valueOf(PrefUtils.getUserLatLong(context).getLattitude()));
                    builder.addFormDataPart("long", String.valueOf(PrefUtils.getUserLatLong(context).getLongitude()));

                }



            }catch (Exception e){e.printStackTrace();}

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            LogUtils.printf("I am SignUpByThirdParty ====  4");
            builder.addFormDataPart("third_party_id", thirdPartyModel.getId());
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
        }catch (Exception e){e.printStackTrace();}

        try {
            pDialogSignIn = new ProgressDialog(context);
            pDialogSignIn.setMessage("Signing In...");
            pDialogSignIn.setCancelable(false);
            pDialogSignIn.show();

        }catch (Exception e){e.printStackTrace();}

         LogUtils.printf("I am Builder ====  " + builder.toString());
        new mAsyncTaskMultipart(RegistrationActivity.this, url, builder, new mAsyncTaskMultipart.AsyncResponse() {
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

                        startActivity(new Intent(context, MainActivity.class));
                        finish();

                    } else if (object.optString("status").equalsIgnoreCase("0")) {
                        Toast.makeText(RegistrationActivity.this, "You are not registered yet", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).execute();
    }
*/



    //============================================================================




    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void openHomePage() {

        startActivity(new Intent(context, SwipeHomeActivity.class));
        finish();


    }

    private class ReqRegistration extends AsyncTask<Void, Void, String> {

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
            params.put("username", et_name.getText().toString().trim());
            params.put("email", et_email.getText().toString().trim());
            params.put("password", et_password.getText().toString().trim());
            params.put("device_type", "A");
            params.put("device_token", PrefUtils.getFirebaseToken(context));
            try {
                if (PrefUtils.getUserLatLong(context)!=null){

                    params.put("lat", String.valueOf(PrefUtils.getUserLatLong(context).getLattitude()));
                    params.put("long", String.valueOf(PrefUtils.getUserLatLong(context).getLongitude()));

                }



            }catch (Exception e){e.printStackTrace();}

            JSONParser parser = new JSONParser();


            String mURL = AppData.SIGNUP_URL;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am registration response: " + response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status") == 1) {


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
