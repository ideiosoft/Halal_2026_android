package com.dapl.halaladvisor.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dapl.halaladvisor.Model.getgalleryimagesres.Result;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.RedeemClickInteface;

import java.util.ArrayList;

import static com.dapl.halaladvisor.Utility.Utils.imageWithGlide;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Result> arrayList;
    private ArrayList<String> imagesList= new ArrayList<>();
    RedeemClickInteface redeemClickInteface;
    ViewPager viewPager;

    public GalleryAdapter(Context context, RedeemClickInteface redeemClickInteface, ArrayList<Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.redeemClickInteface = redeemClickInteface;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_gallery, parent, false);
        return new MyViewHolder(itemView);
    }

    View deleteDialogView;
    AlertDialog deleteDialog;
    AlertDialog.Builder deleteDialogBuilder;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        //  holder.tv_row_name.setText(arrayList.get(position).cuisinesName);

        try {
            imageWithGlide(holder.ivGallery, arrayList.get(position).getGalImageName());
            Log.e("images", "--" + arrayList);

        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopupViewPager();
            }
        });



/*

        holder.clParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemClickInteface.onClick(arrayList.get(position).id.toString() ,arrayList.get(position).cuisinesName.toString());
            }
        });
*/


//        holder.tvNew.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /*
     * Initialize universal image loader
     * */




    /*
     * Interface to set item click
     * */

    /*
     * Interface to set item remove
     * */


    public void imagePopupViewPager() {

        //Inflate the dialog with custom view
        deleteDialogView = LayoutInflater.from(context).inflate(R.layout.image_popup, null);
        //AlertDialogBuilder

        deleteDialogBuilder = new AlertDialog.Builder(context)
                .setView(deleteDialogView);
        /// .setTitle("Login Form")
        //show dialog
        deleteDialog = deleteDialogBuilder.show();
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        viewPager = deleteDialogView.findViewById(R.id.pager);
        MyViewPagerAdapter popupImageAdapter = new MyViewPagerAdapter(arrayList);
        viewPager.setAdapter(popupImageAdapter);
        viewPager.setCurrentItem(0);

        deleteDialogView.findViewById(R.id.close_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
            }
        });


        deleteDialogView.findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(-1);
                if (current < arrayList.size()) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }
            }
        });
        deleteDialogView.findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < arrayList.size()) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }
            }
        });


        deleteDialog.show();


    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        ArrayList<Result> imagesList;

        MyViewPagerAdapter(ArrayList<Result> imagesList) {
            this.imagesList = imagesList;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.layout_popup, container, false);

            try {
                imageWithGlide(view.findViewById(R.id.ivPopup), arrayList.get(position).getGalImageName());
            } catch (Exception e) {
                e.printStackTrace();
            }


            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return arrayList.size();
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

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivGallery;
        private LinearLayout llParent;


        MyViewHolder(View view) {
            super(view);
            ivGallery = (ImageView) view.findViewById(R.id.ivGallery);
            llParent = (LinearLayout) view.findViewById(R.id.llParent);
            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}