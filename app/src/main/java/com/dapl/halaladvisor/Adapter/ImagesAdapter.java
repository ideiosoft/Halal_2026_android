package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Model.ImageModel;
import com.dapl.halaladvisor.Model.cuisine.CuisineModelNew;
import com.dapl.halaladvisor.Model.fetchreview.Image;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;
import com.dapl.halaladvisor.interfaces.DeleteReviewImageInterface;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Image> arrayList;

    String reviewId="";

    DeleteReviewImageInterface deleteReviewImageInterface;

    public ImagesAdapter(Context context, ArrayList<Image> arrayList,
                         DeleteReviewImageInterface deleteReviewImageInterface,String reviewId) {

        this.context = context;
        this.deleteReviewImageInterface = deleteReviewImageInterface;
        this.arrayList = arrayList;
        this.reviewId = reviewId;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_featured_images, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        Utils.imageWithGlide(holder.mainImg, arrayList.get(position).getReviewImages());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 deleteReviewImageInterface.onClick(arrayList.get(position).getId(),reviewId,arrayList.get(position));


            }
        });


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


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mainImg, ivDelete;


        MyViewHolder(View view) {
            super(view);
            mainImg = (ImageView) view.findViewById(R.id.mainImg);
            ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}