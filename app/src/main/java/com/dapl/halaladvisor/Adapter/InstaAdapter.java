package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Activity.InstaDetailScreen;
import com.dapl.halaladvisor.Model.instanewres.EdgeXX;
import com.dapl.halaladvisor.Model.instares.Datum;
import com.dapl.halaladvisor.Model.instaresfinal.Data;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;

import java.util.ArrayList;
import java.util.List;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.MyViewHolder> {

    private Context context;

    private List<Data> arrayList;

    public InstaAdapter(Context context , List<Data> arrayList) {

        this.context = context;
   this.arrayList = arrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_insta, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


     //   Utils.imageWithGlide(holder.mainImg, arrayList.get(position).getMediaUrl());
        Utils.imageWithGlide(holder.mainImg, arrayList.get(position).getMediaUrl());



        holder.mainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, InstaDetailScreen.class)
                        .putExtra("image",arrayList.get(position).getMediaUrl())
                        .putExtra("caption",arrayList.get(position).getCaption())
                        .putExtra("media_type",arrayList.get(position).getMediaType())
                        .putExtra("permalink",arrayList.get(position).getPermalink())


                );
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