package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapl.halaladvisor.Activity.AddReviewActivity;
import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.ViewRestVoucherScreen;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.Model.VoucherModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.extra.AppController;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

import static com.dapl.halaladvisor.Utility.Utils.convertDateFormat;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<VoucherModel> arrayList;


    public VoucherAdapter(Context context, ArrayList<VoucherModel> arrayList) {

        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_voucher_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if (holder != null) {
            MyViewHolder viewHolder = holder;
            VoucherModel voucherModel = arrayList.get(position);

/*
            if (TextUtils.isEmpty(voucherModel.getOfferImg())) {

                try {
                    Picasso.get()
                            .load(voucherModel.getOfferImg())
                            .noFade()
                            .placeholder(R.drawable.no_image_special)
                            .error(R.drawable.no_image_special)
                            .into(viewHolder.iv_offer_image);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }*/

         /*   viewHolder.tv_offer_heading.setText(voucherModel.getTagLine());
            viewHolder.tv_offer_desc.setText(voucherModel.getOfferDesc());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            Date date = null;
            try {
                date = simpleDateFormat.parse(voucherModel.getEndDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
*/
           // viewHolder.tv_offer_expiry.setText("Expires on " + outputFormat.format(date));

            if (voucherModel.getOffer_type() != null || !voucherModel.getOffer_type().isEmpty()) {

                if(voucherModel.getOfferPrice().contains("%")){
                    holder.tv_offer_heading.setText(voucherModel.getOfferPrice() + " Off - " + voucherModel.getOffer_type());
                }else {
                    holder.tv_offer_heading.setText(voucherModel.getOfferPrice() + "% Off - " + voucherModel.getOffer_type());
                }


            } else {


                if(voucherModel.getOfferPrice().contains("%")){
                    holder.tv_offer_heading.setText(voucherModel.getOfferPrice() + " Off" + "");
                }else {
                    holder.tv_offer_heading.setText(voucherModel.getOfferPrice() + "% Off" + "");
                }



            }
            if (voucherModel.getRestName() != null) {

                holder.tv_offer_desc.setText(voucherModel.getRestName() );
            } else {
                holder.tv_offer_desc.setText("");


            }
           /* if (voucherModel.getOfferDesc() != null && !voucherModel.getOfferDesc().isEmpty()) {

                holder.tv_offer_desc.setText(voucherModel.getOfferDesc());
            } else {

                holder.tv_offer_desc.setText("N/A");
            }*/
            if (voucherModel.getEndDate() != null && !voucherModel.getEndDate().isEmpty()) {
                holder.tvValidity.setText("Valid till " + convertDateFormat(voucherModel.getEndDate()));
            } else {
                holder.tvValidity.setText("N/A");
            }


holder.tvRateIt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {




   /*
        context.startActivity(new Intent(context, DetailsActivity.class)
                .putExtra("rid", voucherModel.getRestId())
                .putExtra("from", "2")


        );
        */


        context.startActivity(new Intent(context, AddReviewActivity.class)
                .putExtra("rid", voucherModel.getRestId())
                .putExtra("name", voucherModel.getRestName())



        );
    }
});



            holder.tvRedeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RestaurantListModel restaurantListModel = new RestaurantListModel();
                    restaurantListModel.setId(voucherModel.restId.toString());
                //    AppController.getSelectedRestaurent().setId(voucherModel.restId.toString());

                    context.startActivity(new Intent(context, ViewRestVoucherScreen.class)
                            .putExtra("rname",voucherModel.getRestName())
                            .putExtra("price",voucherModel.getOfferPrice())
                            .putExtra("enddate",voucherModel.getEndDate())
                            .putExtra("lat",voucherModel.getRestLat())
                            .putExtra("lng",voucherModel.getRestLon())
                            .putExtra("rid",voucherModel.getRestId())
                            .putExtra("minorder",voucherModel.getMinimum_order())
                            .putExtra("phone",voucherModel.getPhone())

                    );





                }
            });
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


    class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tvRedeem,tvRateIt;
        ImageView iv_offer_image;

      //  TextView tv_offer_heading, tv_offer_desc, tv_offer_expiry;
        private TextView tv_offer_heading, tvValidity, tv_offer_desc;

        MyViewHolder(View rowView) {
            super(rowView);

            this.view = rowView;
             tv_offer_desc = (TextView) view.findViewById(R.id.tv_offer_desc);
            tvRateIt = (TextView) view.findViewById(R.id.tvRateIt);
            tv_offer_heading = (TextView) view.findViewById(R.id.tv_offer_heading);
            tvValidity = (TextView) view.findViewById(R.id.tvValidity);
            tvRedeem = (TextView) view.findViewById(R.id.tvRedeem);



       /*     iv_offer_image = rowView.findViewById(R.id.iv_offer_image);
            tv_offer_heading = rowView.findViewById(R.id.tv_offer_heading);
            tv_offer_desc = rowView.findViewById(R.id.tv_offer_desc);
            tv_offer_expiry = rowView.findViewById(R.id.tv_offer_expiry);*/
        }

    }
}