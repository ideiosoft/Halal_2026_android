package com.dapl.halaladvisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Model.addreviewres.AddReviewRes;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.AlertDialogManager;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.dapl.halaladvisor.Utility.Utils.convertDateFormat;

public class ViewRestVoucherScreen extends AppCompatActivity {
    TextView tvRestName, tvMinimumSpend;
    TextView tv_offer_heading, tvValidity, tvLocate, tvMenu, tvCall;
    String lat = "";
    String lng = "";
    ImageView ivClose;
    ApiService apiService;
    private String str_contact_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant_screen);
        initUi();
    }

    public void initUi() {
        tv_offer_heading = findViewById(R.id.tv_offer_heading);
        tvRestName = findViewById(R.id.tv_main);
        tvValidity = findViewById(R.id.tvValidity);
        tvMinimumSpend = findViewById(R.id.tvMinimumSpend);
        ivClose = findViewById(R.id.ivClose);
        tvLocate = findViewById(R.id.tvLocate);
        tvMenu = findViewById(R.id.tvMenu);
        tvCall = findViewById(R.id.tvCall);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);
        //set text
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");



        if(getIntent().getStringExtra("price").contains("%")){

            tv_offer_heading.setText(getIntent().getStringExtra("price") + " Off");
        }else {

            tv_offer_heading.setText(getIntent().getStringExtra("price") + " Off");
        }



        Log.e("minorder", "---" + getIntent().getStringExtra("minorder"));


        if (getIntent().getStringExtra("minorder").equals("null") || getIntent().getStringExtra("minorder").isEmpty()) {
            tvMinimumSpend.setText("in conjunction with other offers.");
        } else {
            tvMinimumSpend.setText("in conjunction with other offers. $" + getIntent().getStringExtra("minorder") + " minimum spend.");
        }



        tvRestName.setText(getIntent().getStringExtra("rname"));
        tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewRestVoucherScreen.this, DetailsActivity.class)
                        .putExtra("rid", getIntent().getStringExtra("rid"))
                        .putExtra("from", "1")


                );
            }
        });


        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trackActivity("call_button");

                LogUtils.printf("I am number: " + getIntent().getStringExtra("phone"));
                str_contact_no = getIntent().getStringExtra("phone");
                if (isValidMobile(str_contact_no)) {
                    dialNumber(str_contact_no);
                } else {
                    Toast.makeText(ViewRestVoucherScreen.this, "Not Available", Toast.LENGTH_SHORT).show();
                }
            }

        });


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try {
            tvValidity.setText("The voucher is valid till " + convertDateFormat(getIntent().getStringExtra("enddate")));
        } catch (Exception e) {
            e.printStackTrace();
        }


        tvLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackActivity("get_direction");

                AllConstants.lat = String.valueOf(lat);
                AllConstants.lng = String.valueOf(lng);
                //startActivityForResult(new Intent(context,LocationPickerActivity.class),    REQ_LOCATION_FETCH );
                if (AllConstants.UPlat != null && AllConstants.UPlng != null && AllConstants.lat != null && AllConstants.lng != null) {


                    startActivity(new Intent(ViewRestVoucherScreen.this, DirectionActivity.class));
                } else {
                    AlertDialogManager.showAlertDialog(ViewRestVoucherScreen.this, "Alert", "An error occurred, please try again later.", false, false);
                }
            }
        });
    }


    private boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    private void dialNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);

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
                PrefUtils.getProfileObject(ViewRestVoucherScreen.this).getU_id(), getCurrentTimestamp(), getCurrentMonth(),"").
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

}