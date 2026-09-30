package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;

import java.util.ArrayList;

public class ReviewImagesAdapter extends RecyclerView.Adapter<ReviewImagesAdapter.MyViewHolder> {

    private Context context;
    String baseUrl = "";
    private ArrayList<String> arrayList;

    public ReviewImagesAdapter(Context context, ArrayList<String> arrayList, String baseUrl) {

        this.baseUrl = baseUrl;
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_images, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            Utils.imageWithGlide(holder.mainImg, baseUrl + arrayList.get(position));

            Log.e("url--","--"+baseUrl + arrayList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }


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