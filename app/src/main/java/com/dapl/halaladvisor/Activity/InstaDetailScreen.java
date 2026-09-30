package com.dapl.halaladvisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;

public class InstaDetailScreen extends AppCompatActivity {


    ImageView ivInstaPic;
    ImageView ivBack;
    TextView tvCaption;
    TextView tvPermaLink;
    TextView tvViewOnInsta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_detail_screen);

        initViews();
        getDataFromIntent();
    }


    public void initViews() {
        ivInstaPic = findViewById(R.id.ivInstaPic);
        tvViewOnInsta = findViewById(R.id.tvViewOnInsta);
        tvPermaLink = findViewById(R.id.tvPermaLink);
        tvCaption = findViewById(R.id.tvCaption);
        ivBack = findViewById(R.id.ivBack);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


    }

    public void getDataFromIntent() {


        if (getIntent().hasExtra("image")) {
            Utils.imageWithGlide(ivInstaPic, getIntent().getStringExtra("image"));

            tvCaption.setText(getIntent().getStringExtra("caption"));

/*
            if (getIntent().getStringExtra("media_type").equals("CAROUSEL_ALBUM")) {

                //  tvPermaLink.setVisibility(View.VISIBLE);


                //   tvPermaLink.setText(getIntent().getStringExtra("permalink"));

            } else {
                tvPermaLink.setVisibility(View.GONE);
            }*/

            tvViewOnInsta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

Log.e("link","---"+getIntent().getStringExtra("permalink"));
                    if (getIntent().getStringExtra("permalink") != null && !getIntent().getStringExtra("permalink").isEmpty()
                            && !getIntent().getStringExtra("permalink").equals("")
                            && !getIntent().getStringExtra("permalink").equals("null")) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra("permalink")));
//                        startActivity(browserIntent);
                        Uri uri = Uri.parse(getIntent().getStringExtra("permalink"));
                        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                        likeIng.setPackage("com.instagram.android");

                        try {
                            startActivity(likeIng);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(getIntent().getStringExtra("permalink"))));
                        }
                    } else {
                        Toast.makeText(InstaDetailScreen.this, "Post Not Available", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }


    }
}