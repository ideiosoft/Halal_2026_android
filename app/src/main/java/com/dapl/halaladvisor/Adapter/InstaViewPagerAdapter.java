package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.dapl.halaladvisor.Activity.AboutWebScreen;
import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.ViewRestVoucherScreen;
import com.dapl.halaladvisor.Model.featuredimages.Datum;
import com.dapl.halaladvisor.R;

import java.util.ArrayList;

import static com.dapl.halaladvisor.Utility.Utils.imageWithGlide;

public class InstaViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Context context;
    String baseurl="";
    ArrayList<Datum> imagesList;

    public InstaViewPagerAdapter(ArrayList<Datum> imagesList, Context context,String baseurl) {
        this.imagesList = imagesList;
        this.context = context;
        this.baseurl=baseurl;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_popup, container, false);


        try {
            imageWithGlide(view.findViewById(R.id.ivPopup), baseurl+imagesList.get(position).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.ivPopup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(imagesList.get(position).getRes_id().equalsIgnoreCase("")||imagesList.get(position).getRes_id().equalsIgnoreCase("0"))
                {
                    context.startActivity(new Intent(context, AboutWebScreen.class).
                            putExtra("about",imagesList.get(position).getRes_url())
                            . putExtra("type","Details")
                    );

                  //  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imagesList.get(position).getRes_url()));
                  //  context.startActivity(browserIntent);
                }
                else {
                    context.startActivity(new Intent(context, DetailsActivity.class)
                            .putExtra("rid", imagesList.get(position).getRes_id().toString())
                            .putExtra("from", "0")


                    );
                }
            }
        });





        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}