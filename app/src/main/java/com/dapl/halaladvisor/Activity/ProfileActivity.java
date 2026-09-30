package com.dapl.halaladvisor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.ProfileDetailObject;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.AlertDialogManager;
import com.dapl.halaladvisor.interfaces.OnClickOkDelegate;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    private TextView textViewName, textViewLocation,tvBtnLogin,deleteAccountTv;
    private EditText editTextName, editTextEmail, editTextPhone, editTextLocation;
    private ImageView userImage, imageViewBack, imageViewEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        initUi();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new ReqProfileDetails().execute();
    }

    private void setData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optInt("status") == 1) {

                String jsnObj = jsonObject.optJSONObject("data").toString().trim();
                PrefUtils.saveProfileDetail(context,jsnObj);
                ProfileDetailObject profile = (ProfileDetailObject) PrefUtils.getObjectFromJson(jsnObj,
                        ProfileDetailObject.class);

                if (profile != null) {

                    textViewName.setText(profile.getUsername());
                    editTextName.setText(profile.getUsername());
                    if (profile.getUseremail().trim().length() > 1) {
                        editTextEmail.setText(profile.getUseremail().trim());
                    }
                    if (profile.getUsermobile().trim().length() > 1) {
                        editTextPhone.setText(profile.getUsermobile().trim());
                    }
                    if (profile.getLocation().trim().length() > 1) {
                        editTextLocation.setText(profile.getLocation().trim());
                        textViewLocation.setText(profile.getLocation().trim());
                    }

                    if (!profile.getProfile_image_url().trim().equalsIgnoreCase("")) {
                        try {
                            Glide.with(this)
                                    .load(profile.getProfile_image_url().trim())

                                    .apply(RequestOptions.circleCropTransform()) /*  .transform(new GlideCircleWithBorder(view.getContext(), 2, Color.parseColor("#005194")))*/
                                    .placeholder(R.mipmap.ic_app_icon)
                                    .into(userImage);
//                            Picasso.get()
//                                    .load(profile.getProfile_image_url().trim())
//                                    .placeholder(R.mipmap.ic_app_icon)
//                                    .error(R.mipmap.ic_app_icon)
//                                    .noFade()
//                                    .into(userImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //==================
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initUi() {


        userImage = findViewById(R.id.imageView_image);

        TextView textViewProfile = findViewById(R.id.textView_profile);


        textViewName = findViewById(R.id.textView_name);
        deleteAccountTv = findViewById(R.id.deleteAccountTv);

        textViewLocation = findViewById(R.id.textView_location);

        editTextName = findViewById(R.id.editText_name);
        editTextEmail = findViewById(R.id.editText_email);
        editTextPhone = findViewById(R.id.editText_phone);
        editTextLocation = findViewById(R.id.editText_location);

        imageViewBack = findViewById(R.id.imageView_back);
        imageViewBack.setOnClickListener(this);
        imageViewBack.bringToFront();
        imageViewEdit = findViewById(R.id.imageView_edit);
        tvBtnLogin = findViewById(R.id.tv_btnLogin);
        imageViewEdit.setOnClickListener(this);
        tvBtnLogin.setOnClickListener(this);
        deleteAccountTv.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imageView_back: {

                onBackPressed();
                break;
            }
            case R.id.deleteAccountTv: {

                AlertDialogManager alertDialogManager = new AlertDialogManager();
                alertDialogManager.setLogoutDelegate(new OnClickOkDelegate() {
                    @Override
                    public void onClickOkDelegate(Boolean isLogoutOk) {
                        if (isLogoutOk) {

                          new ReqDeleteAccount().execute();
                            //logOutFunction();
                        }
                    }
                });
                AlertDialogManager.showOkCancelAlertDialog(context, "Delete Account", "Do you really want to delete account?");

                break;
            }
            case R.id.imageView_edit: {


                startActivity(new Intent(context, EditProfileActivity.class));
                break;
            }
            case R.id.tv_btnLogin: {


                startActivity(new Intent(context, EditProfileActivity.class));
                break;
            }


        }


    }

    private class ReqDeleteAccount extends AsyncTask<Void, Void, String> {

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

            params.put("u_id", PrefUtils.getProfileObject(context).getU_id());

            JSONParser parser = new JSONParser();


            String mURL = AppData.DELETE_USER_URL;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", params);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am LogOut response: " + response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optString("status").equalsIgnoreCase("1")) {


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
                 //   setUpGoogleApiClientInstance();

                    PrefUtils.removeAllSavedData(context);

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
    private class ReqProfileDetails extends AsyncTask<Void, Void, String> {

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
            map.put("u_id", PrefUtils.getProfileObject(context).getU_id());



            JSONParser parser = new JSONParser();


            String mURL = AppData.GET_PROFILE_DATA;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", map);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Restaurant List response: " + response);


            setData(response);


        }


    }
}
