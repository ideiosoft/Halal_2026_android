package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.DroidWebViewActivity;
import com.dapl.halaladvisor.Model.RestaurantFavoriteListModel;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.AppController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class FavoriteResturentAdapter extends RecyclerView.Adapter<FavoriteResturentAdapter.MyViewHolder> {

    public OnRequestRemoveFromFavListListener onRequestRemoveFromFavListListener;
    Context mContext;

    private ArrayList<RestaurantFavoriteListModel> modelMyActivities;


    public FavoriteResturentAdapter(Context context, ArrayList<RestaurantFavoriteListModel> moviesList) {

        this.modelMyActivities = moviesList;
        mContext = context;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        try {
            final RestaurantFavoriteListModel modeldata = modelMyActivities.get(position);


            if (modeldata.getR_name() != null) {
                holder.rowName.setText(modeldata.getR_name());
            }
            if (modeldata.getTotal_rating() != null) {
                holder.numberOfVote.setText("(" + modeldata.getTotal_rating() + ")");

            }

            holder.rowCuisineType.setText(("$$ " + "\u2022 " + modeldata.getCuisine().trim()));

            if(modeldata.getR_is_featured().equalsIgnoreCase("1"))
            {
                holder.featuredIv.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.featuredIv.setVisibility(View.GONE);
            }
            try {


                Picasso.get()
                        .load(modeldata.getR_image_path())
                        .placeholder(R.drawable.list_placeholder)
                        .error(R.drawable.list_placeholder)
                        .noFade()
                        .into(holder.thumbnail);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String r_status = modeldata.getR_status();
            // setting open or close image tag
            if (r_status.equalsIgnoreCase("Closed")) {

                holder.status.setText("CLOSED");

                holder.status.setBackgroundResource(R.drawable.rounded_corner_red_bg);
                //  holder.status.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_corner_red_bg));
            } else {
//                holder.opencloseview.setImageResource(R.drawable.open);
                holder.status.setText("OPEN");
                holder.status.setBackgroundResource(R.drawable.rounded_corner_green_bg);


            }
            holder.linBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RestaurantListModel model = new RestaurantListModel();
                    model.setId(modeldata.getR_id());
                    model.setName(modeldata.getR_name());
                    model.setStatus(modeldata.getR_status());
                    model.setImage(modeldata.getR_image_path());
                    AppController.setSelectedRestaurent(model);
                    PrefUtils.saveActivityOpenFromNotification(mContext,false);
                    mContext.startActivity(new Intent(mContext, DetailsActivity.class));
                }
            });

            if (modeldata.getOrder_now_link().length() > 0) {
                holder.tv_order_now.setVisibility(View.VISIBLE);

            } else {
                holder.tv_order_now.setVisibility(View.GONE);

            }

            try {
                if (modeldata.getRest_avg_rating() != null) {

                    holder.ratingBarList.setRating(Float.parseFloat(modeldata.getRest_avg_rating()));

                    holder.rating.setText(modeldata.getRest_avg_rating());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //  holder.status.setText(modeldata.getR_status());

            holder.tv_order_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllConstants.webUrl = modeldata.getOrder_now_link();
                    Intent next = new Intent(mContext, DroidWebViewActivity.class);
                    next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(next);
                }
            });


            holder.iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    onRequestRemoveFromFavListListener.onRequestRemoveFromFavListListener(modeldata.getR_id(), position);


                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return modelMyActivities.size();
    }

    public void setOnRequestRemoveFromFavListListener(OnRequestRemoveFromFavListListener mmOnRequestRemoveFromFavListListener) {

        onRequestRemoveFromFavListListener = mmOnRequestRemoveFromFavListListener;

    }


    public interface OnRequestRemoveFromFavListListener {
        void onRequestRemoveFromFavListListener(String restId, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail, iv_favorite;
        RatingBar ratingBarList;
        ImageView featuredIv;
        TextView status, tv_order_now, tv_pre_order, rowCuisineType;
        TextView rowName, numberOfVote, rating;
        LinearLayout linBox;

        public MyViewHolder(View view) {
            super(view);
            featuredIv = view.findViewById(R.id.featuredIv);
            thumbnail = view.findViewById(R.id.thumbnail);
            iv_favorite = view.findViewById(R.id.iv_favorite);
            ratingBarList = view.findViewById(R.id.ratingBarList);
            status = view.findViewById(R.id.status);
            rating = view.findViewById(R.id.rating);
            numberOfVote = view.findViewById(R.id.numberOfVote);
            tv_order_now = view.findViewById(R.id.tv_order_now);
            tv_pre_order = view.findViewById(R.id.tv_pre_order);
            rowName = view.findViewById(R.id.rowName);
            rowCuisineType = view.findViewById(R.id.rowCuisineType);
            linBox = view.findViewById(R.id.linBox);

        }
    }


}
