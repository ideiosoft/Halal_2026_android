package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dapl.halaladvisor.Activity.DroidWebViewActivity;
import com.dapl.halaladvisor.Model.RestaurantBannerListModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.extra.AllConstants;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    ArrayList<RestaurantBannerListModel> images;

    // Layout Inflater
    LayoutInflater mLayoutInflater;


    // Viewpager Constructor
    public ViewPagerAdapter(Context context, ArrayList<RestaurantBannerListModel> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllConstants.webUrl = images.get(position).resUrl;

                Intent i=new Intent(context, DroidWebViewActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        });
        // setting the image in the imageView
        //  imageView.setImageResource(images[position]);
        Glide.with(context)
                .load(images.get(position).image)

                //  .apply(RequestOptions.circleCropTransform()) /*  .transform(new GlideCircleWithBorder(view.getContext(), 2, Color.parseColor("#005194")))*/
                .placeholder(R.drawable.list_placeholder)
                .into(imageView);
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}