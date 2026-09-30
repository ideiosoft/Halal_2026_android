package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.OfferRedeemScreen;
import com.dapl.halaladvisor.Model.getofferres.Datum;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;
import com.dapl.halaladvisor.interfaces.RedeemClickInteface;

import java.util.ArrayList;

import static com.dapl.halaladvisor.Utility.Utils.convertDateFormat;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Datum> arrayList;

    public OffersAdapter(Context context, ArrayList<Datum> arrayList) {

        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_offers, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if (arrayList.get(position).getType() != null) {
            if (arrayList.get(position).getOfferPrice().contains("%")){
                holder.tv_offer_heading.setText(arrayList.get(position).getOfferPrice() + " Off - " + arrayList.get(position).getType());
            }else {
                holder.tv_offer_heading.setText(arrayList.get(position).getOfferPrice() + "% Off - " + arrayList.get(position).getType());
            }

        } else {
            if (arrayList.get(position).getOfferPrice().contains("%")){
                holder.tv_offer_heading.setText(arrayList.get(position).getOfferPrice() + " Off" + "");
            }else {
                holder.tv_offer_heading.setText(arrayList.get(position).getOfferPrice() + "% Off" + "");

            }
        }

        if (arrayList.get(position).getOfferDesc() != null && !arrayList.get(position).getOfferDesc().isEmpty()) {

            holder.tv_offer_desc.setText(arrayList.get(position).getOfferDesc());
        } else {

            holder.tv_offer_desc.setText("N/A");
        }


        if (arrayList.get(position).getEndDate() != null && !arrayList.get(position).getEndDate().isEmpty()) {
            holder.tvValidity.setText("Valid till " + convertDateFormat(arrayList.get(position).getEndDate()));
        } else {
            holder.tvValidity.setText("N/A");
        }


        holder.tvRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 context.startActivity(new Intent(context, OfferRedeemScreen.class)
                        .putExtra("price", arrayList.get(position).getOfferPrice())
                        .putExtra("r_name", arrayList.get(position).getR_name())
                        .putExtra("des", arrayList.get(position).getOfferDesc())
                        .putExtra("id", arrayList.get(position).getOfferId())
                         .putExtra("redeem_steps", arrayList.get(position).getHowToRedeem())
                );
            }
        });

/*
        holder.tv_row_name.setText(arrayList.get(position).cuisinesName);

        if (arrayList.get(position).cuisineImage != null) {
            Utils.imageWithGlide(holder.ivCatimage, arrayList.get(position).cuisineImage);
        }*/





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


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvRedeem;
        private TextView tv_offer_heading, tvValidity, tv_offer_desc;
        private ImageView ivCatimage;
        private RelativeLayout clParent;


        MyViewHolder(View view) {
            super(view);
            tvRedeem = (TextView) view.findViewById(R.id.tvRedeem);
            tv_offer_desc = (TextView) view.findViewById(R.id.tv_offer_desc);
            tv_offer_heading = (TextView) view.findViewById(R.id.tv_offer_heading);
            tvValidity = (TextView) view.findViewById(R.id.tvValidity);


            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}