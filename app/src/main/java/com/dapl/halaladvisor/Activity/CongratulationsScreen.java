package com.dapl.halaladvisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dapl.halaladvisor.R;

public class CongratulationsScreen extends AppCompatActivity {
    TextView tv_offer_heading, tvRestName, tvGoToV,tv_reg_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations_screen);
        initViews();


        getDataFromIntent();
    }


    public void getDataFromIntent() {

        try {
            tvRestName.setText(getIntent().getStringExtra("r_name"));

            if (getIntent().getStringExtra("discount").contains("%")){
                tv_offer_heading.setText(getIntent().getStringExtra("discount") + " OFF");
            }else {
                tv_offer_heading.setText(getIntent().getStringExtra("discount") + "% OFF");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void initViews() {
        tv_offer_heading = findViewById(R.id.tv_offer_heading);
        tv_reg_cancel = findViewById(R.id.tv_reg_cancel);
        tvRestName = findViewById(R.id.tvRestName);
        tvGoToV = findViewById(R.id.tvGoToV);


        tvGoToV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CongratulationsScreen.this, VoucharActivity.class));
                finish();
            }
        });



        tv_reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}