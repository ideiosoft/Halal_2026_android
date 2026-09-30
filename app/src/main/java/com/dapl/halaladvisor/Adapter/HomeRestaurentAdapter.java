package com.dapl.halaladvisor.Adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.DroidWebViewActivity;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRestaurentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ITEM = 0;

    Context context;
String type;
    private ArrayList<Object> arrayList;


    public HomeRestaurentAdapter(Context context, ArrayList<Object> arrayList, String type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View mItem = inflater.inflate(R.layout.list_row_new, parent, false);
            return new GenericViewHolder(mItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GenericViewHolder) {

            final GenericViewHolder vh = (GenericViewHolder) holder;

            final RestaurantListModel model = (RestaurantListModel) arrayList.get(position);

            //  Toast.makeText(context, model.getImage(), Toast.LENGTH_SHORT).show();

            Picasso.get()
                    .load(model.getImage())
                    .error(R.drawable.list_placeholder)
                    .placeholder(R.drawable.list_placeholder)
                    //.resize(300, 200)
                    .noFade()
                    .into(vh.ivThumbnail);

            if (model.getIs_featured()!=null){

                if (!model.getOffer_price().isEmpty() && model.getType()!=null ){
                    if (model.getOffer_price().contains("%")){
                        vh.tvFeaturedOffer.setVisibility(View.VISIBLE);
                        vh.tvFeaturedOffer.setText(model.getOffer_price() +" OFF - " + model.getType());
                    }else {
                        vh.tvFeaturedOffer.setVisibility(View.VISIBLE);
                        vh.tvFeaturedOffer.setText(model.getOffer_price() +"% OFF - " + model.getType());
                    }


                }

            }
            else {
                vh.tvFeaturedOffer.setVisibility(View.GONE);
            }


            vh.iv_make_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    System.out.println("sdfgnh==" + position);

                    vh.iv_make_favorite.setBackgroundResource(R.drawable.fav_fill);

                    new ReqAddToFavList(model.getId()).execute();
                    // makeFavoriteRestroListner.makeFavourite(v,position,model.getRestaurant_id());


                }
            });
            if (!model.getOrder_now_link().equals("")) {
                vh.tvOrderNow.setVisibility(View.VISIBLE);
                vh.tvOrderNow.setText("Order Now");
            } else {
                vh.tvOrderNow.setVisibility(View.GONE);
            }

            if (!model.getPre_order().equals("")) {
                vh.tvPreOrder.setVisibility(View.VISIBLE);
                vh.tvPreOrder.setText(model.getPre_order());
            } else {
                vh.tvPreOrder.setVisibility(View.GONE);
            }

            if (!model.getPick_up().equals("")) {
                vh.tvPickUp.setVisibility(View.VISIBLE);
                vh.tvPickUp.setText(model.getPick_up());
            } else {
                vh.tvPickUp.setVisibility(View.GONE);
            }
Log.e("dddd",model.getR_is_featured());
            if(model.getR_is_featured().equalsIgnoreCase("1"))
            {
                vh.featuredIv.setVisibility(View.VISIBLE);
            }
            else
            {
                vh.featuredIv.setVisibility(View.GONE);
            }

            if (!model.getDelivery_tag().equals("")) {
                vh.tvDelivery.setVisibility(View.VISIBLE);
                vh.tvDelivery.setText(model.getDelivery_tag());
            } else {
                vh.tvDelivery.setVisibility(View.GONE);
            }

            System.out.println("Rana_cuisineName cuisine name_tag: " + model.getName().toUpperCase());
            String r_status = model.getStatus();
            // setting open or close image tag
            if (r_status.equalsIgnoreCase("Closed")) {
//                vh.opencloseview.setImageResource(R.drawable.closed);
                vh.status.setText("CLOSED");

                vh.status.setBackgroundResource(R.drawable.rounded_corner_red_bg);
                //  vh.status.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_corner_red_bg));
            } else {
//                vh.opencloseview.setImageResource(R.drawable.open);
                vh.status.setText("OPEN");
                vh.status.setBackgroundResource(R.drawable.rounded_corner_green_bg);


            }
            vh.numberOfVote.setText("(" + model.getTotal_rating() + ")");

            try {
                String rating = model.getRating();
                Float count = Float.parseFloat(rating);
                // setting rating
                vh.listRatings.setRating(count);
                vh.rating.setText(String.valueOf(count));
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                System.out.println("Rana_cuisineName cuisine name: " + model.getName().toUpperCase());
                // setting Restaurant name
                vh.name.setText(model.getName());

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }


            try {
                // setting Address
                System.out.println("Rana_cuisineName cuisine address: " + model.getAddress());
                vh.address.setText(model.getAddress());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }


            // Setting cuisine type

