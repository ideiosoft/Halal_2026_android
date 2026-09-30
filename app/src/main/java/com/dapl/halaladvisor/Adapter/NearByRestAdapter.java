package com.dapl.halaladvisor.Adapter;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.NearByResturent;
import com.dapl.halaladvisor.Model.cuisine.CuisineModelNew;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NearByRestAdapter extends RecyclerView.Adapter<NearByRestAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<NearByResturent> arrayList;
    private Integer favPosition;



    public NearByRestAdapter(Context context, ArrayList<NearByResturent> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder vh, final int position) {



        NearByResturent model =arrayList.get(position);
        Picasso.get()
                .load(model.getImage())
                .error(R.drawable.list_placeholder)
                .placeholder(R.drawable.list_placeholder)
                //.resize(300, 200)
                .noFade()
                .into(vh.ivThumbnail);



        System.out.println("Rana_cuisineName cuisine name_tag: " + model.getName().toUpperCase());
        String r_status = model.getStatus_resturent();
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




        try {
            System.out.println("Rana_cuisineName cuisine name: " + model.getName().toUpperCase());
            // setting Restaurant name
            vh.name.setText(model.getName());

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


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

        if(model.getRes_fav()==1){
            vh.iv_make_favorite.setBackgroundResource(R.drawable.fav_fill);
        }else{
            vh.iv_make_favorite.setBackgroundResource(R.drawable.fav_stroke);

        }

        vh.iv_make_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                System.out.println("sdfgnh==" + position);
                favPosition =position;
             //   vh.iv_make_favorite.setBackgroundResource(R.drawable.fav_fill);

                new NearByRestAdapter.ReqAddToFavList(model.getId()).execute();
                // makeFavoriteRestroListner.makeFavourite(v,position,model.getRestaurant_id());


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

                NearByResturent item = arrayList.get(favPosition);
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
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout linBox;
        public TextView tvOrderNow;
        public TextView tvPreOrder;
        public TextView tvPickUp;
        public TextView tvDelivery,tvFeaturedOffer;
         ImageView ivThumbnail;

        ImageView opencloseview;
        TextView name, address, cusine_tag, cuisine_type, rating, numberOfVote, status;
        RatingBar listRatings;

        Button iv_make_favorite;
        MyViewHolder(View view) {
            super(view);
            ivThumbnail = view.findViewById(R.id.thumbnail);
            tvFeaturedOffer = view.findViewById(R.id.tvFeaturedOffer);
            name = view.findViewById(R.id.rowName);
            address = view.findViewById(R.id.rowAddress);
            opencloseview = view.findViewById(R.id.openclose);
            status = view.findViewById(R.id.status);
            linBox = view.findViewById(R.id.linBox);

            iv_make_favorite = view.findViewById(R.id.iv_make_favorite);

        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}