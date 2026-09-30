package com.dapl.halaladvisor.Adapter;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.NearByResturent;
import com.dapl.halaladvisor.Model.NearbyRestaurantResponse;
import com.dapl.halaladvisor.Model.Result;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class NewNearByRestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ITEM = 0;

    Context context;

    private ArrayList<Result> arrayList;
    private Integer favPosition;

    public NewNearByRestaurantAdapter(Context context, ArrayList<Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View mItem = inflater.inflate(R.layout.neary_row_new, parent, false);
            return new GenericViewHolder(mItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof GenericViewHolder) {

            final GenericViewHolder vh = (GenericViewHolder) holder;

            final Result model =   arrayList.get(position);

            //  Toast.makeText(context, model.getImage(), Toast.LENGTH_SHORT).show();

            Picasso.get()
                    .load(model.getImage())
                    .error(R.drawable.list_placeholder)
                    .placeholder(R.drawable.list_placeholder)
                    //.resize(300, 200)
                    .noFade()
                    .into(vh.ivThumbnail);


            if(model.getRes_fav()==1){
                vh.iv_make_favorite.setBackgroundResource(R.drawable.fav_fill);
            }else{
                vh.iv_make_favorite.setBackgroundResource(R.drawable.fav_stroke);
            }

            if(model.is_featured().equals("0")){
                vh.featuredIv.setVisibility(View.GONE);
            }else{
                vh.featuredIv.setVisibility(View.VISIBLE);
            }

            String cusineNames="";
            if(model.getCuisines_name()!=null && model.getCuisines_name().size()>0){
                for(int i=0; i<model.getCuisines_name().size();i++){
                    cusineNames= cusineNames+" • "+model.getCuisines_name().get(i).getCuisines_name();
                }
            }

            vh.cuisine_type.setText(model.getCost_sign()+cusineNames);
//            try {
//
//                JSONArray cusineArray = new JSONArray(arrayList.get(position).getCusineJsnArray());
//                if (cusineArray.length() > 0)
//                    vh.cuisine_type.setText(cusineArray.getJSONObject(0).getString("cuisines_name"));
//                else
//                    vh.cuisine_type.setText("NA");
//                vh.cuisine_type.setText((arrayList.get(position).getCost_sign() + arrayList.get(favPosition).getCuisine_type()).trim());
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
            vh.iv_make_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    System.out.println("sdfgnh==" + position);

                  //  vh.iv_make_favorite.setBackgroundResource(R.drawable.fav_fill);
                    favPosition =position;
                    new ReqAddToFavList(model.getId()).execute();
                    // makeFavoriteRestroListner.makeFavourite(v,position,model.getRestaurant_id());


                }
            });


            System.out.println("Rana_cuisineName cuisine name_tag: " + model.getName().toUpperCase());
            String r_status = model.getStatus_resturent();
            // setting open or close image tag
            if (r_status.equalsIgnoreCase("Closed")) {
//                vh.opencloseview.setImageResource(R.drawable.closed);
                vh.status.setText("CLOSED");


                vh.status.setBackgroundResource(R.drawable.little_rounded_edge_red_bg);
                //  vh.status.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_corner_red_bg));
            } else {
//                vh.opencloseview.setImageResource(R.drawable.open);
                vh.status.setText("OPEN");
                vh.status.setBackgroundResource(R.drawable.little_round_edges_bg);


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







//      Onclick Set


            vh.linBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String restaurant_id = model.getId();
                    Log.e("List- ", "Restaurant_id: " + restaurant_id);


                    context.startActivity(new Intent(context, DetailsActivity.class)
                            .putExtra("rid", restaurant_id)
                            .putExtra("from", "0")


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
    public int getItemViewType(int position) {
        Object item = arrayList.get(position);
        if (item instanceof Result) {
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

                Result item = arrayList.get(favPosition);
                if(item.getRes_fav()==0){
                    item.setRes_fav(1);
                    arrayList.set(favPosition,item);
                    notifyDataSetChanged();
                }

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
        public TextView tvDelivery,tvFeaturedOffer;
        View rowview;
        ImageView ivThumbnail;

        ImageView opencloseview,featuredIv;
        TextView name, address, cusine_tag, cuisine_type, rating, numberOfVote, status;
        RatingBar listRatings;

        Button iv_make_favorite;

        GenericViewHolder(View rowView) {
            super(rowView);
            this.rowview = rowView;
            ivThumbnail = rowView.findViewById(R.id.thumbnail);
            tvFeaturedOffer = rowView.findViewById(R.id.tvFeaturedOffer);
            featuredIv = rowView.findViewById(R.id.featuredIv);
            name = rowView.findViewById(R.id.rowName);
            address = rowView.findViewById(R.id.rowAddress);
            opencloseview = rowView.findViewById(R.id.openclose);
            status = rowView.findViewById(R.id.status);
            linBox = rowView.findViewById(R.id.linBox);
            cuisine_type = rowView.findViewById(R.id.rowCuisineType);

            iv_make_favorite = rowView.findViewById(R.id.iv_make_favorite);

        }

    }
}
