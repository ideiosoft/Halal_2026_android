package com.dapl.halaladvisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Model.getofferres.GetOfferListingRes;
import com.dapl.halaladvisor.Model.offerredeemres.OfferRedeemRes;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.ApiFactory;
import com.dapl.halaladvisor.extra.ApiService;
import com.dapl.halaladvisor.interfaces.RedeemOfferInterface;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OfferRedeemScreen extends AppCompatActivity {
    TextView tvRedeem, tv_offer_heading, tvRestName, tvDes, tvRedeemSteps,tv_reg_cancel;
    ProgressBar progressBar;
    ApiService apiService;
    String id = "";
    EditText edName, edEmail, edPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_redeem_screen);
        initUi();

    }


    public void getDataFromIntent() {

               /* .putExtra("redeem_steps", arrayList.get(position).getHowToRedeem())



                ;*/


        try {
            if (getIntent().getStringExtra("price").contains("%")) {
                tv_offer_heading.setText(getIntent().getStringExtra("price") + " OFF");
            } else {
                tv_offer_heading.setText(getIntent().getStringExtra("price") + "% OFF");
            }



            tvRestName.setText(getIntent().getStringExtra("r_name"));
            if (getIntent().getStringExtra("des").isEmpty()) {
                tvDes.setVisibility(View.GONE);


            } else {
                tvDes.setText(getIntent().getStringExtra("des"));
                tvDes.setVisibility(View.VISIBLE);
            }
            id = getIntent().getStringExtra("id");
            tvRedeemSteps.setText(getIntent().getStringExtra("redeem_steps"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void redeemOffer(String offer_id, String user_id, String email) {
        progressBar.setVisibility(View.VISIBLE);
        apiService.redeemOfferApi(offer_id, user_id,email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<OfferRedeemRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OfferRedeemRes res) {
                        progressBar.setVisibility(View.GONE);
                        if (res.isStatus()) {
                            Toast.makeText(OfferRedeemScreen.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OfferRedeemScreen.this,
                                    CongratulationsScreen.class)
                                    .putExtra("r_name", res.getOtherDetails().getrName().toString())
                                    .putExtra("discount", res.getOtherDetails().getOfferPrice())


                            );

                            finish();

                        } else {
                            Toast.makeText(OfferRedeemScreen.this, res.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void initUi() {
        tvRedeem = findViewById(R.id.tvRedeem);
        tv_reg_cancel = findViewById(R.id.tv_reg_cancel);
        progressBar = findViewById(R.id.progressBar);
        tvRedeemSteps = findViewById(R.id.tvRedeemSteps);
        apiService = ApiFactory.getRetrofitInstance().create(ApiService.class);

        tv_offer_heading = findViewById(R.id.tv_offer_heading);
        tvRestName = findViewById(R.id.tvRestName);
        tvDes = findViewById(R.id.tvDes);
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edPhone = findViewById(R.id.edPhone);

        getDataFromIntent();




        tv_reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edName.getText().toString().isEmpty()) {
                    Toast.makeText(OfferRedeemScreen.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                } else if (edEmail.getText().toString().isEmpty()) {
                    Toast.makeText(OfferRedeemScreen.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else if (edPhone.getText().toString().isEmpty()) {
                    Toast.makeText(OfferRedeemScreen.this, "Please enter your contact number", Toast.LENGTH_SHORT).show();
                } else {

                    redeemOffer(id, PrefUtils.getProfileObject(OfferRedeemScreen.this).getU_id(),edEmail.getText().toString());
                }
                // startActivity(new Intent(OfferRedeemScreen.this, CongratulationsScreen.class));
            }
        });
    }


}