//===========Need To solve Cusine List======================


            try {

                JSONArray cusineArray = new JSONArray(model.getCusineJsnArray());
                if (cusineArray.length() > 0)
                    vh.cuisine_type.setText(cusineArray.getJSONObject(0).getString("cuisines_name"));
                else
                    vh.cuisine_type.setText("NA");
                vh.cuisine_type.setText((model.getCost_sign() + model.getCuisine_type()).trim());

            } catch (Exception ex) {
                ex.printStackTrace();
            }





//      Onclick Set

            vh.tvOrderNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllConstants.webUrl = model.getOrder_now_link();
                    Intent next = new Intent(context, DroidWebViewActivity.class);
                    next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(next);
                }
            });
            vh.linBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                 String restaurant_id = model.getId();
                    Log.e("List- ", "Restaurant_id: " + restaurant_id);
                    AppController.setSelectedRestaurent(model);
                    PrefUtils.saveActivityOpenFromNotification(context,false);



                    context.startActivity(new Intent(context, DetailsActivity.class));



                }
            });

            if(type.equalsIgnoreCase("home"))
            {
                vh.tvFeaturedOffer.setVisibility(View.GONE);
            }
            else
            {
                vh.tvFeaturedOffer.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = arrayList.get(position);
        if (item instanceof RestaurantListModel) {
            return ITEM;
        } else {
            return -1;
        }
    }

    private class ReqAddToFavList extends AsyncTask<Void, Void, String> {

        String response;
        String restId;
        ProgressDialog progressDialog;


        public ReqAddToFavList(String restId) {
            this.restId = restId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Please Wait...");

            progressDialog.setCancelable(false);

            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            HashMap<String, String> map = new HashMap<>();

            JSONParser parser = new JSONParser();


            String mURL = "https://admin.halaladvisor.com.au/halal_api/res_fav.php?req=fav_res&key=1702NKDRDA&user_id=" + PrefUtils.getProfileObject(context).getU_id() + "&r_id=" + restId;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", map);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Restaurant List response: " + response);


            try {

                JSONObject jsonObject = new JSONObject(response);

                String message = jsonObject.getString("message");

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            } catch (Exception eX) {

                eX.printStackTrace();

            }


        }


    }

    class GenericViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout linBox;
        public TextView tvOrderNow;
        public TextView tvPreOrder;
        public TextView tvPickUp;
        ImageView featuredIv;
        public TextView tvDelivery,tvFeaturedOffer;
        View rowview;
        ImageView ivThumbnail;

        ImageView opencloseview;
        TextView name, address, cusine_tag, cuisine_type, rating, numberOfVote, status;
        RatingBar listRatings;

        Button iv_make_favorite;

        GenericViewHolder(View rowView) {
            super(rowView);
            this.rowview = rowView;
            ivThumbnail = rowView.findViewById(R.id.thumbnail);
            tvFeaturedOffer = rowView.findViewById(R.id.tvFeaturedOffer);
            name = rowView.findViewById(R.id.rowName);
            address = rowView.findViewById(R.id.rowAddress);
            cusine_tag = rowView.findViewById(R.id.rowCuisineTag);
            cuisine_type = rowView.findViewById(R.id.rowCuisineType);
            rating = rowView.findViewById(R.id.rating);
            numberOfVote = rowView.findViewById(R.id.numberOfVote);
            opencloseview = rowView.findViewById(R.id.openclose);
            status = rowView.findViewById(R.id.status);
            tvOrderNow = rowView.findViewById(R.id.tv_order_now);
            tvPreOrder = rowView.findViewById(R.id.tv_pre_order);
            featuredIv = rowView.findViewById(R.id.featuredIv);
            tvPickUp = rowView.findViewById(R.id.tv_pick_up);
            tvDelivery = rowView.findViewById(R.id.tv_delivery);
            listRatings = rowView.findViewById(R.id.ratingBarList);
            linBox = rowView.findViewById(R.id.linBox);

            iv_make_favorite = rowView.findViewById(R.id.iv_make_favorite);

        }

    }
}
