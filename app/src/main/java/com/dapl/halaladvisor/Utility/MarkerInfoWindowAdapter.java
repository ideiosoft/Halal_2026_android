package com.dapl.halaladvisor.Utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.Model.fetchreview.Image;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.interfaces.MapViewChangeListner;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    RestaurantListModel restaurentList;
    MapViewChangeListner mapViewChangeListner;

    public MarkerInfoWindowAdapter(Context context, RestaurantListModel restaurentList, MapViewChangeListner mapViewChangeListner) {
        this.context = context.getApplicationContext();
        this.restaurentList = restaurentList;
        this.mapViewChangeListner = mapViewChangeListner;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.info_window, null );
         v.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_location_area));
        final RestaurantListModel user = (RestaurantListModel) marker.getTag();
        ImageView image = (ImageView) v.findViewById(R.id.image);
        TextView tvClose = (TextView) v.findViewById(R.id.tvClose);
        TextView tvPlaceName = (TextView) v.findViewById(R.id.tvPlaceName);
        TextView tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        TextView tvPhoneVal = (TextView) v.findViewById(R.id.tvPhoneVal);
        TextView tvDirection = (TextView) v.findViewById(R.id.tvDirection);
        TextView tvStreetView = (TextView) v.findViewById(R.id.tvStreetView);


        tvPlaceName.setText(user.getName());
        tvAddress.setText(user.getAddress());
        tvPhoneVal.setText(user.getR_contact());

         image.setImageBitmap(user.getBitmap());
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                marker.hideInfoWindow();
            }
        });


        tvDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.latitude != null) {
                    Uri uri = Uri.parse("google.navigation:q=" + user.latitude + "," + user.longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }

            }
        });

        tvStreetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mapViewChangeListner.onClick();
            }
        });


        return v;
    }
    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        private Bitmap bitmap;

        public GetImageFromUrl(ImageView img ){
            this.imageView = img;

        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {

                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                 bitmap= Bitmap.createScaledBitmap(bitmap, 80, 80, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);

            //imageView.setImageBitmap(bitmap);
        }
    }
    @Override
    public View getInfoContents(final Marker marker) {


        return  null;
    }

}