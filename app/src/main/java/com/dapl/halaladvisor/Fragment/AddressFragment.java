package com.dapl.halaladvisor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Activity.DirectionActivity;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.extra.AlertDialogManager;
import com.dapl.halaladvisor.extra.AllConstants;

 import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;

import static com.google.android.gms.common.util.CollectionUtils.listOf;


public class AddressFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    TextView tv_address;
    TextView tvMsg;
     RelativeLayout navigateAddress;
    MapView map_restaurant;
    String address = "";
    double lat;
    private Context context;
    GoogleMap googleMap;
    double lng;

    RestaurantDetailObject restDetailsModel;

    public AddressFragment(String address, RestaurantDetailObject restDetailsModel) {
        this.address = address;
        this.restDetailsModel = restDetailsModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        //init ui
        initUi(view ,savedInstanceState);

      //  initAds(view);
        return view;
    }

   /* public  void initAds(View view){
        adView = view.findViewById(R.id.ad_view);

        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(context) ;

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
       MobileAds.setRequestConfiguration(new RequestConfiguration.Builder()
               // .setTestDeviceIds(listOf("8158E5D3B1EB35D6983BD8FAF1A58DDC"))
                .build()
        );



        AdRequest adRequest= new AdRequest.Builder().build();
        // Create an ad request.


        // Start loading the ad in the background.
       adView.loadAd(adRequest);
    }*/





 /*   @Override
    protected void onPause() {
        adView.pause();
        super.onPause();

    }



    @Override
    protected void onDestroy() {

        map_restaurant.onResume();
        adView.pause();
        super.onDestroy();

    }
*/



    @Override
    public void onResume() {
         super.onResume();

        map_restaurant.onResume();


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_restaurant.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        map_restaurant.onSaveInstanceState(outState);
    }

    private void initMap(Bundle savedInstanceState, View view) {


        map_restaurant = view.findViewById(R.id.map_restaurant);
        map_restaurant.onCreate(savedInstanceState);
        map_restaurant.getMapAsync(this);
    }

    public void initUi(View view,Bundle savedInstanceState) {

        tv_address = view.findViewById(R.id.tv_address);
        tvMsg = view.findViewById(R.id.tvMsgg);

        navigateAddress = view.findViewById(R.id.navigateAddress);

        //set text
        tv_address.setText(restDetailsModel.getAddress());

        initMap(savedInstanceState, view);

        clicks();

    }


    public void clicks() {
        navigateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllConstants.lat = String.valueOf(lat);
                AllConstants.lng = String.valueOf(lng);
                //startActivityForResult(new Intent(context,LocationPickerActivity.class),    REQ_LOCATION_FETCH );
                if (AllConstants.UPlat != null && AllConstants.UPlng != null && AllConstants.lat != null && AllConstants.lng != null) {


                    startActivity(new Intent(context, DirectionActivity.class));
                } else {
                    AlertDialogManager.showAlertDialog(context, "Alert", "An error occurred, please try again later.", false, false);
                }
            }
        });
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMapLoadedCallback(this);
        try {
            if (map_restaurant != null) {

                lat = Double.parseDouble(restDetailsModel.getLatitude());
                lng = Double.parseDouble(restDetailsModel.getLongitude());
                //googleMap = map_restaurant.getMap();
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.new_image_location_pin))
                        .anchor(0.0f, 1.0f)
                        .position(new LatLng(lat, lng)));
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                googleMap.getUiSettings().setZoomControlsEnabled(false);
                MapsInitializer.initialize(context);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(lat, lng));
                LatLngBounds bounds = builder.build();
                int padding = 0;
                // Updates the location and zoom of the MapView
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                //googleMap.moveCamera(cameraUpdate);
                googleMap.setMaxZoomPreference(14.0f);
                googleMap.animateCamera(cameraUpdate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}