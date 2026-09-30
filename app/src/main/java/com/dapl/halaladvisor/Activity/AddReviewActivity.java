package com.dapl.halaladvisor.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Adapter.ImagesAdapter;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.MainActivity;
import com.dapl.halaladvisor.Model.DeleteImageRes;
import com.dapl.halaladvisor.Model.ImageModel;
import com.dapl.halaladvisor.Model.RestaurentReviewObject;
import com.dapl.halaladvisor.Model.ReviewObject;
import com.dapl.halaladvisor.Model.UserDataModel;
import com.dapl.halaladvisor.Model.addreviewres.AddReviewRes;
import com.dapl.halaladvisor.Model.fetchreview.FetchReviewRes;
import com.dapl.halaladvisor.Model.fetchreview.Image;
import com.dapl.halaladvisor.Model.getmenures.GetMenuRes;
import com.dapl.halaladvisor.Model.profileupdateres.ProfileUpdateRes;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.AlertDialogManager;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.extra.AppController;
import com.dapl.halaladvisor.interfaces.DeleteReviewImageInterface;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener, DeleteReviewImageInterface {
    Context context;
    RelativeLayout rlAdd;
    RecyclerView imagesRecyclerView;
    ImagesAdapter imagesAdapter;
    private RatingBar ratingBar_addReview;
    private EditText et_reviewDetails;
    private TextView tv_cancel, tvTitle;
    private TextView tv_addReview_rating, tv_addReview_charCount, tv_buttonPublish;
    private int textCount = 0;
    private String mRating_id = "";
    ProgressBar progressBar;
    String rid = "";
    String review_id = "";
    String name = "";

    Image image = null;
    ApiService apiService;
    ArrayList<Image> imageslist = new ArrayList<>();
    private String edit_userRating = "";
    private String edit_userPost = "";
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textCount = s.length();
            tv_addReview_charCount.setText(textCount + "");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            textCount = s.length();
            tv_addReview_charCount.setText(textCount + "");
            Log.e("AddReview: ", "textChange after- " + textCount);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        context = this;
        initViews();
        fetchReviewRes();
        setDataForExistingUserReview();
    }

    private void initViews() {


        if(getIntent().hasExtra("noti_type"))
        {
            Log.e("from noti","yes");

        }
        else {

            Log.e("from noti","no");
        }

        if (getIntent().hasExtra("rid")) {
            Log.e("review","1111");
            rid = getIntent().getStringExtra("rid");
            name = getIntent().getStringExtra("name");
        } else if(AppController.getSelectedRestaurent()!=null){
            Log.e("review","22222");
                rid = AppController.getSelectedRestaurent().getId();

                if(rid.equals(""))
                {
                    rid=PrefUtils.getResId(AddReviewActivity.this);
                }
                name = AppController.getSelectedRestaurent().getName();

                Log.e("nameee",name+"");
                Log.e("rid",rid+"");
            }
        else if(AppController.getSelectedRestaurantDetailObject()!=null){
            Log.e("review","3333");
                rid = AppController.getSelectedRestaurantDetailObject().getId();
                name = AppController.getSelectedRestaurantDetailObject().getName();
            }
        else {
            Log.e("dddddd",""+PrefUtils.getResId(AddReviewActivity.this));
            rid=PrefUtils.getResId(AddReviewActivity.this);

        }


        mappingViews();


        onEdittextChangedListener();

        onRatingBarChanged();

        changeEdittextBackground(et_reviewDetails);


    }

    /*Mapping all views*/
    private void mappingViews() {
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        progressBar = findViewById(R.id.progressBar);

        tvTitle = findViewById(R.id.tvTitle);
        imagesRecyclerView = findViewById(R.id.imagesRecyclerView);
        rlAdd = findViewById(R.id.rlAdd);
        tvTitle.setText(/*AppController.getSelectedRestaurent().getName()*/ name);


        ratingBar_addReview = findViewById(R.id.ratingbar_addReview);
        et_reviewDetails = findViewById(R.id.et_addReview_details);


        tv_addReview_rating = findViewById(R.id.tv_addReview_rating);

        tv_addReview_charCount = findViewById(R.id.tv_addReview_charCount);
        tv_buttonPublish = findViewById(R.id.tv_buttonPublish);

        tv_cancel = findViewById(R.id.tvCancel);
        tv_cancel.setOnClickListener(this);

        et_reviewDetails.addTextChangedListener(watcher);
        onClickEvents();


        setImagesAdapter();
    }

    private void onClickEvents() {


        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageslist.size() == 5) {
                    Toast.makeText(AddReviewActivity.this, "You can add only 5 images", Toast.LENGTH_SHORT).show();
                } else {

                    ImagePicker.with(AddReviewActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            .crop(512f, 1024f)//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(
                                    1080,
                                    1080
                            )    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();


                }
            }
        });
        tv_cancel.setOnClickListener(this);
        tv_buttonPublish.setOnClickListener(this);
