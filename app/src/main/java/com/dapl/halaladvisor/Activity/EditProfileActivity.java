package com.dapl.halaladvisor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.ProfileDetailObject;
import com.dapl.halaladvisor.Model.UserDataModel;
import com.dapl.halaladvisor.Model.profileupdateres.ProfileUpdateRes;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.extra.CircleImageView;
import com.dapl.halaladvisor.extra.mAsyncTaskMultipart;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
PrefUtils prefUtils;
    Context context;
    String profileImage = "";
    ImageView cIV_userImage;
    EditText et_name, et_email, et_phone, et_location;
    TextView tv_editPhoto, tv_buttonSave;
    RelativeLayout rl_main;
    Bitmap profileimgBitmap = null;
    ImageView ivBack;
    ProgressBar progressBar;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = this;


        initUi();


        setData(PrefUtils.getProfileDetailObject(context));


    }

    private void setData(ProfileDetailObject profileDetailObject) {


        if (profileDetailObject != null) {

            //==================================================================

            et_name.setText(profileDetailObject.getUsername());
            et_name.setSelection(et_name.getText().length());

            if (profileDetailObject.getUseremail().length() > 1) {
                et_email.setText(profileDetailObject.getUseremail());
                et_email.setSelection(et_email.getText().length());
            }
            if (profileDetailObject.getUsermobile().length() > 1) {
                et_phone.setText(profileDetailObject.getUsermobile());
                et_phone.setSelection(et_phone.getText().length());
            }
            if (profileDetailObject.getLocation().length() > 1) {
                et_location.setText(profileDetailObject.getLocation());
                et_location.setSelection(et_location.getText().length());
            }


            if (!profileDetailObject.getProfile_image_url().equalsIgnoreCase("")) {

                profileImage=profileDetailObject.getProfile_image_url().trim();
                try {
//                    Picasso.get()
//                            .load(profileDetailObject.getProfile_image_url().trim())
//                            .placeholder(R.mipmap.ic_app_icon)
//                            .error(R.mipmap.ic_app_icon)
//                            .noFade()
//                            .into(cIV_userImage);
                    Glide.with(this)
                            .load(profileDetailObject.getProfile_image_url().trim())

                            .apply(RequestOptions.circleCropTransform()) /*  .transform(new GlideCircleWithBorder(view.getContext(), 2, Color.parseColor("#005194")))*/
                            .placeholder(R.mipmap.ic_app_icon)
                            .into(cIV_userImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            //==============================
        }


    }

    private void initUi() {
        prefUtils= new PrefUtils();
          apiService = ApiFactory.getRetrofitInstance().create(ApiService .class);
        progressBar = findViewById(R.id.progressBar);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        cIV_userImage = findViewById(R.id.iv_profile_userImage);
        cIV_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditProfileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
       // cIV_userImage.setOnClickListener(this);
        et_name = findViewById(R.id.et_profile_name);

        et_email = findViewById(R.id.et_profile_email);
        et_phone = findViewById(R.id.et_profile_phone);
        et_location = findViewById(R.id.et_profile_location);
        rl_main = findViewById(R.id.rl_profile_layout);

        tv_editPhoto = findViewById(R.id.tv_editPhoto);
        tv_editPhoto.setOnClickListener(this);


        tv_buttonSave = findViewById(R.id.tv_buttonSave);
      //  tv_buttonSave.setOnClickListener(this);


        tv_buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEveryThingOk()) {

                //    updateProfileToServer();
                    updateDetails();

                }
            }
        });
//        changeEdittextBackground(et_name);
//        changeEdittextBackground(et_email);
//        changeEdittextBackground(et_phone);
//        changeEdittextBackground(et_location);

        et_email.setEnabled(false);


    }


    private void changeEdittextBackground(EditText editText) {
        /*For changing edittext default underline colour (default background)*/
        // get current EditText drawable
        Drawable drawable = editText.getBackground();
        drawable.setColorFilter(getResources().getColor(R.color.edittext_line_color), PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT > 16) {
            // set the new drawable to EditText
            editText.setBackground(drawable);
        } else {
            // use setBackgroundDrawable because setBackground required API 16
            editText.setBackgroundDrawable(drawable);
        }

        try {
            // changing edittext cursor colour
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, R.drawable.login_edittext_cursor_color);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_editPhoto:
          /*  case R.id.iv_profile_userImage:

                ImagePicker.pickImage(EditProfileActivity.this, "Select your image:");

                break;*/

/*
            case R.id.tv_buttonSave: {

              *//*  if (isEveryThingOk()) {

                    updateProfileToServer();


                }*//*


                break;
            }*/
            case R.id.ivBack: {

                onBackPressed();


                break;
            }
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


        if (et_phone.getText().toString().trim().length() == 0) {
            errorList.add("Enter your 10 digit mobile number");

        }

        if (et_location.getText().toString().trim().length() == 0) {
            errorList.add("Enter your location");

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
  //      Bitmap profileimgBitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        //  val resultUri: Uri = data?.data!!

        Uri resultUri = data.getData();

        profileImage = resultUri.getPath();
      //  cIV_userImage.setImageURI(resultUri);

        Glide.with(this)
                .load(resultUri)

                .apply(RequestOptions.circleCropTransform()) /*  .transform(new GlideCircleWithBorder(view.getContext(), 2, Color.parseColor("#005194")))*/
                .placeholder(R.mipmap.ic_app_icon)
                .into(cIV_userImage);
        Log.e("resultUri","--"+resultUri);
        Log.e("profile iMGE","--"+profileImage);



    }

    private File bitmapToFile() throws Exception {
        File file = new File(EditProfileActivity.this.getCacheDir(), "temp_picture.jpg");
        file.createNewFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        profileimgBitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos);
        byte[] bitmapData = bos.toByteArray();


        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

        return file;
    }

    private void updateProfileToServer() {
        try {
      //  updateProfileWithProfilePic(/*bitmapToFile(),*/ AppData.UPDATE_USER_PROFILE ,profileImage);


        } catch (Exception e) {
            e.printStackTrace();
        }
       /* if (!profileImage.isEmpty()) {
            try {
                updateProfileWithProfilePic(bitmapToFile(), AppData.UPDATE_USER_PROFILE ,profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new updateProfileWithOutProfilePic().execute();
        }*/

    }


    private void updateProfileWithProfilePic(/*File file, */String url,String profileImage) {
        final okhttp3.MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);



        if (profileImage != null) {
            if (!profileImage.startsWith("http")) {
                File file2 = new File(profileImage);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
           //     profile_image = MultipartBody.Part.createFormData("profile_image", file2.getName(), requestFile);

                builder.addFormDataPart("profileimage", file2.getName(),requestFile);


            }
        }
