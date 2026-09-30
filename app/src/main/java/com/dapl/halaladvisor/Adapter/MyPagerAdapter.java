package com.dapl.halaladvisor.Adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Fragment.AddressFragment;
import com.dapl.halaladvisor.Fragment.GalleryFragment;
import com.dapl.halaladvisor.Fragment.HoursFragment;
import com.dapl.halaladvisor.Fragment.MenuFragment;
import com.dapl.halaladvisor.Fragment.OfferFragment;
import com.dapl.halaladvisor.Fragment.ReviewFragment;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 5;
    Context mcontext;
    String address;
    RestaurantDetailObject restDetailsModel;

    public MyPagerAdapter(Context context, FragmentManager fragmentManager, String address, RestaurantDetailObject restDetailsModel) {
        super(fragmentManager);
        this.address = address;
        this.restDetailsModel = restDetailsModel;
        mcontext = context;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment


                return new ReviewFragment(restDetailsModel);
            //   return new AddressFragment(address,restDetailsModel);
            case 1: // Fragment # 0 - This will show FirstFragment different title


                return new MenuFragment(restDetailsModel);
            case 2: // Fragment # 1 - This will show SecondFragment

                 return new OfferFragment(restDetailsModel);
            case 3: // Fragment # 1 - This will show SecondFragment

                 return new GalleryFragment(restDetailsModel);
            case 4: // Fragment # 1 - This will show SecondFragment

                 return new HoursFragment(restDetailsModel);


            default:
                return null;
        }
    }



}