//        rl_main.setOnClickListener(this);

    }


    public void setImagesAdapter() {
        imagesAdapter = new ImagesAdapter(AddReviewActivity.this, imageslist, this, review_id);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(AddReviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        imagesRecyclerView.setLayoutManager(linearLayoutManager);
        imagesRecyclerView.setAdapter(imagesAdapter);


    }




    /*Getting Intent data from ReviewNewActivity Class*/

    /*RatingBar Change Listener*/
    private void onRatingBarChanged() {
        ratingBar_addReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tv_addReview_rating.setText(rating + "");
            }
        });
    }

    /*Edittext Change Listener*/
    private void onEdittextChangedListener() {

    }

    /*OnClick Menu Items
     * i.e. onClick for back Icon*/
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            //val resultUri:Uri = data ?.data !!


            Uri uri = data.getData();
            if (imageslist.size() == 5) {
                Toast.makeText(AddReviewActivity.this, "You can add only 5 images", Toast.LENGTH_SHORT).show();
                rlAdd.setVisibility(View.GONE);
            } else {
                rlAdd.setVisibility(View.VISIBLE);

                Image imageModel = new Image("", uri.getPath());
                imageslist.add(imageModel);

                // imageslist.add(uri.getPath());
                if (imageslist.size() == 4) {
                    rlAdd.setVisibility(View.GONE);
                }
                imagesAdapter.notifyDataSetChanged();
            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_buttonPublish:

                String str_rating = String.valueOf(ratingBar_addReview.getRating());
                String str_review = et_reviewDetails.getText().toString().trim();

                if (isValidToPost(str_rating, str_review)) {
                    addReviewWithImages(str_rating, str_review);
                    //   new ReqAddReview(str_rating, str_review).execute();
                }

                break;

            case R.id.tvCancel:

                if(AppController.getSelectedRestaurent()==null){

                    startActivity(new Intent(AddReviewActivity.this, DetailsActivity.class)
                            .putExtra("rid", rid)
                            .putExtra("new_type", rid)



                    );
                  //  startActivity(new Intent(AddReviewActivity.this, MainActivity.class));
                    finishAffinity();
                }
                else {
                    finish();
                }
                break;

        }
    }


    /*Checks Validation for review fields*/
    private boolean isValidToPost(String rating, String review) {

        boolean valid_fields = true;
        float sRating = Float.parseFloat(rating);

        if (review.equalsIgnoreCase("")) {
            et_reviewDetails.setError("Review empty");

            valid_fields = false;
        }
        if (sRating == 0.0) {
            Toast.makeText(AddReviewActivity.this, "Give your Rating", Toast.LENGTH_SHORT).show();
            valid_fields = false;
        }

        return valid_fields;
    }


    /*For changing edittext default underline color
     * and changing edittext default cursor colour
     * */


    private void changeEdittextBackground(EditText editText) {

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
    public void onBackPressed() {
        if(AppController.getSelectedRestaurent()!=null){

            startActivity(new Intent(AddReviewActivity.this, DetailsActivity.class)
                    .putExtra("rid", rid)
                    .putExtra("new_type", rid)



            );

           // startActivity(new Intent(AddReviewActivity.this, MainActivity.class));
       finishAffinity();
        }
        else {
            finish();
        }
    }

    public void setDataForExistingUserReview() {

        boolean userFound = false;

        try {

            JSONArray array = new JSONArray(PrefUtils.getAllReviewsFromReviewListScreen(context));

            for (int i = 0; i < array.length(); i++) {

                ReviewObject reviewObject = (ReviewObject) PrefUtils.getObjectFromJson(array.getJSONObject(i).toString().trim(), ReviewObject.class);

                if (reviewObject.getU_id().equalsIgnoreCase(PrefUtils.getProfileObject(context).getU_id())) {

                    userFound = true;


                    edit_userRating = reviewObject.getRating();
                    if (!edit_userRating.equalsIgnoreCase("") && !edit_userRating.equals(null)) {
                        ratingBar_addReview.setRating(Float.parseFloat(edit_userRating));
                        /*set rating count*/
                        tv_addReview_rating.setText(edit_userRating + "");
                    } else {
                        ratingBar_addReview.setRating(0);
                        /*set rating count*/
                        tv_addReview_rating.setText("0.0");
                    }

                    /*set review post*/
                    edit_userPost = reviewObject.getReview();
                    et_reviewDetails.setText(edit_userPost);

                    textCount = et_reviewDetails.getText().length();
                    et_reviewDetails.setSelection(textCount);
                    tv_addReview_charCount.setText(textCount + "");

                    break;
                } else {

                    userFound = false;
                }


            }

            //====================================
            if (userFound) {


                JSONArray arrayDetail = new JSONArray(PrefUtils.getAllReviewsFromDetailScreen(context));

                for (int i = 0; i < array.length(); i++) {

                    RestaurentReviewObject reviewObject = (RestaurentReviewObject) PrefUtils.getObjectFromJson(array.getJSONObject(i).toString().trim(), RestaurentReviewObject.class);

                    if (reviewObject.getU_id().equalsIgnoreCase(PrefUtils.getProfileObject(context).getU_id())) {

                        userFound = true;


                        edit_userRating = reviewObject.getRating();
                        if (!edit_userRating.equalsIgnoreCase("") && !edit_userRating.equals(null)) {
                            ratingBar_addReview.setRating(Float.parseFloat(edit_userRating));
                            /*set rating count*/
                            tv_addReview_rating.setText(edit_userRating + "");
                        } else {
                            ratingBar_addReview.setRating(0);
                            /*set rating count*/
                            tv_addReview_rating.setText("0.0");
                        }

                        /*set review post*/
                        edit_userPost = reviewObject.getReview();
                        et_reviewDetails.setText(edit_userPost);

                        textCount = et_reviewDetails.getText().length();
                        et_reviewDetails.setSelection(textCount);
                        tv_addReview_charCount.setText(textCount + "");

                        break;
                    } else {

                        userFound = false;
                    }


                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(String imageId, String rid, Image image) {
        this.image = image;


        if (image.getReviewImages().contains("http")){
            deleteReviewImage(review_id, imageId);
        }else {
            imageslist.remove(image);
            imagesAdapter.notifyDataSetChanged();
        }







    }

    private class ReqAddReview extends AsyncTask<Void, Void, String> {

        String response;

        ProgressDialog progressDialog;

        String rating;

        String strReview;

        public ReqAddReview(String rating, String strReview) {
            this.rating = rating;
            this.strReview = strReview;
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


            HashMap<String, String> params = new HashMap<String, String>();
            params.put("res_id", /*AppController.getSelectedRestaurent().getId()*/ rid);
            params.put("userid", PrefUtils.getProfileObject(context).getU_id());
            params.put("rating_count", rating);
            params.put("review", strReview);
            params.put("rating_id", mRating_id);


            JSONParser parser = new JSONParser();


            String mURL = AppData.ADD_EDIT_REVIEWS_URL;

            LogUtils.printf("I am url: " + params);


            response = parser.makeHttpRequest(mURL, "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Details response: " + response);


            try {

                JSONObject obj = new JSONObject(response);


                if (obj.optString("status").equalsIgnoreCase("1")) {
                    String msg = obj.optString("message");
                    AlertDialogManager.showAlertDialog(AddReviewActivity.this, "", msg, false, true);
                } else {
                    String msg = obj.optString("message");
                    AlertDialogManager.showAlertDialog(AddReviewActivity.this, "", msg + ", Please edit your review", false, true);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


    private void fetchReviewRes() {

        progressBar.setVisibility(View.VISIBLE);
        apiService.fetchReview(rid, PrefUtils.getProfileObject(context).getU_id()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<FetchReviewRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FetchReviewRes res) {
                        Log.e("res", " res--" + res);
                        progressBar.setVisibility(View.GONE);



                        if (res.getStatus()) {
                            mRating_id = res.getResult().getRatingId();
                            //  tv_addReview_rating.setText(res.getResult().getRating() + "");
                            et_reviewDetails.setText(res.getResult().getReview() + "");


                            edit_userRating = res.getResult().getRating();
                            if (!edit_userRating.equalsIgnoreCase("") && !edit_userRating.equals(null)) {
                                ratingBar_addReview.setRating(Float.parseFloat(edit_userRating));
                                /*set rating count*/
                                tv_addReview_rating.setText(edit_userRating + "");
                            } else {
                                ratingBar_addReview.setRating(0);
                                /*set rating count*/
                                tv_addReview_rating.setText("0.0");
                            }


                            imageslist.addAll(res.getResult().getImages());


                            review_id = res.getResult().getRatingId();
                            imagesAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err", " in profile--" + e.toString());
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void deleteReviewImage(String review_id, String imageid) {

        apiService.deleteReviewImage(review_id, imageid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<DeleteImageRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DeleteImageRes res) {
                        Log.e("res", " res--" + res);


                        if (res.getStatus()){
                            imageslist.remove(image);
                            imagesAdapter.notifyDataSetChanged();
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

    private void addReviewWithImages(String rating, String strReview) {


        RequestBody res_id = RequestBody.create(MediaType.parse("multipart/form-data"), /*AppController.getSelectedRestaurent().getId()*/ rid);
        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), PrefUtils.getProfileObject(context).getU_id());
        RequestBody rating_count = RequestBody.create(MediaType.parse("multipart/form-data"), rating);
        RequestBody review = RequestBody.create(MediaType.parse("multipart/form-data"), strReview);
        RequestBody rating_id = RequestBody.create(MediaType.parse("multipart/form-data"), mRating_id);

        /**/
        ArrayList<MultipartBody.Part> mArrayListImages = new ArrayList<>();

        MultipartBody.Part mImageParts = null;
        for (int i = 0; i < imageslist.size(); i++) {


            if (!imageslist.get(i).toString().contains("http")) {


                if (!TextUtils.isEmpty(imageslist.get(i).getReviewImages())) {


                    File file = new File(imageslist.get(i).getReviewImages());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    mImageParts = MultipartBody.Part.createFormData("review_images[]", file.getName(), requestBody);


                    mArrayListImages.add(mImageParts);
                }
            }
        }


        progressBar.setVisibility(View.VISIBLE);
        apiService.addReviewWithImages(res_id, userid, rating_count, review, rating_id, mArrayListImages).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<AddReviewRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AddReviewRes response) {
                        progressBar.setVisibility(View.GONE);

                        LogUtils.printf("I am Details response: " + response);


                        try {

                            // JSONObject obj = new JSONObject(response);


                            if (response.getStatus().equalsIgnoreCase("1")) {


                                String msg = response.getMessage();
                                AlertDialogManager.showAlertDialog(AddReviewActivity.this, "", msg, false, true);
                            } else {
                                String msg = response.getMessage();
                                AlertDialogManager.showAlertDialog(AddReviewActivity.this, "", msg + ", Please edit your review", false, true);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err", " in profile--" + e.toString());
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


}