/*

        if (!profileImage.toString().startsWith("http")) {


            File fileProfile = new File(profileImage);


            Log.e("fileProfile","--"+fileProfile);

            if (fileProfile != null) {

                builder.addFormDataPart("profileimage", fileProfile.getName(), RequestBody.create(MEDIA_TYPE_PNG, fileProfile));

            }


         //   val file = File(mImagePth)
         */
/*   val requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
            mImageParts =
                    MultipartBody.Part.createFormData("profile_image", file.name, requestFile)*//*

        }
*/


        builder.addFormDataPart("u_id", PrefUtils.getProfileObject(context).getU_id());
        builder.addFormDataPart("username", et_name.getText().toString().trim());
        builder.addFormDataPart("useremail", et_email.getText().toString().trim());
        builder.addFormDataPart("usermobile", et_phone.getText().toString().trim());
        builder.addFormDataPart("location", et_location.getText().toString().trim());


        LogUtils.printf("I am Builder ====  " + builder.toString());
        new mAsyncTaskMultipart(EditProfileActivity.this, url, builder, new mAsyncTaskMultipart.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                LogUtils.printf("I am MultiPart OutPut " + output);
            }
        }).execute();
    }

    private void updateProfileData(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optInt("status") == 1) {

                JSONObject user = jsonObject.optJSONObject("userdetails");


                UserDataModel userDataModel = new UserDataModel();

                userDataModel.setU_id(user.optString("u_id"));
                userDataModel.setUsername(user.optString("username"));
                userDataModel.setUseremail(user.optString("useremail"));
                userDataModel.setProfile_image_url(user.optString("profile_image_url"));
                userDataModel.setProfileimage(user.optString("profile_image_url"));


                Log.e("Image from server " ,"--"+user.optString("profile_image_url"));
                PrefUtils.saveProfile(context, PrefUtils.getJsonFromObject(userDataModel, UserDataModel.class));


            }


            Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private class updateProfileWithOutProfilePic extends AsyncTask<Void, Void, String> {

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
            map.put("username", et_name.getText().toString().trim());
            map.put("useremail", et_email.getText().toString().trim());
            map.put("usermobile", et_phone.getText().toString().trim());
            map.put("location", et_location.getText().toString().trim());
            map.put("profileimage", "");


            JSONParser parser = new JSONParser();


            String mURL = AppData.UPDATE_USER_PROFILE;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", map);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am LogIn response: " + response);

            updateProfileData(response);


        }


    }




    private void updateDetails() {

        MultipartBody.Part profile_image = null;

        if (profileImage != null) {
            if (!profileImage.startsWith("http")) {
                File file2 = new File(profileImage);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
               profile_image = MultipartBody.Part.createFormData("profileimage", file2.getName(), requestFile);



            }
        }



        RequestBody u_id = RequestBody.create(MediaType.parse("multipart/form-data"), PrefUtils.getProfileObject(context).getU_id());
        RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), et_name.getText().toString().trim());
        RequestBody useremail = RequestBody.create(MediaType.parse("multipart/form-data"), et_email.getText().toString().trim());
        RequestBody usermobile = RequestBody.create(MediaType.parse("multipart/form-data"), et_phone.getText().toString().trim());
        RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), et_location.getText().toString().trim());


        progressBar.setVisibility(View.VISIBLE);
        apiService.updateUserProfile(u_id, username, useremail, usermobile, location,profile_image).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<ProfileUpdateRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ProfileUpdateRes res) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context,res.getMessage(), Toast.LENGTH_SHORT).show();
                         if (res.getStatus().equals("1")) {
                             UserDataModel userDataModel = new UserDataModel();

                             userDataModel.setU_id(res.getUserdetails().getuId() );
                             userDataModel.setUsername(res.getUserdetails().getUsername() );
                             userDataModel.setUseremail(res.getUserdetails().getUseremail() );
                             userDataModel.setProfile_image_url(res.getUserdetails().getProfileImageUrl() );
                             userDataModel.setProfileimage(res.getUserdetails().getProfileImageUrl() );



                             PrefUtils.saveProfile(context, PrefUtils.getJsonFromObject(userDataModel, UserDataModel.class));
                           /* if (res.getData().getProfileImage() != null) {
                                setProfilepicsession(res.getData().getProfileImage());
                            }*/
                             finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err"," in profile--"+e.toString());
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

}
