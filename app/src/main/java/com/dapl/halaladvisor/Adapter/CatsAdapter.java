package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Model.cuisine.CuisineModelNew;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;

import java.util.ArrayList;

public class CatsAdapter extends RecyclerView.Adapter<CatsAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<CuisineModelNew.Datum> arrayList;
    CuisineClickInterface cuisineClickInterface;

    public CatsAdapter(Context context, ArrayList<CuisineModelNew.Datum> arrayList, CuisineClickInterface cuisineClickInterface) {

        this.context = context;
        this.arrayList = arrayList;
        this.cuisineClickInterface = cuisineClickInterface;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cats, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tv_row_name.setText(arrayList.get(position).cuisinesName);

        if (arrayList.get(position).cuisineImage != null) {
            Utils.imageWithGlide(holder.ivCatimage, arrayList.get(position).cuisineImage);
        }






        holder.clParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuisineClickInterface.onClick(arrayList.get(position).id.toString() ,arrayList.get(position).cuisinesName.toString());
            }
        });


        holder.tvNew.setVisibility(View.GONE);

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
        private TextView tv_row_name;
        private TextView tvNew;
        private ImageView ivCatimage;
        private RelativeLayout clParent;


        MyViewHolder(View view) {
            super(view);
            tv_row_name = (TextView) view.findViewById(R.id.tvName);
            tvNew = (TextView) view.findViewById(R.id.tvNew);
            ivCatimage = (ImageView) view.findViewById(R.id.ivCatimage);
            clParent = (RelativeLayout) view.findViewById(R.id.clParent);


            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}