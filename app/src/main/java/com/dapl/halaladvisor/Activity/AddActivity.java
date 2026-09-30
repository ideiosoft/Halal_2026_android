package com.dapl.halaladvisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dapl.halaladvisor.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.util.Arrays;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class AddActivity extends AppCompatActivity {
AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

adView = findViewById(R.id.ad_view);




        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(this) ;

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
      /*  MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
                        .setTestDeviceIds(listOf("ABCDEF012345"))
                        .build()
        );*/



        AdRequest adRequest= new AdRequest.Builder().build();
        // Create an ad request.


        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }


    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();

    }


    @Override
    protected void onResume() {
        adView.pause();
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        adView.pause();
        super.onDestroy();

    }


}

       /* MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);
        RequestConfiguration configuration = new RequestConfiguration.Builder()
                .setTestDeviceIds(Arrays.asList("96EA4D3BEA9BFECA4EFCAFDD7B870BA5")).build();
        MobileAds.setRequestConfiguration(configuration);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);




        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                Log.e("loaded"," in ads---");

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.

                Log.e("error"," in ads---"+adError);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }


}*/